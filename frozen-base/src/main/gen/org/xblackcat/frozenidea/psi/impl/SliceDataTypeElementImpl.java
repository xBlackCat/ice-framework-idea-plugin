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
import org.xblackcat.frozenidea.util.FQN;

public class SliceDataTypeElementImpl extends SliceNamedElementImpl implements SliceDataTypeElement {

  public SliceDataTypeElementImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SliceVisitor visitor) {
    visitor.visitDataTypeElement(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SliceVisitor) accept((SliceVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SliceBodyBlock getBodyBlock() {
    return findChildByClass(SliceBodyBlock.class);
  }

  @Override
  @Nullable
  public SliceCompactTypeIdDeclaration getCompactTypeIdDeclaration() {
    return findChildByClass(SliceCompactTypeIdDeclaration.class);
  }

  @Override
  @Nullable
  public SliceExtendsBlock getExtendsBlock() {
    return findChildByClass(SliceExtendsBlock.class);
  }

  @Override
  @Nullable
  public SliceGenericType getGenericType() {
    return findChildByClass(SliceGenericType.class);
  }

  @Override
  @Nullable
  public SliceImplementsBlock getImplementsBlock() {
    return findChildByClass(SliceImplementsBlock.class);
  }

  @Override
  @NotNull
  public SliceMetadata getMetadata() {
    return findNotNullChildByClass(SliceMetadata.class);
  }

  @Override
  @NotNull
  public SliceTypeWord getTypeWord() {
    return findNotNullChildByClass(SliceTypeWord.class);
  }

  @Override
  @Nullable
  public PsiElement getId() {
    return findChildByType(ICE_ID);
  }

  @Override
  public boolean isClass() {
    return SlicePsiImplUtil.isClass(this);
  }

  @Override
  public boolean isInterface() {
    return SlicePsiImplUtil.isInterface(this);
  }

  @Override
  public boolean isException() {
    return SlicePsiImplUtil.isException(this);
  }

  @Override
  public boolean isEnum() {
    return SlicePsiImplUtil.isEnum(this);
  }

  @Override
  public boolean isStruct() {
    return SlicePsiImplUtil.isStruct(this);
  }

  @Override
  public boolean isSequence() {
    return SlicePsiImplUtil.isSequence(this);
  }

  @Override
  public boolean isDictionary() {
    return SlicePsiImplUtil.isDictionary(this);
  }

  @Override
  public SliceModule getModule() {
    return SlicePsiImplUtil.getModule(this);
  }

  @Override
  public FQN getQualifiedName() {
    return SlicePsiImplUtil.getQualifiedName(this);
  }

}
