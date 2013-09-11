package org.xblackcat.frozenice.jps;

import com.intellij.util.xmlb.XmlSerializer;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsElement;
import org.jetbrains.jps.model.JpsProject;
import org.jetbrains.jps.model.module.JpsModule;
import org.jetbrains.jps.model.serialization.JpsModelSerializerExtension;
import org.jetbrains.jps.model.serialization.JpsProjectExtensionSerializer;
import org.jetbrains.jps.model.serialization.facet.JpsFacetConfigurationSerializer;
import org.xblackcat.frozenice.config.IceConfig;
import org.xblackcat.frozenice.config.SliceCompilerSettings;

import java.util.Arrays;
import java.util.List;

/**
 * 11.09.13 12:02
 *
 * @author xBlackCat
 */
public class ModelSerializerExtension extends JpsModelSerializerExtension {
    @NotNull
    @Override
    public List<? extends JpsFacetConfigurationSerializer<?>> getFacetConfigurationSerializers() {
        return Arrays.asList(
                new JpsFacetConfigurationSerializer<SliceCompilerSettings>(
                        SliceCompilerSettings.ROLE,
                        "ice",
                        "ZeroC ICE"
                ) {
                    @Override
                    protected SliceCompilerSettings loadExtension(
                            @NotNull Element facetConfigurationElement,
                            String name,
                            JpsElement parent,
                            JpsModule module
                    ) {
                        SliceCompilerSettings configuration = XmlSerializer.deserialize(
                                facetConfigurationElement,
                                SliceCompilerSettings.class
                        );

                        if (configuration == null) {
                            configuration = new SliceCompilerSettings();
                        }

                        return configuration;
                    }

                    @Override
                    protected void saveExtension(
                            SliceCompilerSettings extension, Element facetConfigurationTag, JpsModule module
                    ) {

                    }
                }
        );
    }

    @NotNull
    @Override
    public List<? extends JpsProjectExtensionSerializer> getProjectExtensionSerializers() {
        return Arrays.asList(
                new JpsProjectExtensionSerializer("projectIceFramework.xml", "IceFramework") {
                    @Override
                    public void loadExtension(
                            @NotNull JpsProject jpsProject, @NotNull Element componentTag
                    ) {
                        Element fwList = componentTag.getChild("ice-frameworks");
                        IceConfig iceConfig = null;
                        if (fwList != null) {
                            Element fw = fwList.getChild("item");
                            if (fw != null) {
                                String url = fw.getAttributeValue("url");
                                if (url != null) {
                                    iceConfig = new IceConfig(url);
                                }
                            }
                        }
                        if (iceConfig == null) {
                            iceConfig = new IceConfig("");
                        }
                        jpsProject.getContainer().setChild(IceConfig.ROLE, iceConfig);

                    }

                    @Override
                    public void saveExtension(
                            @NotNull JpsProject jpsProject, @NotNull Element componentTag
                    ) {

                    }
                }
        );
    }
}
