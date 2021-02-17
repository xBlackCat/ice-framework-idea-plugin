package org.xblackcat.frozenidea.link;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.psi.PsiElement;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.integration.SliceHelper;
import org.xblackcat.frozenidea.psi.*;
import org.xblackcat.frozenidea.util.SliceBundle;
import org.xblackcat.frozenidea.util.SliceIcons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class SliceLineMarkerProvider extends RelatedItemLineMarkerProvider {
    private final Option myOverriddenOption = new Option(
            "slice.overridden.class",
            SliceBundle.message("gutter.go.to.overridden.class"),
            SliceIcons.OVERRIDDEN
    );
    private final Option myImplementedOption = new Option(
            "slice.implemented.interface",
            SliceBundle.message("gutter.go.to.implemented.interface"),
            SliceIcons.IMPLEMENTED
    );

    private static void collectExtending(
            Collection<? super RelatedItemLineMarkerInfo<?>> result,
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

        final SliceModuleBody body = module.getModuleBody();
        if (body == null) {
            return;
        }

        for (SliceDataTypeElement exceptionDef : body.getDataTypeElementList()) {
            if (searchInReferenceList(element, exceptionDef.getExtendsBlock())) {
                items.add(exceptionDef);
            }
        }

        if (!items.isEmpty()) {
            final NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(SliceIcons.OVERRIDDEN).
                    setTargets(items).setTooltipText(SliceBundle.message("gutter.go.to.overridden.class"));
            final PsiElement id = element.getId();
            result.add(builder.createLineMarkerInfo(id != null ? id : element));
        }
    }

    private static boolean searchInReferenceList(SliceDataTypeElement element, SliceReferenceListElement list) {
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
            Collection<? super RelatedItemLineMarkerInfo<?>> result,
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

        final SliceModuleBody body = module.getModuleBody();
        if (body == null) {
            return;
        }

        for (SliceDataTypeElement typeDef : body.getDataTypeElementList()) {
            if (searchInReferenceList(element, typeDef.getImplementsBlock())) {
                items.add(typeDef);
            }
            if (searchInReferenceList(element, typeDef.getExtendsBlock())) {
                items.add(typeDef);
            }
        }

        if (!items.isEmpty()) {
            final NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(SliceIcons.IMPLEMENTED).
                    setTargets(items).setTooltipText(SliceBundle.message("gutter.go.to.implemented.interface"));
            final PsiElement id = element.getId();
            if (id != null) {
                result.add(builder.createLineMarkerInfo(id));
            }
        }
    }

    @Override
    public void collectNavigationMarkers(
            @NotNull List<? extends PsiElement> elements,
            @NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result,
            boolean forNavigation
    ) {
        Set<PsiElement> visited = forNavigation ? new THashSet<>() : null;

        for (PsiElement element : elements) {
            if (element instanceof SliceDataTypeElement) {
                final SliceDataTypeElement typeDef = (SliceDataTypeElement) element;
                // The element is interface
                if (typeDef.isInterface()) {
                    if (myImplementedOption.isEnabled()) {
                        collectImplementations(result, forNavigation, visited, typeDef);
                    }
                } else if (typeDef.isClass()) {
                    if (myOverriddenOption.isEnabled()) {
                        collectExtending(result, forNavigation, visited, typeDef);
                    }
                }
            }
        }
    }

    @Override
    public String getName() {
        return SliceBundle.message("slice.line.markers");
    }

    @Override
    public Option @NotNull [] getOptions() {
        return new Option[]{myImplementedOption, myOverriddenOption};
    }
}
