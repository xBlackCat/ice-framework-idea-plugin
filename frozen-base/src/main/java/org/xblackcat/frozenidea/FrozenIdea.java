package org.xblackcat.frozenidea;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.xblackcat.frozenidea.config.IceConfig;
import org.xblackcat.frozenidea.jps.IceProjectExtensionSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xBlackCat
 */
@State(
        name = IceProjectExtensionSerializer.NAME,
        storages = @Storage(IceProjectExtensionSerializer.CONFIG_FILE_NAME)

)
public class FrozenIdea implements PersistentStateComponent<Element> {
    private IceConfig iceConfig;

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
        List<String> includes = new ArrayList<>();
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
            iceConfig = new IceConfig(homeUrl, includes.toArray(new String[0]));
        }
    }

    public void setConfig(IceConfig iceConfig) {
        this.iceConfig = iceConfig;
    }

    public IceConfig getConfig() {
        return iceConfig;
    }
}
