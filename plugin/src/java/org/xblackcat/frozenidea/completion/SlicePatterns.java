package org.xblackcat.frozenidea.completion;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import org.xblackcat.frozenidea.psi.SliceTypes;

/**
 * 09.02.12 12:19
 *
 * @author xBlackCat
 */
public class SlicePatterns extends PlatformPatterns {
    public static ElementPattern<? extends PsiElement> moduleBody() {
        return psiElement();
    }

    public static ElementPattern<? extends PsiElement> metadataDirective() {
        return psiElement().inside(psiElement(SliceTypes.ICE_STRING_LITERAL).withParent(psiElement(SliceTypes.ICE_METADATA_ELEMENT)));
    }
}
