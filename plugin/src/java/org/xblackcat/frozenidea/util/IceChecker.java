package org.xblackcat.frozenidea.util;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VfsUtil;
import org.apache.sanselan.util.IOUtils;
import org.xblackcat.frozenidea.FrozenIdea;
import org.xblackcat.frozenidea.config.IceComponent;
import org.xblackcat.frozenidea.config.IceConfig;

import java.io.File;
import java.io.InputStream;
import java.util.EnumSet;

/**
 * 29.01.12 13:31
 *
 * @author xBlackCat
 */
public class IceChecker {
    public static EnumSet<IceComponent> getInstalledComponents(File home) {
        EnumSet<IceComponent> found = EnumSet.noneOf(IceComponent.class);

        if (home != null && home.isDirectory()) {
            for (IceComponent c : IceComponent.values()) {
                if (c.isInstalled(home)) {
                    found.add(c);
                }
            }
        }

        return found;
    }

    public static String readVersion(IceComponent component, File home) {
        if (component == null || !component.isInstalled(home)) {
            return null;
        }

        // check file version
        try {
            final File translatorPath = component.getTranslatorPath(home);
            if (translatorPath == null) {
                return null;
            }

            Process process = new ProcessBuilder(translatorPath.getPath(), "-v").start();
            process.waitFor();

            byte[] stdOut;
            InputStream stdOutIS = process.getErrorStream();
            try {
                stdOut = IOUtils.getInputStreamBytes(stdOutIS);
                if (stdOut == null || stdOut.length == 0) {
                    return null;
                }
            } finally {
                stdOutIS.close();
            }

            return new String(stdOut);
        } catch (Exception e1) {
            // Ignore
        }

        return null;
    }

    public static EnumSet<IceComponent> getAvailableTranslators(Module module) {
        FrozenIdea plugin = module.getProject().getComponent(FrozenIdea.class);
        IceConfig pluginConfig = plugin.getConfig();

        return getInstalledComponents(
                pluginConfig != null ?
                        VfsUtil.virtualToIoFile(pluginConfig.getFrameworkHome()) :
                        null
        );
    }
}
