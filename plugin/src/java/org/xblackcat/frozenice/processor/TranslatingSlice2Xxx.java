package org.xblackcat.frozenice.processor;

import com.intellij.compiler.CompilerConfiguration;
import com.intellij.compiler.impl.CompilerUtil;
import com.intellij.facet.FacetManager;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.CompilerMessageCategory;
import com.intellij.openapi.compiler.TranslatingCompiler;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.io.StreamUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.Chunk;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.FrozenIdea;
import org.xblackcat.frozenice.config.IceComponent;
import org.xblackcat.frozenice.config.IceConfig;
import org.xblackcat.frozenice.config.SliceCompilerSettings;
import org.xblackcat.frozenice.facet.IceFacet;
import org.xblackcat.frozenice.util.IceChecker;
import org.xblackcat.frozenice.util.IceErrorMessages;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 16.01.12 10:02
 *
 * @author xBlackCat
 */
public class TranslatingSlice2Xxx implements TranslatingCompiler {

    @Override
    public boolean isCompilableFile(VirtualFile file, CompileContext context) {
        return false;
    }

    @Override
    public void compile(CompileContext context, Chunk<Module> moduleChunk, VirtualFile[] files, OutputSink sink) {
        Module[] affectedModules = context.getCompileScope().getAffectedModules();
        if (affectedModules.length > 0) {
            CompilerConfiguration compilerConfiguration = CompilerConfiguration.getInstance(context.getProject());

            Collection<File> folders = new ArrayList<>();

            Project project = context.getProject();

            VirtualFile baseDir = project.getBaseDir();
            File projectBaseDir = baseDir != null ? VfsUtil.virtualToIoFile(baseDir) : null;

            FrozenIdea plugin = ServiceManager.getService(project, FrozenIdea.class);
            IceConfig pluginConfig = plugin.getConfig();

            File frameworkHome = VfsUtil.virtualToIoFile(pluginConfig.getFrameworkHome());

            Set<IceComponent> installedComponents = IceChecker.getInstalledComponents(frameworkHome);

            HashMap<IceFacet, List<VirtualFile>> filesByFacet = new HashMap<>();

            if (installedComponents.isEmpty()) {
                context.addMessage(CompilerMessageCategory.ERROR, "Facet home is invalid", null, -1, -1);
                return;
            }

            for (VirtualFile file : files) {
                if (context.isMake() && compilerConfiguration.isExcludedFromCompilation(file)) {
                    continue;
                }

                final Module moduleByFile = context.getModuleByFile(file);
                if (moduleByFile != null) {
                    IceFacet iceFacet = FacetManager.getInstance(moduleByFile).getFacetByType(IceFacet.ID);

                    SliceCompilerSettings facetConfig = iceFacet.getConfiguration().getConfig();
                    if (!facetConfig.isValid()) {
                        context.addMessage(
                                CompilerMessageCategory.WARNING,
                                "IceFacet is not configured for module " + iceFacet.getModule().getName() + ".",
                                null,
                                -1,
                                -1
                        );

                        continue;
                    }

                    if (iceFacet != null) {
                        List<VirtualFile> list = filesByFacet.get(iceFacet);

                        if (list == null) {
                            list = new ArrayList<>();
                            filesByFacet.put(iceFacet, list);
                        }

                        list.add(file);
                    }
                }
            }

            if (filesByFacet.size() > 0) {
                for (Map.Entry<IceFacet, List<VirtualFile>> item : filesByFacet.entrySet()) {
                    IceFacet iceFacet = item.getKey();

                    assert iceFacet != null;
                    SliceCompilerSettings facetConfig = iceFacet.getConfiguration().getConfig();

                    Set<IceComponent> translators = facetConfig.getConfiguredComponents();
                    translators.retainAll(installedComponents);

                    if (translators.isEmpty()) {
                        context.addMessage(
                                CompilerMessageCategory.WARNING,
                                "No valid translators found for module " +
                                        iceFacet.getModule().getName() +
                                        ". Check facet configuration.",
                                null,
                                -1,
                                -1
                        );

                        continue;
                    }

                    List<File> sourceFiles = new ArrayList<>();

                    // Prepare files list to process:
                    for (VirtualFile source : item.getValue()) {
                        if (source == null || !source.exists() || !source.isValid()) {
                            continue;
                        }

                        sourceFiles.add(VfsUtil.virtualToIoFile(source));
                    }

                    if (sourceFiles.isEmpty()) {
                        context.addMessage(
                                CompilerMessageCategory.WARNING,
                                "No files found to translate in module " +
                                        iceFacet.getModule().getName() +
                                        ". Check facet configuration.",
                                null,
                                -1,
                                -1
                        );

                        continue;
                    }

                    // Translate files

                    for (IceComponent c : translators) {
                        compileFiles(context, projectBaseDir, frameworkHome, iceFacet, sourceFiles, c);
                    }

                    for (IceComponent c : facetConfig.getConfiguredComponents()) {
                        VirtualFile dir = facetConfig.getOutputDirFile(c);
                        if (dir != null) {
                            folders.add(VfsUtil.virtualToIoFile(dir));
                        }
                    }

                }
            }

            if (!folders.isEmpty()) {
                CompilerUtil.refreshIODirectories(folders);
            }
        }
    }

