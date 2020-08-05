package org.xblackcat.frozenidea.util;

import com.intellij.codeInsight.lookup.AutoCompletionPolicy;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.IceFileType;
import org.xblackcat.frozenidea.psi.SliceDataTypeElement;
import org.xblackcat.frozenidea.psi.SliceFile;
import org.xblackcat.frozenidea.psi.SliceModule;
import org.xblackcat.frozenidea.psi.SliceNamedElement;

import java.util.*;
import java.util.function.Predicate;

/**
 *
 */
public class SlicePsiUtil {
    public static final Predicate<SliceNamedElement> SLICE_CLASS_PREDICATE = c -> c instanceof SliceDataTypeElement &&
            ((SliceDataTypeElement) c).isClass();
    public static final Predicate<SliceNamedElement> SLICE_INTERFACE_PREDICATE = c -> c instanceof SliceDataTypeElement &&
            ((SliceDataTypeElement) c).isInterface();
    public static final Predicate<SliceNamedElement> SLICE_EXCEPTION_PREDICATE = c -> c instanceof SliceDataTypeElement &&
            ((SliceDataTypeElement) c).isException();
    public static final Predicate<SliceNamedElement> SLICE_DATA_DECLARATION_PREDICATE = c -> c instanceof SliceDataTypeElement;

    @NotNull
    public static List<SliceNamedElement> searchElements(Project project, Predicate<SliceNamedElement> matcher) {
        List<SliceNamedElement> result = new ArrayList<>();

        Collection<VirtualFile> virtualFiles = FileTypeIndex.getFiles(
                IceFileType.INSTANCE,
                GlobalSearchScope.allScope(project)
        );

        for (VirtualFile virtualFile : virtualFiles) {
            SliceFile simpleFile = (SliceFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (simpleFile != null) {
                SliceModule[] modules = PsiTreeUtil.getChildrenOfType(simpleFile, SliceModule.class);
                if (modules != null) {
                    for (SliceModule m : modules) {
                        searchThroughModule(m, result, matcher);
                    }
                }
            }
        }
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    private static void searchThroughModule(SliceModule m, List<SliceNamedElement> result, Predicate<SliceNamedElement> matcher) {
        // Search in current file and module
        for (PsiElement c : m.getChildren()) {
            if (c instanceof SliceDataTypeElement) {
                final SliceDataTypeElement typeElement = (SliceDataTypeElement) c;
                if (matcher.test(typeElement)) {
                    if (typeElement.isClass() || typeElement.isInterface() || typeElement.isException()) {
                        if ((typeElement).getBodyBlock() != null) {
                            result.add(typeElement);
                        }
                    } else {
                        result.add(typeElement);
                    }
                }
            }
        }
        for (SliceModule mm : m.getModuleList()) {
            searchThroughModule(mm, result, matcher);
        }
    }

    public static void findAll(Project project, Map<FQN, SliceNamedElement> map, Predicate<SliceNamedElement> matcher) {
        for (SliceNamedElement el : searchElements(project, matcher)) {
            map.put(FQN.buildFQN(el), el);
        }
    }

    @NotNull
    public static LookupElement[] buildLookupElementsForReferences(FQN modulePath, Map<FQN, SliceNamedElement> references) {
        return references.entrySet()
                .stream()
                .map(e -> buildLookupElement(modulePath, e.getKey(), e.getValue()))
                .toArray(LookupElement[]::new);
    }

    @NotNull
    public static LookupElement buildLookupElement(FQN defaultPath, FQN fqn, SliceNamedElement element) {
        return LookupElementBuilder
                .createWithSmartPointer(fqn.startWith(defaultPath) ? fqn.getName() : fqn.getFQN(), element)
                .withPresentableText(fqn.getName())
                .withIcon(element.getIcon(Iconable.ICON_FLAG_VISIBILITY))
                .withTailText(" (" + fqn.getPathString() + ")", true)
                .withAutoCompletionPolicy(AutoCompletionPolicy.GIVE_CHANCE_TO_OVERWRITE);
    }
}
