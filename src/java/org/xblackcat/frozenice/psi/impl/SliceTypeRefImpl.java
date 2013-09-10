package org.xblackcat.frozenice.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.psi.SliceTypeReference;

/**
 * 15.06.12 15:33
 *
 * @author xBlackCat
 */
public abstract class SliceTypeRefImpl extends SliceCompositeElementImpl implements SliceTypeReference {
    public SliceTypeRefImpl(@NotNull ASTNode node) {
        super(node);
    }

    @NotNull
    @Override
    public PsiReference[] getReferences() {
        return new PsiReference[]{
                new SliceReferenceImpl<SliceTypeReference>(this, TextRange.from(0, getTextLength()))
        };
    }

    @Override
    public String toString() {
        return super.toString() + ": " + getText();
    }

}
