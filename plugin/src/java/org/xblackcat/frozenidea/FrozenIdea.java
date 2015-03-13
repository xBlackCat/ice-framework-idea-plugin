package org.xblackcat.frozenidea;

import com.intellij.openapi.components.*;
import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.config.IceConfig;
import org.xblackcat.frozenidea.jps.IceProjectExtensionSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xBlackCat
 */
@State(
        name = IceProjectExtensionSerializer.NAME,
        storages = {
                @Storage(
                        id = "default",
                        file = StoragePathMacros.PROJECT_FILE
                ),
                @Storage(
                        id = "dir",
                        file = StoragePathMacros.PROJECT_CONFIG_DIR + "/" + IceProjectExtensionSerializer.CONFIG_FILE_NAME,
                        scheme = StorageScheme.DIRECTORY_BASED
                )
        }
)
public class FrozenIdea implements BaseComponent, PersistentStateComponent<Element>, ProjectComponent {
    private IceConfig iceConfig;

    public FrozenIdea() {
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return "FrozenIdea";
    }

    @Override
    public void projectOpened() {
    }

    @Override
    public void projectClosed() {
    }

    @Override
    public Element getState() {
        final Element root = new Element("frozen-idea");

        Element frameWorksList = new Element("ice-frameworks");
        if (iceConfig != null && iceConfig.getFrameworkHomeUrl() != null) {
            Element fw = new Element("item");

            fw.setAttribute("url", iceConfig.getFrameworkHomeUrl());

            final Element include = new Element("include");
            for (String url : iceConfig.getIncludeUrls()) {
                final Element item = new Element("item");
                item.setAttribute("url", url);
                include.addContent(item);
            }
            fw.addContent(include);

            frameWorksList.addContent(fw);
        }

        root.addContent(frameWorksList);

        return root;
    }

    @Override
    public void loadState(Element state) {
        String homeUrl = null;
        List<String> includes = new ArrayList<String>();
        Element fwList = state.getChild("ice-frameworks");
        if (fwList != null) {
            Element fw = fwList.getChild("item");
            if (fw != null) {
                homeUrl = fw.getAttributeValue("url");

                final Element includeList = fw.getChild("include");
                if (includeList != null) {
                    for (Element incl : includeList.getChildren("item")) {
                        String includeUrl = incl.getAttributeValue("url");
                        if (StringUtils.isNotBlank(includeUrl)) {
                            includes.add(includeUrl);
                        }
                    }
                }
            }
        }
        if (homeUrl != null) {
            iceConfig = new IceConfig(homeUrl, includes.toArray(new String[includes.size()]));
        }
    }

    public void setConfig(IceConfig iceConfig) {
        this.iceConfig = iceConfig;
    }

    public IceConfig getConfig() {
        return iceConfig;
    }
}
