package org.xblackcat.frozenidea.link.java;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.integration.SliceModuleHelper;
import org.xblackcat.frozenidea.psi.SliceDataTypeElement;
import org.xblackcat.frozenidea.psi.SliceMethodDef;
import org.xblackcat.frozenidea.util.FQN;
import org.xblackcat.frozenidea.util.IceMessages;
import org.xblackcat.frozenidea.util.SliceIcons;
import org.xblackcat.frozenidea.util.SliceUtil;

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

            final int parametersCount = method.getParameterList().getParametersCount() - 1;
            if (parametersCount < 0) {
                return null;
            }
            List<PsiElement> targets = new ArrayList<>();

            List<SliceDataTypeElement> sliceClasses = searchSliceClasses(method.getContainingClass(), sliceHelper);

            for (SliceDataTypeElement e : sliceClasses) {
                for (SliceMethodDef sliceMethod : SliceUtil.getMethodList(e)) {
                    if (method.getName().equals(sliceMethod.getName()) && parametersCount == sliceMethod.getParametersCount()) {
                        targets.add(sliceMethod);
                    }
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

            List<SliceDataTypeElement> targets = searchSliceClasses((PsiClass) parent, sliceHelper);

            if (targets.isEmpty()) {
                return null;
            }

            final NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(SliceIcons.IMPLEMENTED_SLICE_CLASS).
                    setTargets(targets).setTooltipText(IceMessages.message("goto.ice.declaration.interface"));

            return builder.createLineMarkerInfo(element);
        }

        return null;
    }

    @NotNull
    private List<SliceDataTypeElement> searchSliceClasses(PsiClass parent, SliceModuleHelper sliceHelper) {
        List<SliceDataTypeElement> targets = new ArrayList<>();
        Set<PsiElement> visited = new HashSet<>();

        for (
                PsiClass targetClass = parent;
                targetClass != null;
                targetClass = targetClass.getSuperClass()
        ) {
            searchReferences(sliceHelper, targets, visited, targetClass);
        }
        return targets;
    }

    private void searchReferences(
            SliceModuleHelper sliceHelper,
            List<SliceDataTypeElement> targets,
            Set<PsiElement> visited,
            PsiClass targetClass
    ) {
        if (!visited.add(targetClass)) {
            return;
        }
        final FQN fqn = FQN.buildFQN(targetClass.getQualifiedName());
        if (fqn != null) {
            final SliceDataTypeElement sliceClass = sliceHelper.findClass(fqn);

            if (sliceClass != null) {
                targets.add(sliceClass);
            }

            final String name = fqn.getName();
            if (name.endsWith("Disp")) {
                final SliceDataTypeElement sliceClassDisp = sliceHelper.findClass(fqn.withNewName(name.substring(0, name.length() - 4)));

                if (sliceClassDisp != null) {
                    targets.add(sliceClassDisp);
                }

            }

        }
        for (PsiClass i : targetClass.getInterfaces()) {
            searchReferences(sliceHelper, targets, visited, i);
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
}