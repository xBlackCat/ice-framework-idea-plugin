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

public class SliceConstantValueImpl extends SliceCompositeElementImpl implements SliceConstantValue {

  public SliceConstantValueImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SliceVisitor visitor) {
    visitor.visitConstantValue(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SliceVisitor) accept((SliceVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SliceBoolLiteral getBoolLiteral() {
    return findChildByClass(SliceBoolLiteral.class);
  }

  @Override
  @Nullable
  public SliceEnumConstantReference getEnumConstantReference() {
    return findChildByClass(SliceEnumConstantReference.class);
  }

  @Override
  @Nullable
  public SliceNumberLiteral getNumberLiteral() {
    return findChildByClass(SliceNumberLiteral.class);
  }

  @Override
  @Nullable
  public SliceStringLiteral getStringLiteral() {
    return findChildByClass(SliceStringLiteral.class);
  }

}
