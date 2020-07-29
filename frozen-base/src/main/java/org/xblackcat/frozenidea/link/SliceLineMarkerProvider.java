package org.xblackcat.frozenidea.link;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.psi.PsiElement;
import gnu.trove.THashSet;
import org.xblackcat.frozenidea.integration.SliceHelper;
import org.xblackcat.frozenidea.psi.*;
import org.xblackcat.frozenidea.util.SliceIcons;

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
        Set<PsiElement> visited = forNavigation ? new THashSet<>() : null;

        for (PsiElement element : elements) {
            if (element instanceof SliceDataTypeElement) {
                final SliceDataTypeElement typeDef = (SliceDataTypeElement) element;
                // The element is interface
                if (typeDef.isInterface()) {
                    collectImplementations(result, forNavigation, visited, typeDef);
                } else if (typeDef.isClass()) {
                    collectExtending(result, forNavigation, visited, typeDef);
                }
            }
        }
    }

    private static void collectExtending(
            Collection<? super RelatedItemLineMarkerInfo> result,
            boolean forNavigation,
            Set<PsiElement> visited,
            SliceDataTypeElement element
    ) {
        List<PsiElement> items = new ArrayList<>();
        SliceModule module = SliceHelper.getContainerSliceModule(element);
        if (module == null) {
            return;
        }
        if (forNavigation && !visited.add(element)) {
            return;
        }

        for (SliceDataTypeElement exceptionDef : module.getDataTypeElementList()) {
            if (searchInExtends(element, exceptionDef.getExtendsDef())) {
                items.add(exceptionDef);
            }
        }

        if (!items.isEmpty()) {
            final NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(SliceIcons.OVERRIDDEN).
                    setTargets(items).setTooltipText("Overriding");
            final PsiElement id = element.getId();
            result.add(builder.createLineMarkerInfo(id != null ? id : element));
        }
    }

    private static boolean searchInExtends(SliceDataTypeElement element, SliceExtendsDef def) {
        return def != null && searchInExtendsList(element, def.getExtendsList());
    }

    private static boolean searchInImplements(SliceDataTypeElement element, SliceImplementsDef def) {
        return def != null && searchInExtendsList(element, def.getExtendsList());
    }

    private static boolean searchInExtendsList(SliceDataTypeElement element, SliceExtendsList list) {
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
        SliceModule module = SliceHelper.getContainerSliceModule(element);
        if (module == null) {
            return;
        }
        if (forNavigation && !visited.add(element)) {
            return;
        }

        for (SliceDataTypeElement typeDef : module.getDataTypeElementList()) {
            if (searchInImplements(element, typeDef.getImplementsDef())) {
                items.add(typeDef);
            }
            if (searchInExtends(element, typeDef.getExtendsDef())) {
                items.add(typeDef);
            }
        }

        if (!items.isEmpty()) {
            final NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(SliceIcons.IMPLEMENTED).
                    setTargets(items).setTooltipText("Implemented");
            final PsiElement id = element.getId();
            if (id != null) {
                result.add(builder.createLineMarkerInfo(id));
            }
        }
    }
}
