package org.xblackcat.frozenice.processor;

import com.intellij.compiler.impl.CompilerUtil;
import com.intellij.facet.FacetManager;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.compiler.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.facet.Config;
import org.xblackcat.frozenice.facet.IceFacet;
import org.xblackcat.frozenice.util.IceComponent;
import org.xblackcat.frozenice.util.IceErrorMessages;

import java.io.DataInput;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 16.01.12 10:02
 *
 * @author xBlackCat
 */
public class Slice2Xxx implements SourceGeneratingCompiler {
    public static final GenerationItem[] NO_ITEMS = new GenerationItem[0];

    @Override
    public VirtualFile getPresentableFile(CompileContext context, Module module, VirtualFile outputRoot, VirtualFile generatedFile) {
        return null;
    }

    @Override
    public GenerationItem[] getGenerationItems(CompileContext context) {
        Module[] affectedModules = context.getCompileScope().getAffectedModules();
        if (affectedModules.length > 0) {
            Application application = ApplicationManager.getApplication();
            return application.runReadAction(new PrepareAction(context));
        }

        return NO_ITEMS;
    }

    @Override
    public GenerationItem[] generate(CompileContext context, GenerationItem[] items, VirtualFile outputRootDirectory) {
        if (items != null && items.length > 0) {
            Application application = ApplicationManager.getApplication();
            final Project project = context.getProject();
            final Sdk projectSdk = ProjectRootManager.getInstance(project).getProjectSdk();

            GenerationItem[] generationItems = application.runReadAction(new GenerateAction(context, items, outputRootDirectory, projectSdk));

            Set<Module> processedModules = new HashSet<Module>();

            Collection<File> folders = new ArrayList<File>();
            for (GenerationItem item : generationItems) {
                Module module = item.getModule();
                if (processedModules.contains(module)) {
                    continue;
                }

                processedModules.add(module);

                IceFacet iceFacet = FacetManager.getInstance(module).getFacetByType(IceFacet.ID);

                assert iceFacet != null;
                Config facetConfig = iceFacet.getConfiguration().getConfig();

                for (IceComponent c : facetConfig.getConfiguredComponents()) {
                    VirtualFile dir = facetConfig.getOutputDir(c);
                    if (dir != null) {
                        folders.add(VfsUtil.virtualToIoFile(dir));
                    }
                }

            }
            CompilerUtil.refreshIODirectories(folders);

            return generationItems;
        }
        return NO_ITEMS;
    }

    @NotNull
    @Override
    public String getDescription() {
        return "slice";
    }

    @Override
    public boolean validateConfiguration(CompileScope scope) {
        final Module[] modules = scope.getAffectedModules();

        for (Module m : modules) {
            final IceFacet iceFacet = FacetManager.getInstance(m).getFacetByType(IceFacet.ID);

            if (iceFacet != null) {
                Config facetConfiguration = iceFacet.getConfiguration().getConfig();
                Set<IceComponent> componentSet = facetConfiguration.getConfiguredComponents();
                if (!componentSet.isEmpty()) {
                    // Check paths for validness
                    for (IceComponent ic : componentSet) {
                        VirtualFile outputDir = facetConfiguration.getOutputDir(ic);

                        if (outputDir == null || !outputDir.isDirectory() || !outputDir.isValid()) {
                            Notification notification = new Notification(
                                    "Ice Facet",
                                    IceErrorMessages.message("ICE.output.folder.invalid.title"),
                                    IceErrorMessages.message("ICE.output.folder.invalid", ic.name()),
                                    NotificationType.WARNING
                            );
                            Notifications.Bus.notify(notification, m.getProject());
                        }
                    }
                }
            }
        }

        return true;
    }

    @Override
    public ValidityState createValidityState(DataInput in) throws IOException {
        return TimestampValidityState.load(in);
    }

}
