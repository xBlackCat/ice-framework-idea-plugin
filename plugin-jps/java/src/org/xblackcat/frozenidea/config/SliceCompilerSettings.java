package org.xblackcat.frozenidea.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsElementChildRole;
import org.jetbrains.jps.model.ex.JpsElementBase;
import org.jetbrains.jps.model.ex.JpsElementChildRoleBase;
import org.jetbrains.jps.model.module.JpsModule;

import java.util.ArrayList;
import java.util.List;

/**
 * 30.01.12 9:22
 *
 * @author xBlackCat
 */
public class SliceCompilerSettings extends JpsElementBase<SliceCompilerSettings> {
    public static final JpsElementChildRole<SliceCompilerSettings> ROLE = JpsElementChildRoleBase.create(
            "Slice Compiler Configuration"
    );

    private boolean cleanOutput;
    private List<Target> components = new ArrayList<Target>();

    public SliceCompilerSettings() {
    }

    public SliceCompilerSettings(SliceCompilerSettings settings) {
        cleanOutput = settings.cleanOutput;
        components.addAll(settings.components);
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

        return cleanOutput == config.cleanOutput && components.equals(config.components);
    }

    @Override
    public int hashCode() {
        int result = components != null ? components.hashCode() : 0;
        result = 31 * result + (cleanOutput ? 1 : 0);
        return result;
    }

    public List<Target> getConfiguredComponents() {
        return components;
    }

    public void setComponents(List<Target> components) {
        this.components = components;
    }

    public boolean isValid() {
        return !components.isEmpty();
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
    public static SliceCompilerSettings getSettings(@NotNull JpsModule module) {
        SliceCompilerSettings settings = module.getContainer().getChild(ROLE);
        return settings == null ? new SliceCompilerSettings() : settings;
    }
}
