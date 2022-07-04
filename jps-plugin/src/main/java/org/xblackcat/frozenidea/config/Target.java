package org.xblackcat.frozenidea.config;

import org.jetbrains.jps.util.JpsPathUtil;

import java.io.File;
import java.io.Serializable;

/**
 * 21.08.2014 11:26
 *
 * @author xBlackCat
 */
public class Target implements Serializable, Cloneable {
    private IceComponent component;
    private String outputDir;

    public Target() {
    }

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
        return JpsPathUtil.urlToFile(outputDir);
    }

    @Override
    public Target clone() {
        try {
            return (Target) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
