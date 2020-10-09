package org.xblackcat.frozenidea.findUsages.slice;

import com.intellij.ide.TypePresentationService;
import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.ElementDescriptionUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.TokenSet;
import com.intellij.usageView.UsageViewLongNameLocation;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.parser.SliceLexer;
import org.xblackcat.frozenidea.psi.*;
import org.xblackcat.frozenidea.util.FQN;
import org.xblackcat.frozenidea.util.SliceBundle;

public class SliceFindUsagesProvider implements FindUsagesProvider {
    @Override
    public WordsScanner getWordsScanner() {
        return new DefaultWordsScanner(
                new SliceLexer(),
                TokenSet.create(SliceTypes.ICE_ID),
                TokenSet.create(SliceTypes.ICE_C_STYLE_COMMENT, SliceTypes.ICE_DOC_STYLE_COMMENT, SliceTypes.ICE_END_OF_LINE_COMMENT),
                TokenSet.create(SliceTypes.ICE_STRING_LITERAL)
        );
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
            return SliceBundle.message("slice.terms.file");
        }
        if (element instanceof SliceDataTypeElement && ((SliceDataTypeElement) element).isException()) {
            return SliceBundle.message("slice.terms.exception");
        }
        if (element instanceof SliceModule) {
            return SliceBundle.message("slice.terms.module");
        }
        if (element instanceof SliceDataTypeElement && ((SliceDataTypeElement) element).isEnum()) {
            return SliceBundle.message("slice.terms.enum");
        }
        if (element instanceof SliceDataTypeElement && ((SliceDataTypeElement) element).isInterface()) {
            return SliceBundle.message("slice.terms.interface");
        }
        if (element instanceof SliceDataTypeElement && ((SliceDataTypeElement) element).isClass()) {
            return SliceBundle.message("slice.terms.class");
        }
        if (element instanceof SliceFieldDef) {
            return SliceBundle.message("slice.terms.field");
        }
        if (element instanceof SliceParameterDef) {
            return SliceBundle.message("slice.terms.parameter");
        }
        if (element instanceof SliceMethodDef) {
            return SliceBundle.message("slice.terms.method");
        }
        if (element instanceof SliceConstantDef) {
            return SliceBundle.message("slice.terms.variable");
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
            return FQN.buildFQN((SliceNamedElement) element).getFQN();
        }

        if (element instanceof SliceTypeReference) {
            return FQN.buildFQN((SliceTypeReference) element).getFQN();
        }

        return "";
    }
}
