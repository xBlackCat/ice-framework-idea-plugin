package org.xblackcat.frozenice.psi.source;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.SliceLanguage;

/**
 * 06.01.12 17:26
 *
 * @author xBlackCat
 */
public class SliceStubPsiElement<T extends StubElement> extends StubBasedPsiElementBase<T> {
    public SliceStubPsiElement(@NotNull T stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public SliceStubPsiElement(@NotNull ASTNode node) {
        super(node);
    }

    @NotNull
    @Override
    public Language getLanguage() {
        return SliceLanguage.INSTANCE;
    }

    @Override
    public PsiElement getParent() {
        return getParentByStub();
    }

    @Override
    public int getTextOffset() {
        return calcTreeElement().getTextOffset();
    }

    protected CompositeElement calcTreeElement() {
        return (CompositeElement) getNode();
    }

}
