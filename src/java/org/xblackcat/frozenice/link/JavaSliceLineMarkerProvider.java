package org.xblackcat.frozenice.link;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import gnu.trove.THashSet;
import org.xblackcat.frozenice.integration.JavaHelper;
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
public class JavaSliceLineMarkerProvider extends RelatedItemLineMarkerProvider {
    @Override
    public void collectNavigationMarkers(List<PsiElement> elements, Collection<? super RelatedItemLineMarkerInfo> result, boolean forNavigation) {
        Set<PsiElement> visited = forNavigation ? new THashSet<PsiElement>() : null;

        for (PsiElement element : elements) {
            if (!(element instanceof SliceNamedElement)) {
                continue;
            }


            // The element is interface
            if (element instanceof SliceInterfaceDef) {
                collectInterfaceLinks(result, forNavigation, visited, (SliceInterfaceDef) element);
            } else if (element instanceof SliceClassDef) {
                collectClassLinks(result, forNavigation, visited, (SliceClassDef) element);
            } else if (element instanceof SliceMethodDef) {
                collectMethodLinks(result, forNavigation, visited, (SliceMethodDef) element);
            }

        }
    }

    private void collectMethodLinks(Collection<? super RelatedItemLineMarkerInfo> result, boolean forNavigation, Set<PsiElement> visited, SliceMethodDef element) {

    }

    private static void collectClassLinks(Collection<? super RelatedItemLineMarkerInfo> result, boolean forNavigation, Set<PsiElement> visited, SliceClassDef element) {
        List<PsiElement> items = new ArrayList<PsiElement>();
        Project project = element.getProject();

        String implFQN = SliceHelper.getFQN(element);

        JavaHelper javaHelper = JavaHelper.getJavaHelper(project);
        NavigatablePsiElement interfaceImplClass = javaHelper.findClass(implFQN);

        if (interfaceImplClass instanceof PsiClass) {
            PsiClass implClass = (PsiClass) interfaceImplClass;

            if (implClass.hasModifierProperty(PsiModifier.ABSTRACT) && (!forNavigation || visited.add(interfaceImplClass))) {
                // Search for implementations
                PsiClass first = ClassInheritorsSearch.search(implClass, false).findFirst();

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

    private static void collectInterfaceLinks(Collection<? super RelatedItemLineMarkerInfo> result, boolean forNavigation, Set<PsiElement> visited, SliceInterfaceDef element) {
        List<PsiElement> items = new ArrayList<PsiElement>();
        Project project = element.getProject();

        String moduleFQN = SliceHelper.getFQN(element.getModule());
        String name = element.getName();

        String implFQN = moduleFQN + "._" + name + "Disp";

        JavaHelper javaHelper = JavaHelper.getJavaHelper(project);
        NavigatablePsiElement interfaceImplClass = javaHelper.findClass(implFQN);

        if (interfaceImplClass instanceof PsiClass && (!forNavigation || visited.add(interfaceImplClass))) {
            // Search for implementations
            PsiClass implClass = (PsiClass) interfaceImplClass;

            PsiClass first = ClassInheritorsSearch.search(implClass, false).findFirst();

            if (first != null) {
                items.add(first);
            }

        }

        if (!items.isEmpty()) {
            final NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(SliceIcons.IMPLEMENTED).
                    setTargets(items).setTooltipText("Implemented");
            result.add(builder.createLineMarkerInfo(element));
        }
    }
}
