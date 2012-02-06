package org.xblackcat.frozenice;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenice.psi.SliceElementType;
import org.xblackcat.frozenice.psi.SliceElementTypes;
import org.xblackcat.frozenice.psi.SliceTokenTypes;

/**
 * 19.01.12 14:44
 *
 * @author xBlackCat
 */
public class SliceBraceMatcher implements PairedBraceMatcher {
    public static final BracePair[] BRACES = new BracePair[]{
            new BracePair(SliceTokenTypes.LPARENTH, SliceTokenTypes.RPARENTH, false),
            new BracePair(SliceTokenTypes.LBRACE, SliceTokenTypes.RBRACE, true),
            new BracePair(SliceElementTypes.ICE_METADATA_BEGIN, SliceElementTypes.ICE_METADATA_END, false)
    };

    public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
        return 0;
    }

    public BracePair[] getPairs() {
        return BRACES;
    }

    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType) {
        return !(contextType instanceof SliceElementType) ||
                contextType == SliceTokenTypes.C_STYLE_COMMENT ||
                contextType == SliceTokenTypes.END_OF_LINE_COMMENT ||
                contextType == SliceTokenTypes.WHITE_SPACE ||
                contextType == SliceTokenTypes.SEMICOLON ||
                contextType == SliceTokenTypes.COMMA ||
                contextType == SliceTokenTypes.RPARENTH ||
                contextType == SliceTokenTypes.RBRACKET ||
                contextType == SliceTokenTypes.RBRACE ||
                contextType == SliceTokenTypes.LBRACE;
    }
}
