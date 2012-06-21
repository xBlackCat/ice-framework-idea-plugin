package org.xblackcat.frozenice.syntax;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.SyntaxHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.SliceParserDefinition;
import org.xblackcat.frozenice.parser.IceLexer;
import org.xblackcat.frozenice.psi.SliceTypes;

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
        keys1 = new HashMap<IElementType, TextAttributesKey>();
        keys2 = new HashMap<IElementType, TextAttributesKey>();

        fillMap(keys1, SliceParserDefinition.KEYWORD_BIT_SET, SyntaxHighlighterColors.KEYWORD);

        keys1.put(SliceTypes.ICE_STRING, SyntaxHighlighterColors.STRING);
        keys1.put(SliceTypes.ICE_INTEGER_VALUE, SyntaxHighlighterColors.NUMBER);
        keys1.put(SliceTypes.ICE_FLOAT_VALUE, SyntaxHighlighterColors.NUMBER);

        keys1.put(SliceParserDefinition.C_STYLE_COMMENT, SyntaxHighlighterColors.JAVA_BLOCK_COMMENT);
        keys1.put(SliceParserDefinition.END_OF_LINE_COMMENT, SyntaxHighlighterColors.LINE_COMMENT);
        keys1.put(SliceParserDefinition.ICE_DIRECTIVE, SyntaxHighlighterColors.DOC_COMMENT_MARKUP);

        keys1.put(SliceTypes.ICE_LEFT_PARENTH, SyntaxHighlighterColors.PARENTHS);
        keys1.put(SliceTypes.ICE_RIGHT_PARENTH, SyntaxHighlighterColors.PARENTHS);

        keys1.put(SliceTypes.ICE_LEFT_BRACE, SyntaxHighlighterColors.BRACES);
        keys1.put(SliceTypes.ICE_RIGHT_BRACE, SyntaxHighlighterColors.BRACES);

        keys1.put(SliceTypes.ICE_LEFT_BRACKET, SyntaxHighlighterColors.BRACKETS);
        keys1.put(SliceTypes.ICE_RIGHT_BRACKET, SyntaxHighlighterColors.BRACKETS);

        keys1.put(com.intellij.psi.TokenType.BAD_CHARACTER, HighlighterColors.BAD_CHARACTER);
    }

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new IceLexer();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        return pack(keys1.get(tokenType), keys2.get(tokenType));
    }
}
