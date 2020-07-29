// This is a generated file. Not intended for manual editing.
package org.xblackcat.frozenidea.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SliceClassDef extends SliceDataTypeElement, SliceNamedElement {

  @Nullable
  SliceClassBody getClassBody();

  @Nullable
  SliceExtendsDef getExtendsDef();

  @Nullable
  SliceImplementsDef getImplementsDef();

  @NotNull
  List<SliceMetadata> getMetadataList();

  @Nullable
  PsiElement getId();

}
