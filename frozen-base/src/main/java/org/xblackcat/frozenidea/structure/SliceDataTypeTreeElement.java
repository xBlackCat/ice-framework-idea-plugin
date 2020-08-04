package org.xblackcat.frozenidea.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.psi.*;

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
        final SliceDataTypeElement element = getElement();
        if (element == null) {
            return Collections.emptyList();
        }
        final SliceBodyBlock block = element.getBodyBlock();
        if (block == null) {
            return Collections.emptyList();
        }

        final List<StructureViewTreeElement> elements = new ArrayList<>();
        for (SliceMethodDef method : block.getMethodDefList()) {
            elements.add(new SliceMethodTreeElement(method));
        }

        return elements;
    }

    @Override
    public @Nullable String getPresentableText() {
        @Nullable SliceNamedElement element = getElement();
        return element != null ? element.getName() : "";
    }
}
