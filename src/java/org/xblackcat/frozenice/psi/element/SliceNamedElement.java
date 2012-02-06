package org.xblackcat.frozenice.psi.element;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collections;
import java.util.Set;

/**
 * 06.02.12 11:57
 *
 * @author xBlackCat
 */
public interface SliceNamedElement extends PsiNamedElement {
    Set<? extends SliceNamedElement> EMPTY_SET = Collections.emptySet();

    @Nullable
    ASTNode getNameNode();

    Icon getIcon();

    @Nullable
    <ParentType extends SlicePsiElement> ParentType getParentOfType(Class<ParentType> parentTypeClass);

    @Nullable
    String getFQN();

    @NotNull
    String getNamespaceName();

    boolean isDeprecated();

}
