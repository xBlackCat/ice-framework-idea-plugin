package org.xblackcat.frozenidea.psi.impl;

import com.intellij.navigation.ColoredItemPresentation;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.FileBasedIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.IceFileType;
import org.xblackcat.frozenidea.psi.*;

import javax.swing.*;
import java.util.*;
import java.util.function.Predicate;


/**
 * 06.07.12 11:41
 *
 * @author xBlackCat
 */
public class SlicePsiImplUtil {
    @NotNull
    public static PsiReference[] getReferences(SliceTypeReference o) {
        return new PsiReference[]{
                getReference(o), // Slice reference is the main
        };
    }

    @NotNull
    public static PsiReference[] getReferences(SliceEnumConstantReference o) {
        return new PsiReference[]{
                getReference(o), // Slice reference is the main
        };
    }

    @NotNull
    public static PsiReference getReference(SliceTypeReference o) {
        return new SliceReferenceImpl<>(o, TextRange.from(0, o.getTextLength()));
    }

    @NotNull
    public static PsiReference getReference(SliceEnumConstantReference o) {
        return new SliceEnumConstRefImpl(o);
    }

    @NotNull
    public static String getValue(SliceStringLiteral o) {
        return StringUtil.unescapeStringCharacters(o.getText());
    }

    public static Integer getConstant(SliceEnumConstantInitializer o) {
        final String text = o.getText();
        if (text == null || text.isEmpty()) {
            return null;
        }
        try {
            // TODO: check modifiers for other than decimal base
            return Integer.parseInt(StringUtil.unescapeStringCharacters(text));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @NotNull
    public static String getValue(SliceIntegerLiteral o) {
        // TODO: fix
        return StringUtil.unescapeStringCharacters(o.getText());
    }

    @NotNull
    public static String getValue(SliceFloatLiteral o) {
        // TODO: fix
        return StringUtil.unescapeStringCharacters(o.getText());
    }

    @NotNull
    public static String getValue(SliceBoolLiteral o) {
        // TODO: fix
        return StringUtil.unescapeStringCharacters(o.getText());
    }

    public static PsiElement resolveDataType(SliceTypeReference constType, TextRange from) {
        final List<SliceNamedElement> elements = resolveDataTypes(constType, from);
        return elements.size() == 1 ? elements.get(0) : null;
    }

    protected static List<SliceNamedElement> resolveDataTypes(SliceCompositeElement element, TextRange rangeInElement) {
        SliceModule module = PsiTreeUtil.getParentOfType(element, SliceModule.class);
        if (module == null) {
            return Collections.emptyList();
        }

        FQN referenceName = FQN.buildFQN(rangeInElement.substring(element.getText()), module);

        return searchElements(element.getProject(), c -> referenceName.equals(FQN.buildFQN(c)));
    }


    @NotNull
    public static List<SliceNamedElement> searchElements(Project project, Predicate<SliceNamedElement> matcher) {
        List<SliceNamedElement> result = new ArrayList<>();

        Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(
                FileTypeIndex.NAME,
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

    private static void searchThroughModule(
            SliceModule m,
            List<SliceNamedElement> result,
            Predicate<SliceNamedElement> matcher
    ) {
        // Search in current file and module
        for (PsiElement c : m.getChildren()) {
            if (c instanceof SliceDataTypeElement) {
                if (matcher.test((SliceDataTypeElement) c)) {
                    if (c instanceof SliceClassDef) {
                        if (((SliceClassDef) c).getClassBody() != null) {
                            result.add((SliceClassDef) c);
                        }
                    } else if (c instanceof SliceInterfaceDef) {
                        if (((SliceInterfaceDef) c).getInterfaceBody() != null) {
                            result.add((SliceInterfaceDef) c);
                        }
                    } else {
                        result.add((SliceDataTypeElement) c);
                    }
                }
            }
        }
        for (SliceModule mm : m.getModuleList()) {
            searchThroughModule(mm, result, matcher);
        }
    }

    public static <T extends SliceNamedElement> void addAll(Map<FQN, T> map, Collection<? extends T> collection) {
        for (T el : collection) {
            map.put(FQN.buildFQN(el), el);
        }
    }

    public static ItemPresentation getPresentation(final SliceNamedElement element) {
        return new ColoredItemPresentation() {
            @Nullable
            @Override
            public TextAttributesKey getTextAttributesKey() {
                return isDeprecated() ? CodeInsightColors.DEPRECATED_ATTRIBUTES : null;
            }

            private boolean isDeprecated() {
                return false;
            }

            @Nullable
            @Override
            public String getPresentableText() {
                return element.getName();
            }

            @Nullable
            @Override
            public String getLocationString() {
                return element.getContainingFile().getName();
            }

            @Nullable
            @Override
            public Icon getIcon(boolean unused) {
                return element.getIcon(Iconable.ICON_FLAG_VISIBILITY);
            }
        };
    }
}
