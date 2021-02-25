package org.xblackcat.frozenidea.link.java;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
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
import org.xblackcat.frozenidea.util.SliceBundle;
import org.xblackcat.frozenidea.util.SliceIcons;

import java.util.*;

/**
 * 14.06.12 14:26
 *
 * @author xBlackCat
 */
public class Slice2JavaLineMarkerProvider extends RelatedItemLineMarkerProvider {
    private final Option myImplementingMethodOption = new Option(
            "slice.java.translated.method",
            SliceBundle.message("gutter.go.to.java.translated.method"),
            SliceIcons.IMPLEMENTED_SLICE_METHOD
    );
    private final Option myImplementingInterfaceOption = new Option(
            "slice.java.translated.interface",
            SliceBundle.message("gutter.go.to.java.translated.interface"),
            SliceIcons.IMPLEMENTED_SLICE_CLASS
    );

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

        SliceModule module = element.getModule();
        if (module == null) {
            return Collections.emptyList();
        }

        List<PsiClass> result = new ArrayList<>();
        final JavaModuleHelper javaModuleHelper = JavaModuleHelper.getJavaHelper(element);

        final PsiClass javaClass = javaModuleHelper.findClass(element.getModule(), name);
        if (javaClass != null) {
            result.add(javaClass);
        }

        final PsiClass javaDispClass = javaModuleHelper.findClass(element.getModule(), name + "Disp");
        if (javaDispClass != null) {
            result.add(javaDispClass);
        }

        return result;
    }

    private static void collectToJavaClassLinks(
            Collection<? super RelatedItemLineMarkerInfo<?>> result,
            Set<PsiElement> visited,
            SliceDataTypeElement element
    ) {
        if (element.getId() == null) {
            return;
        }
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
            final NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(SliceIcons.TRANSLATED_CLASS).
                    setTargets(items).setTooltipText(SliceBundle.message("gutter.go.to.java.translated.interface"));
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
        if (!(body instanceof SliceBodyBlock)) {
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
                    .create(SliceIcons.TRANSLATED_METHOD)
                    .setTargets(items)
                    .setTooltipText(SliceBundle.message("gutter.go.to.java.translated.method"));
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
        if (element.isClass()) {
            SliceHelper.findAllSubclasses(elements, module, element);
        } else if (element.isInterface()) {
            SliceHelper.findAllImplementations(elements, module, element);
        } else {
            return Collections.emptySet();
        }

        Set<PsiClass> items = new HashSet<>(classes);
        for (PsiClass clazz : classes) {
            final String clazzName = clazz.getName();
            if (clazzName == null) {
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

    @Override
    public void collectNavigationMarkers(
            @NotNull List<? extends PsiElement> elements,
            @NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result,
            boolean forNavigation
    ) {
        Set<PsiElement> visited = forNavigation ? new THashSet<>() : new AbyssSet<>();

        for (PsiElement element : elements) {
            if (element instanceof SliceNamedElement) {
                // The element is interface
                if (element instanceof SliceDataTypeElement) {
                    if (myImplementingInterfaceOption.isEnabled()) {
                        collectToJavaClassLinks(result, visited, (SliceDataTypeElement) element);
                    }
                } else if (element instanceof SliceMethodDef) {
                    if (myImplementingMethodOption.isEnabled()) {
                        collectToJavaMethodLinks(result, visited, (SliceMethodDef) element);
                    }
                }
            }
        }
    }

    @Override
    public String getName() {
        return SliceBundle.message("slice.to.java.line.markers");
    }

    @NotNull
    @Override
    public Option @NotNull [] getOptions() {
        return new Option[]{myImplementingMethodOption, myImplementingInterfaceOption};
    }
}
