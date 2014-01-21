package org.xblackcat.frozenice.config;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsElementChildRole;
import org.jetbrains.jps.model.ex.JpsElementBase;
import org.jetbrains.jps.model.ex.JpsElementChildRoleBase;
import org.jetbrains.jps.model.module.JpsModule;
import org.xblackcat.frozenice.util.Utils;

import java.io.File;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * 30.01.12 9:22
 *
 * @author xBlackCat
 */
public class SliceCompilerSettings extends JpsElementBase<SliceCompilerSettings> {
    public static final JpsElementChildRole<SliceCompilerSettings> ROLE = JpsElementChildRoleBase.create(
            "Slice Compiler Configuration"
    );

    public boolean cleanOutput;
    public Map<IceComponent, String> outputDirPath = new EnumMap<>(IceComponent.class);

    public SliceCompilerSettings() {
    }

    public SliceCompilerSettings(SliceCompilerSettings settings) {
        cleanOutput = settings.cleanOutput;
        outputDirPath.putAll(settings.outputDirPath);
    }

    public VirtualFile getOutputDirFile(IceComponent c) {
        final String url = outputDirPath.get(c);
        if (url != null) {
            return VirtualFileManager.getInstance().findFileByUrl(url);
        }
        return null;
    }

    public File getOutputDir(IceComponent c) {
        return Utils.ideaUrlToFile(outputDirPath.get(c));
    }

    public void setOutputDir(IceComponent c, VirtualFile path) {
        if (path != null) {
            outputDirPath.put(c, path.getUrl());
        }
    }

    public void setCleanOutput(boolean cleanOutput) {
        this.cleanOutput = cleanOutput;
    }

    public boolean isCleanOutput() {
        return cleanOutput;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SliceCompilerSettings config = (SliceCompilerSettings) o;

        return cleanOutput == config.cleanOutput && outputDirPath.equals(config.outputDirPath);
    }

    @Override
    public int hashCode() {
        int result = outputDirPath != null ? outputDirPath.hashCode() : 0;
        result = 31 * result + (cleanOutput ? 1 : 0);
        return result;
    }

    public Set<IceComponent> getConfiguredComponents() {
        return outputDirPath.keySet();
    }

    public boolean isValid() {
        return !outputDirPath.isEmpty();
    }

    @NotNull
    @Override
    public SliceCompilerSettings createCopy() {
        return new SliceCompilerSettings(this);
    }

    @Override
    public void applyChanges(@NotNull SliceCompilerSettings modified) {
    }

    @NotNull
    public static SliceCompilerSettings getSettings(@NotNull JpsModule project) {
        SliceCompilerSettings settings = project.getContainer().getChild(ROLE);
        return settings == null ? new SliceCompilerSettings() : settings;
    }
}
