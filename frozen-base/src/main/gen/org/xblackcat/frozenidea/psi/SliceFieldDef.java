// This is a generated file. Not intended for manual editing.
package org.xblackcat.frozenidea.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SliceFieldDef extends SliceInnerElement, SliceNamedElement {

  @Nullable
  SliceFieldInitializer getFieldInitializer();

  @NotNull
  List<SliceMetadata> getMetadataList();

  @NotNull
  SliceVariableType getVariableType();

  @Nullable
  PsiElement getId();

}
