package org.xblackcat.frozenice.util;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VfsUtil;
import org.apache.sanselan.util.IOUtils;
import org.xblackcat.frozenice.FrozenIdea;
import org.xblackcat.frozenice.config.IceComponent;
import org.xblackcat.frozenice.config.IceConfig;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
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
            try (InputStream stdOutIS = process.getErrorStream()) {
                stdOut = IOUtils.getInputStreamBytes(stdOutIS);
                if (stdOut == null || stdOut.length == 0) {
                    return null;
                }
            }

            return new String(stdOut);
        } catch (Exception e1) {
            // Ignore
        }

        return null;
    }

    public static Collection<IceComponent> getAvailableTranslators(Module module) {
        FrozenIdea plugin = ServiceManager.getService(module.getProject(), FrozenIdea.class);
        IceConfig pluginConfig = plugin.getConfig();

        return getInstalledComponents(
                pluginConfig != null ?
                        VfsUtil.virtualToIoFile(pluginConfig.getFrameworkHome()) :
                        null
        );
    }
}
