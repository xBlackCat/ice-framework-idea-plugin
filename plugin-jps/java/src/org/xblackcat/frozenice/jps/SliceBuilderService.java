package org.xblackcat.frozenice.jps;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.incremental.BuilderService;
import org.jetbrains.jps.incremental.ModuleLevelBuilder;

import java.util.Collections;
import java.util.List;

/**
 * 10.09.13 16:55
 *
 * @author xBlackCat
 */
public class SliceBuilderService extends BuilderService {
    @NotNull
    @Override
    public List<? extends ModuleLevelBuilder> createModuleLevelBuilders() {
        return Collections.singletonList(new SliceBuilder());
    }
}
