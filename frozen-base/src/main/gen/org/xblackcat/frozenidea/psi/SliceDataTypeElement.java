// This is a generated file. Not intended for manual editing.
package org.xblackcat.frozenidea.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SliceDataTypeElement extends SliceNamedElement {

  @Nullable
  SliceBodyBlock getBodyBlock();

  @Nullable
  SliceExtendsDef getExtendsDef();

  @Nullable
  SliceGenericType getGenericType();

  @Nullable
  SliceImplementsDef getImplementsDef();

  @NotNull
  List<SliceMetadata> getMetadataList();

  @NotNull
  SliceTypeWord getTypeWord();

  @Nullable
  PsiElement getId();

  boolean isClass();

  boolean isInterface();

  boolean isException();

  boolean isEnum();

  boolean isStruct();

  boolean isSequence();

  boolean isDictionary();

  SliceModule getModule();

}
