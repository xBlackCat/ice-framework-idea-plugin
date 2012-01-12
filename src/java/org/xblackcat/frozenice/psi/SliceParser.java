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
    private final Map<String, Set<String>> validInterfaces = new HashMap<String, Set<String>>();
    private final Map<String, Set<String>> validClasses = new HashMap<String, Set<String>>();
    private final Map<String, Set<String>> forwardDeclarations = new HashMap<String, Set<String>>();

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

                    // TODO: implement
                    skipBlock();
                } else if (Checker.isExceptionToken(token)) {
                    type = SliceElementTypes.ICE_EXCEPTION;

                    // TODO: implement
                    skipBlock();
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
        while (!eof()) {
            if (token() == SliceTokenTypes.RBRACE) {
                --bracesCount;
                if (bracesCount < 0) {
                    break;
                }
            }
            if (token() == SliceTokenTypes.LBRACE) {
                ++bracesCount;
            }

            advance();
        }
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

        advance();

        if (token() == SliceTokenTypes.SEMICOLON) {
            advance();
        } else {
            mark().error(IceErrorMessages.message("semicolon.is.required"));
        }
    }

    private void parseClass(String moduleName) {
        advance();

        final PsiBuilder.Marker classNameMark = mark();
        boolean dropErrorMark = true;
        String className = readIdentifier(IceErrorMessages.message("identifier.required"));
        if (className != null) {
            if (isInterfaceDeclared(moduleName, className) ||
                    isClassDeclared(moduleName, className) && !isClassForwardDeclared(moduleName, className)) {
                classNameMark.error(IceErrorMessages.message("already.defined"));
                dropErrorMark = false;
            }
        }

        if (token() == SliceTokenTypes.SEMICOLON) {
            // Forward declaration
            if (isClassForwardDeclared(moduleName, className)) {
                classNameMark.error(IceErrorMessages.message("already.defined"));
            } else {
                classNameMark.drop();
            }

            forwardDeclareName(moduleName, className);

            advance();
            return;
        }

        if (dropErrorMark) {
            classNameMark.drop();
        }

        storeClassName(moduleName, className);
        if (token() == SliceTokenTypes.KEYWORD_EXTENDS) {
            // Read extends declaration
            advance();

            final PsiBuilder.Marker superClass = mark();
            final PsiBuilder.Marker extendingClassNameMark = mark();
            String extendingClassName = readIdentifier(IceErrorMessages.message("superclass.name.required"));

            if (!isClassDeclared(moduleName, extendingClassName)) {
                extendingClassNameMark.error(IceErrorMessages.message("invalid.class"));
            } else {
                extendingClassNameMark.drop();
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

                if (implementingInterfaceName != null && !isInterfaceDeclared(moduleName, implementingInterfaceName)) {
                    implMark.error(IceErrorMessages.message("invalid.interface"));
                } else {
                    implMark.drop();
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

        advance();

        if (token() == SliceTokenTypes.SEMICOLON) {
            advance();
        } else {
            mark().error(IceErrorMessages.message("semicolon.is.required"));
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

        while (!eof() && token() != SliceTokenTypes.SEMICOLON  && token() != SliceTokenTypes.RBRACE) {
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
            if (isInterfaceDeclared(moduleName, interfaceName) && !isClassForwardDeclared(moduleName, interfaceName) ||
                    isClassDeclared(moduleName, interfaceName)) {
                interfaceNameMark.error("already.defined");
                dropErrorMark = false;
            }
        }

        if (token() == SliceTokenTypes.SEMICOLON) {
            // Forward declaration
            if (isClassForwardDeclared(moduleName, interfaceName)) {
                interfaceNameMark.error(IceErrorMessages.message("already.defined"));
            } else {
                interfaceNameMark.drop();
            }

            forwardDeclareName(moduleName, interfaceName);

            advance();
            return;
        }

        if (dropErrorMark) {
            interfaceNameMark.drop();
        }


        if (token() == SliceTokenTypes.KEYWORD_EXTENDS) {
            // Read extends declaration
            advance();
            final PsiBuilder.Marker superInterfaces = mark();

            do {
                final PsiBuilder.Marker implInt = mark();
                String implementingInterfaceName = readIdentifier(IceErrorMessages.message("superclass.name.required"));

                if (implementingInterfaceName != null &&
                        (!isInterfaceDeclared(moduleName, implementingInterfaceName) || isClassDeclared(moduleName, implementingInterfaceName))
                        ) {
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

        advance();

        if (token() == SliceTokenTypes.SEMICOLON) {
            advance();
        } else {
            mark().error(IceErrorMessages.message("semicolon.is.required"));
        }
    }

    private String readIdentifier(String errorMessage) {
        String moduleName;

        if (token() == SliceTokenTypes.IDENTIFIER) {
            moduleName = tokenText();
            advance();
        } else {
            moduleName = null;
            mark().error(errorMessage);
        }
        return moduleName;
    }

    private boolean isClassDeclared(String moduleName, String className) {
        final Set<String> names = validClasses.get(moduleName);
        return names != null && names.contains(className);
    }

    private boolean isInterfaceDeclared(String moduleName, String interfaceName) {
        final Set<String> names = validInterfaces.get(moduleName);
        return names != null && names.contains(interfaceName);
    }

    private boolean isClassForwardDeclared(String moduleName, String className) {
        final Set<String> names = forwardDeclarations.get(moduleName);
        return names != null && names.contains(className);
    }

    private boolean dataTypeExists(String moduleName, String name) {
        return isClassDeclared(moduleName, name) || isInterfaceDeclared(moduleName, name) || isClassForwardDeclared(moduleName, name);
    }

    private void forwardDeclareName(String moduleName, String className) {
        if (className == null || className.length() == 0) {
            return;
        }

        final Set<String> names = forwardDeclarations.get(moduleName);
        if (names == null) {
            Set<String> nameList = new HashSet<String>();
            nameList.add(className);
            forwardDeclarations.put(moduleName, nameList);
        } else {
            names.add(className);
        }
    }

    private void storeClassName(String moduleName, String className) {
        if (className == null || className.length() == 0) {
            return;
        }

        final Set<String> names = validClasses.get(moduleName);
        if (names == null) {
            Set<String> nameList = new HashSet<String>();
            nameList.add(className);
            validClasses.put(moduleName, nameList);
        } else {
            names.add(className);
        }

        final Set<String> list = forwardDeclarations.get(moduleName);
        if (list != null) {
            list.remove(className);
        }
    }

    private void storeInterface(String moduleName, String interfaceName) {
        if (interfaceName == null || interfaceName.length() == 0) {
            return;
        }

        final Set<String> names = validInterfaces.get(moduleName);
        if (names == null) {
            Set<String> nameList = new HashSet<String>();
            nameList.add(interfaceName);
            validInterfaces.put(moduleName, nameList);
        } else {
            names.add(interfaceName);
        }

        final Set<String> list = forwardDeclarations.get(moduleName);
        if (list != null) {
            list.remove(interfaceName);
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
}
