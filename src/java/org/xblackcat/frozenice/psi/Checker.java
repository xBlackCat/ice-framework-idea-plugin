package org.xblackcat.frozenice.psi;

import com.intellij.psi.tree.IElementType;

/**
 * 10.01.12 16:34
 *
 * @author xBlackCat
 */
public class Checker {
    static boolean isTypeKeyword(IElementType token) {
        return token == SliceTokenTypes.KEYWORD_BOOL ||
                token == SliceTokenTypes.KEYWORD_DOUBLE ||
                token == SliceTokenTypes.KEYWORD_BYTE ||
                token == SliceTokenTypes.KEYWORD_FLOAT ||
                token == SliceTokenTypes.KEYWORD_INT ||
                token == SliceTokenTypes.KEYWORD_LONG ||
                token == SliceTokenTypes.KEYWORD_SHORT ||
                token == SliceTokenTypes.KEYWORD_STRING ||
                token == SliceTokenTypes.KEYWORD_OBJECT ||
                token == SliceTokenTypes.KEYWORD_VOID;
    }

    static boolean isExceptionToken(IElementType token) {
        return token == SliceTokenTypes.KEYWORD_EXCEPTION;
    }

    static boolean isEnumToken(IElementType token) {
        return token == SliceElementTypes.KEYWORD_ENUM;
    }

    static boolean isInterfaceToken(IElementType token) {
        return token == SliceTokenTypes.KEYWORD_INTERFACE;
    }

    static boolean isClassToken(IElementType token) {
        return token == SliceTokenTypes.KEYWORD_CLASS;
    }

    static boolean isModuleToken(IElementType token) {
        return token == SliceTokenTypes.KEYWORD_MODULE;
    }

    static boolean isMacroDefinition(IElementType token) {
        return token == SliceTokenTypes.MACROS_LINE;
    }

    static boolean isCommentToken(IElementType token) {
        return token == SliceTokenTypes.C_STYLE_COMMENT || token == SliceTokenTypes.END_OF_LINE_COMMENT;
    }

    static boolean isKeyword(IElementType token) {
        return SliceTokenTypes.KEYWORD_BIT_SET.contains(token);
    }

    static boolean isKeywordString(String name) {
        return "BOOL".equalsIgnoreCase(name) ||
                "EXCEPTION".equalsIgnoreCase(name) ||
                "INTERFACE".equalsIgnoreCase(name) ||
                "SEQUENCE".equalsIgnoreCase(name) ||
                "BYTE".equalsIgnoreCase(name) ||
                "EXTENDS".equalsIgnoreCase(name) ||
                "LOCAL".equalsIgnoreCase(name) ||
                "SHORT".equalsIgnoreCase(name) ||
                "CLASS".equalsIgnoreCase(name) ||
                "FALSE".equalsIgnoreCase(name) ||
                "LOCALOBJECT".equalsIgnoreCase(name) ||
                "STRING".equalsIgnoreCase(name) ||
                "CONST".equalsIgnoreCase(name) ||
                "FLOAT".equalsIgnoreCase(name) ||
                "LONG".equalsIgnoreCase(name) ||
                "STRUCT".equalsIgnoreCase(name) ||
                "DICTIONARY".equalsIgnoreCase(name) ||
                "IDEMPOTENT".equalsIgnoreCase(name) ||
                "MODULE".equalsIgnoreCase(name) ||
                "THROWS".equalsIgnoreCase(name) ||
                "DOUBLE".equalsIgnoreCase(name) ||
                "IMPLEMENTS".equalsIgnoreCase(name) ||
                "OBJECT".equalsIgnoreCase(name) ||
                "TRUE".equalsIgnoreCase(name) ||
                "ENUM".equalsIgnoreCase(name) ||
                "INT".equalsIgnoreCase(name) ||
                "OUT".equalsIgnoreCase(name) ||
                "VOID".equalsIgnoreCase(name);
    }
}
