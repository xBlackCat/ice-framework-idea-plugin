package org.xblackcat.frozenice.util;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.sanselan.util.IOUtils;
import org.xblackcat.frozenice.FrozenIdea;
import org.xblackcat.frozenice.config.IceConfig;

import java.io.InputStream;
import java.util.Collection;
import java.util.EnumSet;

/**
 * 29.01.12 13:31
 *
 * @author xBlackCat
 */
public class IceChecker {
    public static EnumSet<IceComponent> getInstalledComponents(VirtualFile home) {
        EnumSet<IceComponent> found = EnumSet.noneOf(IceComponent.class);

        if (home != null && home.isValid()) {
            for (IceComponent c : IceComponent.values()) {
                if (c.isInstalled(home)) {
                    found.add(c);
                }
            }
        }

        return found;
    }

    public static String readVersion(IceComponent component, VirtualFile home) {
        if (component == null || !component.isInstalled(home)) {
            return null;
        }

        // check file version
        try {
            final VirtualFile translatorPath = component.getTranslatorPath(home);
            if (translatorPath == null) {
                return null;
            }

            Process process = new ProcessBuilder(translatorPath.getPath(), "-v").start();
            process.waitFor();

            InputStream stdOutIS = process.getErrorStream();
            byte[] stdOut;
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
        }

        return null;
    }

    public static Collection<IceComponent> getAvailableTranslators(Module module) {
        FrozenIdea plugin = ServiceManager.getService(module.getProject(), FrozenIdea.class);
        IceConfig pluginConfig = plugin.getConfig();

        return getInstalledComponents(pluginConfig != null ? pluginConfig.getFrameworkHome() : null);
    }
}
