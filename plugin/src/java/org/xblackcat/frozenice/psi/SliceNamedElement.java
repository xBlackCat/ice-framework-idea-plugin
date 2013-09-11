package org.xblackcat.frozenice.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;

/**
 * 07.06.12 17:36
 *
 * @author xBlackCat
 */
public interface SliceNamedElement extends SliceCompositeElement, PsiNameIdentifierOwner {
    PsiElement getId();
}
