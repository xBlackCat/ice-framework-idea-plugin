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

public class SliceMethodDefImpl extends SliceInnerElementImpl implements SliceMethodDef {

  public SliceMethodDefImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SliceVisitor visitor) {
    visitor.visitMethodDef(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SliceVisitor) accept((SliceVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<SliceMetadata> getMetadataList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SliceMetadata.class);
  }

  @Override
  @NotNull
  public SliceMethodReturnType getMethodReturnType() {
    return findNotNullChildByClass(SliceMethodReturnType.class);
  }

  @Override
  @NotNull
  public List<SliceModifier> getModifierList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SliceModifier.class);
  }

  @Override
  @Nullable
  public SliceParametersList getParametersList() {
    return findChildByClass(SliceParametersList.class);
  }

  @Override
  @Nullable
  public SliceThrowsBlock getThrowsBlock() {
    return findChildByClass(SliceThrowsBlock.class);
  }

  @Override
  @NotNull
  public PsiElement getId() {
    return findNotNullChildByType(ICE_ID);
  }

  @Override
  public int getParametersCount() {
    return SlicePsiImplUtil.getParametersCount(this);
  }

}
