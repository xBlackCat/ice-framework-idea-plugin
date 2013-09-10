package org.xblackcat.frozenice.facet;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.xblackcat.frozenice.util.IceComponent;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 30.01.12 9:22
 *
 * @author xBlackCat
 */
public class Config {
    public boolean cleanOutput;
    public Map<IceComponent, String> outputDirPath = new HashMap<IceComponent, String>();

    transient private Map<IceComponent, VirtualFile> outputDir = new EnumMap<IceComponent, VirtualFile>(IceComponent.class);

    public VirtualFile getOutputDir(IceComponent c) {
        VirtualFile file = outputDir.get(c);
        if (outputDirPath.get(c) != null && file == null) {
            file = VirtualFileManager.getInstance().findFileByUrl(outputDirPath.get(c));
            outputDir.put(c, file);
        }
        return file;
    }

    public void setOutputDir(IceComponent c, VirtualFile path) {
        if (path != null) {
            outputDir.put(c, path);
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

        Config config = (Config) o;

        return cleanOutput == config.cleanOutput && outputDir.equals(config.outputDir);
    }

    @Override
    public int hashCode() {
        int result = outputDir != null ? outputDir.hashCode() : 0;
        result = 31 * result + (cleanOutput ? 1 : 0);
        return result;
    }

    public Set<IceComponent> getConfiguredComponents() {
        return outputDirPath.keySet();
    }

    public boolean isValid() {
        return !outputDirPath.isEmpty();
    }
}
