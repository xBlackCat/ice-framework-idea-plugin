// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.xblackcat.frozenidea.link;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor;
import com.intellij.codeInsight.daemon.MergeableLineMarkerInfo;
import com.intellij.codeInsight.daemon.impl.MarkerType;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.*;
import com.intellij.psi.search.searches.SuperMethodsSearch;
import com.intellij.psi.util.MethodSignatureBackedByPsiMethod;
import com.intellij.psi.util.PsiExpressionTrimRenderer;
import com.intellij.util.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.integration.SliceModuleHelper;
import org.xblackcat.frozenidea.psi.SliceMethodDef;
import org.xblackcat.frozenidea.psi.impl.FQN;
import org.xblackcat.frozenidea.util.IceMessages;
import org.xblackcat.frozenidea.util.SliceIcons;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Java2SliceLineMarkerProvider extends LineMarkerProviderDescriptor {
    private final Option myImplementingMethodOption = new Option(
            "slice.java.implementing.method",
            "Implementing method",
            SliceIcons.IMPLEMENTED_SLICE_METHOD
    );
    private final Option myImplementingInterfaceOption = new Option(
            "slice.java.implementing.interface",
            "Implementing interface",
            SliceIcons.IMPLEMENTED_SLICE_CLASS
    );

    @Override
    @Nullable
    public LineMarkerInfo<PsiElement> getLineMarkerInfo(@NotNull final PsiElement element) {
        PsiElement parent = element.getParent();
        final SliceModuleHelper sliceHelper = SliceModuleHelper.getSliceHelper(element);
        if (element instanceof PsiIdentifier && parent instanceof PsiMethod) {
            if (!myImplementingMethodOption.isEnabled()) {
                return null;
            }

            PsiMethod method = (PsiMethod) parent;

            List<PsiElement> targets = new ArrayList<>();


            for (
                    MethodSignatureBackedByPsiMethod superSignature = SuperMethodsSearch.search(method, null, true, false).findFirst();
                    superSignature != null;
                    superSignature = SuperMethodsSearch.search(superSignature.getMethod(), null, true, false).findFirst()
            ) {
                final PsiClass containingClass = superSignature.getMethod().getContainingClass();
                if (containingClass == null) {
                    continue;
                }
                final FQN fqn = FQN.buildFQN(containingClass);
                if (fqn == null) {
                    continue;
                }
                final int parametersCount = method.getParameterList().getParametersCount();
                if (parametersCount == 0) {
                    continue;
                }
                final String name = fqn.getName();
                if (name.endsWith("Disp")) {
                    final SliceMethodDef sliceMethod = sliceHelper.findClassMethod(
                            fqn.withNewName(name.substring(0, name.length() - 4)),
                            method.getName(),
                            parametersCount - 1
                    );

                    if (sliceMethod != null) {
                        targets.add(sliceMethod);
                    }
                }
                final SliceMethodDef sliceMethod = sliceHelper.findClassMethod(fqn, method.getName(), parametersCount - 1);

                if (sliceMethod != null) {
                    targets.add(sliceMethod);
                }
            }
            if (targets.isEmpty()) {
                return null;
            }

            final NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(SliceIcons.IMPLEMENTED_SLICE_METHOD).
                    setTargets(targets).setTooltipText(IceMessages.message("goto.ice.declaration.method"));

            return builder.createLineMarkerInfo(element);
        } else if (element instanceof PsiIdentifier && parent instanceof PsiClass) {
            if (!myImplementingInterfaceOption.isEnabled()) {
                return null;
            }

            List<PsiElement> targets = new ArrayList<>();
            Set<PsiElement> visited = new HashSet<>();

            for (
                    PsiClass targetClass = (PsiClass) parent;
                    targetClass != null;
                    targetClass = targetClass.getSuperClass()
            ) {
                searchReferences(sliceHelper, targets, visited, targetClass);
            }

            if (targets.isEmpty()) {
                return null;
            }

            final NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(SliceIcons.IMPLEMENTED_SLICE_CLASS).
                    setTargets(targets).setTooltipText(IceMessages.message("goto.ice.declaration.interface"));

            return builder.createLineMarkerInfo(element);
        }

        return null;
    }

    private void searchReferences(SliceModuleHelper sliceHelper, List<PsiElement> targets, Set<PsiElement> visited, PsiClass targetClass) {
        if (!visited.add(targetClass)) {
            return;
        }
        final FQN fqn = FQN.buildFQN(targetClass);
        if (fqn != null) {
            final PsiElement sliceClass = sliceHelper.findClass(fqn);

            if (sliceClass != null) {
                targets.add(sliceClass);
            }

            final String name = fqn.getName();
            if (name.endsWith("Disp")) {
                final PsiElement sliceClassDisp = sliceHelper.findClass(fqn.withNewName(name.substring(0, name.length() - 4)));

                if (sliceClassDisp != null) {
                    targets.add(sliceClassDisp);
                }

            }

            for (PsiClass i : targetClass.getInterfaces()) {
                searchReferences(sliceHelper, targets, visited, i);
            }
        }
    }


    @Override
    public String getName() {
        return "Java2Slice line markers";
    }

    @NotNull
    @Override
    public Option[] getOptions() {
        return new Option[]{myImplementingMethodOption, myImplementingInterfaceOption};
    }

    private static class ArrowUpLineMarkerInfo extends MergeableLineMarkerInfo<PsiElement> {
        private ArrowUpLineMarkerInfo(@NotNull PsiElement element, @NotNull Icon icon, @NotNull MarkerType markerType) {
            super(element, element.getTextRange(), icon, markerType.getTooltip(),
                  markerType.getNavigationHandler(), GutterIconRenderer.Alignment.LEFT
            );
        }

        @Override
        public boolean canMergeWith(@NotNull MergeableLineMarkerInfo<?> info) {
            if (!(info instanceof ArrowUpLineMarkerInfo)) return false;
            PsiElement otherElement = info.getElement();
            PsiElement myElement = getElement();
            return otherElement != null && myElement != null;
        }

        @Override
        public Icon getCommonIcon(@NotNull List<MergeableLineMarkerInfo> infos) {
            return myIcon;
        }

        @NotNull
        @Override
        public Function<? super PsiElement, String> getCommonTooltip(@NotNull List<MergeableLineMarkerInfo> infos) {
            return (Function<PsiElement, String>) element -> "Multiple method overrides";
        }

        @NotNull
        @Override
        public String getElementPresentation(@NotNull PsiElement element) {
            final PsiElement parent = element.getParent();
            return parent instanceof PsiFunctionalExpression
                    ? PsiExpressionTrimRenderer.render((PsiExpression) parent)
                    : super.getElementPresentation(element);
        }
    }
}