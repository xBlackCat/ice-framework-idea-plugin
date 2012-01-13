package org.xblackcat.frozenice.config;

import com.intellij.ide.ui.UISettings;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.FixedSizeButton;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.IdeBorderFactory;
import org.apache.sanselan.util.IOUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.FrozenIdea;
import org.xblackcat.frozenice.util.Icons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

/**
 * 12.01.12 14:37
 *
 * @author xBlackCat
 */
public class IceFrameworkConfigurable extends BaseConfigurable implements SearchableConfigurable {
    private final Project project;
    private IceFrameworkConfigForm configForm;

    public IceFrameworkConfigurable(Project project) {
        this.project = project;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "ZeroC ICE Framework";
    }

    @Override
    public Icon getIcon() {
        return Icons.FACET_ICON_32;
    }

    @Override
    public String getHelpTopic() {
        return null;
    }

    @Override
    public JComponent createComponent() {
        if (configForm == null) {
            final FrozenIdea plugin = ServiceManager.getService(project, FrozenIdea.class);
            configForm = new IceFrameworkConfigForm(plugin);
        }

        return configForm;
    }

    @Override
    public void apply() throws ConfigurationException {
        if (configForm != null) {
            configForm.apply();
        }
    }

    @Override
    public void reset() {
        if (configForm != null) {
            configForm.reset();
        }
    }

    @Override
    public void disposeUIResources() {
        configForm = null;
    }

    @NotNull
    @Override
    public String getId() {
        return "frozenidea";
    }

    @Override
    public Runnable enableSearch(String option) {
        return null;
    }

    /**
     * 12.01.12 16:19
     *
     * @author xBlackCat
     */
    public class IceFrameworkConfigForm extends JPanel {
        private final FrozenIdea plugin;
        private final JTextField iceHomeFolder;
        private VirtualFile selectedFolder;

        public IceFrameworkConfigForm(FrozenIdea plugin) {
            super(new BorderLayout());
            this.plugin = plugin;

            setBorder(IdeBorderFactory.createTitledBorder("ZeroC ICE framework", false, false, true));

            JPanel fieldsPane = new JPanel();
            final GroupLayout gl = new GroupLayout(fieldsPane);
            fieldsPane.setLayout(gl);
            gl.setAutoCreateGaps(true);

            add(fieldsPane, BorderLayout.NORTH);

            GroupLayout.SequentialGroup hGroup = gl.createSequentialGroup();

            GroupLayout.SequentialGroup vGroup = gl.createSequentialGroup();

            gl.setVerticalGroup(vGroup);
            gl.setHorizontalGroup(hGroup);

            final JLabel fieldLabel = new JLabel("ICE home");

            iceHomeFolder = new JTextField(30);
            iceHomeFolder.setEditable(false);
            FixedSizeButton browseDirectoryButton = new FixedSizeButton(iceHomeFolder);

            hGroup
                    .addGroup(
                            gl.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                    .addComponent(fieldLabel)
                    )
                    .addGroup(
                            gl.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                    .addComponent(iceHomeFolder)
                    )
                    .addGroup(
                            gl.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                    .addComponent(browseDirectoryButton)
                    );

            vGroup
                    .addGroup(
                            gl.createParallelGroup(GroupLayout.Alignment.CENTER)
                                    .addComponent(fieldLabel)
                                    .addComponent(iceHomeFolder)
                                    .addComponent(browseDirectoryButton)
                    );

            TextFieldWithBrowseButton.MyDoClickAction.addTo(browseDirectoryButton, iceHomeFolder);
            browseDirectoryButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    final VirtualFile[] files = FileChooser.chooseFiles(
                            IceFrameworkConfigForm.this,
                            new FileChooserDescriptor(false, true, false, false, false, false),
                            selectedFolder
                    );
                    if (files.length != 0) {
                        final Task.Modal checkTask = new Task.Modal(project, "Checking ZeroC home folder", true) {
                            private VirtualFile checkingFolder = files[0];
                            private String version;

                            private VirtualFile check(VirtualFile root, String name) {
                                VirtualFile bin = root.findChild("bin");
                                if (bin == null || !bin.isDirectory()) {
                                    return null;
                                }

                                VirtualFile config = bin.findChild(name);
                                if (config == null || !config.isValid()) {
                                    return null;
                                }

                                return config;
                            }

                            @Override
                            public void run(@NotNull ProgressIndicator indicator) {
                                indicator.setIndeterminate(true);

                                if (checkingFolder.isDirectory()) {
                                    // Find executable files
                                    String translatorName = SystemInfo.isWindows ? "slice2java.exe" : "slice2java";
                                    VirtualFile translatorFile;
                                    if ((translatorFile = check(checkingFolder, translatorName)) == null) {
                                        checkingFolder = checkingFolder.getParent();

                                        if (checkingFolder == null) {
                                            return;
                                        } else {
                                            translatorFile = check(checkingFolder, translatorName);
                                            if (translatorFile == null || !translatorFile.isValid() || translatorFile.isDirectory()) {
                                                checkingFolder = null;
                                                return;
                                            }
                                        }
                                    }

                                    // check file version
                                    try {
                                        Process process = new ProcessBuilder(translatorFile.getPath(), "-v").start();
                                        process.waitFor();

                                        InputStream stdOutIS = process.getErrorStream();
                                        byte[] stdOut;
                                        try {
                                            stdOut = IOUtils.getInputStreamBytes(stdOutIS);
                                            if (stdOut == null || stdOut.length == 0) {
                                                checkingFolder = null;
                                                return;
                                            }
                                        } finally {
                                            stdOutIS.close();
                                        }

                                        version = new String(stdOut);
                                    } catch (IOException e1) {
                                        checkingFolder = null;
                                    } catch (InterruptedException e1) {
                                        checkingFolder = null;
                                    }
                                }
                            }

                            @Override
                            public void onSuccess() {
                                if (checkingFolder != null) {
                                    selectedFolder = checkingFolder;
                                    iceHomeFolder.setText(selectedFolder.getPresentableUrl());

                                    if (version != null) {
                                        Messages.showInfoMessage(project, "Found ZeroC framework. Version: " + version, "ZeroC Framework is found");
                                    }

                                    setModified(true);
                                } else {
                                    Messages.showWarningDialog(project, "Selected folder is not ZeroC ICE framework home directory", "ICE not found");
                                }
                            }
                        };

                        checkTask.queue();
                    }
                }
            });

        }

        void reset() {
            IceConfig config = plugin.getConfig();

            if (config != null) {
                selectedFolder = config.getFrameworkHome();
                if (selectedFolder != null) {
                    iceHomeFolder.setText(selectedFolder.getUrl());
                }
            }
            
            setModified(false);
        }

        void apply() {
            plugin.setConfig(new IceConfig(selectedFolder));
            setModified(false);

            UISettings.getInstance().fireUISettingsChanged();
        }
    }
}
