package org.xblackcat.frozenice.config;

import a.j.re;
import com.intellij.openapi.util.SystemInfo;

import java.io.File;

/**
 * 29.01.12 13:31
 *
 * @author xBlackCat
 */
public enum IceComponent {
    Java("slice2java", true) {
        @Override
        public File getLibraryPath(File home) {
            File lib = new File(home, "lib");
            if (!lib.isDirectory()) {
                return null;
            }

            File iceLib = new File(lib, "Ice.jar");
            if (!iceLib.isFile()) {
                return null;
            }

            return iceLib;
        }

        @Override
        public String extractPackageName(String text) {
            if (text != null && text.startsWith("java:package:")) {
                return text.substring(12);
            } else {
                return null;
            }
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
    private final boolean hasLibrary;

    private IceComponent(String translatorName) {
        this(translatorName, false);
    }

    private IceComponent(String translatorName, boolean hasLibrary) {
        this.translatorName = translatorName;
        this.hasLibrary = hasLibrary;
    }

    public String getTranslatorName() {
        return SystemInfo.isWindows ? translatorName + ".exe" : translatorName;
    }

    public boolean isInstalled(File home) {
        File translator = getTranslatorPath(home);

        if (!translator.canExecute()) {
            return false;
        }

        if (hasLibrary && getLibraryPath(home) == null) {
            return false;
        }

        return true;
    }

    public File getTranslatorPath(File home) {
        return new File(new File(home, "bin"), getTranslatorName());
    }

    public File getLibraryPath(File home) {
        return null;
    }

    public String extractPackageName(String text) {
        return null;
    }
}
