package org.xblackcat.frozenice.psi;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenice.util.IceErrorMessages;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 06.01.12 11:48
 *
 * @author xBlackCat
 */
public class SliceParser {
    private final Map<String, Map<String, ElementType>> validTypes = new HashMap<String, Map<String, ElementType>>();
    private final Map<String, Map<String, ElementType>> forwardDeclarations = new HashMap<String, Map<String, ElementType>>();

    private final Map<String, Map<String, Set<String>>> enumConstants = new HashMap<String, Map<String, Set<String>>>();
    private final Map<String, Set<String>> constants = new HashMap<String, Set<String>>();

    private final PsiBuilder builder;

    public SliceParser(PsiBuilder builder) {
        this.builder = builder;
        builder.setDebugMode(true);
    }

    public void parseBlock() {
        parseBlock(true, "");
    }

    private void parseBlock(boolean topLevel, String moduleName) {
        final PsiBuilder.Marker block = mark();

        IElementType type = null;
        while (!eof()) {
            final IElementType token = token();

            if (Checker.isCommentToken(token)) {
                type = SliceElementTypes.END_OF_LINE_COMMENT;
                advance();
                continue;
            }

            if (Checker.isMacroDefinition(token)) {
                type = SliceElementTypes.ICE_MACROS;
                parseMacros();
                continue;
            }

            if (Checker.isModuleToken(token)) {
                type = SliceElementTypes.ICE_MODULE;

                parseModule(moduleName);
            } else if (!topLevel) {
                if (Checker.isClassToken(token)) {
                    type = SliceElementTypes.ICE_CLASS;

                    parseClass(moduleName);
                } else if (Checker.isInterfaceToken(token)) {
                    type = SliceElementTypes.ICE_INTERFACE;

                    parseInterface(moduleName);
                } else if (Checker.isEnumToken(token)) {
                    type = SliceElementTypes.ICE_ENUM;

                    parseEnum(moduleName);
                } else if (Checker.isExceptionToken(token)) {
                    type = SliceElementTypes.ICE_EXCEPTION;

                    parseException(moduleName);
                } else if (token == SliceTokenTypes.KEYWORD_SEQUENCE) {
                    type = SliceElementTypes.ICE_TYPE_SEQUENCE;

                    parseSequence(moduleName);
                } else if (token == SliceTokenTypes.KEYWORD_STRUCT) {
                    type = SliceElementTypes.ICE_STRUCT;

                    parseStruct(moduleName);
                } else if (token == SliceTokenTypes.KEYWORD_DICTIONARY) {
                    type = SliceElementTypes.ICE_TYPE_DICTIONARY;

                    parseDictionary(moduleName);
                } else if (token == SliceTokenTypes.KEYWORD_CONST) {
                    type = SliceElementTypes.ICE_CONSTANT;

                    parseConstant(moduleName);
                }
            }
            break;
        }

        if (type != null) {
            block.done(type);
        } else {
            advance();
            block.error(IceErrorMessages.message("unexpected.token"));
        }
    }

