// This is a generated file. Not intended for manual editing.
package org.xblackcat.frozenidea.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SliceModule extends SliceNamedElement {

  @NotNull
  List<SliceClassDef> getClassDefList();

  @NotNull
  List<SliceConstantDef> getConstantDefList();

  @NotNull
  List<SliceDictionaryDef> getDictionaryDefList();

  @NotNull
  List<SliceEnumDef> getEnumDefList();

  @NotNull
  List<SliceExceptionDef> getExceptionDefList();

  @NotNull
  List<SliceInterfaceDef> getInterfaceDefList();

  @Nullable
  SliceMetadata getMetadata();

  @NotNull
  List<SliceModule> getModuleList();

  @NotNull
  List<SliceSequenceDef> getSequenceDefList();

  @NotNull
  List<SliceStructDef> getStructDefList();

  @Nullable
  PsiElement getId();

}
