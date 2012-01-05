package org.xblackcat.frozenice.psi;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * 04.01.12 12:23
 *
 * @author xBlackCat
 */
public interface SliceTokenTypes extends TokenType {
    IElementType IDENTIFIER = new SliceElementType("Ice.Identifier");
    IElementType C_STYLE_COMMENT = new SliceElementType("Ice.C_Comment");
    IElementType MACROS_LINE = new SliceElementType("Ice.MacrosLine");
    IElementType END_OF_LINE_COMMENT = new SliceElementType("Ice.EnfOfLineComment");

    IElementType KEYWORD_FALSE = new SliceElementType("Ice.Keyword.false");
    IElementType KEYWORD_TRUE = new SliceElementType("Ice.Keyword.true");

    IElementType KEYWORD_BOOL = new SliceElementType("Ice.Keyword.bool");
    IElementType KEYWORD_EXCEPTION = new SliceElementType("Ice.Keyword.exception");
    IElementType KEYWORD_INTERFACE = new SliceElementType("Ice.Keyword.interface");
    IElementType KEYWORD_SEQUENCE = new SliceElementType("Ice.Keyword.sequence");
    IElementType KEYWORD_BYTE = new SliceElementType("Ice.Keyword.byte");
    IElementType KEYWORD_EXTENDS = new SliceElementType("Ice.Keyword.extends");
    IElementType KEYWORD_LOCAL = new SliceElementType("Ice.Keyword.local");
    IElementType KEYWORD_SHORT = new SliceElementType("Ice.Keyword.short");
    IElementType KEYWORD_CLASS = new SliceElementType("Ice.Keyword.class");
    IElementType KEYWORD_LOCALOBJECT = new SliceElementType("Ice.Keyword.LocalObject");
    IElementType KEYWORD_STRING = new SliceElementType("Ice.Keyword.string");
    IElementType KEYWORD_CONST = new SliceElementType("Ice.Keyword.const");
    IElementType KEYWORD_FLOAT = new SliceElementType("Ice.Keyword.float");
    IElementType KEYWORD_LONG = new SliceElementType("Ice.Keyword.long");
    IElementType KEYWORD_STRUCT = new SliceElementType("Ice.Keyword.struct");
    IElementType KEYWORD_DICTIONARY = new SliceElementType("Ice.Keyword.dictionary");
    IElementType KEYWORD_IDEMPOTENT = new SliceElementType("Ice.Keyword.idempotent");
    IElementType KEYWORD_MODULE = new SliceElementType("Ice.Keyword.module");
    IElementType KEYWORD_THROWS = new SliceElementType("Ice.Keyword.throws");
    IElementType KEYWORD_DOUBLE = new SliceElementType("Ice.Keyword.double");
    IElementType KEYWORD_IMPLEMENTS = new SliceElementType("Ice.Keyword.implements");
    IElementType KEYWORD_OBJECT = new SliceElementType("Ice.Keyword.Object");
    IElementType KEYWORD_ENUM = new SliceElementType("Ice.Keyword.enum");
    IElementType KEYWORD_INT = new SliceElementType("Ice.Keyword.int");
    IElementType KEYWORD_OUT = new SliceElementType("Ice.Keyword.out");
    IElementType KEYWORD_VOID = new SliceElementType("Ice.Keyword.void");

    IElementType GT = new SliceElementType("GT");
    IElementType LT = new SliceElementType("LT");
    IElementType LPARENTH = new SliceElementType("LPARENTH");
    IElementType RPARENTH = new SliceElementType("RPARENTH");
    IElementType LBRACE = new SliceElementType("LBRACE");
    IElementType RBRACE = new SliceElementType("RBRACE");
    IElementType LBRACKET = new SliceElementType("LBRACKET");
    IElementType RBRACKET = new SliceElementType("RBRACKET");
    IElementType COMMA = new SliceElementType("COMMA");
    IElementType DOT = new SliceElementType("DOT");
    IElementType SEMICOLON = new SliceElementType("SEMICOLON");
    IElementType ELLIPSIS = new SliceElementType("ELLIPSIS");


    TokenSet KEYWORD_BIT_SET = TokenSet.create(KEYWORD_BOOL, KEYWORD_EXCEPTION, KEYWORD_INTERFACE, KEYWORD_SEQUENCE,
            KEYWORD_BYTE, KEYWORD_EXTENDS, KEYWORD_LOCAL, KEYWORD_SHORT, KEYWORD_CLASS, KEYWORD_FALSE,
            KEYWORD_LOCALOBJECT, KEYWORD_STRING, KEYWORD_CONST, KEYWORD_FLOAT, KEYWORD_LONG, KEYWORD_STRUCT,
            KEYWORD_DICTIONARY, KEYWORD_IDEMPOTENT, KEYWORD_MODULE, KEYWORD_THROWS, KEYWORD_DOUBLE, KEYWORD_IMPLEMENTS,
            KEYWORD_OBJECT, KEYWORD_TRUE, KEYWORD_ENUM, KEYWORD_INT, KEYWORD_OUT, KEYWORD_VOID);
}
