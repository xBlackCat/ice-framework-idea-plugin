package org.xblackcat.frozenice.psi;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.xblackcat.frozenice.SliceLanguage;

/**
 * 04.01.12 12:23
 *
 * @author xBlackCat
 */
public abstract class SliceStubElementType extends IElementType {
    public SliceStubElementType(@org.jetbrains.annotations.NotNull @org.jetbrains.annotations.NonNls String debugName) {
        super(debugName, Language.findInstance(SliceLanguage.class));
    }

    public abstract PsiElement createPsi(ASTNode node);
}
