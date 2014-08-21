package org.xblackcat.frozenidea.jps;

import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsProject;
import org.jetbrains.jps.model.serialization.JpsProjectExtensionSerializer;
import org.xblackcat.frozenidea.config.IceConfig;

/**
* 11.07.2014 09:38
*
* @author xBlackCat
*/
public class IceProjectExtensionSerializer extends JpsProjectExtensionSerializer {
    public static final String NAME = "IceFramework";
    public static final String CONFIG_FILE_NAME = "projectIceFramework.xml";

    public IceProjectExtensionSerializer() {
        super(CONFIG_FILE_NAME, NAME);
    }

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
