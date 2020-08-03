package org.xblackcat.frozenidea.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.psi.SliceDataTypeElement;
import org.xblackcat.frozenidea.psi.SliceModule;
import org.xblackcat.frozenidea.psi.SliceNamedElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class SliceDataTypeTreeElement extends PsiTreeElementBase<SliceDataTypeElement> {
    public SliceDataTypeTreeElement(SliceDataTypeElement psiElement) {
        super(psiElement);
    }

    @Override
    public @NotNull Collection<StructureViewTreeElement> getChildrenBase() {
        // TODO: implement show members (fields, methods, etc.)

        return Collections.emptyList();
    }

    @Override
    public @Nullable String getPresentableText() {
        @Nullable SliceNamedElement element = getElement();
        return element != null ? element.getName() : "";
    }
}
