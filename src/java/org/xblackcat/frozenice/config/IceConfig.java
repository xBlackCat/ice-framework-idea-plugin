package org.xblackcat.frozenice.config;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;

/**
 * 13.01.12 12:36
 *
 * @author xBlackCat
 */
public class IceConfig {
    private final VirtualFile frameworkHome;
    private final String frameworkHomeUrl;

    public IceConfig(VirtualFile frameworkHome) {
        this.frameworkHome = frameworkHome;
        frameworkHomeUrl = frameworkHome != null ? frameworkHome.getUrl() : null;
    }

    public IceConfig(String url) {
        frameworkHomeUrl = url;
        frameworkHome = VirtualFileManager.getInstance().findFileByUrl(url);
    }

    public VirtualFile getFrameworkHome() {
        return frameworkHome;
    }

    public String getFrameworkHomeUrl() {
        return frameworkHomeUrl;
    }
}