    private void parseConstant(String moduleName) {
        boolean declarationValid = true;

        final PsiBuilder.Marker constantDeclarationMark = mark();
        advance();

        final PsiBuilder.Marker dataTypeMark = mark();
        IElementType typeToken = token();
        String typeTokenText = tokenText();
        if (Checker.isTypeKeyword(typeToken) && typeToken != SliceTokenTypes.KEYWORD_VOID) {
            advance();
            dataTypeMark.drop();
        } else if (typeToken == SliceTokenTypes.IDENTIFIER) {
            advance();
            if (isTypeDeclared(moduleName, typeTokenText, ElementType.Enum)) {
                dataTypeMark.drop();
            } else {
                typeToken = null;
                dataTypeMark.error("invalid.datatype");
            }
        } else {
            typeToken = null;
            dataTypeMark.error("invalid.datatype");
        }

        final PsiBuilder.Marker constNameMark = mark();
        String constantName = readIdentifier();
        Set<String> moduleConstants = constants.get(moduleName);
        if (moduleConstants == null) {
            constants.put(moduleName, moduleConstants = new HashSet<String>());
        }
        if (moduleConstants.contains(constantName)) {
            constNameMark.error(IceErrorMessages.message("already.defined"));
        } else {
            constNameMark.drop();
        }

        if (token() == SliceTokenTypes.EQUAL) {
            advance();
        } else {
            declarationValid = false;
        }

        // Check validity types
        if (typeToken == SliceTokenTypes.KEYWORD_INT ||
                typeToken == SliceTokenTypes.KEYWORD_BYTE ||
                typeToken == SliceTokenTypes.KEYWORD_LONG ||
                typeToken == SliceTokenTypes.KEYWORD_SHORT
                ) {
            if (token() == SliceTokenTypes.INTEGER_VALUE) {
                advance();
            } else {
                declarationValid = false;
            }
        } else if (typeToken == SliceTokenTypes.KEYWORD_FLOAT ||
                typeToken == SliceTokenTypes.KEYWORD_DOUBLE
                ) {
            if (token() == SliceTokenTypes.FLOAT_VALUE || token() == SliceTokenTypes.FLOAT_VALUE) {
                advance();
            } else {
                declarationValid = false;
            }
        } else if (typeToken == SliceTokenTypes.KEYWORD_STRING) {
            if (token() == SliceTokenTypes.STRING_LITERAL) {
                advance();
            } else {
                declarationValid = false;
            }
        } else if (typeToken == SliceTokenTypes.KEYWORD_BOOL) {
            if (token() == SliceTokenTypes.KEYWORD_TRUE ||
                    token() == SliceTokenTypes.KEYWORD_FALSE) {
                advance();
            } else {
                declarationValid = false;
            }
        } else if (typeToken == SliceTokenTypes.IDENTIFIER) {
            // Enum
            if (token() == SliceTokenTypes.IDENTIFIER) {
                final String enumValue = tokenText();

                advance();
                final Map<String, Set<String>> enums = enumConstants.get(moduleName);
                if (enums == null || !enums.containsKey(typeTokenText) || !enums.get(typeTokenText).contains(enumValue)) {
                    declarationValid = false;
                }
            } else {
                declarationValid = false;
            }
        }
        if (!declarationValid) {
            if (token() != SliceTokenTypes.SEMICOLON) {
                advance();
            }
        }

        if (token() == SliceTokenTypes.SEMICOLON) {
            advance();
        } else {
            mark().error(IceErrorMessages.message("semicolon.is.required"));
        }

        if (declarationValid) {
            constantDeclarationMark.drop();
        } else {
            constantDeclarationMark.error(IceErrorMessages.message("invalid.constant.declaration"));
        }
    }

    private void parseEnum(String moduleName) {
        advance();

        final PsiBuilder.Marker enumNameMark = mark();
        boolean dropErrorMark = true;
        String enumName = readIdentifier();
        if (enumName != null) {
            if (isTypeDeclared(moduleName, enumName, null) &&
                    !isForwardDeclared(moduleName, enumName, ElementType.Enum)) {
                enumNameMark.error(IceErrorMessages.message("already.defined"));
                dropErrorMark = false;
            } else if (Checker.isKeywordString(enumName)) {
                enumNameMark.error(IceErrorMessages.message("reserved.word"));
                dropErrorMark = false;
            }
        }

        if (token() == SliceTokenTypes.SEMICOLON) {
            // Forward declaration
            if (isForwardDeclared(moduleName, enumName, ElementType.Enum)) {
                enumNameMark.error(IceErrorMessages.message("already.defined"));
            } else {
                enumNameMark.drop();
            }

            forwardDeclareName(moduleName, enumName, ElementType.Enum);

            advance();
            return;
        }

        if (dropErrorMark) {
            enumNameMark.drop();
        }

        storeName(moduleName, enumName, ElementType.Enum);
        if (token() == SliceTokenTypes.LBRACE) {
            advance();
        } else {
            mark().error(IceErrorMessages.message("left.brace.is.required"));
        }

        Set<String> values = new HashSet<String>();
        while (!eof() && token() != SliceTokenTypes.RBRACE) {
            values.addAll(parseEnumValues(moduleName));
        }

        Map<String, Set<String>> enums = enumConstants.get(moduleName);
        if (enums == null) {
            enums = new HashMap<String, Set<String>>();
            enumConstants.put(moduleName, enums);
        }
        enums.put(enumName, values);

        checkBlockEnd();
    }

