// This is a generated file. Not intended for manual editing.
package org.xblackcat.frozenidea.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SliceModule extends SliceNamedElement {

  @NotNull
  List<SliceConstantDef> getConstantDefList();

  @NotNull
  List<SliceDataTypeElement> getDataTypeElementList();

  @Nullable
  SliceMetadata getMetadata();

  @NotNull
  List<SliceModule> getModuleList();

  @Nullable
  PsiElement getId();

}
