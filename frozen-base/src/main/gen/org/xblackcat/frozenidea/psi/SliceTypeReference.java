// This is a generated file. Not intended for manual editing.
package org.xblackcat.frozenidea.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

public interface SliceTypeReference extends SliceCompositeElement {

  @NotNull
  SliceModulePath getModulePath();

  @NotNull
  PsiElement getId();

  @NotNull PsiReference[] getReferences();

  @NotNull PsiReference getReference();

}
