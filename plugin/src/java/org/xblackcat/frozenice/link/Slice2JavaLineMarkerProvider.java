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
import org.xblackcat.frozenice.psi.*;
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
public class Slice2JavaLineMarkerProvider extends RelatedItemLineMarkerProvider {
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
                items.add(classImplClass);

                // Search for implementations
                items.addAll(ClassInheritorsSearch.search(classImplClass, true).findAll());
            }
        }

        if (!items.isEmpty()) {
            final NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(SliceIcons.TRANSLATED).
                    setTargets(items).setTooltipText("Used");
            result.add(builder.createLineMarkerInfo(element));
        }
    }

    private void collectToJavaMethodLinks(
            Collection<? super RelatedItemLineMarkerInfo> result,
            boolean forNavigation,
            Set<PsiElement> visited,
            SliceMethodDef element
    ) {
        List<PsiElement> items = new ArrayList<>();

        PsiClass implClass = SliceHelper.searchImplementation(
                PsiTreeUtil.getParentOfType(element, SliceClassDef.class)
        );
        if (implClass == null) {
            implClass = SliceHelper.searchImplementation(PsiTreeUtil.getParentOfType(element, SliceInterfaceDef.class));
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
                    .create(SliceIcons.TRANSLATED)
                    .setTargets(items)
                    .setTooltipText("Used");
            result.add(builder.createLineMarkerInfo(element));
        }
    }
}
