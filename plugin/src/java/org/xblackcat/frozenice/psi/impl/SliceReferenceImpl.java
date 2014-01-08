package org.xblackcat.frozenice.psi.impl;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.psi.SliceCompositeElement;
import org.xblackcat.frozenice.psi.SliceDataTypeElement;
import org.xblackcat.frozenice.psi.SliceModule;

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
        return resolveDataType(myElement, getRangeInElement());
    }

    protected PsiElement resolveDataType(SliceCompositeElement element, TextRange rangeInElement) {
        String referenceName = rangeInElement.substring(element.getText());
        SliceModule module = PsiTreeUtil.getParentOfType(element, SliceModule.class);

        if (module == null) {
            return null;
        }

        for (PsiElement c : module.getChildren()) {
            if (c instanceof SliceDataTypeElement) {
                if (referenceName.equals(((SliceDataTypeElement) c).getName())) {
                    return c;
                }
            }
        }

        return null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
