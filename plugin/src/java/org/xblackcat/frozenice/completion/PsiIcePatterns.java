package org.xblackcat.frozenice.completion;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.StandardPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.xblackcat.frozenice.psi.SliceTypes;

/**
 * 09.02.12 12:19
 *
 * @author xBlackCat
 */
public class PsiIcePatterns extends StandardPatterns {
    public static PsiIceElementPattern.Capture<PsiElement> psiElement() {
        return new PsiIceElementPattern.Capture<PsiElement>(PsiElement.class);
    }

    public static PsiIceElementPattern.Capture<PsiElement> psiElement(IElementType type) {
        return psiElement().withElementType(type);
    }

    public static ElementPattern<? extends PsiElement> metadataDirective() {
        return psiElement(SliceTypes.ICE_STRING_LITERAL).inside(psiElement(SliceTypes.ICE_METADATA_ELEMENT));
    }
}
