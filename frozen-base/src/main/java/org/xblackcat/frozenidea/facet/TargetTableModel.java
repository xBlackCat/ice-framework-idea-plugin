package org.xblackcat.frozenidea.facet;

import com.intellij.ui.AddEditRemovePanel;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.config.Target;

/**
 * 21.08.2014 11:25
 *
 * @author xBlackCat
 */
public class TargetTableModel extends AddEditRemovePanel.TableModel<Target> {
    @Override
    public int getColumnCount() {
        return 2;
    }

    @Nullable
    @Override
    public String getColumnName(int columnIndex) {
        return columnIndex == 0 ? "Target" : "Output path";
    }

    @Override
    public Object getField(Target o, int i) {
        return i == 0 ? o.getComponent() : o.getOutputDir();
    }
}