    private void parseModule(String modulePrefix) {
        final PsiBuilder.Marker moduleError = mark();
        // We already set a mark ICE_MODULE
        advance();

        String moduleName = readIdentifier();

        final PsiBuilder.Marker moduleErrorEnd = mark();
        if (token() == SliceTokenTypes.LBRACE) {
            advance();
        } else {
            mark().error(IceErrorMessages.message("left.brace.is.required"));
        }

        final String fullModuleName = modulePrefix + "." + moduleName;

        while (!eof() && token() != SliceTokenTypes.RBRACE) {
            parseBlock(false, fullModuleName);
        }

        if (forwardDeclarations.containsKey(fullModuleName) && !forwardDeclarations.get(fullModuleName).isEmpty()) {
            moduleError.errorBefore(IceErrorMessages.message("missed.declarations"), moduleErrorEnd);
        } else {
            moduleError.drop();
        }
        moduleErrorEnd.drop();

        checkBlockEnd();
    }

    private void parseClass(String moduleName) {
        advance();

        final PsiBuilder.Marker classNameMark = mark();
        boolean dropErrorMark = true;
        String className = readIdentifier();
        if (className != null) {
            if (isTypeDeclared(moduleName, className, null) &&
                    !isForwardDeclared(moduleName, className, ElementType.Class)) {
                classNameMark.error(IceErrorMessages.message("already.defined"));
                dropErrorMark = false;
            } else if (Checker.isKeywordString(className)) {
                classNameMark.error(IceErrorMessages.message("reserved.word"));
                dropErrorMark = false;
            }
        }

        if (token() == SliceTokenTypes.SEMICOLON) {
            // Forward declaration
            if (isForwardDeclared(moduleName, className, ElementType.Class)) {
                classNameMark.error(IceErrorMessages.message("already.defined"));
            } else {
                classNameMark.drop();
            }

            forwardDeclareName(moduleName, className, ElementType.Class);

            advance();
            return;
        }

        if (dropErrorMark) {
            classNameMark.drop();
        }

        storeName(moduleName, className, ElementType.Class);
        if (token() == SliceTokenTypes.KEYWORD_EXTENDS) {
            // Read extends declaration
            advance();

            final PsiBuilder.Marker superClass = mark();
            final PsiBuilder.Marker extendingClassNameMark = mark();
            String extendingClassName = readIdentifier(IceErrorMessages.message("superclass.name.required"));

            if (isTypeDeclared(moduleName, extendingClassName, ElementType.Class) ||
                    isForwardDeclared(moduleName, extendingClassName, ElementType.Class)) {
                extendingClassNameMark.drop();
            } else {
                extendingClassNameMark.error(IceErrorMessages.message("invalid.class"));
            }

            superClass.done(SliceElementTypes.ICE_CLASS_SUPER_CLASS);
            advance();
        }

        if (token() == SliceTokenTypes.KEYWORD_IMPLEMENTS) {
            advance();
            // Read extends declaration
            final PsiBuilder.Marker implementList = mark();

            do {
                final PsiBuilder.Marker implMark = mark();
                String implementingInterfaceName = readIdentifier(IceErrorMessages.message("superclass.name.required"));

                if (implementingInterfaceName == null ||
                        isTypeDeclared(moduleName, implementingInterfaceName, ElementType.Interface) ||
                        isForwardDeclared(moduleName, implementingInterfaceName, ElementType.Interface)) {
                    implMark.drop();
                } else {
                    implMark.error(IceErrorMessages.message("invalid.interface"));
                }

                advance();
                if (token() != SliceTokenTypes.COMMA) {
                    break;
                }

                advance();
            } while (true);


            implementList.done(SliceElementTypes.ICE_CLASS_INTERFACE_LIST);
            advance();
        }

        if (token() == SliceTokenTypes.LBRACE) {
            advance();
        } else {
            mark().error(IceErrorMessages.message("left.brace.is.required"));
        }

        while (!eof() && token() != SliceTokenTypes.RBRACE) {
            parseClassBody(moduleName, false);
        }

        checkBlockEnd();
    }

