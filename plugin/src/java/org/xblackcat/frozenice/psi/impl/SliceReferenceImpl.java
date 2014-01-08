package org.xblackcat.frozenice.psi.impl;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.psi.SliceCompositeElement;

/**
 * 15.06.12 15:37
 *
 * @author xBlackCat
 */
public class SliceReferenceImpl<T extends SliceCompositeElement> extends PsiReferenceBase<T> {
    public SliceReferenceImpl(T element, TextRange range) {
        super(element, range);
    }

    @Override
    public PsiElement resolve() {
        return SlicePsiImplUtil.resolveDataType(myElement, getRangeInElement());
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
