package org.xblackcat.frozenice.config;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetTypeId;
import com.intellij.openapi.module.Module;

/**
 * 08.01.12 13:28
 *
 * @author xBlackCat
 */
public class IceFacet extends Facet<IceFacetConfiguration> {
    public static final FacetTypeId<IceFacet> ID = new FacetTypeId<IceFacet>("Ice");
    public static final IceFacetType TYPE = new IceFacetType();

    public IceFacet(
            @org.jetbrains.annotations.NotNull Module module,
            @org.jetbrains.annotations.NotNull String name,
            @org.jetbrains.annotations.NotNull IceFacetConfiguration configuration,
            Facet underlyingFacet) {
        super(TYPE, module, name, configuration, underlyingFacet);
    }

}
