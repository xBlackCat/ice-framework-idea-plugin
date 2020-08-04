// This is a generated file. Not intended for manual editing.
package org.xblackcat.frozenidea.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SliceParameterDef extends SliceVariableElement, SliceNamedElement {

  @NotNull
  SliceDataType getDataType();

  @NotNull
  List<SliceMetadata> getMetadataList();

  @NotNull
  List<SliceParameterModifier> getParameterModifierList();

  @Nullable
  PsiElement getId();

}
