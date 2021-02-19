package org.xblackcat.frozenidea.facet;

import com.intellij.designer.propertyTable.renderers.BooleanRenderer;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.ide.util.BrowseFilesListener;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.*;
import com.intellij.ui.popup.PopupFactoryImpl;
import com.intellij.util.IconUtil;
import com.intellij.util.ui.UIUtil;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.io.LocalFileFinder;
import org.jetbrains.jps.model.java.JavaModuleSourceRootTypes;
import org.xblackcat.frozenidea.config.Accessor;
import org.xblackcat.frozenidea.config.IceComponent;
import org.xblackcat.frozenidea.config.SliceCompilerSettings;
import org.xblackcat.frozenidea.config.Target;
import org.xblackcat.frozenidea.util.IceChecker;
import org.xblackcat.frozenidea.util.SliceBundle;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Objects;

/**
 *
 */
public class IceFacetConfigurationEditor extends FacetEditorTab {
    private final JComponent pane;
    private final JButton addFacetButton;
    private final JCheckBox cleanOnBuildCheckBox = new BooleanRenderer();
    private final TargetsListPanel targetsListPanel;
    private final @NotNull Project project;
    private final @NotNull Module module;
    private final @NotNull Accessor<SliceCompilerSettings> configAccessor;
    private boolean initialized = false;

    public IceFacetConfigurationEditor(
            @NotNull Accessor<SliceCompilerSettings> configAccessor,
            @NotNull Project project,
            @NotNull Module module
    ) {
        this.project = project;
        this.module = module;
        this.configAccessor = configAccessor;
        cleanOnBuildCheckBox.setText("Clean output folder before build");

        pane = new JPanel(new BorderLayout());
        targetsListPanel = new TargetsListPanel();

        addFacetButton = new JButton(new AbstractAction("Add Facet", IconUtil.getAddIcon()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                setInitialized(true);
            }
        });

        pane.add(addFacetButton, BorderLayout.NORTH);

        pane.add(targetsListPanel, BorderLayout.CENTER);

        targetsListPanel.setBorder(IdeBorderFactory.createTitledBorder("Generated files result folder.", false));

