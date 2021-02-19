package org.xblackcat.frozenidea.facet;

import com.intellij.facet.FacetConfiguration;
import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.facet.ui.FacetValidatorsManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.xblackcat.frozenidea.config.Accessor;
import org.xblackcat.frozenidea.config.SliceCompilerSettings;

/**
 * 08.01.12 13:35
 *
 * @author xBlackCat
 */
@State(
        name = "IceFacetConfiguration",
        storages = {
                @Storage("$MODULE_FILE$")
        }
)
public class IceFacetConfiguration implements FacetConfiguration, PersistentStateComponent<SliceCompilerSettings> {
    private SliceCompilerSettings config = new SliceCompilerSettings();


    @Override
    public FacetEditorTab[] createEditorTabs(
            FacetEditorContext editorContext,
            FacetValidatorsManager validatorsManager
    ) {
        return new FacetEditorTab[]{new IceFacetConfigurationEditor(new Accessor<>() {
            @Override
            public void accept(SliceCompilerSettings sliceCompilerSettings) {
                config = sliceCompilerSettings;
            }

            @Override
            public SliceCompilerSettings get() {
                return config;
            }
        }, editorContext.getProject(), editorContext.getModule())};
    }

    @Override
    public SliceCompilerSettings getState() {
        return config;
    }

    @Override
    public void loadState(SliceCompilerSettings state) {
        if (state != null) {
            config = state;
        }
    }

}
