package org.xblackcat.frozenice.psi.impl;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.psi.*;


/**
 * 06.07.12 11:41
 *
 * @author xBlackCat
 */
public class SlicePsiImplUtil {
    @NotNull
    public static PsiReference[] getReferences(SliceTypeReference o) {
        return new PsiReference[]{
                new SliceReferenceImpl<>(o, TextRange.from(0, o.getTextLength()))
        };
    }

    @NotNull
    public static PsiReference[] getReferences(SliceEnumConstantReference o) {
        return new PsiReference[]{
                new SliceEnumConstRefImpl(o)
        };
    }

    protected static PsiElement resolveDataType(SliceCompositeElement element, TextRange rangeInElement) {
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
}
