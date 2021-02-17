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

public class SliceModuleBodyImpl extends SliceCompositeElementImpl implements SliceModuleBody {

  public SliceModuleBodyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SliceVisitor visitor) {
    visitor.visitModuleBody(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SliceVisitor) accept((SliceVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<SliceConstantDef> getConstantDefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SliceConstantDef.class);
  }

  @Override
  @NotNull
  public List<SliceDataTypeElement> getDataTypeElementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SliceDataTypeElement.class);
  }

  @Override
  @NotNull
  public List<SliceModule> getModuleList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SliceModule.class);
  }

}
