package org.xblackcat.frozenice.util;

import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * 29.01.12 13:31
 *
 * @author xBlackCat
 */
public enum IceComponent {
    Java("slice2java") {
        @Override
        public boolean isInstalled(VirtualFile home) {
            if (!super.isInstalled(home)) {
                return false;
            }

            // Check for library
            VirtualFile lib = home.findChild("lib");
            if (lib == null || !lib.isDirectory()) {
                return false;
            }

            VirtualFile iceLib = lib.findChild("Ice.jar");

            return iceLib != null && iceLib.isValid() && !iceLib.isDirectory();
        }
    },
    Ruby("slice2rb"),
    PHP("slice2php"),
    CPP("slice2cpp"),
    CS("slice2cs"),
    Python("slice2py"),
    HTML("slice2html"),
    Freeze("slice2freeze"),
    FreezeJ("slice2freezej");

    private final String translatorName;

    private IceComponent(String translatorName) {
        this.translatorName = translatorName;
    }

    public String getTranslatorName() {
        return SystemInfo.isWindows ? translatorName + ".exe" : translatorName;
    }

    public boolean isInstalled(VirtualFile home) {
        VirtualFile bin = home.findChild("bin");
        if (bin == null || !bin.isDirectory()) {
            return false;
        }

        VirtualFile translator = bin.findChild(getTranslatorName());

        return translator != null && translator.isValid() && !translator.isDirectory();
    }

    public VirtualFile getTranslatorPath(VirtualFile home) {
        return home.findFileByRelativePath("./bin/" + getTranslatorName());
    }
}
