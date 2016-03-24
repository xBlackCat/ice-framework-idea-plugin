package org.xblackcat.frozenidea.link;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import gnu.trove.THashSet;
import org.xblackcat.frozenidea.integration.SliceHelper;
import org.xblackcat.frozenidea.psi.*;
import org.xblackcat.frozenidea.util.SliceIcons;

import java.util.*;

/**
 * 14.06.12 14:26
 *
 * @author xBlackCat
 */
public class Slice2JavaLineMarkerProvider extends RelatedItemLineMarkerProvider {
    @Override
    public void collectNavigationMarkers(
            List<PsiElement> elements,
            Collection<? super RelatedItemLineMarkerInfo> result,
            boolean forNavigation
    ) {
        Set<PsiElement> visited = forNavigation ? new THashSet<>() : null;

        for (PsiElement element : elements) {
            if (element instanceof SliceNamedElement) {
                // The element is interface
                if (element instanceof SliceDataTypeElement) {
                    collectToJavaClassLinks(result, forNavigation, visited, (SliceDataTypeElement) element);
                } else if (element instanceof SliceMethodDef) {
                    collectToJavaMethodLinks(result, forNavigation, visited, (SliceMethodDef) element);
                }
            }
        }
    }

    private static void collectToJavaClassLinks(
            Collection<? super RelatedItemLineMarkerInfo> result,
            boolean forNavigation,
            Set<PsiElement> visited,
            SliceDataTypeElement element
    ) {
        List<PsiElement> items = new ArrayList<>();
        PsiClass classImplClass = SliceHelper.searchImplementation(element);

        if (classImplClass != null) {
            if (!forNavigation || visited.add(element)) {
                // Search for implementations
                final Collection<PsiClass> classes = ClassInheritorsSearch.search(classImplClass, true).findAll();
                items.addAll(filterGeneratedClasses(classes, element));
            }
        }

        if (!items.isEmpty()) {
            final NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(SliceIcons.TRANSLATED).
                    setTargets(items).setTooltipText("Used");
            result.add(builder.createLineMarkerInfo(element));
        }
    }

    private static void collectToJavaMethodLinks(
            Collection<? super RelatedItemLineMarkerInfo> result,
            boolean forNavigation,
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
        PsiClass implClass = SliceHelper.searchImplementation(typeElement);

        if (implClass != null) {
            if (!forNavigation || visited.add(element)) {
                final PsiMethod[] m = implClass.findMethodsByName(element.getName(), false);
                if (m.length != 0) {
                    PsiMethod baseMethod = m[0];

                    // Search for implementations
                    Collection<PsiClass> classes = ClassInheritorsSearch.search(implClass, true).findAll();

                    for (PsiClass clazz : filterGeneratedClasses(classes, typeElement)) {
                        Collections.addAll(items, clazz.findMethodsBySignature(baseMethod, false));
                    }
                }
            }
        }

        if (!items.isEmpty()) {
            final NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
                    .create(SliceIcons.TRANSLATED)
                    .setTargets(items)
                    .setTooltipText("Used");
            result.add(builder.createLineMarkerInfo(element));
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

                if (clazzName.equals("_" + name + "Operations") ||
                    clazzName.equals(name) ||
                    clazzName.equals("_" + name + "Disp")) {
                    items.remove(clazz);
                }
            }
        }

        return items;
    }
}
