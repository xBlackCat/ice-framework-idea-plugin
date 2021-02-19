package org.xblackcat.frozenidea.config;

import com.intellij.facet.FacetManager;
import com.intellij.facet.ModifiableFacetModel;
import com.intellij.ide.ui.UISettings;
import com.intellij.ide.util.BrowseFilesListener;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.CompositeConfigurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.ui.*;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.FrozenIdea;
import org.xblackcat.frozenidea.facet.IceFacet;
import org.xblackcat.frozenidea.facet.IceFacetConfigurationEditor;
import org.xblackcat.frozenidea.util.IceChecker;
import org.xblackcat.frozenidea.util.SliceBundle;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 12.01.12 14:37
 *
 * @author xBlackCat
 */
public class IceFrameworkConfigurable extends CompositeConfigurable<IceFacetConfigurationEditor> implements SearchableConfigurable {
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
    protected @NotNull List<IceFacetConfigurationEditor> createConfigurables() {
        final ModuleManager moduleManager = ModuleManager.getInstance(project);

        return Arrays.stream(moduleManager.getModules()).map(module -> new IceFacetConfigurationEditor(
                new Accessor<>() {
                    @Override
                    public void accept(SliceCompilerSettings sliceCompilerSettings) {
                        final FacetManager facetManager = FacetManager.getInstance(module);
                        final IceFacet facet = facetManager.getFacetByType(IceFacet.ID);
                        if (facet != null) {
                            facet.getConfiguration().loadState(sliceCompilerSettings);
                        } else {
                            WriteCommandAction.runWriteCommandAction(project, () -> {
                                final ModifiableFacetModel modifiableModel = facetManager.createModifiableModel();
                                final IceFacet newFacet = facetManager.createFacet(IceFacet.TYPE, IceFacet.TYPE.getPresentableName(), null);
                                modifiableModel.addFacet(newFacet);
                                modifiableModel.commit();
                                newFacet.getConfiguration().loadState(sliceCompilerSettings);
                            });
                        }
                    }

                    @Override
                    public SliceCompilerSettings get() {
                        final IceFacet iceFacet = FacetManager.getInstance(module).getFacetByType(IceFacet.ID);
                        if (iceFacet != null) {
                            return iceFacet.getConfiguration().getState();
                        } else {
                            return null;
                        }
                    }
                },
                project,
                module
        )).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public JComponent createComponent() {
        if (configForm == null) {
            final FrozenIdea plugin = project.getService(FrozenIdea.class);
            configForm = new IceFrameworkConfigForm(plugin);
        }

        return configForm;
    }

    @Override
    public boolean isModified() {
        return myModified || super.isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
        super.apply();
        if (configForm != null) {
            configForm.apply();
        }
    }

    @Override
    public void reset() {
        super.reset();
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

    private class IceFrameworkConfigForm extends JPanel {
        private final FrozenIdea plugin;
        private final JTextField iceHomeFolder;
        private final AddEditRemovePanel<VirtualFile> includesListPane;
        private VirtualFile selectedFolder;

        public IceFrameworkConfigForm(FrozenIdea plugin) {
            super(new BorderLayout());
            this.plugin = plugin;

            setBorder(IdeBorderFactory.createTitledBorder(SliceBundle.message("configuration.framework.title"), false));

            iceHomeFolder = new JTextField(30);
            final JLabel fieldLabel = new JLabel("ICE home");
            JPanel fieldsPane = new JPanel(new BorderLayout(5, 0));
            fieldsPane.add(fieldLabel, BorderLayout.WEST);
            fieldsPane.add(new TextFieldWithBrowseButton(iceHomeFolder, e -> {
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
            }), BorderLayout.CENTER);
            add(fieldsPane, BorderLayout.NORTH);

            iceHomeFolder.setEditable(false);

            final var includePathModel = new AddEditRemovePanel.TableModel<VirtualFile>() {
                @Override
                public int getColumnCount() {
                    return 1;
                }

                @Override
                public @NotNull String getColumnName(int columnIndex) {
                    return SliceBundle.message("configuration.title.include.path");
                }

                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    return VirtualFile.class;
                }

                @Override
                public Object getField(VirtualFile o, int columnIndex) {
                    return o;
                }
            };
            includesListPane = new AddEditRemovePanel<>(includePathModel, new ArrayList<>(), "Include list") {
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

                            setForeground(vf.isValid() ? JBColor.BLACK : JBColor.RED);
                            setBackground(JBColor.WHITE);

                            setText(vf.getPath());
                        }
                    }
            );
            var modulesConfigurations = new JBSplitter(false, .16f);

            final JPanel borderWrapper = new JPanel(new BorderLayout());
            borderWrapper.setBorder(IdeBorderFactory.createTitledBorder(
                    SliceBundle.message("configuration.modules.facets.title"),
                    false
            ));
            borderWrapper.add(modulesConfigurations, BorderLayout.CENTER);

            final DefaultListModel<Module> modulesListModel = new DefaultListModel<>();
            final JBList<Module> moduleList = new JBList<>(modulesListModel);
            final JBScrollPane scrollPane = new JBScrollPane(moduleList);
            scrollPane.setBorder(IdeBorderFactory.createBorder(SideBorder.ALL));
            modulesConfigurations.setFirstComponent(scrollPane);
            final JBCardLayout cardLayout = new JBCardLayout();
            final var settings = new JBPanel<>(cardLayout);
            modulesConfigurations.setSecondComponent(settings);

            final ModuleManager moduleManager = ModuleManager.getInstance(project);
            modulesListModel.addAll(Arrays.asList(moduleManager.getModules()));

            final List<IceFacetConfigurationEditor> configurables = getConfigurables();

            for (var c : configurables) {
                settings.add(c.getModule().getName(), c.createComponent());
            }

            moduleList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    cardLayout.show(settings, configurables.get(moduleList.getSelectedIndex()).getModule().getName());
                }
            });

            if (!moduleList.isEmpty()) {
                moduleList.setSelectedIndex(0);
            }

            final JBSplitter pane = new JBSplitter(true);
            pane.setFirstComponent(includesListPane);
            pane.setSecondComponent(borderWrapper);
            add(pane, BorderLayout.CENTER);
        }

        void reset() {
            IceConfig config = plugin.getConfig();

            if (config != null) {
                selectedFolder = config.getFrameworkHome();
                if (selectedFolder != null) {
                    iceHomeFolder.setText(selectedFolder.getPath());
                }
                final ArrayList<VirtualFile> files = new ArrayList<>();
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
                    final ArrayList<VirtualFile> includes = new ArrayList<>();
                    final VirtualFile slice = selectedFolder.findFileByRelativePath("slice");
                    if (slice != null) {
                        includes.add(slice);
                    }
                    includesListPane.setData(includes);

                    if (version != null) {
                        Messages.showInfoMessage(
                                project,
                                "Found ZeroC framework. Version: " + version,
                                "ZeroC ICE Framework found"
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
