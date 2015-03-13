package org.xblackcat.frozenidea.config;

import com.intellij.ide.ui.UISettings;
import com.intellij.ide.util.BrowseFilesListener;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.FixedSizeButton;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.ui.AddEditRemovePanel;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.FrozenIdea;
import org.xblackcat.frozenidea.util.IceChecker;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

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
    private class IceFrameworkConfigForm extends JPanel {
        private final FrozenIdea plugin;
        private final JTextField iceHomeFolder;
        private VirtualFile selectedFolder;
        private final AddEditRemovePanel<VirtualFile> includesListPane;

        public IceFrameworkConfigForm(FrozenIdea plugin) {
            super(new BorderLayout());
            this.plugin = plugin;

            setBorder(IdeBorderFactory.createTitledBorder("ZeroC ICE framework", false));

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
            browseDirectoryButton.addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            final VirtualFile[] files = FileChooser.chooseFiles(
                                    BrowseFilesListener.SINGLE_DIRECTORY_DESCRIPTOR,
                                    IceFrameworkConfigForm.this,
                                    null,
                                    selectedFolder
                            );
                            if (files.length != 0) {
                                final Task.Modal checkTask = new IceFrameworkVersionChecker(
                                        VfsUtil.virtualToIoFile(files[0])
                                );

                                checkTask.queue();
                            }
                        }
                    }
            );

            includesListPane = new AddEditRemovePanel<VirtualFile>(
                    new AddEditRemovePanel.TableModel<VirtualFile>() {
                        @Override
                        public int getColumnCount() {
                            return 1;
                        }

                        @Nullable
                        @Override
                        public String getColumnName(int columnIndex) {
                            return "Include path";
                        }

                        @Override
                        public Class getColumnClass(int columnIndex) {
                            return VirtualFile.class;
                        }

                        @Override
                        public Object getField(VirtualFile o, int columnIndex) {
                            return o;
                        }
                    }, new ArrayList<VirtualFile>(), "Include list"
            ) {
                @Nullable
                @Override
                protected VirtualFile addItem() {
                    return editItem(null);
                }

                @Override
                protected boolean removeItem(VirtualFile o) {
                    setModified(true);
                    return true;
                }

                @Nullable
                @Override
                protected VirtualFile editItem(VirtualFile o) {
                    VirtualFile[] files = FileChooser.chooseFiles(
                            BrowseFilesListener.SINGLE_DIRECTORY_DESCRIPTOR, includesListPane, project, o
                    );
                    if (files.length == 0) {
                        return null;
                    }

                    setModified(true);
                    return files[0];
                }
            };
            includesListPane.getTable().setShowColumns(true);
            includesListPane.setRenderer(
                    0,
                    new DefaultTableCellRenderer() {
                        @Override
                        protected void setValue(Object value) {
                            VirtualFile vf = (VirtualFile) value;

                            if (vf.isValid()) {
                                setForeground(JBColor.BLACK);
                                setBackground(JBColor.WHITE);
                            } else {
                                setForeground(JBColor.RED);
                                setBackground(JBColor.WHITE);
                            }

                            setText(vf.getPath());
                        }
                    }
            );

            add(includesListPane, BorderLayout.CENTER);

        }

        void reset() {
            IceConfig config = plugin.getConfig();

            if (config != null) {
                selectedFolder = config.getFrameworkHome();
                if (selectedFolder != null) {
                    iceHomeFolder.setText(selectedFolder.getPath());
                }
                final ArrayList<VirtualFile> files = new ArrayList<VirtualFile>();
                final VirtualFileManager manager = VirtualFileManager.getInstance();

                for (String url : config.getIncludeUrls()) {
                    final VirtualFile file = manager.findFileByUrl(url);
                    if (file != null) {
                        files.add(file);
                    }
                }
                includesListPane.setData(files);
            }

            setModified(false);
        }

        void apply() {
            final List<VirtualFile> includesListPaneData = includesListPane.getData();
            final String[] urls = new String[includesListPaneData.size()];
            int i = 0;
            for (VirtualFile vf : includesListPaneData) {
                urls[i++] = vf.getUrl();
            }
            final IceConfig config = new IceConfig(selectedFolder != null ? selectedFolder.getUrl() : null, urls);
            plugin.setConfig(config);
            setModified(false);

            UISettings.getInstance().fireUISettingsChanged();
        }

        private class IceFrameworkVersionChecker extends Task.Modal {
            private File checkingFolder;
            private String version;

            public IceFrameworkVersionChecker(File folder) {
                super(project, "Checking ZeroC home folder", true);
                checkingFolder = folder;
            }

            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.setIndeterminate(true);

                if (checkingFolder.isDirectory()) {
                    // Find executable files
                    EnumSet<IceComponent> installedComponents = IceChecker.getInstalledComponents(
                            checkingFolder
                    );
                    if (installedComponents.isEmpty()) {
                        checkingFolder = checkingFolder.getParentFile();

                        if (checkingFolder == null) {
                            return;
                        } else {
                            installedComponents = IceChecker.getInstalledComponents(
                                    checkingFolder
                            );
                            if (installedComponents.isEmpty()) {
                                checkingFolder = null;
                                return;
                            }
                        }
                    }

                    final IceComponent anyComponent = installedComponents.iterator().next();
                    version = IceChecker.readVersion(anyComponent, checkingFolder);
                    if (version == null) {
                        checkingFolder = null;
                    }
                }
            }

            @Override
            public void onSuccess() {
                if (checkingFolder != null) {
                    selectedFolder = VfsUtil.findFileByIoFile(checkingFolder, true);
                    assert selectedFolder != null;
                    iceHomeFolder.setText(selectedFolder.getPath());
                    final ArrayList<VirtualFile> includes = new ArrayList<VirtualFile>();
                    final VirtualFile slice = selectedFolder.findFileByRelativePath("slice");
                    if (slice != null) {
                        includes.add(slice);
                    }
                    includesListPane.setData(includes);

                    if (version != null) {
                        Messages.showInfoMessage(
                                project,
                                "Found ZeroC framework. Version: " + version,
                                "ZeroC ICE Framework is found"
                        );
                    }

                    setModified(true);
                } else {
                    Messages.showWarningDialog(
                            project,
                            "Selected folder is not ZeroC ICE framework home directory",
                            "ZeroC ICE Framework is not found"
                    );
                }
            }
        }
    }
}
