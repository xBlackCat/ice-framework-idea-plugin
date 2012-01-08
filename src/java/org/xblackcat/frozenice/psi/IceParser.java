package org.xblackcat.frozenice.psi;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenice.util.IceErrorMessages;

/**
 * 06.01.12 11:48
 *
 * @author xBlackCat
 */
public class IceParser {
    private final PsiBuilder builder;

    public IceParser(PsiBuilder builder) {
        this.builder = builder;
    }


    public void parseBlock() {
        parseBlock(true, "");
    }

    public void parseBlock(boolean topLevel, String modulePrefix) {
        final PsiBuilder.Marker block = mark();

        IElementType type = null;
        while (!eof()) {
            if (isCommentToken(token())) {
                advance();
                continue;
            }

            if (isMacroDefinition(token())) {
                parseMacros();
                continue;
            }

            final IElementType token = token();
            if (isModuleToken(token)) {
                type = SliceElementTypes.ICE_MODULE;

                parseModule(modulePrefix);
            } else if (!topLevel) {
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

//                if (isClassToken(token)) {
//                    // TODO: parse class
//                } else if (isInterfaceToken(token)) {
//                    // TODO: parse interface
//                } else if (isEnumToken(token)) {
//                    // TODO: parse
//                } else if (isExceptionToken(token)) {
//
//                }
            } else {
                block.error(IceErrorMessages.message("unexpected.token"));
            }
            break;
        }

        if (type != null) {
            block.done(type);
        } else {
            block.drop();
        }
    }

    private void parseModule(String modulePrefix) {
        String moduleName;
        // We already set a mark ICE_MODULE
        advance();
        if (token() == SliceTokenTypes.IDENTIFIER) {
            moduleName = tokenText();
            advance();
        } else {
            moduleName = "";
            mark().error(IceErrorMessages.message("identifier.required"));
        }

        if (token() == SliceTokenTypes.LBRACE) {
            advance();
        } else {
            mark().error(IceErrorMessages.message("left.brace.is.required"));
        }

        while (!eof() && token() != SliceTokenTypes.RBRACE) {
            parseBlock(false, modulePrefix + "." + moduleName);
        }

        advance();

        if (token() == SliceTokenTypes.SEMICOLON) {
            advance();
        } else {
            mark().error(IceErrorMessages.message("semicolon.is.required"));
        }
    }

    private String tokenText() {
        return builder.getTokenText();
    }

    private boolean isExceptionToken(IElementType token) {
        return token == SliceTokenTypes.KEYWORD_EXCEPTION;
    }

    private boolean isEnumToken(IElementType token) {
        return token == SliceElementTypes.KEYWORD_ENUM;
    }

    private boolean isInterfaceToken(IElementType token) {
        return token == SliceTokenTypes.KEYWORD_INTERFACE;
    }

    private boolean isClassToken(IElementType token) {
        return token == SliceTokenTypes.KEYWORD_CLASS;
    }

    private boolean isModuleToken(IElementType token) {
        return token == SliceTokenTypes.KEYWORD_MODULE;
    }

    private void parseMacros() {
        final PsiBuilder.Marker macros = mark();

        while (!eof() && token() == SliceTokenTypes.MACROS_LINE) {
            advance();
        }

        macros.done(SliceElementTypes.ICE_MACROS);
    }

    private boolean isMacroDefinition(IElementType token) {
        return token == SliceTokenTypes.MACROS_LINE;
    }

    private boolean isCommentToken(IElementType token) {
        return token == SliceTokenTypes.C_STYLE_COMMENT || token == SliceTokenTypes.END_OF_LINE_COMMENT;
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

    @Nullable
    private IElementType token() {
        return builder.getTokenType();
    }

    private void advance() {
        builder.advanceLexer();
    }
}
