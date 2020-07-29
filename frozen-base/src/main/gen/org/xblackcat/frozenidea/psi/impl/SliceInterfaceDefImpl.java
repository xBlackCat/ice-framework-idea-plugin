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

public class SliceInterfaceDefImpl extends SliceDataTypeElementImpl implements SliceInterfaceDef {

  public SliceInterfaceDefImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SliceVisitor visitor) {
    visitor.visitInterfaceDef(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SliceVisitor) accept((SliceVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SliceExtendsDef getExtendsDef() {
    return findChildByClass(SliceExtendsDef.class);
  }

  @Override
  @Nullable
  public SliceInterfaceBody getInterfaceBody() {
    return findChildByClass(SliceInterfaceBody.class);
  }

  @Override
  @NotNull
  public List<SliceMetadata> getMetadataList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SliceMetadata.class);
  }

  @Override
  @Nullable
  public PsiElement getId() {
    return findChildByType(ICE_ID);
  }

}
