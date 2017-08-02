package org.xblackcat.frozenidea.psi.impl;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.psi.*;

import java.util.List;

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

        final SliceEnumConstantList constantList = obtainEnumConstantList();
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

    protected SliceEnumConstantList obtainEnumConstantList() {
        @SuppressWarnings("unchecked")
        final SliceCompositeElement constantDef = PsiTreeUtil.getParentOfType(myElement, SliceConstantDef.class, SliceFieldDef.class);
        if (constantDef == null) {
            return null;
        }

        final SliceTypeReference constType = PsiTreeUtil.findChildOfType(constantDef, SliceTypeReference.class);
        if (constType == null) {
            return null;
        }

        final PsiElement enumType = SlicePsiImplUtil.resolveDataType(constType, TextRange.from(0, constType.getTextLength()));
        if (!(enumType instanceof SliceEnumDef)) {
            return null;
        }

        final SliceEnumConstantList constantList = ((SliceEnumDef) enumType).getEnumConstantList();
        if (constantList == null) {
            return null;
        }

        return constantList;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        final SliceEnumConstantList constantList = obtainEnumConstantList();
        if (constantList == null) {
            return EMPTY_ARRAY;
        }

        final List<SliceEnumConstant> list = constantList.getEnumConstantList();
        return list.toArray(new PsiElement[list.size()]);
    }
}
