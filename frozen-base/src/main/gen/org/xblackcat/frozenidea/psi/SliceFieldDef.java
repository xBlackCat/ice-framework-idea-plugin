// This is a generated file. Not intended for manual editing.
package org.xblackcat.frozenidea.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SliceFieldDef extends SliceInnerElement, SliceVariableElement, SliceNamedElement {

  @NotNull
  SliceDataType getDataType();

  @Nullable
  SliceFieldInitializer getFieldInitializer();

  @NotNull
  SliceMetadata getMetadata();

  @NotNull
  PsiElement getId();

}
