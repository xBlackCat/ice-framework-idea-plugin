package org.xblackcat.frozenidea.highlight;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.ElementType;
import org.xblackcat.frozenidea.parser.SliceLexer;
import org.xblackcat.frozenidea.psi.SliceTypes;

import java.util.HashMap;
import java.util.Map;

/**
 * 04.01.12 12:08
 *
 * @author xBlackCat
 */
public class SliceSyntaxHighlighter extends SyntaxHighlighterBase {
    private static final Map<IElementType, TextAttributesKey> keys1;
    private static final Map<IElementType, TextAttributesKey> keys2;

    static {
        keys1 = new HashMap<>();
        keys2 = new HashMap<>();

        SyntaxHighlighterBase.fillMap(keys1, ElementType.KEYWORD_BIT_SET, SliceHighlighterColors.ICE_KEYWORD);

        keys1.put(SliceTypes.ICE_STRING_VALUE, SliceHighlighterColors.ICE_STRING_LITERAL);
        keys1.put(SliceTypes.ICE_FLOAT_VALUE, SliceHighlighterColors.ICE_NUMERIC_LITERAL);
        keys1.put(SliceTypes.ICE_INTEGER_VALUE, SliceHighlighterColors.ICE_NUMERIC_LITERAL);

        keys1.put(SliceTypes.ICE_DOC_STYLE_COMMENT, SliceHighlighterColors.ICE_DOC_STYLE_COMMENT);
        keys1.put(SliceTypes.ICE_C_STYLE_COMMENT, SliceHighlighterColors.ICE_C_STYLE_COMMENT);
        keys1.put(SliceTypes.ICE_END_OF_LINE_COMMENT, SliceHighlighterColors.ICE_END_OF_LINE_COMMENT);
        keys1.put(SliceTypes.ICE_DIRECTIVE, SliceHighlighterColors.ICE_DIRECTIVE);
        keys1.put(SliceTypes.ICE_METADATA, SliceHighlighterColors.ICE_DIRECTIVE);
        keys1.put(SliceTypes.ICE_GLOBAL_METADATA, SliceHighlighterColors.ICE_DIRECTIVE);

        keys1.put(SliceTypes.ICE_LEFT_PARENTH, SliceHighlighterColors.ICE_PARENTHESES);
        keys1.put(SliceTypes.ICE_RIGHT_PARENTH, SliceHighlighterColors.ICE_PARENTHESES);

        keys1.put(SliceTypes.ICE_LEFT_BRACE, SliceHighlighterColors.ICE_BRACES);
        keys1.put(SliceTypes.ICE_RIGHT_BRACE, SliceHighlighterColors.ICE_BRACES);

        keys1.put(SliceTypes.ICE_OPEN_GL_MD, SliceHighlighterColors.ICE_BRACKETS);
        keys1.put(SliceTypes.ICE_CLOSE_GL_MD, SliceHighlighterColors.ICE_BRACKETS);
        keys1.put(SliceTypes.ICE_LEFT_BRACKET, SliceHighlighterColors.ICE_BRACKETS);
        keys1.put(SliceTypes.ICE_RIGHT_BRACKET, SliceHighlighterColors.ICE_BRACKETS);

        keys1.put(com.intellij.psi.TokenType.BAD_CHARACTER, HighlighterColors.BAD_CHARACTER);
    }

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new SliceLexer();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        return pack(keys1.get(tokenType), keys2.get(tokenType));
    }
}
