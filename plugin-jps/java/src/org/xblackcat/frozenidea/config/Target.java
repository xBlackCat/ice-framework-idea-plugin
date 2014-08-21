package org.xblackcat.frozenidea.config;

import org.xblackcat.frozenidea.util.Utils;

import java.io.File;

/**
 * 21.08.2014 11:26
 *
 * @author xBlackCat
 */
public class Target {
    private IceComponent component;
    private String outputDir;

    public Target(IceComponent component, String outputDir) {
        this.component = component;
        this.outputDir = outputDir;
    }

    public IceComponent getComponent() {
        return component;
    }

    public void setComponent(IceComponent component) {
        this.component = component;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public File getOutputFile() {
        return Utils.ideaUrlToFile(outputDir);
    }
}
