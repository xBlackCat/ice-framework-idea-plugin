package org.xblackcat.frozenidea.psi;

import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 *
 */
public interface SliceReferenceListElement extends SliceCompositeElement {
    @NotNull
    List<SliceTypeReference> getTypeReferenceList();

    default SliceDataTypeElement getContainingClass() {
        return PsiTreeUtil.getParentOfType(this, SliceDataTypeElement.class);
    }
}
