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

public class SliceEnumConstantInitializerImpl extends SliceCompositeElementImpl implements SliceEnumConstantInitializer {

  public SliceEnumConstantInitializerImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SliceVisitor visitor) {
    visitor.visitEnumConstantInitializer(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SliceVisitor) accept((SliceVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SliceNumberLiteral getNumberLiteral() {
    return findChildByClass(SliceNumberLiteral.class);
  }

  @Override
  public Integer getConstant() {
    return SlicePsiImplUtil.getConstant(this);
  }

}
