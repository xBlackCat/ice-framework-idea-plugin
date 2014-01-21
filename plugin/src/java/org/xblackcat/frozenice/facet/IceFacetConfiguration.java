package org.xblackcat.frozenice.facet;

import com.intellij.facet.FacetConfiguration;
import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.facet.ui.FacetValidatorsManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.IdeBorderFactory;
import org.jdom.Element;
import org.jetbrains.annotations.Nls;
import org.xblackcat.frozenice.config.IceComponent;
import org.xblackcat.frozenice.config.SliceCompilerSettings;
import org.xblackcat.frozenice.util.IceChecker;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 08.01.12 13:35
 *
 * @author xBlackCat
 */
@State(
        name = "IceFacetConfiguration",
        storages = {
                @Storage(
                        id = "default",
                        file = "$MODULE_FILE$"
                )
        }
)
public class IceFacetConfiguration implements FacetConfiguration, PersistentStateComponent<SliceCompilerSettings> {
    private SliceCompilerSettings config = new SliceCompilerSettings();

    @Override
    public FacetEditorTab[] createEditorTabs(
            FacetEditorContext editorContext,
            FacetValidatorsManager validatorsManager
    ) {
        return new FacetEditorTab[]{new IceFacetEditor(editorContext, validatorsManager)};
    }

    @Override
    @Deprecated
    public void readExternal(Element element) throws InvalidDataException {
    }

    @Override
    @Deprecated
    public void writeExternal(Element element) throws WriteExternalException {
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

    public SliceCompilerSettings getConfig() {
        return config;
    }

    private class IceFacetEditor extends FacetEditorTab {
        private final FacetEditorContext editorContext;
        private final FacetValidatorsManager validatorsManager;

        private final JComponent pane;

        private final JCheckBox cleanOnBuildCheckBox = new JCheckBox("Clean output folder before build.");
        private final Map<IceComponent, TranslatorItemConfig> outputFolders = new HashMap<IceComponent, TranslatorItemConfig>();

        public IceFacetEditor(FacetEditorContext editorContext, FacetValidatorsManager validatorsManager) {
            this.editorContext = editorContext;
            this.validatorsManager = validatorsManager;


            pane = new JPanel(new BorderLayout());
            final JPanel settingsPane = new JPanel(new GridLayout(0, 1, 5, 5));

            pane.add(settingsPane, BorderLayout.NORTH);

            settingsPane.setBorder(IdeBorderFactory.createTitledBorder("Generated files result folder.", false));

            for (IceComponent c : IceChecker.getAvailableTranslators(editorContext.getModule())) {
                VirtualFile outputDir = config.getOutputDirFile(c);

                TranslatorItemConfig itemConfig = new TranslatorItemConfig(c.name());

                outputFolders.put(c, itemConfig);

                itemConfig.setSelectedFolder(outputDir);
                itemConfig.setCompilationEnabled(outputDir != null);

                settingsPane.add(itemConfig);
            }

//            settingsPane.add(cleanOnBuildCheckBox, BorderLayout.SOUTH);
//
//            // TODO: make visible after implementing
//            cleanOnBuildCheckBox.setVisible(false);

        }

        @Nls
        @Override
        public String getDisplayName() {
            return "Ice";
        }

        @Override
        public JComponent createComponent() {
            return pane;
        }

        @Override
        public boolean isModified() {
            SliceCompilerSettings newConfig = getConfig();
            return !config.equals(newConfig);
        }

        private SliceCompilerSettings getConfig() {
            SliceCompilerSettings cfg = new SliceCompilerSettings();

            cfg.setCleanOutput(cleanOnBuildCheckBox.isSelected());

            for (Map.Entry<IceComponent, TranslatorItemConfig> item : outputFolders.entrySet()) {
                TranslatorItemConfig itemConfig = item.getValue();
                if (itemConfig.getCompilationEnabled()) {
                    cfg.setOutputDir(item.getKey(), itemConfig.getSelectedFolder());
                }
            }

            return cfg;
        }

        @Override
        public void reset() {
            cleanOnBuildCheckBox.setSelected(config.isCleanOutput());

            for (IceComponent item : config.getConfiguredComponents()) {
                TranslatorItemConfig itemConfig = outputFolders.get(item);

                itemConfig.setCompilationEnabled(true);
                itemConfig.setSelectedFolder(config.getOutputDirFile(item));
            }
        }

        @Override
        public void apply() throws ConfigurationException {
            config = getConfig();
        }

        @Override
        public void disposeUIResources() {
        }
    }
}
