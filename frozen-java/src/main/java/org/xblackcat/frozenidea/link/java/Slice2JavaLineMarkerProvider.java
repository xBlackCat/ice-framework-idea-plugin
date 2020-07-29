package org.xblackcat.frozenidea.link.java;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.integration.SliceHelper;
import org.xblackcat.frozenidea.integration.java.JavaModuleHelper;
import org.xblackcat.frozenidea.psi.*;
import org.xblackcat.frozenidea.util.AbyssSet;
import org.xblackcat.frozenidea.util.SliceIcons;

import java.util.*;

/**
 * 14.06.12 14:26
 *
 * @author xBlackCat
 */
public class Slice2JavaLineMarkerProvider extends RelatedItemLineMarkerProvider {
    public static List<PsiClass> searchGeneratedJavaClasses(SliceDataTypeElement element) {
        if (element == null) {
            return Collections.emptyList();
        }

        final PsiElement id = element.getId();
        if (id == null) {
            return Collections.emptyList();
        }
        String name = id.getText();

        if (name == null) {
            return Collections.emptyList();
        }

        SliceModule module = SliceHelper.getContainerSliceModule(element);
        if (module == null) {
            return Collections.emptyList();
        }

        List<PsiClass> result = new ArrayList<>();
        final PsiClass javaClass = checkJavaClass(element, SliceHelper.buildFQN(name, module));
        if (javaClass != null) {
            result.add(javaClass);
        }

        final PsiClass javaDispClass = checkJavaClass(element, SliceHelper.buildFQN(name + "Disp", module));
        if (javaDispClass != null) {
            result.add(javaDispClass);
        }

        return result;
    }

    private static PsiClass checkJavaClass(SliceDataTypeElement element, String implFQN) {
        JavaModuleHelper javaHelper = JavaModuleHelper.getJavaHelper(element);
        NavigatablePsiElement aClass = javaHelper.findClass(implFQN);

        if ((aClass instanceof PsiClass)) {
            return (PsiClass) aClass;
        }
        return null;

    }

    @Override
    public void collectNavigationMarkers(
            List<PsiElement> elements,
            @NotNull Collection<? super RelatedItemLineMarkerInfo> result,
            boolean forNavigation
    ) {
        Set<PsiElement> visited = forNavigation ? new THashSet<>() : new AbyssSet<>();

        for (PsiElement element : elements) {
            if (element instanceof SliceNamedElement) {
                // The element is interface
                if (element instanceof SliceDataTypeElement) {
                    collectToJavaClassLinks(result, visited, (SliceDataTypeElement) element);
                } else if (element instanceof SliceMethodDef) {
                    collectToJavaMethodLinks(result, visited, (SliceMethodDef) element);
                }
            }
        }
    }

    private static void collectToJavaClassLinks(
            Collection<? super RelatedItemLineMarkerInfo<?>> result,
            Set<PsiElement> visited,
            SliceDataTypeElement element
    ) {
        List<PsiClass> classes = searchGeneratedJavaClasses(element);

        List<PsiElement> items = new ArrayList<>(classes);
        for (PsiClass classImplClass : classes) {
            if (visited.add(element)) {
                // Search for implementations
                final Collection<PsiClass> found = ClassInheritorsSearch.search(classImplClass, true).findAll();
                items.addAll(filterGeneratedClasses(found, element));
            }
        }

        if (!items.isEmpty()) {
            final NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(SliceIcons.TRANSLATED_JAVA_CLASS).
                    setTargets(items).setTooltipText("Used");
            result.add(builder.createLineMarkerInfo(element.getId()));
        }
    }

    private static void collectToJavaMethodLinks(
            Collection<? super RelatedItemLineMarkerInfo<?>> result,
            Set<PsiElement> visited,
            SliceMethodDef element
    ) {
        List<PsiElement> items = new ArrayList<>();

        final PsiElement body = element.getParent();
        if (!(body instanceof SliceClassBody) && !(body instanceof SliceInterfaceBody)) {
            return;
        }
        final PsiElement type = body.getParent();
        if (!(type instanceof SliceDataTypeElement)) {
            return;
        }

        final SliceDataTypeElement typeElement = (SliceDataTypeElement) type;
        List<PsiClass> classes = searchGeneratedJavaClasses(typeElement);

        for (PsiClass implClass : classes) {
            if (visited.add(element)) {
                final PsiMethod[] m = implClass.findMethodsByName(element.getName(), false);
                if (m.length != 0) {
                    PsiMethod baseMethod = m[0];

                    // Search for implementations
                    Collection<PsiClass> javaClasses = ClassInheritorsSearch.search(implClass, true).findAll();

                    for (PsiClass clazz : filterGeneratedClasses(javaClasses, typeElement)) {
                        Collections.addAll(items, clazz.findMethodsBySignature(baseMethod, false));
                    }
                }
            }
        }

        if (!items.isEmpty()) {
            final NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
                    .create(SliceIcons.TRANSLATED_JAVA_METHOD)
                    .setTargets(items)
                    .setTooltipText("Used");
            result.add(builder.createLineMarkerInfo(element.getId()));
        }
    }

    private static Collection<PsiClass> filterGeneratedClasses(Collection<PsiClass> classes, SliceDataTypeElement element) {
        final SliceModule module = SliceHelper.getContainerSliceModule(element);
        if (module == null) {
            return Collections.emptySet();
        }

        final Set<SliceDataTypeElement> elements = new HashSet<>();
        elements.add(element);
        if (element instanceof SliceClassDef) {
            SliceHelper.findAllSubclasses(elements, module, (SliceClassDef) element);
        } else if (element instanceof SliceInterfaceDef) {
            SliceHelper.findAllImplementations(elements, module, (SliceInterfaceDef) element);
        } else {
            return Collections.emptySet();
        }

        Set<PsiClass> items = new HashSet<>(classes);
        for (PsiClass clazz : classes) {
            final String clazzName = clazz.getName();
            if (clazzName == null) {
                continue;
            }

            //noinspection SimplifiableIfStatement
            if (!(SliceHelper.buildFQN(clazzName, module)).equals(clazz.getQualifiedName())) {
                // Packages are not equals - not generated class
                continue;
            }


            for (SliceDataTypeElement e : elements) {
                final String name = e.getName();
                if (name == null) {
                    continue;
                }

                if (clazzName.equals(name + "Disp")) {
                    items.remove(clazz);
                }
            }
        }

        return items;
    }

    @NotNull
    @Override
    public Option[] getOptions() {
        return super.getOptions();
    }
}
