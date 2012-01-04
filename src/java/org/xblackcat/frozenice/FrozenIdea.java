package org.xblackcat.frozenice;

import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

/**
 * @author xBlackCat
 */
public class FrozenIdea implements ApplicationComponent {
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
}
