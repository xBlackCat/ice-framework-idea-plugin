package org.xblackcat.frozenice.psi.impl;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.psi.SliceTypeReference;


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
}
