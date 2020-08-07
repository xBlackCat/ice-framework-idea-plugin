// This is a generated file. Not intended for manual editing.
package org.xblackcat.frozenidea.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SliceMethodDef extends SliceInnerElement, SliceNamedElement {

  @NotNull
  SliceMetadata getMetadata();

  @NotNull
  SliceMethodReturnType getMethodReturnType();

  @NotNull
  List<SliceModifier> getModifierList();

  @Nullable
  SliceParametersList getParametersList();

  @Nullable
  SliceThrowsBlock getThrowsBlock();

  @NotNull
  PsiElement getId();

  int getParametersCount();

}
