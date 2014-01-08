package org.xblackcat.frozenice.psi.impl;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.xblackcat.frozenice.psi.*;

/**
 * 08.01.14 14:08
 *
 * @author xBlackCat
 */
class SliceEnumConstRefImpl extends SliceReferenceImpl<SliceEnumConstantReference> {
    public SliceEnumConstRefImpl(SliceEnumConstantReference o) {
        super(o, TextRange.from(0, o.getTextLength()));
    }

    @Override
    public PsiElement resolve() {
        final String referenceName = getRangeInElement().substring(myElement.getText());

        final SliceConstant constantDef = PsiTreeUtil.getParentOfType(myElement, SliceConstant.class);
        if (constantDef == null) {
            return null;
        }

        final SliceTypeReference constType = PsiTreeUtil.findChildOfType(constantDef, SliceTypeReference.class);
        if (constType == null) {
            return null;
        }

        final PsiElement enumType = resolveDataType(constType, TextRange.from(0, constType.getTextLength()));
        if (!(enumType instanceof SliceEnumDef)) {
            return null;
        }

        final SliceEnumConstantList constantList = ((SliceEnumDef) enumType).getEnumConstantList();
        if (constantList == null) {
            return null;
        }

        for (SliceEnumConstant enumConst : constantList.getEnumConstantList()) {
            if (referenceName.equals(enumConst.getName())) {
                return enumConst;
            }
        }

        return null;
    }
}