    private void compileFiles(
            CompileContext context,
            File projectBaseDir,
            File frameworkHome,
            IceFacet iceFacet,
            List<File> sourceFiles,
            IceComponent target
    ) {
        VirtualFile outputDir = iceFacet.getConfiguration().getConfig().getOutputDirFile(target);

        if (outputDir == null) {
            context.addMessage(
                    CompilerMessageCategory.WARNING,
                    "Output folder is not specified for " +
                            target.getTranslatorName() +
                            " in module " +
                            iceFacet.getModule().getName() +
                            ". Check facet configuration.",
                    null,
                    -1,
                    -1
            );

            return;
        }

        ModuleRootManager rootManager = ModuleRootManager.getInstance(iceFacet.getModule());

        File realOutputDir = VfsUtil.virtualToIoFile(outputDir);

        List<String> command = new ArrayList<>();
        command.add(target.getTranslatorPath(frameworkHome).getAbsolutePath());
        command.add("--output-dir");
        command.add(FileUtil.getRelativePath(projectBaseDir, realOutputDir));
        for (VirtualFile contentRoot : rootManager.getSourceRoots(true)) {
            command.add("-I" + FileUtil.getRelativePath(projectBaseDir, VfsUtil.virtualToIoFile(contentRoot)));
        }
        for (VirtualFile contentRoot : rootManager.getContentRoots()) {
            command.add("-I" + FileUtil.getRelativePath(projectBaseDir, VfsUtil.virtualToIoFile(contentRoot)));
        }
        for (File source : sourceFiles) {
            command.add(FileUtil.getRelativePath(projectBaseDir, source));
        }

        try {
            Process process = new ProcessBuilder()
                    .directory(projectBaseDir)
                    .command(command)
                    .redirectErrorStream(true)
                    .start();

            try (InputStream out = process.getInputStream()) {
                String result = StreamUtil.readText(out, "UTF-8");
                int code = 0;

                try {
                    code = process.waitFor();
                } catch (InterruptedException e) {
                    context.addMessage(
                            CompilerMessageCategory.WARNING,
                            "Translator " + target.getTranslatorName() + " was interrupted",
                            null,
                            -1,
                            -1
                    );
                }
                context.addMessage(CompilerMessageCategory.INFORMATION, result, null, -1, -1);
                if (code != 0) {
                    context.addMessage(
                            CompilerMessageCategory.ERROR,
                            "Failed to translate files " +
                                    target.getTranslatorName() +
                                    ". Process returns error code " +
                                    code,
                            null,
                            -1,
                            -1
                    );
                }
            }
        } catch (IOException e) {
            context.addMessage(
                    CompilerMessageCategory.ERROR,
                    "Failed to translate files with " +
                            target.getTranslatorName() +
                            ". Error: " +
                            e.getMessage(),
                    null,
                    -1,
                    -1
            );
        }
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
                SliceCompilerSettings facetConfiguration = iceFacet.getConfiguration().getConfig();
                Set<IceComponent> componentSet = facetConfiguration.getConfiguredComponents();
                if (!componentSet.isEmpty()) {
                    // Check paths for validness
                    for (IceComponent ic : componentSet) {
                        VirtualFile outputDir = facetConfiguration.getOutputDirFile(ic);

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

}
