package org.xblackcat.frozenice.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.ILightStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.tree.ICompositeElementType;
import org.xblackcat.frozenice.SliceLanguage;

/**
 * 05.01.12 16:54
 *
 * @author xBlackCat
 */
public abstract class IceStubElementType<StubT extends StubElement, PsiT extends PsiElement>
        extends ILightStubElementType<StubT, PsiT> implements ICompositeElementType {
    public IceStubElementType(@org.jetbrains.annotations.NotNull @org.jetbrains.annotations.NonNls String debugName) {
        super(debugName, SliceLanguage.INSTANCE);
    }

    public abstract PsiT createPsi(ASTNode node);
}
