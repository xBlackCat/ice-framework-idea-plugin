package org.xblackcat.frozenidea.facet;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetTypeId;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.FrozenIdea;
import org.xblackcat.frozenidea.config.IceConfig;
import org.xblackcat.frozenidea.config.IceFrameworkConfigurable;
import org.xblackcat.frozenidea.util.IceChecker;
import org.xblackcat.frozenidea.util.SliceBundle;

import java.io.File;

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

    private class IceSettingsChecker implements Runnable {
        private final Project project;

        public IceSettingsChecker(Project project) {
            this.project = project;
        }

        @Override
        public void run() {
            final FrozenIdea plugin = project.getService(FrozenIdea.class);

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
                    PsiClass iceClass = facade.findClass("com.zeroc.Ice.Object", allLibraryScope);

                    if (iceClass == null) {
                        // No class found!
                        final Notification notification = new Notification(
                                "Ice Facet Notification",
                                SliceBundle.message("ICE.library.not.added"),
                                SliceBundle.message("ICE.library.not.added.message"),
                                NotificationType.ERROR
                        );
                        Notifications.Bus.notify(notification, project);
                    }

                    return;
                }
            }

            final Notification notification = new Notification(
                    "Ice Facet Notification",
                    SliceBundle.message("ICE.not.configured"),
                    SliceBundle.message("ICE.not.configured.message"),
                    NotificationType.ERROR
            );
            notification.addAction(new NotificationAction(SliceBundle.message("ICE.not.configured.action")) {
                @Override
                public void actionPerformed(@NotNull AnActionEvent e, @NotNull Notification notification) {
                    notification.expire();

                    IceFrameworkConfigurable configurable = new IceFrameworkConfigurable(project);
                    ShowSettingsUtil.getInstance().editConfigurable(project, configurable);
                }
            });
            Notifications.Bus.notify(notification, project);
        }
    }
}
