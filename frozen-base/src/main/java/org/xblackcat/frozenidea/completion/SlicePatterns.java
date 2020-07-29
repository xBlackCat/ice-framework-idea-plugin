package org.xblackcat.frozenidea.completion;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import org.xblackcat.frozenidea.psi.*;

/**
 * 09.02.12 12:19
 *
 * @author xBlackCat
 */
public class SlicePatterns extends PlatformPatterns {
    public static ElementPattern<? extends PsiElement> moduleBody() {
        return psiElement().inside(psiFile(SliceFile.class)).and(not(psiElement().inside(psiElement(SliceDataTypeElement.class))));
    }

    public static ElementPattern<? extends PsiElement> metadataDirective() {
        return psiElement().inside(psiElement(SliceTypes.ICE_STRING_LITERAL).withParent(psiElement(SliceTypes.ICE_METADATA_ELEMENT)));
    }

    public static ElementPattern<? extends PsiElement> elementDef() {
        return psiElement()
                .inside(psiElement(SliceDataTypeElement.class))
                .afterLeaf(psiElement(SliceTypes.ICE_ID));
    }

    public static ElementPattern<? extends PsiElement> parameterList() {
        return or(
                psiElement().inside(SliceDataTypeElement.class),//.andNot(psiElement().inside(SliceFieldDef.class)),
                psiElement().inside(SliceParametersList.class).afterLeaf(","),
                psiElement().atStartOf(psiElement(SliceParametersList.class))
        );
    }

    public static ElementPattern<? extends PsiElement> classBody() {
        return or(
                psiElement().inside(SliceDataTypeElement.class)
        );
    }
}
