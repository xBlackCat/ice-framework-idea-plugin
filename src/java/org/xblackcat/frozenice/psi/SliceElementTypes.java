package org.xblackcat.frozenice.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import org.xblackcat.frozenice.SliceLanguage;
import org.xblackcat.frozenice.psi.source.SliceStubPsiElement;

/**
 * 04.01.12 16:40
 *
 * @author xBlackCat
 */
public interface SliceElementTypes extends SliceTokenTypes {
    IFileElementType FILE_ELEMENT_TYPE = new IFileElementType("SLICE", SliceLanguage.INSTANCE);

    // Psi Elements definitions
    IElementType ICE_MODULE = new SliceStubElementType("Ice.Module") {
        @Override
        public PsiElement createPsi(ASTNode node) {
            return new SliceStubPsiElement(node);
        }
    };

    IElementType ICE_CLASS = new SliceStubElementType("Ice.Class") {
        @Override
        public PsiElement createPsi(ASTNode node) {
            return new SliceStubPsiElement(node);
        }
    };
    IElementType ICE_INTERFACE = new SliceStubElementType("Ice.Interface") {
        @Override
        public PsiElement createPsi(ASTNode node) {
            return new SliceStubPsiElement(node);
        }
    };
    IElementType ICE_ENUM = new SliceStubElementType("Ice.Enum") {
        @Override
        public PsiElement createPsi(ASTNode node) {
            return new SliceStubPsiElement(node);
        }
    };
    IElementType ICE_ENUM_CONSTANT_LIST = new SliceStubElementType("Ice.Enum_constant_list") {
        @Override
        public PsiElement createPsi(ASTNode node) {
            return new SliceStubPsiElement(node);
        }
    };
    IElementType ICE_CLASS_FIELD_DECLARATION = new SliceStubElementType("Ice.Field_declaration") {
        @Override
        public PsiElement createPsi(ASTNode node) {
            return new SliceStubPsiElement(node);
        }
    };
    IElementType ICE_CLASS_INTERFACE_LIST = new SliceStubElementType("Ice.ImplementList_declaration") {
        @Override
        public PsiElement createPsi(ASTNode node) {
            return new SliceStubPsiElement(node);
        }
    };
    IElementType ICE_CLASS_SUPER_CLASS = new SliceStubElementType("Ice.SuperClass_declaration") {
        @Override
        public PsiElement createPsi(ASTNode node) {
            return new SliceStubPsiElement(node);
        }
    };
    IElementType ICE_CLASS_METHOD_DECLARATION = new SliceStubElementType("Ice.Method_declaration") {
        @Override
        public PsiElement createPsi(ASTNode node) {
            return new SliceStubPsiElement(node);
        }
    };
    IElementType ICE_ENUM_CONSTANT = new SliceStubElementType("Ice.Enum_constant") {
        @Override
        public PsiElement createPsi(ASTNode node) {
            return new SliceStubPsiElement(node);
        }
    };
    IElementType ICE_EXCEPTION = new SliceStubElementType("Ice.Exception") {
        @Override
        public PsiElement createPsi(ASTNode node) {
            return new SliceStubPsiElement(node);
        }
    };
    IElementType ICE_TYPE_DECLARATION = new SliceStubElementType("Ice.Type_declaration") {
        @Override
        public PsiElement createPsi(ASTNode node) {
            return new SliceStubPsiElement(node);
        }
    };
    IElementType ICE_MACROS = new SliceStubElementType("Ice.Macros") {
        @Override
        public PsiElement createPsi(ASTNode node) {
            return new SliceStubPsiElement(node);
        }
    };

}
