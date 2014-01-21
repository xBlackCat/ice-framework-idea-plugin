package org.xblackcat.frozenice.processor;

import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompilerMessageCategory;
import com.intellij.openapi.compiler.GeneratingCompiler;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.io.StreamUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.xblackcat.frozenice.FrozenIdea;
import org.xblackcat.frozenice.config.IceComponent;
import org.xblackcat.frozenice.config.IceConfig;
import org.xblackcat.frozenice.config.SliceCompilerSettings;
import org.xblackcat.frozenice.facet.IceFacet;
import org.xblackcat.frozenice.util.IceChecker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 08.02.12 10:47
 *
 * @author xBlackCat
 */
class GenerateAction implements Computable<GeneratingCompiler.GenerationItem[]> {
    private final CompileContext context;
    private final GeneratingCompiler.GenerationItem[] items;
    private final VirtualFile outputRootDirectory;
    private final Sdk projectSdk;

    public GenerateAction(
            CompileContext context,
            GeneratingCompiler.GenerationItem[] items,
            VirtualFile outputRootDirectory,
            Sdk projectSdk
    ) {
        this.context = context;
        this.items = items;
        this.outputRootDirectory = outputRootDirectory;
        this.projectSdk = projectSdk;
    }

    @Override
    public GeneratingCompiler.GenerationItem[] compute() {
        Project project = context.getProject();

        VirtualFile baseDir = project.getBaseDir();
        File projectBaseDir = baseDir != null ? VfsUtil.virtualToIoFile(baseDir) : null;

        FrozenIdea plugin = ServiceManager.getService(project, FrozenIdea.class);
        IceConfig pluginConfig = plugin.getConfig();

        VirtualFile frameworkHome = pluginConfig.getFrameworkHome();

        Set<IceComponent> installedComponents = IceChecker.getInstalledComponents(VfsUtil.virtualToIoFile(frameworkHome));

        if (installedComponents.isEmpty()) {
            context.addMessage(CompilerMessageCategory.ERROR, "Facet home is invalid", null, -1, -1);
            return Slice2Xxx.NO_ITEMS;
        }

        List<GeneratingCompiler.GenerationItem> results = new ArrayList<GeneratingCompiler.GenerationItem>(items.length);

        Map<IceFacet, List<IceGenerationItem>> group = groupItems(items);

        for (Map.Entry<IceFacet, List<IceGenerationItem>> entry : group.entrySet()) {
            // Prepare translator lists

            IceFacet facet = entry.getKey();
            SliceCompilerSettings facetConfig = facet.getConfiguration().getConfig();
            if (!facetConfig.isValid()) {
                context.addMessage(
                        CompilerMessageCategory.WARNING,
                        "IceFacet is not configured for module " + facet.getModule().getName() + ".",
                        null,
                        -1,
                        -1
                );

                continue;
            }

            List<IceGenerationItem> itemsToProcess = entry.getValue();

            Set<IceComponent> translators = facetConfig.getConfiguredComponents();
            translators.retainAll(installedComponents);

            if (translators.isEmpty()) {
                context.addMessage(
                        CompilerMessageCategory.WARNING,
                        "No valid translators found for module " +
                                facet.getModule().getName() +
                                ". Check facet configuration.",
                        null,
                        -1,
                        -1
                );

                continue;
            }

            List<File> sourceFiles = new ArrayList<File>();

            // Prepare files list to process:
            for (IceGenerationItem item : itemsToProcess) {
                VirtualFile source = item.getSource();

                if (source == null || !source.exists() || !source.isValid()) {
                    continue;
                }

                sourceFiles.add(VfsUtil.virtualToIoFile(source));
            }

            if (sourceFiles.isEmpty()) {
                context.addMessage(
                        CompilerMessageCategory.WARNING,
                        "No files found to translate in module " +
                                facet.getModule().getName() +
                                ". Check facet configuration.",
                        null,
                        -1,
                        -1
                );

                continue;
            }

            // Translate files

            for (IceComponent c : translators) {
                VirtualFile outputDir = facetConfig.getOutputDirFile(c);

                if (outputDir == null) {
                    context.addMessage(
                            CompilerMessageCategory.WARNING,
                            "Output folder is not specified for " +
                                    c.getTranslatorName() +
                                    " in module " +
                                    facet.getModule().getName() +
                                    ". Check facet configuration.",
                            null,
                            -1,
                            -1
                    );

                    continue;
                }

                ModuleRootManager rootManager = ModuleRootManager.getInstance(facet.getModule());

                File realOutputDir = VfsUtil.virtualToIoFile(outputDir);

                List<String> command = new ArrayList<String>();
                command.add(c.getTranslatorPath(VfsUtil.virtualToIoFile(frameworkHome)).getAbsolutePath());
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
                        String result = StreamUtil.readText(out, "utf-8");
                        int code = 0;

                        try {
                            code = process.waitFor();
                        } catch (InterruptedException e) {
                            context.addMessage(
                                    CompilerMessageCategory.WARNING,
                                    "Translator " + c.getTranslatorName() + " was interrupted",
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
                                            c.getTranslatorName() +
                                            ". Process returns error code " +
                                            code,
                                    null,
                                    -1,
                                    -1
                            );
                        } else {
                            results.addAll(itemsToProcess);
                        }
                    }
                } catch (IOException e) {
                    context.addMessage(
                            CompilerMessageCategory.ERROR,
                            "Failed to translate files with " +
                                    c.getTranslatorName() +
                                    ". Error: " +
                                    e.getMessage(),
                            null,
                            -1,
                            -1
                    );
                }
            }
        }

        return results.toArray(new GeneratingCompiler.GenerationItem[results.size()]);
    }

    private static Map<IceFacet, List<IceGenerationItem>> groupItems(GeneratingCompiler.GenerationItem[] items) {
        HashMap<IceFacet, List<IceGenerationItem>> group = new HashMap<IceFacet, List<IceGenerationItem>>();

        for (GeneratingCompiler.GenerationItem item : items) {
            if (item instanceof IceGenerationItem) {
                IceGenerationItem i = (IceGenerationItem) item;
                List<IceGenerationItem> list = group.get(i.getFacet());

                if (list == null) {
                    list = new ArrayList<IceGenerationItem>();
                    group.put(i.getFacet(), list);
                }

                list.add(i);
            }
        }

        return group;
    }
}
