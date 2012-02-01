package org.xblackcat.frozenice.facet;

import com.intellij.facet.FacetManager;
import com.intellij.ide.util.frameworkSupport.FrameworkSupportConfigurable;
import com.intellij.ide.util.frameworkSupport.FrameworkSupportModel;
import com.intellij.ide.util.frameworkSupport.FrameworkSupportProvider;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.UnknownModuleType;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.config.IceFrameworkSupportConfigurable;
import org.xblackcat.frozenice.util.IceErrorMessages;
import org.xblackcat.frozenice.util.Icons;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;

/**
 * 08.01.12 12:58
 *
 * @author xBlackCat
 */
public class IceFacetSupportProvider extends FrameworkSupportProvider {
    protected IceFacetSupportProvider() {
        super("Slice", "ICE");
    }

    @NotNull
    @Override
    public FrameworkSupportConfigurable createConfigurable(@NotNull FrameworkSupportModel model) {
        return new IceFrameworkSupportConfigurable(model.getProject());
    }

    @Override
    public boolean isEnabledForModuleType(@NotNull ModuleType moduleType) {
        return !(moduleType instanceof UnknownModuleType);
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
                    IceErrorMessages.message("ICE.library.not.added"),
                    IceErrorMessages.message("ICE.library.not.added.message"),
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

    @Override
    public Icon getIcon() {
        return Icons.FACET_ICON;
    }
}
