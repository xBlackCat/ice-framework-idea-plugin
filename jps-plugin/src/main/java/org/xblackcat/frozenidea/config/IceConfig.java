package org.xblackcat.frozenidea.config;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsElementChildRole;
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
    private final String[] includeUrls;

    public IceConfig(String url, String... includeUrls) {
        frameworkHomeUrl = url;
        this.includeUrls = includeUrls;
    }

    public VirtualFile getFrameworkHome() {
        return VirtualFileManager.getInstance().findFileByUrl(frameworkHomeUrl);
    }

    public String getFrameworkHomeUrl() {
        return frameworkHomeUrl;
    }

    public String[] getIncludeUrls() {
        return includeUrls.clone();
    }

    public VirtualFile[] getIncludePathFiles() {
        VirtualFile[] files = new VirtualFile[includeUrls.length];
        final VirtualFileManager fileManager = VirtualFileManager.getInstance();
        int i = 0;
        while (i < files.length) {
            files[i] = fileManager.findFileByUrl(includeUrls[i]);
        }

        return files;
    }

    @NotNull
    @Override
    public IceConfig createCopy() {
        return new IceConfig(frameworkHomeUrl, includeUrls);
    }

    @Override
    public void applyChanges(@NotNull IceConfig modified) {

    }

}
