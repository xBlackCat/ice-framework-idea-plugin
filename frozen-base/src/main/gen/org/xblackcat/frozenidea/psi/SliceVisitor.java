// This is a generated file. Not intended for manual editing.
package org.xblackcat.frozenidea.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;

public class SliceVisitor extends PsiElementVisitor {

  public void visitBodyBlock(@NotNull SliceBodyBlock o) {
    visitCompositeElement(o);
  }

  public void visitBoolLiteral(@NotNull SliceBoolLiteral o) {
    visitCompositeElement(o);
  }

  public void visitCompactTypeIdDeclaration(@NotNull SliceCompactTypeIdDeclaration o) {
    visitCompositeElement(o);
  }

  public void visitConstantDef(@NotNull SliceConstantDef o) {
    visitVariableElement(o);
    // visitNamedElement(o);
  }

  public void visitConstantValue(@NotNull SliceConstantValue o) {
    visitCompositeElement(o);
  }

  public void visitDataType(@NotNull SliceDataType o) {
    visitCompositeElement(o);
  }

  public void visitDataTypeElement(@NotNull SliceDataTypeElement o) {
    visitNamedElement(o);
  }

  public void visitEnumConstant(@NotNull SliceEnumConstant o) {
    visitInnerElement(o);
    // visitVariableElement(o);
    // visitNamedElement(o);
  }

  public void visitEnumConstantReference(@NotNull SliceEnumConstantReference o) {
    visitCompositeElement(o);
  }

  public void visitExtendsBlock(@NotNull SliceExtendsBlock o) {
    visitReferenceListElement(o);
  }

  public void visitFieldDef(@NotNull SliceFieldDef o) {
    visitInnerElement(o);
    // visitVariableElement(o);
    // visitNamedElement(o);
  }

  public void visitFieldInitializer(@NotNull SliceFieldInitializer o) {
    visitCompositeElement(o);
  }

  public void visitFloatLiteral(@NotNull SliceFloatLiteral o) {
    visitCompositeElement(o);
  }

  public void visitGenericType(@NotNull SliceGenericType o) {
    visitCompositeElement(o);
  }

  public void visitGlobalMetadata(@NotNull SliceGlobalMetadata o) {
    visitCompositeElement(o);
  }

  public void visitGlobalMetadataStatement(@NotNull SliceGlobalMetadataStatement o) {
    visitCompositeElement(o);
  }

  public void visitImplementsBlock(@NotNull SliceImplementsBlock o) {
    visitReferenceListElement(o);
  }

  public void visitIntegerLiteral(@NotNull SliceIntegerLiteral o) {
    visitCompositeElement(o);
  }

  public void visitMetadata(@NotNull SliceMetadata o) {
    visitCompositeElement(o);
  }

  public void visitMetadataElement(@NotNull SliceMetadataElement o) {
    visitCompositeElement(o);
  }

  public void visitMetadataStatement(@NotNull SliceMetadataStatement o) {
    visitCompositeElement(o);
  }

  public void visitMethodDef(@NotNull SliceMethodDef o) {
    visitInnerElement(o);
    // visitNamedElement(o);
  }

  public void visitMethodReturnType(@NotNull SliceMethodReturnType o) {
    visitCompositeElement(o);
  }

  public void visitModifier(@NotNull SliceModifier o) {
    visitCompositeElement(o);
  }

  public void visitModule(@NotNull SliceModule o) {
    visitNamedElement(o);
  }

  public void visitModuleBody(@NotNull SliceModuleBody o) {
    visitCompositeElement(o);
  }

  public void visitModulePath(@NotNull SliceModulePath o) {
    visitCompositeElement(o);
  }

  public void visitNumberLiteral(@NotNull SliceNumberLiteral o) {
    visitCompositeElement(o);
  }

  public void visitParameterDef(@NotNull SliceParameterDef o) {
    visitVariableElement(o);
    // visitNamedElement(o);
  }

  public void visitParameterModifier(@NotNull SliceParameterModifier o) {
    visitCompositeElement(o);
  }

  public void visitParametersList(@NotNull SliceParametersList o) {
    visitCompositeElement(o);
  }

  public void visitProxyType(@NotNull SliceProxyType o) {
    visitCompositeElement(o);
  }

  public void visitSecondGenericType(@NotNull SliceSecondGenericType o) {
    visitCompositeElement(o);
  }

  public void visitStringLiteral(@NotNull SliceStringLiteral o) {
    visitCompositeElement(o);
  }

  public void visitThrowsBlock(@NotNull SliceThrowsBlock o) {
    visitReferenceListElement(o);
  }

  public void visitTypeReference(@NotNull SliceTypeReference o) {
    visitCompositeElement(o);
  }

  public void visitTypeWord(@NotNull SliceTypeWord o) {
    visitCompositeElement(o);
  }

  public void visitInnerElement(@NotNull SliceInnerElement o) {
    visitCompositeElement(o);
  }

  public void visitNamedElement(@NotNull SliceNamedElement o) {
    visitCompositeElement(o);
  }

  public void visitReferenceListElement(@NotNull SliceReferenceListElement o) {
    visitCompositeElement(o);
  }

  public void visitVariableElement(@NotNull SliceVariableElement o) {
    visitCompositeElement(o);
  }

  public void visitCompositeElement(@NotNull SliceCompositeElement o) {
    visitElement(o);
  }

}
