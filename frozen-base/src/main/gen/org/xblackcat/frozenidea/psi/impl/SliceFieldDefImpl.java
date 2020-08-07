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

public class SliceFieldDefImpl extends SliceInnerElementImpl implements SliceFieldDef {

  public SliceFieldDefImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SliceVisitor visitor) {
    visitor.visitFieldDef(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SliceVisitor) accept((SliceVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public SliceDataType getDataType() {
    return findNotNullChildByClass(SliceDataType.class);
  }

  @Override
  @Nullable
  public SliceFieldInitializer getFieldInitializer() {
    return findChildByClass(SliceFieldInitializer.class);
  }

  @Override
  @NotNull
  public SliceMetadata getMetadata() {
    return findNotNullChildByClass(SliceMetadata.class);
  }

  @Override
  @NotNull
  public PsiElement getId() {
    return findNotNullChildByType(ICE_ID);
  }

}
