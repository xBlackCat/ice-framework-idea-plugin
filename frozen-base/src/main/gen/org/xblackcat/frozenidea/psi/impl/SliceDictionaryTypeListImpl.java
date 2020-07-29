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

public class SliceDictionaryTypeListImpl extends SliceCompositeElementImpl implements SliceDictionaryTypeList {

  public SliceDictionaryTypeListImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SliceVisitor visitor) {
    visitor.visitDictionaryTypeList(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SliceVisitor) accept((SliceVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public SliceElementType getElementType() {
    return findNotNullChildByClass(SliceElementType.class);
  }

  @Override
  @NotNull
  public SliceKeyType getKeyType() {
    return findNotNullChildByClass(SliceKeyType.class);
  }

}
