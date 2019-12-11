package org.xblackcat.frozenidea.psi.impl;

import com.intellij.codeInsight.lookup.AutoCompletionPolicy;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.psi.*;

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

        if (parent instanceof SliceExtendsList) {
            PsiElement block = parent.getParent();

            final PsiElement declaration;
            if (block instanceof SliceImplementsDef) {
                declaration = block.getParent();
                if (declaration instanceof SliceClassDef) {
                    SlicePsiImplUtil.addAll(references, SlicePsiImplUtil.searchElements(project, c -> c instanceof SliceInterfaceDef));
                } else {
                    return EMPTY_ARRAY;
                }
            } else if (block instanceof SliceExtendsDef) {
                declaration = block.getParent();
                if (declaration instanceof SliceClassDef) {
                    SlicePsiImplUtil.addAll(references, SlicePsiImplUtil.searchElements(project, c -> c instanceof SliceClassDef));
                } else if (declaration instanceof SliceInterfaceDef) {
                    SlicePsiImplUtil.addAll(references, SlicePsiImplUtil.searchElements(project, c -> c instanceof SliceInterfaceDef));
                } else if (declaration instanceof SliceExceptionDef) {
                    SlicePsiImplUtil.addAll(references, SlicePsiImplUtil.searchElements(project, c -> c instanceof SliceExceptionDef));
                } else {
                    return EMPTY_ARRAY;
                }
            } else {
                return EMPTY_ARRAY;
            }

            FQN referenceName = FQN.buildFQN((SliceNamedElement) declaration);
            if (declaration.equals(references.get(referenceName))) {
                references.remove(referenceName);
            }
        } else if (parent instanceof SliceImplementsDef) {
            SlicePsiImplUtil.addAll(references, SlicePsiImplUtil.searchElements(project, c -> c instanceof SliceInterfaceDef));
        } else if (parent instanceof SliceThrowsList) {
            SlicePsiImplUtil.addAll(references, SlicePsiImplUtil.searchElements(project, c -> c instanceof SliceExceptionDef));
        } else {
            SlicePsiImplUtil.addAll(
                    references,
                    SlicePsiImplUtil.searchElements(
                            project,
                            c -> c instanceof SliceDataTypeElement && !(c instanceof SliceExceptionDef)
                    )
            );
        }

        return references.entrySet()
                         .stream()
                         .map(
                                 e -> {
                                     final FQN fqn = e.getKey();
                                     return LookupElementBuilder
                                             .createWithSmartPointer(
                                                     fqn.startWith(modulePath) ? fqn.getName() : fqn.getFQN(), e.getValue()
                                             )
                                             .withPresentableText(fqn.getName())
                                             .withIcon(e.getValue().getIcon(Iconable.ICON_FLAG_VISIBILITY))
                                             .withTailText(" (" + fqn.getPathString() + ")", true)
                                             .withAutoCompletionPolicy(AutoCompletionPolicy.GIVE_CHANCE_TO_OVERWRITE);
                                 }
                         )
                         .toArray(LookupElement[]::new);
    }
}
