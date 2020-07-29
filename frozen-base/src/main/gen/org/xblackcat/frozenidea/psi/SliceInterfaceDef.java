// This is a generated file. Not intended for manual editing.
package org.xblackcat.frozenidea.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SliceInterfaceDef extends SliceDataTypeElement, SliceNamedElement {

  @Nullable
  SliceExtendsDef getExtendsDef();

  @Nullable
  SliceInterfaceBody getInterfaceBody();

  @NotNull
  List<SliceMetadata> getMetadataList();

  @Nullable
  PsiElement getId();

}
