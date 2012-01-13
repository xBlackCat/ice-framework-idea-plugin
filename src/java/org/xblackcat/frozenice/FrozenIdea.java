package org.xblackcat.frozenice;

import com.intellij.openapi.components.*;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

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
    public FrozenIdea() {
    }

    public void initComponent() {
    }

    public void disposeComponent() {
        // TODO: insert component disposal logic here
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


        return root;
    }

    @Override
    public void loadState(Element state) {
    }
}
