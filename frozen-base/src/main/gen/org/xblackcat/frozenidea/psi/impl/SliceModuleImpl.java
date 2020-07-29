// This is a generated file. Not intended for manual editing.
package org.xblackcat.frozenidea.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static org.xblackcat.frozenidea.psi.SliceTypes.*;
import org.xblackcat.frozenidea.psi.*;

public class SliceModuleImpl extends SliceNamedElementImpl implements SliceModule {

  public SliceModuleImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SliceVisitor visitor) {
    visitor.visitModule(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SliceVisitor) accept((SliceVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<SliceClassDef> getClassDefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SliceClassDef.class);
  }

  @Override
  @NotNull
  public List<SliceConstantDef> getConstantDefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SliceConstantDef.class);
  }

  @Override
  @NotNull
  public List<SliceDictionaryDef> getDictionaryDefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SliceDictionaryDef.class);
  }

  @Override
  @NotNull
  public List<SliceEnumDef> getEnumDefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SliceEnumDef.class);
  }

  @Override
  @NotNull
  public List<SliceExceptionDef> getExceptionDefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SliceExceptionDef.class);
  }

  @Override
  @NotNull
  public List<SliceInterfaceDef> getInterfaceDefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SliceInterfaceDef.class);
  }

  @Override
  @Nullable
  public SliceMetadata getMetadata() {
    return findChildByClass(SliceMetadata.class);
  }

  @Override
  @NotNull
  public List<SliceModule> getModuleList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SliceModule.class);
  }

  @Override
  @NotNull
  public List<SliceSequenceDef> getSequenceDefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SliceSequenceDef.class);
  }

  @Override
  @NotNull
  public List<SliceStructDef> getStructDefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SliceStructDef.class);
  }

  @Override
  @Nullable
  public PsiElement getId() {
    return findChildByType(ICE_ID);
  }

}
