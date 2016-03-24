package org.xblackcat.frozenidea.facet;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetTypeId;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import org.xblackcat.frozenidea.FrozenIdea;
import org.xblackcat.frozenidea.config.IceComponent;
import org.xblackcat.frozenidea.config.IceConfig;
import org.xblackcat.frozenidea.config.IceFrameworkConfigurable;
import org.xblackcat.frozenidea.util.IceChecker;
import org.xblackcat.frozenidea.util.IceMessages;

import javax.swing.event.HyperlinkEvent;
import java.io.File;
import java.util.EnumSet;

/**
 * 08.01.12 13:28
 *
 * @author xBlackCat
 */
public class IceFacet extends Facet<IceFacetConfiguration> {
    public static final FacetTypeId<IceFacet> ID = new FacetTypeId<>("ice");
    public static final IceFacetType TYPE = new IceFacetType();

    public IceFacet(
            @org.jetbrains.annotations.NotNull Module module,
            @org.jetbrains.annotations.NotNull String name,
            @org.jetbrains.annotations.NotNull IceFacetConfiguration configuration,
            Facet underlyingFacet
    ) {
        super(TYPE, module, name, configuration, underlyingFacet);
    }

    @Override
    public void initFacet() {
        // Check if project-level options are set.

        final Project project = getModule().getProject();

        StartupManager.getInstance(project).runWhenProjectIsInitialized(new IceSettingsChecker(project));
    }

    private class LibraryInstaller implements Computable<Boolean> {
        private final File frameworkHome;

        public LibraryInstaller(File frameworkHome) {
            this.frameworkHome = frameworkHome;
        }

        public Boolean compute() {
            EnumSet<IceComponent> iceComponents = IceChecker.getInstalledComponents(frameworkHome);

            if (iceComponents.contains(IceComponent.Java)) {
                final ModifiableRootModel rootModel = ModuleRootManager.getInstance(getModule()).getModifiableModel();

                final LibraryTable.ModifiableModel modifiableModel = rootModel.getModuleLibraryTable().getModifiableModel();
                final File libraryPath = IceComponent.Java.getLibraryPath(frameworkHome);
                if (libraryPath != null) {
                    String jarUrl = "jar://" + libraryPath.getPath() + "!/";

                    Library iceLibrary = modifiableModel.createLibrary("Ice");
                    Library.ModifiableModel libModel = iceLibrary.getModifiableModel();
                    libModel.addRoot(jarUrl, OrderRootType.CLASSES);
                    libModel.addRoot(jarUrl, OrderRootType.SOURCES);
                    libModel.commit();
                    modifiableModel.commit();
                    rootModel.commit();

                    return Boolean.TRUE;
                }
            }

            return Boolean.FALSE;
        }
    }

    private class IceSettingsChecker implements Runnable {
        private final Project project;

        public IceSettingsChecker(Project project) {
            this.project = project;
        }

        @Override
        public void run() {
            final FrozenIdea plugin = project.getComponent(FrozenIdea.class);

            if (plugin == null) {
                // How it could be??
                throw new RuntimeException("Invalid state");
            }

            final IceConfig config = plugin.getConfig();
            if (config != null) {
                final File frameworkHome = VfsUtil.virtualToIoFile(config.getFrameworkHome());

                if (!IceChecker.getInstalledComponents(frameworkHome).isEmpty()) {
                    JavaPsiFacade facade = JavaPsiFacade.getInstance(project);

                    final GlobalSearchScope allLibraryScope = getModule().getModuleWithDependenciesAndLibrariesScope(false);
                    PsiClass iceClass = facade.findClass("Ice.Object", allLibraryScope);

                    if (iceClass == null) {
                        // No class found!
                        final Notification notification = new Notification(
                                "Ice Facet",
                                IceMessages.message("ICE.library.not.added"),
                                IceMessages.message("ICE.library.not.added.message"),
                                NotificationType.ERROR,
                                (notification1, event) -> {
                                    Computable<Boolean> runnable = new LibraryInstaller(frameworkHome);

                                    if (ApplicationManager.getApplication().runWriteAction(runnable)) {
                                        notification1.expire();
                                    }
                                }
                        );
                        Notifications.Bus.notify(notification, project);
                    }

                    return;
                }
            }

            final Notification notification = new Notification(
                    "Ice Facet",
                    IceMessages.message("ICE.not.configured"),
                    IceMessages.message("ICE.not.configured.message"),
                    NotificationType.ERROR,
                    (notification1, event) -> {
                        if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                            notification1.expire();

                            IceFrameworkConfigurable configurable = new IceFrameworkConfigurable(project);
                            ShowSettingsUtil.getInstance().editConfigurable(project, configurable);
                        }
                    }
            );
            Notifications.Bus.notify(notification, project);
        }
    }
}
