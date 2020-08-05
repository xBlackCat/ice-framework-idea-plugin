package org.xblackcat.frozenidea.code.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.psi.SliceFile;
import org.xblackcat.frozenidea.psi.SliceModule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class SliceFileTreeElement extends PsiTreeElementBase<PsiFile> {
    public SliceFileTreeElement(PsiFile psiElement) {
        super(psiElement);
    }

    @Override
    public @Nullable String getPresentableText() {
        @Nullable PsiFile element = getElement();
        return element != null ? element.getName() : "";
    }

    @Override
    public @NotNull Collection<StructureViewTreeElement> getChildrenBase() {
        if (!(getElement() instanceof SliceFile)) {
            return Collections.emptyList();
        }

        @NotNull List<SliceModule> modules = PsiTreeUtil.getChildrenOfTypeAsList(getElement(), SliceModule.class);
        List<StructureViewTreeElement> treeElements = new ArrayList<>(modules.size());
        for (SliceModule module : modules) {
            treeElements.add(new SliceModuleTreeElement(module));
        }
        return treeElements;
    }
}
