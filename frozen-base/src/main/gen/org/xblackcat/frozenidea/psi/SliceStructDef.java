// This is a generated file. Not intended for manual editing.
package org.xblackcat.frozenidea.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SliceStructDef extends SliceDataTypeElement, SliceNamedElement {

  @NotNull
  List<SliceFieldDef> getFieldDefList();

  @NotNull
  List<SliceMetadata> getMetadataList();

  @Nullable
  PsiElement getId();

}