    private void parseSequence(String moduleName) {
        advance();

        final PsiBuilder.Marker subTypeMark = mark();
        boolean subTypeError = false;
        if (token() == SliceTokenTypes.LT) {
            advance();
        } else {
            subTypeError = true;
        }

        IElementType token = token();
        if (Checker.isTypeKeyword(token) && token != SliceTokenTypes.KEYWORD_VOID ||
                isTypeDeclared(moduleName, tokenText(), null) ||
                isForwardDeclared(moduleName, tokenText(), null)) {
            // Store type ?
            advance();
        } else {
            subTypeError = true;
        }

        if (token() == SliceTokenTypes.GT) {
            advance();
        } else {
            subTypeError = true;
        }

        if (subTypeError) {
            subTypeMark.error(IceErrorMessages.message("datatype.expected"));
        } else {
            subTypeMark.drop();
        }

        String sequenceName = readIdentifier();
        final PsiBuilder.Marker seqNameMark = mark();
        if (sequenceName != null) {
            if (isTypeDeclared(moduleName, sequenceName, null) || isForwardDeclared(moduleName, sequenceName, null)) {
                seqNameMark.error(IceErrorMessages.message("already.defined"));
            } else {
                seqNameMark.drop();
            }
        } else {
            seqNameMark.drop();
        }

        storeName(moduleName, sequenceName, ElementType.Sequence);

        if (token() == SliceTokenTypes.SEMICOLON) {
            advance();
        } else {
            mark().error(IceErrorMessages.message("semicolon.is.required"));
        }
    }

    private void parseDictionary(String moduleName) {
        advance();

        final PsiBuilder.Marker subTypeMark = mark();
        boolean subTypeError = false;
        if (token() == SliceTokenTypes.LT) {
            advance();
        } else {
            subTypeError = true;
        }

        if (Checker.isTypeKeyword(token()) && token() != SliceTokenTypes.KEYWORD_VOID ||
                isTypeDeclared(moduleName, tokenText(), null) ||
                isForwardDeclared(moduleName, tokenText(), null)) {
            // Store type ?
            advance();
        } else {
            subTypeError = true;
        }

        if (token() == SliceTokenTypes.COMMA) {
            advance();
        } else {
            mark().error(IceErrorMessages.message("coma.is.required"));
        }

        if (Checker.isTypeKeyword(token()) && token() != SliceTokenTypes.KEYWORD_VOID ||
                isTypeDeclared(moduleName, tokenText(), null) ||
                isForwardDeclared(moduleName, tokenText(), null)) {
            // Store type ?
            advance();
        } else {
            subTypeError = true;
        }

        if (token() == SliceTokenTypes.GT) {
            advance();
        } else {
            subTypeError = true;
        }

        if (subTypeError) {
            subTypeMark.error(IceErrorMessages.message("datatype.expected"));
        } else {
            subTypeMark.drop();
        }

        String dictionaryName = readIdentifier();
        final PsiBuilder.Marker seqNameMark = mark();
        if (dictionaryName != null) {
            if (isTypeDeclared(moduleName, dictionaryName, null) || isForwardDeclared(moduleName, dictionaryName, null)) {
                seqNameMark.error(IceErrorMessages.message("already.defined"));
            } else {
                seqNameMark.drop();
            }
        } else {
            seqNameMark.drop();
        }

        storeName(moduleName, dictionaryName, ElementType.Dictionary);

        if (token() == SliceTokenTypes.SEMICOLON) {
            advance();
        } else {
            mark().error(IceErrorMessages.message("semicolon.is.required"));
        }
    }

