package org.xblackcat.frozenidea.link.java;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.index.GeneratedJavaNamesIndex;
import org.xblackcat.frozenidea.psi.SliceDataTypeElement;
import org.xblackcat.frozenidea.psi.SliceMethodDef;
import org.xblackcat.frozenidea.util.SliceBundle;
import org.xblackcat.frozenidea.util.SliceIcons;
import org.xblackcat.frozenidea.util.SliceUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Java2SliceLineMarkerProvider extends LineMarkerProviderDescriptor {
    private final Option myDeclaredMethodOption = new Option(
            "slice.declared.method",
            SliceBundle.message("gutter.go.to.ice.declared.method"),
            SliceIcons.DECLARED_ICE_METHOD
    );
    private final Option myImplementingMethodOption = new Option(
            "slice.java.declared.method",
            SliceBundle.message("gutter.go.to.slice.declaration.method"),
            SliceIcons.IMPLEMENTED_SLICE_METHOD
    );
    private final Option myImplementingInterfaceOption = new Option(
            "slice.java.declared.interface",
            SliceBundle.message("gutter.go.to.slice.declaration.interface"),
            SliceIcons.IMPLEMENTED_SLICE_CLASS
    );

    @Override
    @Nullable
    public LineMarkerInfo<PsiElement> getLineMarkerInfo(@NotNull final PsiElement element) {
        PsiElement parent = element.getParent();
        if (element instanceof PsiIdentifier &&
                parent instanceof PsiReferenceExpression &&
                parent.getParent() instanceof PsiMethodCallExpression) {
            if (!myDeclaredMethodOption.isEnabled()) {
                return null;
            }

            PsiMethodCallExpression methodRef = (PsiMethodCallExpression) parent.getParent();
            final PsiMethod method = methodRef.resolveMethod();
            if (method == null) {
                return null;
            }

            final int parametersCount = method.getParameterList().getParametersCount();

            List<PsiElement> targets = new ArrayList<>();
            List<SliceDataTypeElement> sliceClasses = searchSliceClasses(method.getContainingClass());

            for (SliceDataTypeElement e : sliceClasses) {
                final List<SliceMethodDef> methodDefs = SliceUtil.getMethodList(e);
                if (methodDefs == null || methodDefs.isEmpty()) {
                    continue;
                }
                for (SliceMethodDef sliceMethod : methodDefs) {
                    if (method.getName().equals(sliceMethod.getName()) &&
                            (parametersCount == sliceMethod.getParametersCount() ||
                                    parametersCount - 1 == sliceMethod.getParametersCount())) {
                        targets.add(sliceMethod);
                    }
                }
            }

            if (targets.isEmpty()) {
                return null;
            }

            final NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(SliceIcons.DECLARED_ICE_METHOD).
                    setTargets(targets).setTooltipText(SliceBundle.message("gutter.go.to.slice.declaration.method"));

            return builder.createLineMarkerInfo(element);
        } else if (element instanceof PsiIdentifier && parent instanceof PsiMethod) {
            if (!myImplementingMethodOption.isEnabled()) {
                return null;
            }

            PsiMethod method = (PsiMethod) parent;

            final int parametersCount = method.getParameterList().getParametersCount() - 1;
            if (parametersCount < 0) {
                return null;
            }
            List<PsiElement> targets = new ArrayList<>();

            List<SliceDataTypeElement> sliceClasses = searchSliceClasses(method.getContainingClass());

            for (SliceDataTypeElement e : sliceClasses) {
                final List<SliceMethodDef> methodDefs = SliceUtil.getMethodList(e);
                if (methodDefs == null || methodDefs.isEmpty()) {
                    continue;
                }
                for (SliceMethodDef sliceMethod : methodDefs) {
                    if (method.getName().equals(sliceMethod.getName()) && parametersCount == sliceMethod.getParametersCount()) {
                        targets.add(sliceMethod);
                    }
                }
            }

            if (targets.isEmpty()) {
                return null;
            }

            final NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(SliceIcons.IMPLEMENTED_SLICE_METHOD).
                    setTargets(targets).setTooltipText(SliceBundle.message("gutter.go.to.slice.declaration.method"));

            return builder.createLineMarkerInfo(element);
        } else if (element instanceof PsiIdentifier && parent instanceof PsiClass) {
            if (!myImplementingInterfaceOption.isEnabled()) {
                return null;
            }

            List<SliceDataTypeElement> targets = searchSliceClasses((PsiClass) parent);

            if (targets.isEmpty()) {
                return null;
            }

            final NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(SliceIcons.IMPLEMENTED_SLICE_CLASS).
                    setTargets(targets).setTooltipText(SliceBundle.message("gutter.go.to.slice.declaration.interface"));

            return builder.createLineMarkerInfo(element);
        }

        return null;
    }

    @NotNull
    private List<SliceDataTypeElement> searchSliceClasses(PsiClass parent) {
        List<SliceDataTypeElement> targets = new ArrayList<>();
        Set<PsiElement> visited = new HashSet<>();

        Module module = ModuleUtil.findModuleForPsiElement(parent);

        for (
                PsiClass targetClass = parent;
                targetClass != null;
                targetClass = targetClass.getSuperClass()
        ) {
            searchReferences(targets, visited, targetClass, module);
        }
        return targets;
    }

    private void searchReferences(
            List<SliceDataTypeElement> targets,
            Set<PsiElement> visited,
            PsiClass targetClass,
            Module module
    ) {
        if (!visited.add(targetClass)) {
            return;
        }
        final String name = targetClass.getQualifiedName();
        if (name == null) {
            return;
        }

        final Project project = targetClass.getProject();
        final GlobalSearchScope scope = GlobalSearchScope.moduleWithDependenciesScope(module);
        final SliceDataTypeElement sliceClass = GeneratedJavaNamesIndex.findDeclaration(name, project, scope);
        if (sliceClass != null) {
            targets.add(sliceClass);
        }

        if (name.endsWith("Disp")) {
            final SliceDataTypeElement sliceClassDisp = GeneratedJavaNamesIndex.findDeclaration(
                    name.substring(0, name.length() - 4),
                    project,
                    scope
            );

            if (sliceClassDisp != null) {
                targets.add(sliceClassDisp);
            }

        } else if (name.endsWith("Prx")) {
            final SliceDataTypeElement sliceClassPrx = GeneratedJavaNamesIndex.findDeclaration(
                    name.substring(0, name.length() - 3),
                    project,
                    scope
            );

            if (sliceClassPrx != null) {
                targets.add(sliceClassPrx);
            }
        }

        for (PsiClass i : targetClass.getInterfaces()) {
            searchReferences(targets, visited, i, module);
        }
    }

    @Override
    public String getName() {
        return SliceBundle.message("java.to.slice.line.markers");
    }

    @NotNull
    @Override
    public Option @NotNull [] getOptions() {
        return new Option[]{myDeclaredMethodOption, myImplementingMethodOption, myImplementingInterfaceOption};
    }
}