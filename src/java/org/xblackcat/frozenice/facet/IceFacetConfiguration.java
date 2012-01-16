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
import org.jdom.Element;
import org.jetbrains.annotations.Nls;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 08.01.12 13:35
 *
 * @author xBlackCat
 */
@State(name = "IceFacetConfiguration", storages = {@Storage(id = "default", file = "$MODULE_FILE$")})
public class IceFacetConfiguration implements FacetConfiguration, PersistentStateComponent<Element> {
    private Config config = new Config(false, null);

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
    public Element getState() {
        final Element root = new Element("ice-facet-config");

        final Element output = new Element("output");
        root.addContent(output);
        output.setAttribute("cleanOnBuild", Boolean.toString(config.isCleanOutput()));
        output.setText(config.getOutputDirString());

        return root;
    }

    @Override
    public void loadState(Element state) {
        Element el = state.getChild("output");

        if (el != null) {
            boolean cleanOutput = Boolean.parseBoolean(el.getAttributeValue("cleanOnBuild"));
            String url = el.getTextTrim();

            final VirtualFile file = VirtualFileManager.getInstance().findFileByUrl(url);

            config = new Config(cleanOutput, file);
        }
    }

    public static class Config {
        private final VirtualFile outputDir;
        private final boolean cleanOutput;

        public Config(boolean cleanOutput, VirtualFile outputDir) {
            this.cleanOutput = cleanOutput;
            this.outputDir = outputDir;
        }

        public VirtualFile getOutputDir() {
            return outputDir;
        }

        public boolean isCleanOutput() {
            return cleanOutput;
        }

        public String getOutputDirString() {
            return outputDir != null ? outputDir.getUrl() : null;
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

            return cleanOutput == config.cleanOutput && (outputDir != null ? outputDir.equals(config.outputDir) : config.outputDir == null);
        }

        @Override
        public int hashCode() {
            int result = outputDir != null ? outputDir.hashCode() : 0;
            result = 31 * result + (cleanOutput ? 1 : 0);
            return result;
        }
    }

    private class IceFacetEditor extends FacetEditorTab {
        private final FacetEditorContext editorContext;
        private final FacetValidatorsManager validatorsManager;

        private final JComponent pane;

        private final JCheckBox cleanOnBuildCheckBox = new JCheckBox("Clean output folder before build.");
        private final JTextField outputFolder = new JTextField(40);

        private VirtualFile selectedFolder = null;

        public IceFacetEditor(FacetEditorContext editorContext, FacetValidatorsManager validatorsManager) {
            this.editorContext = editorContext;
            this.validatorsManager = validatorsManager;

            outputFolder.setEditable(false);
            FixedSizeButton browseDirectoryButton = new FixedSizeButton(outputFolder);
            JLabel labelOutputFolder = new JLabel("Generated files result folder.");

            pane = new JPanel(new BorderLayout());
            final JPanel settingsPane = new JPanel(new BorderLayout(5, 5));

            pane.add(settingsPane, BorderLayout.NORTH);

            settingsPane.add(labelOutputFolder, BorderLayout.WEST);
            settingsPane.add(outputFolder, BorderLayout.CENTER);
            settingsPane.add(browseDirectoryButton, BorderLayout.EAST);

            settingsPane.add(cleanOnBuildCheckBox, BorderLayout.SOUTH);

            TextFieldWithBrowseButton.MyDoClickAction.addTo(browseDirectoryButton, outputFolder);
            browseDirectoryButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    final VirtualFile[] files = FileChooser.chooseFiles(
                            settingsPane,
                            new FileChooserDescriptor(false, true, false, false, false, false),
                            selectedFolder
                    );
                    if (files.length != 0) {
                        selectedFolder = files[0];
                    }
                }
            });

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
            return config.equals(newConfig);
        }

        private Config getConfig() {
            boolean cleanOutput = cleanOnBuildCheckBox.isSelected();

            return new Config(cleanOutput, selectedFolder);
        }

        @Override
        public void reset() {
            selectedFolder = config.getOutputDir();
            cleanOnBuildCheckBox.setSelected(config.isCleanOutput());
            outputFolder.setText(config.getOutputDirString());
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
