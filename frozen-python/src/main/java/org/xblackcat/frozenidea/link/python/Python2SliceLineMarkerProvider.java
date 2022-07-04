package org.xblackcat.frozenidea.link.python;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.jetbrains.python.PyTokenTypes;
import com.jetbrains.python.psi.*;
import com.jetbrains.python.psi.resolve.PyResolveContext;
import com.jetbrains.python.psi.types.PyCallableType;
import com.jetbrains.python.psi.types.PyFunctionType;
import com.jetbrains.python.psi.types.TypeEvalContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.index.python.GeneratedPythonNamesIndex;
import org.xblackcat.frozenidea.psi.SliceDataTypeElement;
import org.xblackcat.frozenidea.psi.SliceMethodDef;
import org.xblackcat.frozenidea.util.SliceBundle;
import org.xblackcat.frozenidea.util.SliceIcons;
import org.xblackcat.frozenidea.util.SliceUtil;

import java.util.*;

public class Python2SliceLineMarkerProvider extends LineMarkerProviderDescriptor {
    private final Option myDeclaredMethodOption = new Option(
            "slice.declared.method",
            SliceBundle.message("gutter.go.to.ice.declared.method"),
            SliceIcons.DECLARED_ICE_METHOD
    );

    @Override
    @Nullable
    public LineMarkerInfo<PsiElement> getLineMarkerInfo(@NotNull final PsiElement element) {
        PsiElement parent = element.getParent();
        if (element instanceof LeafPsiElement &&
                ((LeafPsiElement) element).getElementType() == PyTokenTypes.IDENTIFIER &&
                parent instanceof PyReferenceExpression &&
                parent.getParent() instanceof PyCallExpression) {
            if (!myDeclaredMethodOption.isEnabled()) {
                return null;
            }

            PyCallExpression methodRef = (PyCallExpression) parent.getParent();

            final PyResolveContext resolveContext = PyResolveContext.implicitContext(TypeEvalContext.codeInsightFallback(null));
            final List<@NotNull PyCallableType> calleeList = methodRef.multiResolveCallee(resolveContext);
            List<PsiElement> targets = new ArrayList<>();

            for (PyCallableType callee : calleeList) {
                if (!(callee instanceof PyFunctionType)) {
                    continue;
                }
                final PyCallable callable = callee.getCallable();
                if (callable == null) {
                    continue;
                }
                final PyFunction method = callable.asMethod();
                if (method == null) {
                    continue;
                }

                final PyParameter[] parameters = method.getParameterList().getParameters();
                int parametersCount = parameters.length;
                if (parameters.length > 0 && parameters[0].isSelf()) {
                    parametersCount--;
                }

                List<SliceDataTypeElement> sliceClasses = searchSliceClasses(method.getContainingClass());

                for (SliceDataTypeElement e : sliceClasses) {
                    final List<SliceMethodDef> methodDefs = SliceUtil.getMethodList(e);
                    if (methodDefs == null || methodDefs.isEmpty()) {
                        continue;
                    }
                    for (SliceMethodDef sliceMethod : methodDefs) {
                        final String name = sliceMethod.getName();
                        final int count = sliceMethod.getParametersCount();
                        if ((Objects.equals(name, method.getName()) || Objects.equals(name + "Async", method.getName())) &&
                                (parametersCount == count || parametersCount - 1 == count)) {
                            targets.add(sliceMethod);
                        }
                    }
                }
            }
            if (targets.isEmpty()) {
                return null;
            }

            final NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(SliceIcons.DECLARED_ICE_METHOD).
                    setTargets(targets).setTooltipText(SliceBundle.message("gutter.go.to.slice.declaration.method"));

            return builder.createLineMarkerInfo(element);
        }

        return null;
    }

    @NotNull
    private List<SliceDataTypeElement> searchSliceClasses(PyClass parent) {
        if (parent == null) {
            return Collections.emptyList();
        }

        List<SliceDataTypeElement> targets = new ArrayList<>();
        Set<PsiElement> visited = new HashSet<>();

        searchReferences(targets, visited, parent);
        return targets;
    }

    private void searchReferences(List<SliceDataTypeElement> targets, Set<PsiElement> visited, PyClass targetClass) {
        if (!visited.add(targetClass)) {
            return;
        }
        final String name = targetClass.getQualifiedName();
        if (name == null) {
            return;
        }

        final Project project = targetClass.getProject();
        final GlobalSearchScope scope = GlobalSearchScope.projectScope(project);
        final SliceDataTypeElement sliceClass = GeneratedPythonNamesIndex.findDeclaration(name, project, scope);
        if (sliceClass != null) {
            targets.add(sliceClass);
        }

        if (name.endsWith("Disp")) {
            final SliceDataTypeElement sliceClassDisp = GeneratedPythonNamesIndex.findDeclaration(
                    name.substring(0, name.length() - 4),
                    project,
                    scope
            );

            if (sliceClassDisp != null) {
                targets.add(sliceClassDisp);
            }

        } else if (name.endsWith("Prx")) {
            final SliceDataTypeElement sliceClassPrx = GeneratedPythonNamesIndex.findDeclaration(
                    name.substring(0, name.length() - 3),
                    project,
                    scope
            );

            if (sliceClassPrx != null) {
                targets.add(sliceClassPrx);
            }
        }

        for (PyClass i : targetClass.getSuperClasses(TypeEvalContext.codeAnalysis(project, targetClass.getContainingFile()))) {
            searchReferences(targets, visited, i);
        }
    }

    @Override
    public String getName() {
        return SliceBundle.message("python.to.slice.line.markers");
    }

    @NotNull
    @Override
    public Option @NotNull [] getOptions() {
        return new Option[]{myDeclaredMethodOption};
    }
}