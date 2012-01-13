package org.xblackcat.frozenice.facet;

import com.intellij.ide.util.frameworkSupport.FrameworkSupportConfigurable;
import com.intellij.ide.util.frameworkSupport.FrameworkSupportModel;
import com.intellij.ide.util.frameworkSupport.FrameworkSupportProvider;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.UnknownModuleType;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.config.IceFrameworkSupportConfigurable;
import org.xblackcat.frozenice.util.Icons;

import javax.swing.*;

/**
 * 08.01.12 12:58
 *
 * @author xBlackCat
 */
public class IceFacetSupportProvider extends FrameworkSupportProvider {
    protected IceFacetSupportProvider() {
        super("Slice", "ICE");
    }

    @NotNull
    @Override
    public FrameworkSupportConfigurable createConfigurable(@NotNull FrameworkSupportModel model) {
        return new IceFrameworkSupportConfigurable(model.getProject());
    }

    @Override
    public boolean isEnabledForModuleType(@NotNull ModuleType moduleType) {
        return !(moduleType instanceof UnknownModuleType);
    }

    @Override
    public Icon getIcon() {
        return Icons.FACET_ICON;
    }
}
