package org.xblackcat.frozenidea.structure;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Filter;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.psi.SliceDataTypeElement;
import org.xblackcat.frozenidea.psi.SliceFile;
import org.xblackcat.frozenidea.psi.SliceModule;
import org.xblackcat.frozenidea.psi.SliceVariableElement;

/**
 *
 */
public class SliceStructureViewModel extends StructureViewModelBase implements StructureViewModel.ElementInfoProvider {
    public SliceStructureViewModel(@NotNull PsiFile psiFile) {
        super(psiFile, new SliceFileTreeElement(psiFile));
        withSorters(Sorter.ALPHA_SORTER);
        withSuitableClasses(SliceVariableElement.class, SliceModule.class, SliceFile.class, SliceDataTypeElement.class);
    }

    @Override
    public @NotNull Filter[] getFilters() {
        return new Filter[]{new FieldsFilter()};
    }

    @Override
    public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
        return false;
    }

    @Override
    public boolean isAlwaysLeaf(StructureViewTreeElement element) {
        return element instanceof SliceFile;
    }
}
