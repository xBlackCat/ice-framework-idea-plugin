package org.xblackcat.frozenice.psi.impl;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.psi.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 15.06.12 15:37
 *
 * @author xBlackCat
 */
public class SliceReferenceImpl<T extends SliceCompositeElement> extends PsiReferenceBase<T> {
    public SliceReferenceImpl(T element, TextRange range) {
        super(element, range);
    }

    @Override
    public PsiElement resolve() {
        return SlicePsiImplUtil.resolveDataType(myElement, getRangeInElement());
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        final SliceModule module = PsiTreeUtil.getParentOfType(myElement, SliceModule.class);
        if (module == null) {
            return EMPTY_ARRAY;
        }

        Map<String, PsiNamedElement> references = new HashMap<>();

        final PsiElement parent = myElement.getParent();

        if (parent instanceof SliceExtendsList) {
            PsiElement declaration = parent.getParent();

            if (declaration instanceof SliceClassDef) {
                SlicePsiImplUtil.addAll(references, module.getClassDefList());
            } else if (declaration instanceof SliceInterfaceDef) {
                SlicePsiImplUtil.addAll(references, module.getInterfaceDefList());
            } else if (declaration instanceof SliceExceptionDef) {
                SlicePsiImplUtil.addAll(references, module.getExceptionDefList());
            } else {
                return EMPTY_ARRAY;
            }

            String referenceName = ((SliceNamedElement) declaration).getName();
            if (declaration.equals(references.get(referenceName))) {
                references.remove(referenceName);
            }
        } else if (parent instanceof SliceImplementsList) {
            SlicePsiImplUtil.addAll(references, module.getInterfaceDefList());
        } else if (parent instanceof SliceThrowsList) {
            SlicePsiImplUtil.addAll(references, module.getExceptionDefList());
        } else {
            SlicePsiImplUtil.addAll(references, module.getClassDefList());
            SlicePsiImplUtil.addAll(references, module.getInterfaceDefList());
            SlicePsiImplUtil.addAll(references, module.getExceptionDefList());
            SlicePsiImplUtil.addAll(references, module.getEnumDefList());
            SlicePsiImplUtil.addAll(references, module.getStructDefList());
            SlicePsiImplUtil.addAll(references, module.getDictionaryDefList());
            SlicePsiImplUtil.addAll(references, module.getSequenceDefList());
        }

        final Collection<PsiNamedElement> ref = references.values();
        return ref.toArray(new PsiNamedElement[ref.size()]);
    }
}
