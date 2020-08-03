package org.xblackcat.frozenidea.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.psi.SliceDataTypeElement;
import org.xblackcat.frozenidea.psi.SliceFile;
import org.xblackcat.frozenidea.psi.SliceModule;
import org.xblackcat.frozenidea.psi.SliceNamedElement;

import java.util.ArrayList;
import java.util.Collection;
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

        for (SliceModule module : getElement().getModuleList()) {
            children.add(new SliceModuleTreeElement(module));
        }

        for (SliceDataTypeElement dataType : getElement().getDataTypeElementList()) {
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
