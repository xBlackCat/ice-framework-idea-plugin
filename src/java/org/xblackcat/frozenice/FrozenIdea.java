package org.xblackcat.frozenice;

import com.intellij.openapi.components.*;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.config.IceConfig;

/**
 * @author xBlackCat
 */
@State(
        name = "IceFramework",
        storages = {
                @Storage(
                        id = "default",
                        file = "$PROJECT_FILE$"
                ),
                @Storage(
                        id = "dir",
                        file = "$PROJECT_CONFIG_DIR$/projectIceFramework.xml",
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

            frameWorksList.addContent(fw);
        }

        root.addContent(frameWorksList);

        return root;
    }

    @Override
    public void loadState(Element state) {
        Element fwList = state.getChild("ice-frameworks");
        if (fwList != null) {
            Element fw = fwList.getChild("item");
            if (fw != null) {
                String url = fw.getAttributeValue("url");
                if (url != null) {
                    iceConfig = new IceConfig(url);
                }
            }
        }
    }

    public void setConfig(IceConfig iceConfig) {
        this.iceConfig = iceConfig;
    }

    public IceConfig getConfig() {
        return iceConfig;
    }
}
