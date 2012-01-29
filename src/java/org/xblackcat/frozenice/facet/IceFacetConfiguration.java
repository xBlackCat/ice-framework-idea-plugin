package org.xblackcat.frozenice.facet;

import com.intellij.facet.FacetConfiguration;
import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.facet.ui.FacetValidatorsManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.FixedSizeButton;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.ui.IdeBorderFactory;
import org.jdom.Element;
import org.jetbrains.annotations.Nls;
import org.xblackcat.frozenice.util.IceComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
public class IceFacetConfiguration implements FacetConfiguration, PersistentStateComponent<IceFacetConfiguration.Config> {
    private Config config = new Config();

    @Override
    public FacetEditorTab[] createEditorTabs(FacetEditorContext editorContext, FacetValidatorsManager validatorsManager) {
        return new FacetEditorTab[]{new IceFacetEditor(editorContext, validatorsManager)};
    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {
    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
    }

    @Override
    public IceFacetConfiguration.Config getState() {
        return config;
    }

    @Override
    public void loadState(IceFacetConfiguration.Config state) {
        if (state != null) {
            config = state;
        }
    }

    public Config getConfig() {
        return config;
    }

    public static class Config {
        public boolean cleanOutput;
        public Map<IceComponent, String> outputDirPath = new HashMap<IceComponent, String>();

        transient private Map<IceComponent, VirtualFile> outputDir = new HashMap<IceComponent, VirtualFile>();

        public VirtualFile getOutputDir(IceComponent c) {
            VirtualFile file = outputDir.get(c);
            if (outputDirPath.get(c) != null && file == null) {
                file = VirtualFileManager.getInstance().findFileByUrl(outputDirPath.get(c));
                outputDir.put(c, file);
            }
            return file;
        }

        public void setOutputDir(IceComponent c, VirtualFile path) {
            if (path != null) {
                outputDir.put(c, path);
                outputDirPath.put(c, path.getUrl());
            }
        }

        public void setCleanOutput(boolean cleanOutput) {
            this.cleanOutput = cleanOutput;
        }

        public boolean isCleanOutput() {
            return cleanOutput;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Config config = (Config) o;

            return cleanOutput == config.cleanOutput && outputDir.equals(config.outputDir);
        }

        @Override
        public int hashCode() {
            int result = outputDir != null ? outputDir.hashCode() : 0;
            result = 31 * result + (cleanOutput ? 1 : 0);
            return result;
        }

        public Iterable<? extends IceComponent> getConfiguredComponents() {
            return outputDirPath.keySet();
        }

        public boolean isValid() {
            return !outputDir.isEmpty();
        }
    }

    private static class TranslatorItemConfig extends JPanel {
        private final JTextField outputFolder = new JTextField(40);
        private final JCheckBox compilationEnabled;

        private VirtualFile selectedFolder = null;
        private FixedSizeButton browseDirectoryButton;

        private TranslatorItemConfig(String name) {
            super(new BorderLayout(5, 5));

            browseDirectoryButton = new FixedSizeButton(outputFolder);

            compilationEnabled = new JCheckBox(name);
            add(compilationEnabled, BorderLayout.WEST);
            add(outputFolder, BorderLayout.CENTER);
            add(browseDirectoryButton, BorderLayout.EAST);

            outputFolder.setEditable(false);

            compilationEnabled.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    outputFolder.setEnabled(compilationEnabled.isSelected());
                    browseDirectoryButton.setEnabled(compilationEnabled.isSelected());
                }
            });

            Dimension size = new Dimension(80, 2);
            compilationEnabled.setMinimumSize(size);
            compilationEnabled.setPreferredSize(size);

            TextFieldWithBrowseButton.MyDoClickAction.addTo(browseDirectoryButton, outputFolder);
            browseDirectoryButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    final VirtualFile[] files = FileChooser.chooseFiles(
                            TranslatorItemConfig.this.getParent(),
                            new FileChooserDescriptor(false, true, false, false, false, false),
                            selectedFolder
                    );
                    if (files.length != 0) {
                        selectedFolder = files[0];
                        outputFolder.setText(selectedFolder.getPath());
                    }
                }
            });

        }

        public boolean getCompilationEnabled() {
            return compilationEnabled.isSelected();
        }

        public void setCompilationEnabled(boolean b) {
            compilationEnabled.setSelected(b);
            browseDirectoryButton.setEnabled(b);
            outputFolder.setEnabled(b);
        }

        public VirtualFile getSelectedFolder() {
            return selectedFolder;
        }

        public void setSelectedFolder(VirtualFile selectedFolder) {
            this.selectedFolder = selectedFolder;
            outputFolder.setText(selectedFolder == null ? null : selectedFolder.getPath());
        }
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

            settingsPane.setBorder(IdeBorderFactory.createTitledBorder("Generated files result folder.", false, false, true));

            for (IceComponent c : IceComponent.values()) {
                VirtualFile outputDir = config.getOutputDir(c);

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
            Config newConfig = getConfig();
            return !config.equals(newConfig);
        }

        private Config getConfig() {

            Config cfg = new Config();

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
                itemConfig.setSelectedFolder(config.getOutputDir(item));
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
