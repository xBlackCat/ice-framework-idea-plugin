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

public class SliceClassBodyImpl extends SliceCompositeElementImpl implements SliceClassBody {

  public SliceClassBodyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SliceVisitor visitor) {
    visitor.visitClassBody(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SliceVisitor) accept((SliceVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<SliceFieldDef> getFieldDefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SliceFieldDef.class);
  }

  @Override
  @NotNull
  public List<SliceMethodDef> getMethodDefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SliceMethodDef.class);
  }

}
