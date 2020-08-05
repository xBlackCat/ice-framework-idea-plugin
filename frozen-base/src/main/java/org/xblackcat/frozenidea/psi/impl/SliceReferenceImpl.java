package org.xblackcat.frozenidea.psi.impl;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.psi.*;
import org.xblackcat.frozenidea.util.FQN;
import org.xblackcat.frozenidea.util.SlicePsiUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 15.06.12 15:37
 *
 * @author xBlackCat
 */
public class SliceReferenceImpl<T extends SliceCompositeElement> extends PsiReferenceBase<T> implements PsiPolyVariantReference {

    public SliceReferenceImpl(T element, TextRange range) {
        super(element, range);
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        final List<SliceNamedElement> dataTypes = SlicePsiImplUtil.resolveDataTypes(myElement, getRangeInElement());
        List<ResolveResult> results = new ArrayList<>();
        for (PsiElement property : dataTypes) {
            results.add(new PsiElementResolveResult(property));
        }
        return results.toArray(new ResolveResult[0]);
    }

    @Override
    public PsiElement resolve() {
        ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        final SliceModule module = PsiTreeUtil.getParentOfType(myElement, SliceModule.class);
        if (module == null) {
            return EMPTY_ARRAY;
        }

        FQN modulePath = FQN.buildFQN(module);
        Map<FQN, SliceNamedElement> references = new HashMap<>();

        final PsiElement parent = myElement.getParent();
        final Project project = myElement.getProject();

        if (parent instanceof SliceReferenceListElement) {
            final SliceDataTypeElement declaration = ((SliceReferenceListElement) parent).getContainingClass();
            if (parent instanceof SliceImplementsBlock) {
                if (declaration != null && declaration.isClass()) {
                    SlicePsiUtil.findAll(project, references, SlicePsiUtil.SLICE_INTERFACE_PREDICATE);
                } else {
                    return EMPTY_ARRAY;
                }
            } else if (parent instanceof SliceExtendsBlock) {
                if (declaration == null) {
                    return EMPTY_ARRAY;
                }
                if (declaration.isClass()) {
                    SlicePsiUtil.findAll(project, references, SlicePsiUtil.SLICE_CLASS_PREDICATE);
                } else if (declaration.isInterface()) {
                    SlicePsiUtil.findAll(project, references, SlicePsiUtil.SLICE_INTERFACE_PREDICATE);
                } else if (declaration.isException()) {
                    SlicePsiUtil.findAll(project, references, SlicePsiUtil.SLICE_EXCEPTION_PREDICATE);
                } else {
                    return EMPTY_ARRAY;
                }
            } else if (parent instanceof SliceThrowsBlock) {
                SlicePsiUtil.findAll(project, references, SlicePsiUtil.SLICE_EXCEPTION_PREDICATE);
            } else {
                return EMPTY_ARRAY;
            }

            FQN referenceName = FQN.buildFQN(declaration);
            references.remove(referenceName);
        } else {
            SlicePsiUtil.findAll(project, references, SlicePsiUtil.SLICE_DATA_DECLARATION_PREDICATE);
        }

        return SlicePsiUtil.buildLookupElementsForReferences(modulePath, references);
    }
}
