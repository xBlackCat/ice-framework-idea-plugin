package org.xblackcat.frozenice.config;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsElementChildRole;
import org.jetbrains.jps.model.JpsProject;
import org.jetbrains.jps.model.ex.JpsElementBase;
import org.jetbrains.jps.model.ex.JpsElementChildRoleBase;

/**
 * 13.01.12 12:36
 *
 * @author xBlackCat
 */
public class IceConfig extends JpsElementBase<IceConfig> {
    public static final JpsElementChildRole<IceConfig> ROLE = JpsElementChildRoleBase.create(
            "Ice Framework Configuration"
    );

    private final String frameworkHomeUrl;

    public IceConfig(String url) {
        frameworkHomeUrl = url;
    }

    public VirtualFile getFrameworkHome() {
        return VirtualFileManager.getInstance().findFileByUrl(frameworkHomeUrl);
    }

    public String getFrameworkHomeUrl() {
        return frameworkHomeUrl;
    }

    @NotNull
    @Override
    public IceConfig createCopy() {
        return new IceConfig(frameworkHomeUrl);
    }

    @Override
    public void applyChanges(@NotNull IceConfig modified) {

    }

    public static IceConfig getSettings(JpsProject project) {
        final IceConfig config = project.getContainer().getChild(IceConfig.ROLE);
        return config == null ? new IceConfig("") : config;
    }
}
