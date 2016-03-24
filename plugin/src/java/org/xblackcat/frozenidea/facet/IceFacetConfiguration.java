package org.xblackcat.frozenidea.facet;

import com.intellij.designer.propertyTable.renderers.BooleanRenderer;
import com.intellij.facet.FacetConfiguration;
import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.facet.ui.FacetValidatorsManager;
import com.intellij.ide.util.BrowseFilesListener;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.*;
import com.intellij.ui.popup.PopupFactoryImpl;
import com.intellij.util.ui.UIUtil;
import org.apache.commons.lang.NotImplementedException;
import org.jdom.Element;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.io.LocalFileFinder;
import org.jetbrains.jps.model.java.JavaModuleSourceRootTypes;
import org.xblackcat.frozenidea.config.IceComponent;
import org.xblackcat.frozenidea.config.SliceCompilerSettings;
import org.xblackcat.frozenidea.config.Target;
import org.xblackcat.frozenidea.util.IceChecker;
import org.xblackcat.frozenidea.util.IceMessages;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;

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

    private class IceFacetEditor extends FacetEditorTab {
        private final FacetEditorContext editorContext;
        private final FacetValidatorsManager validatorsManager;

        private final JComponent pane;

        private final JCheckBox cleanOnBuildCheckBox = new BooleanRenderer();
        private final TargetsListPanel targetsListPanel;

        public IceFacetEditor(FacetEditorContext editorContext, FacetValidatorsManager validatorsManager) {
            this.editorContext = editorContext;
            this.validatorsManager = validatorsManager;

            cleanOnBuildCheckBox.setText("Clean output folder before build");

            pane = new JPanel(new BorderLayout());
            targetsListPanel = new TargetsListPanel();

            pane.add(targetsListPanel, BorderLayout.NORTH);

            targetsListPanel.setBorder(IdeBorderFactory.createTitledBorder("Generated files result folder.", false));

            pane.add(cleanOnBuildCheckBox, BorderLayout.SOUTH);
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
            return !config.equals(newConfig);
        }

        private SliceCompilerSettings getConfig() {
            SliceCompilerSettings cfg = new SliceCompilerSettings();

            cfg.setCleanOutput(cleanOnBuildCheckBox.isSelected());
            cfg.setComponents(targetsListPanel.getData());

            return cfg;
        }

        @Override
        public void reset() {
            cleanOnBuildCheckBox.setSelected(config.isCleanOutput());
            final java.util.List<Target> data = targetsListPanel.getData();
            data.clear();
            data.addAll(config.getComponents());
        }

        @Override
        public void apply() throws ConfigurationException {
            config = getConfig();
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
                                    JTable table,
                                    Object value,
                                    boolean selected,
                                    boolean hasFocus,
                                    int row,
                                    int column
                            ) {
                                assert value != null;

                                IceComponent type = (IceComponent) value;
                                setIcon(type.getIcon());
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

                                    private final ActionGroup myGeneratorListGroup = new GeneratorListGroup();

                                    @Override
                                    public void run(AnActionButton button) {
                                        final ListPopup popup = PopupFactoryImpl.getInstance().createActionGroupPopup(
                                                IceMessages.message("frozenidea.facet.options.targets.popup.title"),
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
                        editorContext.getProject(),
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
                    final EnumSet<IceComponent> exists = IceChecker.getAvailableTranslators(editorContext.getModule());

                    final IceComponent[] types = IceComponent.values();
                    actions = new AnAction[types.length];
                    int i = 0;
                    while (i < types.length) {
                        final IceComponent type = types[i];
                        final CreateTargetAction action = new CreateTargetAction(type, exists.contains(type));
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
                    super(type.name(), "", type.getIcon());
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
                    final Module module = editorContext.getModule();
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
}
