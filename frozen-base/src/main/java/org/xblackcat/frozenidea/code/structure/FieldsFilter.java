package org.xblackcat.frozenidea.code.structure;

import com.intellij.ide.util.treeView.smartTree.ActionPresentation;
import com.intellij.ide.util.treeView.smartTree.ActionPresentationData;
import com.intellij.ide.util.treeView.smartTree.Filter;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.util.PlatformIcons;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.util.IceMessages;

public class FieldsFilter implements Filter {
    @NonNls
    public static final String ID = "SHOW_FIELDS";

    @Override
    public boolean isVisible(TreeElement treeNode) {
        return !(treeNode instanceof SliceVariableTreeElement);
    }

    @Override
    @NotNull
    public ActionPresentation getPresentation() {
        return new ActionPresentationData(IceMessages.message("action.structureview.show.fields"), null, PlatformIcons.FIELD_ICON);
    }

    @Override
    @NotNull
    public String getName() {
        return ID;
    }

    @Override
    public boolean isReverted() {
        return true;
    }
}
