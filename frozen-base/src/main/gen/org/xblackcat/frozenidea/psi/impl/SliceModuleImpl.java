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

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SliceVisitor) accept((SliceVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public SliceMetadata getMetadata() {
    return findNotNullChildByClass(SliceMetadata.class);
  }

  @Override
  @Nullable
  public SliceModuleBody getModuleBody() {
    return findChildByClass(SliceModuleBody.class);
  }

  @Override
  @Nullable
  public PsiElement getId() {
    return findChildByType(ICE_ID);
  }

  @Override
  public @NotNull List<SliceModule> getSubModules() {
    return SlicePsiImplUtil.getSubModules(this);
  }

  @Override
  public @NotNull List<SliceDataTypeElement> getTypeDeclarations() {
    return SlicePsiImplUtil.getTypeDeclarations(this);
  }

}
