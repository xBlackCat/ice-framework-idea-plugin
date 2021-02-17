package org.xblackcat.frozenidea.psi.impl;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.psi.*;

/**
 * 08.01.14 14:08
 *
 * @author xBlackCat
 */
class SliceEnumConstRefImpl extends PsiReferenceBase<SliceEnumConstantReference> {
    public SliceEnumConstRefImpl(SliceEnumConstantReference o) {
        super(o, TextRange.from(0, o.getTextLength()));
    }

    @Override
    public PsiElement resolve() {
        final String referenceName = getRangeInElement().substring(myElement.getText());

        final SliceEnumConstant[] constantList = obtainEnumConstantList();
        if (constantList == null) {
            return null;
        }

        for (SliceEnumConstant enumConst : constantList) {
            if (referenceName.equals(enumConst.getName())) {
                return enumConst;
            }
        }

        return null;
    }

    protected SliceEnumConstant[] obtainEnumConstantList() {
        @SuppressWarnings("unchecked") final SliceCompositeElement constantDef = PsiTreeUtil.getParentOfType(
                myElement,
                SliceConstantDef.class,
                SliceFieldDef.class
        );
        if (constantDef == null) {
            return null;
        }

        final SliceTypeReference constType = PsiTreeUtil.findChildOfType(constantDef, SliceTypeReference.class);
        if (constType == null) {
            return null;
        }

        final PsiElement enumType = SlicePsiImplUtil.resolveDataType(constType, TextRange.from(0, constType.getTextLength()));
        if (!(enumType instanceof SliceDataTypeElement) || !((SliceDataTypeElement) enumType).isEnum()) {
            return null;
        }

        final SliceBodyBlock bodyBlock = ((SliceDataTypeElement) enumType).getBodyBlock();
        if (bodyBlock == null) {
            return null;
        }

        return bodyBlock.getEnumConstantList().toArray(new SliceEnumConstant[0]);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return obtainEnumConstantList();
    }
}