    private void parseStruct(String moduleName) {
        advance();

        final PsiBuilder.Marker structNameMark = mark();
        boolean dropErrorMark = true;
        String structName = readIdentifier();
        if (structName != null) {
            if (isTypeDeclared(moduleName, structName, null) &&
                    !isForwardDeclared(moduleName, structName, ElementType.Struct)) {
                structNameMark.error(IceErrorMessages.message("already.defined"));
                dropErrorMark = false;
            } else if (Checker.isKeywordString(structName)) {
                structNameMark.error(IceErrorMessages.message("reserved.word"));
                dropErrorMark = false;
            }
        }

        if (token() == SliceTokenTypes.SEMICOLON) {
            // Forward declaration
            if (isForwardDeclared(moduleName, structName, ElementType.Struct)) {
                structNameMark.error(IceErrorMessages.message("already.defined"));
            } else {
                structNameMark.drop();
            }

            forwardDeclareName(moduleName, structName, ElementType.Struct);

            advance();
            return;
        }

        if (dropErrorMark) {
            structNameMark.drop();
        }

        storeName(moduleName, structName, ElementType.Struct);

        if (token() == SliceTokenTypes.LBRACE) {
            advance();
        } else {
            mark().error(IceErrorMessages.message("left.brace.is.required"));
        }

        while (!eof() && token() != SliceTokenTypes.RBRACE) {
            parseStructBody(moduleName);
        }

        checkBlockEnd();
    }

    private void checkBlockEnd() {
        if (token() == SliceElementTypes.RBRACE) {
            advance();
        } else {
            mark().error(IceErrorMessages.message("right.brace.is.required"));
        }

        if (token() == SliceTokenTypes.SEMICOLON) {
            advance();
        } else {
            mark().error(IceErrorMessages.message("semicolon.is.required"));
        }
    }

    private Set<String> parseEnumValues(String moduleName) {
        Set<String> valueSet = new HashSet<String>();

        final PsiBuilder.Marker line = mark();
        boolean expectComa = false;

        while (!eof() && token() != SliceTokenTypes.SEMICOLON && token() != SliceTokenTypes.RBRACE) {
            if (expectComa) {
                if (token() != SliceTokenTypes.COMMA) {
                    mark().error(IceErrorMessages.message("coma.is.required"));
                } else {
                    advance();
                }
            } else {
                expectComa = true;
            }

            final PsiBuilder.Marker mark = mark();
            if (token() == SliceTokenTypes.IDENTIFIER) {
                String enumValue = tokenText();

                advance();
                if (dataTypeExists(moduleName, enumValue)) {
                    mark.error(IceErrorMessages.message("name.is.already.used"));
                } else if (valueSet.contains(enumValue)) {
                    mark.error(IceErrorMessages.message("name.is.already.used"));
                } else {
                    valueSet.add(enumValue);
                    mark.done(SliceElementTypes.ICE_ENUM_CONSTANT);
                }
            } else if (Checker.isKeyword(token())) {
                advance();
                mark.error(IceErrorMessages.message("name.is.already.used"));
            } else {
                mark().error(IceErrorMessages.message("identifier.required"));
            }
        }

        line.done(SliceElementTypes.ICE_ENUM_CONSTANT_LIST);

        return valueSet;
    }

    private void parseStructBody(String moduleName) {
        IElementType type;

        final PsiBuilder.Marker line = mark();

        if (token() == SliceTokenTypes.IDENTIFIER) {
            final PsiBuilder.Marker mark = mark();
            String typeName = tokenText();

            advance();
            if (!dataTypeExists(moduleName, typeName)) {
                mark.error(IceErrorMessages.message("invalid.datatype"));
            } else {
                mark.drop();
            }
        } else if (Checker.isTypeKeyword(token())) {
            advance();
        } else {
            mark().error(IceErrorMessages.message("datatype.expected"));
        }

        String name = readIdentifier();

        if (token() == SliceTokenTypes.EQUAL || token() == SliceTokenTypes.SEMICOLON) {
            type = SliceElementTypes.ICE_CLASS_FIELD_DECLARATION;
        } else {
            type = null;
        }

        while (!eof() && token() != SliceTokenTypes.SEMICOLON && token() != SliceTokenTypes.RBRACE) {
            advance();
        }

        if (type != null) {
            line.done(type);
            advance();
        } else {
            line.error(IceErrorMessages.message("invalid.declaration"));
            if (name == null) {
                advance();
            }
        }
    }

