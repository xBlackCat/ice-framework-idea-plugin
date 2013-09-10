package org.xblackcat.frozenice.facet;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.UnknownModuleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenice.util.SliceIcons;

import javax.swing.*;

/**
 * 08.01.12 12:54
 *
 * @author xBlackCat
 */
public class IceFacetType extends FacetType<IceFacet, IceFacetConfiguration> {
    public IceFacetType() {
        super(IceFacet.ID, "ice", "ZeroC ICE");
    }

    @Override
    public IceFacetConfiguration createDefaultConfiguration() {
        return new IceFacetConfiguration();
    }

    @Override
    public IceFacet createFacet(
            @NotNull Module module,
            String name,
            @NotNull IceFacetConfiguration configuration,
            @Nullable Facet underlyingFacet
    ) {
        return new IceFacet(module, name, configuration, underlyingFacet);
    }

    @Override
    public boolean isSuitableModuleType(ModuleType moduleType) {
        return !(moduleType instanceof UnknownModuleType);
    }

    @Override
    public Icon getIcon() {
        return SliceIcons.FACET_ICON;
    }
}
