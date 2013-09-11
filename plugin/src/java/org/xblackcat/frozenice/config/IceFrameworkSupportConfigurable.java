package org.xblackcat.frozenice.config;

import com.intellij.ide.util.frameworkSupport.FrameworkSupportConfigurable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.libraries.Library;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * 08.01.12 13:54
 *
 * @author xBlackCat
 */
public class IceFrameworkSupportConfigurable extends FrameworkSupportConfigurable {
    public IceFrameworkSupportConfigurable(Project project) {
    }

    @Override
    public JComponent getComponent() {
        return new JPanel();
    }

    @Override
    public void addSupport(@NotNull Module module, @NotNull ModifiableRootModel model, @Nullable Library library) {
    }
}