    private void parseClassBody(String moduleName, boolean onlyMethods) {
        IElementType type;

        final PsiBuilder.Marker line = mark();

        if (token() == SliceTokenTypes.KEYWORD_IDEMPOTENT) {
            advance();
        }

        if (token() == SliceTokenTypes.IDENTIFIER) {
            final PsiBuilder.Marker mark = mark();
            String typeName = tokenText();

            advance();
            if (!dataTypeExists(moduleName, typeName)) {
                mark.error(IceErrorMessages.message("invalid.datatype"));
            } else {
                mark.drop();
            }
        } else if (Checker.isTypeKeyword(token())) {
            advance();
        } else {
            mark().error(IceErrorMessages.message("datatype.expected"));
        }

        String name = readIdentifier();

        if (token() == SliceTokenTypes.EQUAL || token() == SliceTokenTypes.SEMICOLON) {
            if (onlyMethods) {
                type = null;
            } else {
                type = SliceElementTypes.ICE_CLASS_FIELD_DECLARATION;
            }
        } else if (token() == SliceTokenTypes.LPARENTH) {
            type = SliceElementTypes.ICE_CLASS_METHOD_DECLARATION;
        } else {
            type = null;
        }

        while (!eof() && token() != SliceTokenTypes.SEMICOLON && token() != SliceTokenTypes.RBRACE) {
            advance();
        }

        if (type != null) {
            line.done(type);
            advance();
        } else {
            line.error(IceErrorMessages.message("invalid.declaration"));
            if (name == null) {
                advance();
            }
        }

    }

    private void parseInterface(String moduleName) {
        advance();
        final PsiBuilder.Marker interfaceNameMark = mark();
        boolean dropErrorMark = true;
        String interfaceName = readIdentifier();

        if (interfaceName != null) {
            if (isTypeDeclared(moduleName, interfaceName, null) &&
                    !isForwardDeclared(moduleName, interfaceName, ElementType.Interface)) {
                interfaceNameMark.error("already.defined");
                dropErrorMark = false;
            }
        }

        if (token() == SliceTokenTypes.SEMICOLON) {
            // Forward declaration
            if (isForwardDeclared(moduleName, interfaceName, ElementType.Interface)) {
                interfaceNameMark.error(IceErrorMessages.message("already.defined"));
            } else {
                interfaceNameMark.drop();
            }

            forwardDeclareName(moduleName, interfaceName, ElementType.Interface);

            advance();
            return;
        }

        if (dropErrorMark) {
            interfaceNameMark.drop();
        }

        storeName(moduleName, interfaceName, ElementType.Interface);
        if (token() == SliceTokenTypes.KEYWORD_EXTENDS) {
            // Read extends declaration
            advance();
            final PsiBuilder.Marker superInterfaces = mark();

            do {
                final PsiBuilder.Marker implInt = mark();
                String implementingInterfaceName = readIdentifier(IceErrorMessages.message("superclass.name.required"));

                if (implementingInterfaceName != null &&
                        !isTypeDeclared(moduleName, implementingInterfaceName, ElementType.Interface)) {
                    implInt.error(IceErrorMessages.message("invalid.interface"));
                } else {
                    implInt.drop();
                }

                if (token() != SliceTokenTypes.COMMA) {
                    break;
                }

                advance();
            } while (true);


            superInterfaces.done(SliceElementTypes.ICE_CLASS_INTERFACE_LIST);
        }

        if (token() == SliceTokenTypes.LBRACE) {
            advance();
        } else {
            mark().error(IceErrorMessages.message("left.brace.is.required"));
        }

        while (!eof() && token() != SliceTokenTypes.RBRACE) {
            parseClassBody(moduleName, true);
        }

        checkBlockEnd();
    }

