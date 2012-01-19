package org.xblackcat.frozenice.psi;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenice.util.IceErrorMessages;

import java.util.HashMap;
import java.util.Map;

/**
 * 06.01.12 11:48
 *
 * @author xBlackCat
 */
public class SliceParser {
    private final Map<String, Map<String, ElementType>> validTypes = new HashMap<String, Map<String, ElementType>>();
    private final Map<String, Map<String, ElementType>> forwardDeclarations = new HashMap<String, Map<String, ElementType>>();

    private final PsiBuilder builder;

    public SliceParser(PsiBuilder builder) {
        this.builder = builder;
        builder.setDebugMode(true);
    }


    public void parseBlock() {
        parseBlock(true, "");
    }

    public void parseBlock(boolean topLevel, String modulePrefix) {
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

                parseModule(modulePrefix);
            } else if (!topLevel) {
                if (Checker.isClassToken(token)) {
                    type = SliceElementTypes.ICE_CLASS;

                    parseClass(modulePrefix);
                } else if (Checker.isInterfaceToken(token)) {
                    type = SliceElementTypes.ICE_INTERFACE;

                    parseInterface(modulePrefix);
                } else if (Checker.isEnumToken(token)) {
                    type = SliceElementTypes.ICE_ENUM;

                    parseEnum(modulePrefix);
                } else if (Checker.isExceptionToken(token)) {
                    type = SliceElementTypes.ICE_EXCEPTION;

                    parseException(modulePrefix);
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

    private void skipBlock() {
        int bracesCount = 0;
        boolean meetsLBrace = false;
        while (!eof()) {
            if (token() == SliceTokenTypes.RBRACE) {
                --bracesCount;
                if (meetsLBrace) {
                    if (bracesCount <= 0) {
                        advance();
                        break;
                    }
                } else if (bracesCount < 0) {
                    break;
                }
            }
            if (token() == SliceTokenTypes.LBRACE) {
                ++bracesCount;
                meetsLBrace = true;
            }

            advance();
        }

        if (token() == SliceTokenTypes.SEMICOLON) {
            advance();
        } else {
            mark().error(IceErrorMessages.message("semicolon.is.required"));
        }
    }

    private void parseEnum(String modulePrefix) {
        advance();

        final PsiBuilder.Marker classNameMark = mark();
        boolean dropErrorMark = true;
        String enumName = readIdentifier(IceErrorMessages.message("identifier.required"));
        if (enumName != null) {
            if (isTypeDeclared(modulePrefix, enumName, null) &&
                    !isForwardDeclared(modulePrefix, enumName, ElementType.Enum)) {
                classNameMark.error(IceErrorMessages.message("already.defined"));
                dropErrorMark = false;
            } else if (Checker.isKeywordString(enumName)) {
                classNameMark.error(IceErrorMessages.message("reserved.word"));
                dropErrorMark = false;
            }
        }

        if (token() == SliceTokenTypes.SEMICOLON) {
            // Forward declaration
            if (isForwardDeclared(modulePrefix, enumName, ElementType.Enum)) {
                classNameMark.error(IceErrorMessages.message("already.defined"));
            } else {
                classNameMark.drop();
            }

            forwardDeclareName(modulePrefix, enumName, ElementType.Enum);

            advance();
            return;
        }

        if (dropErrorMark) {
            classNameMark.drop();
        }

        storeName(modulePrefix, enumName, ElementType.Enum);
        if (token() == SliceTokenTypes.LBRACE) {
            advance();
        } else {
            mark().error(IceErrorMessages.message("left.brace.is.required"));
        }

        while (!eof() && token() != SliceTokenTypes.RBRACE) {
            parseEnumValues(modulePrefix);
        }

        checkBlockEnd();
    }

    private void parseModule(String modulePrefix) {
        final PsiBuilder.Marker moduleError = mark();
        // We already set a mark ICE_MODULE
        advance();

        String moduleName = readIdentifier(IceErrorMessages.message("identifier.required"));

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
        String className = readIdentifier(IceErrorMessages.message("identifier.required"));
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

    private void parseEnumValues(String moduleName) {
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
                } else {
//                    valueSet.add(enumValue);
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

        String name = readIdentifier(IceErrorMessages.message("identifier.required"));

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
        String interfaceName = readIdentifier(IceErrorMessages.message("identifier.required"));

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

            forwardDeclareName(moduleName, interfaceName, ElementType.Class);

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
            advance();
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
        String exceptionName = readIdentifier(IceErrorMessages.message("identifier.required"));

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

    private boolean isTypeDeclared(String moduleName, String className, @Nullable ElementType type) {
        final Map<String, ElementType> names = validTypes.get(moduleName);
        if (names == null) {
            return false;
        }

        if (type == null) {
            return names.containsKey(className);
        } else {
            return names.get(className) == type;
        }
    }

    private boolean isForwardDeclared(String moduleName, String className, ElementType type) {
        final Map<String, ElementType> names = forwardDeclarations.get(moduleName);
        return names != null && names.get(className) == type;
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
        DataType
    }
}
