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
import com.intellij.psi.PsiReference;

public class SliceEnumConstantReferenceImpl extends SliceCompositeElementImpl implements SliceEnumConstantReference {

  public SliceEnumConstantReferenceImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SliceVisitor visitor) {
    visitor.visitEnumConstantReference(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SliceVisitor) accept((SliceVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getId() {
    return findNotNullChildByType(ICE_ID);
  }

  @Override
  @NotNull
  public PsiReference[] getReferences() {
    return SlicePsiImplUtil.getReferences(this);
  }

  @Override
  @NotNull
  public PsiReference getReference() {
    return SlicePsiImplUtil.getReference(this);
  }

}
