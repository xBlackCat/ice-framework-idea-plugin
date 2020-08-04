// This is a generated file. Not intended for manual editing.
package org.xblackcat.frozenidea.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import org.xblackcat.frozenidea.util.FQN;

public interface SliceDataTypeElement extends SliceNamedElement {

  @Nullable
  SliceBodyBlock getBodyBlock();

  @Nullable
  SliceExtendsBlock getExtendsBlock();

  @Nullable
  SliceGenericType getGenericType();

  @Nullable
  SliceImplementsBlock getImplementsBlock();

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

  FQN getQualifiedName();

}
