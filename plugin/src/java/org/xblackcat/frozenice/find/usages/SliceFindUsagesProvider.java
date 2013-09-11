package org.xblackcat.frozenice.find.usages;

import com.intellij.ide.TypePresentationService;
import com.intellij.lang.LangBundle;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.ElementDescriptionUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.usageView.UsageViewLongNameLocation;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.integration.SliceHelper;
import org.xblackcat.frozenice.psi.*;

public class SliceFindUsagesProvider implements FindUsagesProvider {
    @Override
    public WordsScanner getWordsScanner() {
        return null;
    }

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return psiElement instanceof SliceNamedElement || psiElement instanceof SliceTypeReference;
    }

    @Override
    public String getHelpId(@NotNull PsiElement psiElement) {
        return null;
    }

    @NotNull
    @Override
    public String getType(@NotNull PsiElement element) {
        if (element instanceof PsiFile) {
            return LangBundle.message("terms.file");
        }
        if (element instanceof SliceExceptionDef) {
            return LangBundle.message("java.terms.exception");
        }
        if (element instanceof SliceModule) {
            return LangBundle.message("java.terms.package");
        }
        if (element instanceof SliceEnumDef) {
            return LangBundle.message("java.terms.enum");
        }
        if (element instanceof SliceInterfaceDef || element instanceof SliceInterfaceDefFw) {
            return LangBundle.message("java.terms.interface");
        }
        if (element instanceof SliceClassDef || element instanceof SliceClassDefFw) {
            return LangBundle.message("java.terms.class");

        }
        if (element instanceof SliceFieldDef) {
            return LangBundle.message("java.terms.field");
        }
        if (element instanceof SliceParameter) {
            return LangBundle.message("java.terms.parameter");
        }
        if (element instanceof SliceMethodDef) {
            return LangBundle.message("java.terms.method");
        }
        if (element instanceof SliceConstant) {
            return LangBundle.message("java.terms.variable");
        }

        final String name = TypePresentationService.getService().getTypePresentableName(element.getClass());
        if (name != null) {
            return name;
        }
        return "";
    }

    @NotNull
    @Override
    public String getDescriptiveName(@NotNull PsiElement element) {
        return ElementDescriptionUtil.getElementDescription(element, UsageViewLongNameLocation.INSTANCE);
    }

    @NotNull
    @Override
    public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        if (element instanceof SliceDataTypeElement || element instanceof SliceModule) {
            return SliceHelper.getFQN((SliceNamedElement) element);
        }

        if (element instanceof SliceTypeReference) {
            // Try to find a class
            SliceModule module = PsiTreeUtil.getParentOfType(element, SliceModule.class);
            if (module != null) {
                String moduleFQN = SliceHelper.getFQN(module);
                String fqn = moduleFQN + "." + element.getText();
                System.err.println(fqn);
                return fqn;
            }
        }

        return "";
    }
}
