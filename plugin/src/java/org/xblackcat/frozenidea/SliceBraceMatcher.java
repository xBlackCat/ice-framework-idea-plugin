package org.xblackcat.frozenidea;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.psi.SliceTokenType;
import org.xblackcat.frozenidea.psi.SliceTypes;

/**
 * 19.01.12 14:44
 *
 * @author xBlackCat
 */
public class SliceBraceMatcher implements PairedBraceMatcher {
    public static final BracePair[] BRACES = new BracePair[]{
            new BracePair(SliceTypes.ICE_LEFT_PARENTH, SliceTypes.ICE_RIGHT_PARENTH, false),
            new BracePair(SliceTypes.ICE_LEFT_BRACE, SliceTypes.ICE_RIGHT_BRACE, true),
            new BracePair(SliceTypes.ICE_LEFT_BRACKET, SliceTypes.ICE_RIGHT_BRACKET, false),
            new BracePair(SliceTypes.ICE_OPEN_GL_MD, SliceTypes.ICE_CLOSE_GL_MD, false)
    };

    public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
        return 0;
    }

    public BracePair[] getPairs() {
        return BRACES;
    }

    public boolean isPairedBracesAllowedBeforeType(
            @NotNull IElementType lbraceType,
            @Nullable IElementType contextType
    ) {
        return !(contextType instanceof SliceTokenType) ||
                contextType == SliceTypes.ICE_C_STYLE_COMMENT ||
                contextType == SliceTypes.ICE_END_OF_LINE_COMMENT ||
                contextType == SliceTypes.ICE_SEMICOLON ||
                contextType == SliceTypes.ICE_COMA ||
                contextType == SliceTypes.ICE_RIGHT_PARENTH ||
                contextType == SliceTypes.ICE_RIGHT_BRACKET ||
                contextType == SliceTypes.ICE_RIGHT_BRACE ||
                contextType == SliceTypes.ICE_LEFT_BRACE;
    }
}
