package org.xblackcat.frozenidea.jps;

import com.intellij.util.xmlb.XmlSerializer;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsElement;
import org.jetbrains.jps.model.module.JpsModule;
import org.jetbrains.jps.model.serialization.facet.JpsFacetConfigurationSerializer;
import org.xblackcat.frozenidea.config.SliceCompilerSettings;

/**
 * 11.07.2014 09:38
 *
 * @author xBlackCat
 */
public class IceFacetConfigurationSerializer extends JpsFacetConfigurationSerializer<SliceCompilerSettings> {
    public IceFacetConfigurationSerializer() {
        super(SliceCompilerSettings.ROLE, "ice", "ZeroC ICE");
    }

    @Override
    protected SliceCompilerSettings loadExtension(
            @NotNull Element facetConfigurationElement,
            String name,
            JpsElement parent,
            JpsModule module
    ) {
        return XmlSerializer.deserialize(facetConfigurationElement, SliceCompilerSettings.class);
    }
}
