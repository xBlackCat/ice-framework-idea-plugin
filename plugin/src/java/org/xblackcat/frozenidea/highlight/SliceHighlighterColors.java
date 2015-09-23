package org.xblackcat.frozenidea.highlight;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;

/**
 * 23.09.2015 13:12
 *
 * @author xBlackCat
 */
public class SliceHighlighterColors {
    private SliceHighlighterColors() {
    }

    public static final TextAttributesKey ICE_KEYWORD =
            TextAttributesKey.createTextAttributesKey("ICE_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);

    public static final TextAttributesKey ICE_STRING_LITERAL =
            TextAttributesKey.createTextAttributesKey("ICE_STRING_LITERAL", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey ICE_NUMERIC_LITERAL =
            TextAttributesKey.createTextAttributesKey("ICE_NUMERIC_LITERAL", DefaultLanguageHighlighterColors.NUMBER);

    public static final TextAttributesKey ICE_DOC_STYLE_COMMENT =
            TextAttributesKey.createTextAttributesKey("ICE_DOC_STYLE_COMMENT", DefaultLanguageHighlighterColors.DOC_COMMENT);
    public static final TextAttributesKey ICE_C_STYLE_COMMENT =
            TextAttributesKey.createTextAttributesKey("ICE_C_STYLE_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
    public static final TextAttributesKey ICE_END_OF_LINE_COMMENT =
            TextAttributesKey.createTextAttributesKey("ICE_END_OF_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey ICE_DIRECTIVE =
            TextAttributesKey.createTextAttributesKey("ICE_DIRECTIVE", DefaultLanguageHighlighterColors.DOC_COMMENT_MARKUP);

    public static final TextAttributesKey ICE_PARENTHESES =
            TextAttributesKey.createTextAttributesKey("ICE_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES);

    public static final TextAttributesKey ICE_BRACES =
            TextAttributesKey.createTextAttributesKey("ICE_BRACES", DefaultLanguageHighlighterColors.BRACES);

    public static final TextAttributesKey ICE_BRACKETS =
            TextAttributesKey.createTextAttributesKey("ICE_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS);

}
