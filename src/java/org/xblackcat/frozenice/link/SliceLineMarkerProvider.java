package org.xblackcat.frozenice.link;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import com.intellij.psi.util.PsiTreeUtil;
import gnu.trove.THashSet;
import org.xblackcat.frozenice.integration.SliceHelper;
import org.xblackcat.frozenice.psi.SliceClassDef;
import org.xblackcat.frozenice.psi.SliceInterfaceDef;
import org.xblackcat.frozenice.psi.SliceMethodDef;
import org.xblackcat.frozenice.psi.SliceNamedElement;
import org.xblackcat.frozenice.util.SliceIcons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 14.06.12 14:26
 *
 * @author xBlackCat
 */
public class SliceLineMarkerProvider extends RelatedItemLineMarkerProvider {
    @Override
    public void collectNavigationMarkers(
            List<PsiElement> elements,
            Collection<? super RelatedItemLineMarkerInfo> result,
            boolean forNavigation
    ) {
        Set<PsiElement> visited = forNavigation ? new THashSet<PsiElement>() : null;

        for (PsiElement element : elements) {
            if (element instanceof SliceNamedElement) {
                // The element is interface
                if (element instanceof SliceInterfaceDef) {
                    collectInterfaceLinks(result, forNavigation, visited, (SliceInterfaceDef) element);
                } else if (element instanceof SliceClassDef) {
                    collectClassLinks(result, forNavigation, visited, (SliceClassDef) element);
                } else if (element instanceof SliceMethodDef) {
                    collectMethodLinks(result, forNavigation, visited, (SliceMethodDef) element);
                }
            } else {

            }

        }
    }

    private void collectMethodLinks(
            Collection<? super RelatedItemLineMarkerInfo> result,
            boolean forNavigation,
            Set<PsiElement> visited,
            SliceMethodDef element
    ) {
        List<PsiElement> items = new ArrayList<PsiElement>();

        PsiClass implClass = SliceHelper.searchClassImplementation(
                PsiTreeUtil.getParentOfType(
                        element,
                        SliceClassDef.class
                )
        );
        if (implClass == null) {
            implClass = SliceHelper.searchInterfaceImplementation(
                    PsiTreeUtil.getParentOfType(
                            element,
                            SliceInterfaceDef.class
                    )
            );
        }

        if (implClass != null) {
            if (!forNavigation || visited.add(element)) {
                // Search for implementations
                PsiClass first = ClassInheritorsSearch.search(implClass, false).findFirst();


                if (first != null) {
                    PsiMethod[] methods = first.findMethodsByName(element.getName(), false);
                    if (methods.length > 0) {
                        items.add(methods[0]);
                    }
                }
            }
        }

        if (!items.isEmpty()) {
            final NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
                    .create(SliceIcons.IMPLEMENTED)
                    .setTargets(items)
                    .setTooltipText("Implemented");
            result.add(builder.createLineMarkerInfo(element));
        }
    }

    private static void collectClassLinks(
            Collection<? super RelatedItemLineMarkerInfo> result,
            boolean forNavigation,
            Set<PsiElement> visited,
            SliceClassDef element
    ) {
        List<PsiElement> items = new ArrayList<PsiElement>();
        PsiClass classImplClass = SliceHelper.searchClassImplementation(element);

        if (classImplClass != null) {
            if (!forNavigation || visited.add(element)) {
                // Search for implementations
                PsiClass first = ClassInheritorsSearch.search(classImplClass, false).findFirst();

                if (first != null) {
                    items.add(first);
                }
            }
        }

        if (!items.isEmpty()) {
            final NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(SliceIcons.OVERRIDDEN).
                    setTargets(items).setTooltipText("Implemented");
            result.add(builder.createLineMarkerInfo(element));
        }
    }

    private static void collectInterfaceLinks(
            Collection<? super RelatedItemLineMarkerInfo> result,
            boolean forNavigation,
            Set<PsiElement> visited,
            SliceInterfaceDef element
    ) {
        List<PsiElement> items = new ArrayList<PsiElement>();
        PsiClass interfaceImplClass = SliceHelper.searchInterfaceImplementation(element);

        if (interfaceImplClass != null) {
            if (!forNavigation || visited.add(element)) {
                // Search for implementations
                PsiClass first = ClassInheritorsSearch.search(interfaceImplClass, false).findFirst();

                if (first != null) {
                    items.add(first);
                }
            }
        }

        if (!items.isEmpty()) {
            final NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(SliceIcons.IMPLEMENTED).
                    setTargets(items).setTooltipText("Implemented");
            result.add(builder.createLineMarkerInfo(element));
        }
    }

}
