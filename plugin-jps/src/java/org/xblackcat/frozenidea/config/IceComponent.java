package org.xblackcat.frozenidea.config;

import com.intellij.openapi.util.SystemInfo;

import java.io.File;

/**
 * 29.01.12 13:31
 *
 * @author xBlackCat
 */
public enum IceComponent {
    Java("slice2java", "/fileTypes/java.png") {
        @Override
        public String extractPackageName(String text) {
            if (text != null && text.startsWith("java:package:")) {
                return text.substring(13);
            } else {
                return null;
            }
        }
    },
    Ruby("slice2rb", "/icons/types/ruby_file.png"),
    PHP("slice2php", "/icons/types/php-icon.png"),
    CPP("slice2cpp", "/icons/types/c_file.gif"),
    CS("slice2cs", "/icons/types/dotnet.png"),
    Python("slice2py", "/icons/types/pythonFile.png"),
    HTML("slice2html", "/fileTypes/html.png"),
    Freeze("slice2freeze", "/icons/types/slice.png"),
    FreezeJ("slice2freezej", "/icons/types/slice.png");

    private final String translatorName;
    private final String iconName;

    IceComponent(String translatorName, final String iconName) {
        this.translatorName = translatorName;
        this.iconName = iconName;
    }

    public String getTranslatorName() {
        return SystemInfo.isWindows ? translatorName + ".exe" : translatorName;
    }

    @SuppressWarnings("RedundantIfStatement")
    public boolean isInstalled(File home) {
        File translator = getTranslatorPath(home);

        if (!translator.canExecute()) {
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

    public String getFileExtension() {
        return translatorName.substring(6);
    }

    public String extractPackageName(String text) {
        return null;
    }

    public String getIconName() {
        return iconName;
    }
}
