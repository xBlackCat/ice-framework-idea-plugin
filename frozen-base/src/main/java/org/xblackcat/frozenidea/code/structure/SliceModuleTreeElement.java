package org.xblackcat.frozenidea.code.structure;

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
public class SliceModuleTreeElement extends PsiTreeElementBase<SliceModule> {
    public SliceModuleTreeElement(SliceModule psiElement) {
        super(psiElement);
    }

    @Override
    public @NotNull Collection<StructureViewTreeElement> getChildrenBase() {
        List<StructureViewTreeElement> children = new ArrayList<>();

        final SliceModule element = getElement();
        if (element == null) {
            return Collections.emptyList();
        }

        final @Nullable SliceModuleBody body = element.getModuleBody();
        if (body == null) {
            return Collections.emptyList();
        }

        for (SliceModule module : body.getModuleList()) {
            children.add(new SliceModuleTreeElement(module));
        }

        for (SliceConstantDef constant : body.getConstantDefList()) {
            children.add(new SliceVariableTreeElement(constant));
        }

        for (SliceDataTypeElement dataType : body.getDataTypeElementList()) {
            children.add(new SliceDataTypeTreeElement(dataType));
        }

        return children;
    }

    @Override
    public @Nullable String getPresentableText() {
        @Nullable SliceNamedElement element = getElement();
        return element != null ? element.getName() : "";
    }
}
