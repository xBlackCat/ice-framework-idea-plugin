package org.xblackcat.frozenidea.facet;

import com.intellij.facet.FacetManager;
import com.intellij.facet.ui.FacetBasedFrameworkSupportProvider;
import com.intellij.ide.util.frameworkSupport.FrameworkVersion;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.util.IceMessages;

import javax.swing.event.HyperlinkEvent;

/**
 * 08.01.12 12:58
 *
 * @author xBlackCat
 */
public class IceFacetSupportProvider extends FacetBasedFrameworkSupportProvider<IceFacet> {
    public IceFacetSupportProvider() {
        super(IceFacet.TYPE);
    }

    @Override
    protected void setupConfiguration(
            IceFacet iceFacet, ModifiableRootModel modifiableRootModel, FrameworkVersion frameworkVersion
    ) {

    }

    @Override
    public boolean isSupportAlreadyAdded(@NotNull Module module) {
        com.intellij.openapi.project.Project project = module.getProject();

        JavaPsiFacade facade = project.getComponent(JavaPsiFacade.class);

        PsiClass iceClass = facade.findClass("Ice.Object", module.getModuleWithDependenciesAndLibrariesScope(false));

        if (iceClass == null) {
            // No class found!
            final Notification notification = new Notification(
                    "Ice Facet",
                    IceMessages.message("ICE.library.not.added"),
                    IceMessages.message("ICE.library.not.added.message"),
                    NotificationType.ERROR,
                    new NotificationListener() {
                        @Override
                        public void hyperlinkUpdate(@NotNull Notification notification, @NotNull HyperlinkEvent event) {
                        }

                    }
            );
            Notifications.Bus.notify(notification, project);
        }

        return FacetManager.getInstance(module).getFacetByType(IceFacet.ID) != null;
    }
}