    private void parseException(String moduleName) {
        advance();
        final PsiBuilder.Marker exceptionNameMark = mark();
        boolean dropErrorMark = true;
        String exceptionName = readIdentifier();

        if (exceptionName != null) {
            if (isTypeDeclared(moduleName, exceptionName, null) &&
                    !isForwardDeclared(moduleName, exceptionName, ElementType.Exception)) {
                exceptionNameMark.error("already.defined");
                dropErrorMark = false;
            }
        }

        if (token() == SliceTokenTypes.SEMICOLON) {
            // Forward declaration
            if (isForwardDeclared(moduleName, exceptionName, ElementType.Exception)) {
                exceptionNameMark.error(IceErrorMessages.message("already.defined"));
            } else {
                exceptionNameMark.drop();
            }

            forwardDeclareName(moduleName, exceptionName, ElementType.Class);

            advance();
            return;
        }

        if (dropErrorMark) {
            exceptionNameMark.drop();
        }

        storeName(moduleName, exceptionName, ElementType.Exception);
        if (token() == SliceTokenTypes.KEYWORD_EXTENDS) {
            // Read extends declaration
            advance();
            final PsiBuilder.Marker superExceptions = mark();

            final PsiBuilder.Marker superClass = mark();
            String implementingExceptionName = readIdentifier(IceErrorMessages.message("superclass.name.required"));

            if (implementingExceptionName == null ||
                    isTypeDeclared(moduleName, implementingExceptionName, ElementType.Exception) &&
                            isForwardDeclared(moduleName, exceptionName, ElementType.Exception)
                    ) {
                superClass.drop();
            } else {
                superClass.error(IceErrorMessages.message("invalid.exception"));
            }


            superExceptions.done(SliceElementTypes.ICE_CLASS_SUPER_CLASS);
            advance();
        }

        if (token() == SliceTokenTypes.LBRACE) {
            advance();
        } else {
            mark().error(IceErrorMessages.message("left.brace.is.required"));
        }

        while (!eof() && token() != SliceTokenTypes.RBRACE) {
            parseClassBody(moduleName, false);
        }

        checkBlockEnd();
    }

    private String readIdentifier() {
        return readIdentifier(IceErrorMessages.message("identifier.required"));
    }

    private String readIdentifier(String errorMessage) {
        String identifier;

        if (token() == SliceTokenTypes.IDENTIFIER) {
            identifier = tokenText();
            advance();
        } else {
            identifier = null;
            mark().error(errorMessage);
        }
        return identifier;
    }

    private boolean isTypeDeclared(String moduleName, String typeName, @Nullable ElementType type) {
        final Map<String, ElementType> names = validTypes.get(moduleName);
        if (names == null) {
            return false;
        }

        if (type == null) {
            return names.containsKey(typeName);
        } else {
            return names.get(typeName) == type;
        }
    }

    private boolean isForwardDeclared(String moduleName, String typeName, @Nullable ElementType type) {
        final Map<String, ElementType> names = forwardDeclarations.get(moduleName);

        if (names == null) {
            return false;
        }

        if (type == null) {
            return names.containsKey(typeName);
        } else {
            return names.get(typeName) == type;
        }
    }

    private boolean dataTypeExists(String moduleName, String name) {
        return isTypeDeclared(moduleName, name, null) ||
                Checker.isKeywordString(name);
    }

    private void forwardDeclareName(String moduleName, String className, ElementType type) {
        if (className == null || className.length() == 0) {
            return;
        }

        final Map<String, ElementType> names = forwardDeclarations.get(moduleName);
        if (names == null) {
            Map<String, ElementType> nameList = new HashMap<String, ElementType>();
            nameList.put(className, type);
            forwardDeclarations.put(moduleName, nameList);
        } else {
            names.put(className, type);
        }
    }

    private void storeName(String moduleName, String className, ElementType type) {
        if (className == null || className.length() == 0) {
            return;
        }

        Map<String, ElementType> names = validTypes.get(moduleName);
        if (names == null) {
            names = new HashMap<String, ElementType>();
            validTypes.put(moduleName, names);
        }

        names.put(className, type);

        final Map<String, ElementType> list = forwardDeclarations.get(moduleName);
        if (list != null) {
            final ElementType wasType = list.remove(className);
            assert wasType == type;
        }
    }

    private void parseMacros() {
        final PsiBuilder.Marker macros = mark();

        while (!eof() && token() == SliceTokenTypes.MACROS_LINE) {
            advance();
        }

        macros.done(SliceElementTypes.ICE_MACROS);
    }

    private PsiBuilder.Marker mark() {
        return builder.mark();
    }

    private void error(String messageText) {
        builder.error(messageText);
    }

    private boolean eof() {
        return builder.eof();
    }

    private String tokenText() {
        return builder.getTokenText();
    }

    @Nullable
    private IElementType token() {
        return builder.getTokenType();
    }

    private void advance() {
        builder.advanceLexer();
    }

    private static enum ElementType {
        Class,
        Interface,
        Enum,
        Exception,
        Struct,
        Sequence,
        Dictionary,
        DataType
    }
}
