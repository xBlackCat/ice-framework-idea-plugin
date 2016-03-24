package org.xblackcat.frozenidea.jps;

import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsProject;
import org.jetbrains.jps.model.serialization.JpsProjectExtensionSerializer;
import org.xblackcat.frozenidea.config.IceConfig;

import java.util.ArrayList;
import java.util.List;

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
        String homeUrl = null;
        List<String> includes = new ArrayList<>();
        Element fwList = componentTag.getChild("ice-frameworks");
        IceConfig iceConfig = new IceConfig("");
        if (fwList != null) {
            Element fw = fwList.getChild("item");
            if (fw != null) {
                homeUrl = fw.getAttributeValue("url");

                final Element includeList = fw.getChild("include");
                if (includeList != null) {
                    for (Element incl : includeList.getChildren("item")) {
                        String includeUrl = incl.getAttributeValue("url");
                        if (includeUrl != null && includeUrl.trim().length() != 0) {
                            includes.add(includeUrl);
                        }
                    }
                }
            }
        }
        if (homeUrl != null) {
            iceConfig = new IceConfig(homeUrl, includes.toArray(new String[includes.size()]));
        }

        jpsProject.getContainer().setChild(IceConfig.ROLE, iceConfig);
    }

    @Override
    public void saveExtension(
            @NotNull JpsProject jpsProject, @NotNull Element componentTag
    ) {

    }
}
