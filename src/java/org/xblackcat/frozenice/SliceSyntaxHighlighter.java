package org.xblackcat.frozenice;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.SyntaxHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.psi.SliceTokenTypes;

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

        fillMap(keys1, SliceTokenTypes.KEYWORD_BIT_SET, SyntaxHighlighterColors.KEYWORD);

        keys1.put(SliceTokenTypes.C_STYLE_COMMENT, SyntaxHighlighterColors.JAVA_BLOCK_COMMENT);
        keys1.put(SliceTokenTypes.END_OF_LINE_COMMENT, SyntaxHighlighterColors.LINE_COMMENT);
        keys1.put(SliceTokenTypes.MACROS_LINE, SyntaxHighlighterColors.DOC_COMMENT_MARKUP);

        keys1.put(SliceTokenTypes.LPARENTH, SyntaxHighlighterColors.PARENTHS);
        keys1.put(SliceTokenTypes.RPARENTH, SyntaxHighlighterColors.PARENTHS);

        keys1.put(SliceTokenTypes.LBRACE, SyntaxHighlighterColors.BRACES);
        keys1.put(SliceTokenTypes.RBRACE, SyntaxHighlighterColors.BRACES);

        keys1.put(SliceTokenTypes.LBRACKET, SyntaxHighlighterColors.BRACKETS);
        keys1.put(SliceTokenTypes.RBRACKET, SyntaxHighlighterColors.BRACKETS);

        keys1.put(SliceTokenTypes.BAD_CHARACTER, HighlighterColors.BAD_CHARACTER);
    }

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new FlexAdapter(new _IceLexer());
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        return pack(keys1.get(tokenType), keys2.get(tokenType));
    }
}
