package org.xblackcat.frozenidea.psi.impl;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.FileBasedIndex;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.IceFileType;
import org.xblackcat.frozenidea.psi.*;

import java.util.Collection;
import java.util.Map;


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

    protected static PsiElement resolveDataType(SliceCompositeElement element, TextRange rangeInElement) {
        String referenceName = rangeInElement.substring(element.getText());
        SliceModule module = PsiTreeUtil.getParentOfType(element, SliceModule.class);

        if (module == null) {
            return null;
        }

        // Search in current file and module
        for (PsiElement c : module.getChildren()) {
            if (c instanceof SliceDataTypeElement) {
                if (referenceName.equals(((SliceDataTypeElement) c).getName())) {
                    if (c instanceof SliceClassDef) {
                        if (((SliceClassDef) c).getClassBody() != null) {
                            return c;
                        }
                    } else if (c instanceof SliceInterfaceDef) {
                        if (((SliceInterfaceDef) c).getInterfaceBody() != null) {
                            return c;
                        }
                    } else {
                        return c;
                    }
                }
            }
        }

        final Project project = element.getProject();
        Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(
                FileTypeIndex.NAME,
                IceFileType.INSTANCE,
                GlobalSearchScope.allScope(project)
        );


        return null;
    }

    public static <T extends PsiNamedElement> void addAll(Map<String, T> map, Collection<? extends T> collection) {
        for (T el : collection) {
            map.put(el.getName(), el);
        }
    }
}
