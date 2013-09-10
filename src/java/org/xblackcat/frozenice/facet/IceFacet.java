package org.xblackcat.frozenice.facet;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetTypeId;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
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
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.FrozenIdea;
import org.xblackcat.frozenice.config.IceConfig;
import org.xblackcat.frozenice.config.IceFrameworkConfigurable;
import org.xblackcat.frozenice.util.IceChecker;
import org.xblackcat.frozenice.util.IceComponent;
import org.xblackcat.frozenice.util.IceErrorMessages;

import javax.swing.event.HyperlinkEvent;
import java.util.EnumSet;

/**
 * 08.01.12 13:28
 *
 * @author xBlackCat
 */
public class IceFacet extends Facet<IceFacetConfiguration> {
    public static final FacetTypeId<IceFacet> ID = new FacetTypeId<IceFacet>("ice");
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

        StartupManager.getInstance(project).runWhenProjectIsInitialized(
                new Runnable() {
                    @Override
                    public void run() {
                        final FrozenIdea plugin = ServiceManager.getService(project, FrozenIdea.class);

                        if (plugin == null) {
                            // How it could be??
                            throw new RuntimeException("Invalid state");
                        }

                        final IceConfig config = plugin.getConfig();
                        if (config == null || IceChecker.getInstalledComponents(config.getFrameworkHome()).isEmpty()) {
                            final Notification notification = new Notification(
                                    "Ice Facet",
                                    IceErrorMessages.message("ICE.not.configured"),
                                    IceErrorMessages.message("ICE.not.configured.message"),
                                    NotificationType.ERROR,
                                    new NotificationListener() {
                                        @Override
                                        public void hyperlinkUpdate(
                                                @NotNull Notification notification,
                                                @NotNull HyperlinkEvent event
                                        ) {
                                            if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                                                notification.expire();

                                                IceFrameworkConfigurable configurable = new IceFrameworkConfigurable(
                                                        project
                                                );
                                                ShowSettingsUtil.getInstance().editConfigurable(project, configurable);
                                            }
                                        }

                                    }
                            );
                            Notifications.Bus.notify(notification, project);
                        } else {
                            JavaPsiFacade facade = project.getComponent(JavaPsiFacade.class);

                            PsiClass iceClass = facade.findClass(
                                    "Ice.Object",
                                    getModule().getModuleWithDependenciesAndLibrariesScope(
                                            false
                                    )
                            );

                            if (iceClass == null) {
                                // No class found!
                                final Notification notification = new Notification(
                                        "Ice Facet",
                                        IceErrorMessages.message("ICE.library.not.added"),
                                        IceErrorMessages.message("ICE.library.not.added.message"),
                                        NotificationType.ERROR,
                                        new NotificationListener() {
                                            @Override
                                            public void hyperlinkUpdate(
                                                    @NotNull final Notification notification,
                                                    @NotNull HyperlinkEvent event
                                            ) {
                                                Computable<Boolean> runnable = new Computable<Boolean>() {
                                                    public Boolean compute() {
                                                        EnumSet<IceComponent> iceComponents = IceChecker.getInstalledComponents(
                                                                config.getFrameworkHome()
                                                        );

                                                        if (iceComponents.contains(IceComponent.Java)) {
                                                            final ModifiableRootModel rootModel = ModuleRootManager.getInstance(
                                                                    getModule()
                                                            ).getModifiableModel();

                                                            final LibraryTable.ModifiableModel modifiableModel = rootModel.getModuleLibraryTable().getModifiableModel();
                                                            final VirtualFile libraryPath = IceComponent.Java.getLibraryPath(
                                                                    config.getFrameworkHome()
                                                            );
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
                                                };

                                                if (ApplicationManager.getApplication().runWriteAction(runnable)) {
                                                    notification.expire();
                                                }
                                            }

                                        }
                                );
                                Notifications.Bus.notify(notification, project);
                            }
                        }
                    }
                }
        );
    }
}
