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
public interface SliceElementTypes {
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
    IElementType ICE_CONSTANT = new SliceStubElementType("Ice.Constant") {
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
    IElementType ICE_TYPE_DECLARATION = new SliceStubElementType("Ice.Type_dictionary") {
        @Override
        public PsiElement createPsi(ASTNode node) {
            return new SliceStubPsiElement(node);
        }
    };
    IElementType ICE_TYPE_SEQUENCE = new SliceStubElementType("Ice.Type_sequence") {
        @Override
        public PsiElement createPsi(ASTNode node) {
            return new SliceStubPsiElement(node);
        }
    };
    IElementType ICE_TYPE_DICTIONARY = new SliceStubElementType("Ice.Type_declaration") {
        @Override
        public PsiElement createPsi(ASTNode node) {
            return new SliceStubPsiElement(node);
        }
    };
    IElementType ICE_STRUCT = new SliceStubElementType("Ice.Structure") {
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

    IElementType ICE_METADATA = new SliceStubElementType("Ice.MetaData") {
        @Override
        public PsiElement createPsi(ASTNode node) {
            return new SliceStubPsiElement(node);
        }
    };

    IElementType ICE_METADATA_BEGIN = new SliceStubElementType("Ice.MetaData.Begin") {
        @Override
        public PsiElement createPsi(ASTNode node) {
            return new SliceStubPsiElement(node);
        }
    };

    IElementType ICE_METADATA_END = new SliceStubElementType("Ice.MetaData.End") {
        @Override
        public PsiElement createPsi(ASTNode node) {
            return new SliceStubPsiElement(node);
        }
    };
    IElementType BLOCK_OF_LINE_COMMENTS = new SliceStubElementType("Ice.BlockOfLineComments") {
        @Override
        public PsiElement createPsi(ASTNode node) {
            return new SliceStubPsiElement(node);
        }
    };
    IElementType BLOCK_COMMENT = new SliceStubElementType("Ice.BlockComment") {
        @Override
        public PsiElement createPsi(ASTNode node) {
            return new SliceStubPsiElement(node);
        }
    };
    IElementType ICE_METADATA_LIST = new SliceStubElementType("Ice.MetaData_list") {
        @Override
        public PsiElement createPsi(ASTNode node) {
            return new SliceStubPsiElement(node);
        }
    };
    IElementType ICE_METADATA_ELEMENT = new SliceStubElementType("Ice.MetaData_element") {
        @Override
        public PsiElement createPsi(ASTNode node) {
            return new SliceStubPsiElement(node);
        }
    };
}
