package org.xblackcat.frozenidea.facet;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.FixedSizeButton;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;
import java.awt.*;

/**
 * 30.01.12 9:22
 *
 * @author xBlackCat
 */
class TranslatorItemConfig extends JPanel {
    private final JTextField outputFolder = new JTextField(40);
    private final JCheckBox compilationEnabled;

    private VirtualFile selectedFolder = null;
    private FixedSizeButton browseDirectoryButton;

    TranslatorItemConfig(String name) {
        super(new BorderLayout(5, 5));

        browseDirectoryButton = new FixedSizeButton(outputFolder);

        compilationEnabled = new JCheckBox(name);
        add(compilationEnabled, BorderLayout.WEST);
        add(outputFolder, BorderLayout.CENTER);
        add(browseDirectoryButton, BorderLayout.EAST);

        outputFolder.setEditable(false);

        compilationEnabled.addItemListener(
                e -> {
                    outputFolder.setEnabled(compilationEnabled.isSelected());
                    browseDirectoryButton.setEnabled(compilationEnabled.isSelected());
                }
        );

        Dimension size = new Dimension(80, 2);
        compilationEnabled.setMinimumSize(size);
        compilationEnabled.setPreferredSize(size);

        TextFieldWithBrowseButton.MyDoClickAction.addTo(browseDirectoryButton, outputFolder);
        browseDirectoryButton.addActionListener(
                e -> {
                    final VirtualFile[] files = FileChooser.chooseFiles(
                            new FileChooserDescriptor(false, true, false, false, false, false),
                            TranslatorItemConfig.this.getParent(),
                            null,
                            selectedFolder
                    );
                    if (files.length != 0) {
                        selectedFolder = files[0];
                        outputFolder.setText(selectedFolder.getPath());
                    }
                }
        );

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
