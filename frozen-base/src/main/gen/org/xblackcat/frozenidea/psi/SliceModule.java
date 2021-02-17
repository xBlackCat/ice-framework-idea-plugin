// This is a generated file. Not intended for manual editing.
package org.xblackcat.frozenidea.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SliceModule extends SliceNamedElement {

  @NotNull
  SliceMetadata getMetadata();

  @Nullable
  SliceModuleBody getModuleBody();

  @Nullable
  PsiElement getId();

  @NotNull List<SliceModule> getSubModules();

  @NotNull List<SliceDataTypeElement> getTypeDeclarations();

}
