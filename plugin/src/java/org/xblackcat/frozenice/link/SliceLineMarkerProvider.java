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
                    collectImplementations(result, forNavigation, visited, (SliceInterfaceDef) element);

                } else if (element instanceof SliceClassDef) {
                    collectExtending(result, forNavigation, visited, (SliceClassDef) element);
                } else if (element instanceof SliceExceptionDef) {
                    collectExtending(result, forNavigation, visited, (SliceExceptionDef) element);
                }
            }
        }
    }

    private static void collectExtending(
            Collection<? super RelatedItemLineMarkerInfo> result,
            boolean forNavigation,
            Set<PsiElement> visited,
            SliceExceptionDef element
    ) {
        List<PsiElement> items = new ArrayList<>();
        SliceModule module = SliceHelper.getContaiterSliceModule(element);
        if (module == null) {
            return;
        }
        if (forNavigation && !visited.add(element)) {
            return;
        }

        for (SliceExceptionDef exceptionDef : module.getExceptionDefList()) {
            if (searchInExtends(element, exceptionDef.getExtendsList())) {
                items.add(exceptionDef);
            }
        }

        if (!items.isEmpty()) {
            final NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(SliceIcons.OVERRIDDEN).
                    setTargets(items).setTooltipText("Overriding");
            result.add(builder.createLineMarkerInfo(element));
        }
    }

    private static void collectExtending(
            Collection<? super RelatedItemLineMarkerInfo> result,
            boolean forNavigation,
            Set<PsiElement> visited,
            SliceClassDef element
    ) {
        List<PsiElement> items = new ArrayList<>();
        SliceModule module = SliceHelper.getContaiterSliceModule(element);
        if (module == null) {
            return;
        }
        if (forNavigation && !visited.add(element)) {
            return;
        }

        for (SliceClassDef classDef : module.getClassDefList()) {
            if (searchInExtends(element, classDef.getExtendsList())) {
                items.add(classDef);
            }
        }

        if (!items.isEmpty()) {
            final NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(SliceIcons.OVERRIDDEN).
                    setTargets(items).setTooltipText("Overriding");
            result.add(builder.createLineMarkerInfo(element));
        }
    }

    private static boolean searchInExtends(SliceDataTypeElement element, SliceExtendsList list) {
        if (list != null) {
            for (SliceTypeReference type : list.getTypeReferenceList()) {
                final PsiElement reference = type.getReference().resolve();
                if (element.equals(reference)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean searchInImplements(SliceDataTypeElement element, SliceImplementsList list) {
        if (list != null) {
            for (SliceTypeReference type : list.getTypeReferenceList()) {
                final PsiElement reference = type.getReference().resolve();
                if (element.equals(reference)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void collectImplementations(
            Collection<? super RelatedItemLineMarkerInfo> result,
            boolean forNavigation,
            Set<PsiElement> visited,
            SliceDataTypeElement element
    ) {
        List<PsiElement> items = new ArrayList<>();
        SliceModule module = SliceHelper.getContaiterSliceModule(element);
        if (module == null) {
            return;
        }
        if (forNavigation && !visited.add(element)) {
            return;
        }

        for (SliceClassDef classDef : module.getClassDefList()) {
            if (searchInImplements(element, classDef.getImplementsList())) {
                items.add(classDef);
            }
        }

        for (SliceInterfaceDef interfaceDef : module.getInterfaceDefList()) {
            if (searchInExtends(element, interfaceDef.getExtendsList())) {
                items.add(interfaceDef);
            }
        }

        if (!items.isEmpty()) {
            final NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(SliceIcons.IMPLEMENTED).
                    setTargets(items).setTooltipText("Implemented");
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
                    .create(SliceIcons.IMPLEMENTED)
                    .setTargets(items)
                    .setTooltipText("Implemented");
            result.add(builder.createLineMarkerInfo(element));
        }
    }
}
