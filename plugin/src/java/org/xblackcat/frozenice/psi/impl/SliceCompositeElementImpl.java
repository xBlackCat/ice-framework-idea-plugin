package org.xblackcat.frozenice.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.xblackcat.frozenice.psi.SliceCompositeElement;

/**
 * 08.06.12 14:54
 *
 * @author xBlackCat
 */
public class SliceCompositeElementImpl extends ASTWrapperPsiElement implements SliceCompositeElement {
    public SliceCompositeElementImpl(@org.jetbrains.annotations.NotNull ASTNode node) {
        super(node);
    }
}