        pane.add(cleanOnBuildCheckBox, BorderLayout.SOUTH);
    }

    private static Icon getIcon(IceComponent type) {
        return IconLoader.createLazy(() -> IconLoader.getIcon(type.getIconName(), IceFacetConfiguration.class));
    }

    public Module getModule() {
        return module;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Ice";
    }

    @NotNull
    @Override
    public JComponent createComponent() {
        return pane;
    }

    @Override
    public boolean isModified() {
        SliceCompilerSettings newConfig = getConfig();
        return !Objects.equals(newConfig, configAccessor.get());
    }

    private SliceCompilerSettings getConfig() {
        if (!initialized) {
            return null;
        }
        SliceCompilerSettings cfg = new SliceCompilerSettings();

        cfg.setCleanOutput(cleanOnBuildCheckBox.isSelected());
        cfg.setComponents(targetsListPanel.getData());

        return cfg;
    }

    @Override
    public void reset() {
        final SliceCompilerSettings sliceCompilerSettings = configAccessor.get();
        if (sliceCompilerSettings == null) {
            setInitialized(false);
            return;
        }

        setInitialized(true);
        cleanOnBuildCheckBox.setSelected(sliceCompilerSettings.isCleanOutput());
        final java.util.List<Target> data = targetsListPanel.getData();
        data.clear();
        data.addAll(sliceCompilerSettings.getComponents());
    }

    private void setInitialized(boolean show) {
        initialized = show;
        addFacetButton.setVisible(!show);
        cleanOnBuildCheckBox.setVisible(show);
        targetsListPanel.setVisible(show);
    }

    @Override
    public void apply() throws ConfigurationException {
        if (initialized) {
            configAccessor.accept(getConfig());
        }
    }

    @Override
    public void disposeUIResources() {
    }

    private class TargetsListPanel extends AddEditRemovePanel<Target> {
        public TargetsListPanel() {
            super(new TargetTableModel(), new ArrayList<>());
            setRenderer(
                    0, new ColoredTableCellRenderer() {
                        @Override
                        protected void customizeCellRenderer(
                                @NotNull JTable table,
                                Object value,
                                boolean selected,
                                boolean hasFocus,
                                int row,
                                int column
                        ) {
                            assert value != null;

                            IceComponent type = (IceComponent) value;
                            setIcon(IconLoader.createLazy(() -> IconLoader.getIcon(type.getIconName(), IceFacetConfiguration.class)));
                            append(type.name(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
                        }
                    }
            );
            setRenderer(
                    1, new ColoredTableCellRenderer() {
                        @Override
                        protected void customizeCellRenderer(
                                JTable table,
                                Object value,
                                boolean selected,
                                boolean hasFocus,
                                int row,
                                int column
                        ) {
                            append(VfsUtil.urlToPath(value.toString()), SimpleTextAttributes.REGULAR_ATTRIBUTES);
                        }
                    }
            );

            getTable().setShowColumns(true);
        }

        @Override
        protected void initPanel() {
            setLayout(new BorderLayout());

            final JPanel panel = ToolbarDecorator.createDecorator(getTable())
                    .setAddAction(
                            new AnActionButtonRunnable() {

                                private final ActionGroup myGeneratorListGroup = new TargetsListPanel.GeneratorListGroup();

                                @Override
                                public void run(AnActionButton button) {
                                    final ListPopup popup = PopupFactoryImpl.getInstance().createActionGroupPopup(
                                            SliceBundle.message("frozenidea.facet.options.targets.popup.title"),
                                            myGeneratorListGroup,
                                            button.getDataContext(),
                                            JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
                                            true
                                    );

                                    popup.show(button.getPreferredPopupPoint());
                                }
                            }
                    )
                    .setRemoveAction(
                            button -> doRemove()
                    )
                    .setEditAction(
                            button -> {
                                if (getTable().isEditing()) {
                                    getTable().getCellEditor().stopCellEditing();
                                    return;
                                }
                                doEdit();
                            }
                    )
                    .disableUpAction()
                    .disableDownAction()
                    .createPanel();
            add(panel, BorderLayout.CENTER);
            final String label = getLabelText();
            if (label != null) {
                UIUtil.addBorder(panel, IdeBorderFactory.createTitledBorder(label, false));
            }
        }

        @Nullable
        @Override
        protected Target addItem() {
            throw new NotImplementedException("Should not be invoked");
        }

        @Override
        protected boolean removeItem(Target target) {
            return true;
        }

        @Nullable
        @Override
        protected Target editItem(Target target) {
            final String dir = target.getOutputDir();

            final VirtualFile chooseFile = FileChooser.chooseFile(
                    BrowseFilesListener.SINGLE_DIRECTORY_DESCRIPTOR,
                    project,
                    dir == null ? null : LocalFileFinder.findFile(dir)
            );

            if (chooseFile == null) {
                return null;
            }

            target.setOutputDir(chooseFile.getPath());

            return target;
        }

        private class GeneratorListGroup extends ActionGroup {
            private final AnAction[] actions;

            {
                final EnumSet<IceComponent> exists = IceChecker.getAvailableTranslators(module);

                final IceComponent[] types = IceComponent.values();
                actions = new AnAction[types.length];
                int i = 0;
                while (i < types.length) {
                    final IceComponent type = types[i];
                    final TargetsListPanel.CreateTargetAction action = new TargetsListPanel.CreateTargetAction(type, exists.contains(type));
                    actions[i] = action;
                    i++;
                }
            }

            @NotNull
            @Override
            public AnAction[] getChildren(@Nullable AnActionEvent e) {
                return actions;
            }
        }

        private class CreateTargetAction extends AnAction {
            private final IceComponent myType;
            private final boolean enabled;

            public CreateTargetAction(IceComponent type, boolean enabled) {
                super(type.name(), "", getIcon(type));
                myType = type;
                this.enabled = enabled;
            }


            @Override
            public void update(AnActionEvent e) {
                super.update(e);
                e.getPresentation().setEnabled(enabled);
            }

            @Override
            public void actionPerformed(AnActionEvent e) {
                ModuleRootManager rootManager = ModuleRootManager.getInstance(module);
                final Collection<VirtualFile> roots = rootManager.getSourceRoots(JavaModuleSourceRootTypes.SOURCES);

                final Target o = editItem(new Target(myType, roots.isEmpty() ? null : roots.iterator().next().getPath()));
                if (o == null) return;

                getData().add(o);
                int index = getData().size() - 1;
                ((AbstractTableModel) getTable().getModel()).fireTableRowsInserted(index, index);
                getTable().setRowSelectionInterval(index, index);
            }
        }
    }
}
