package org.xblackcat.frozenice.jps;

import com.intellij.openapi.util.io.StreamUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.ModuleChunk;
import org.jetbrains.jps.builders.DirtyFilesHolder;
import org.jetbrains.jps.builders.FileProcessor;
import org.jetbrains.jps.builders.java.JavaSourceRootDescriptor;
import org.jetbrains.jps.incremental.*;
import org.jetbrains.jps.incremental.messages.BuildMessage;
import org.jetbrains.jps.incremental.messages.CompilerMessage;
import org.jetbrains.jps.model.module.JpsModule;
import org.jetbrains.jps.model.module.JpsModuleSourceRoot;
import org.xblackcat.frozenice.config.IceComponent;
import org.xblackcat.frozenice.config.IceConfig;
import org.xblackcat.frozenice.config.SliceCompilerSettings;
import org.xblackcat.frozenice.util.Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 10.09.13 17:01
 *
 * @author xBlackCat
 */
public class SliceBuilder extends ModuleLevelBuilder {
    protected SliceBuilder() {
        super(BuilderCategory.SOURCE_GENERATOR);
    }

    @Override
    public ExitCode build(
            CompileContext context,
            ModuleChunk chunk,
            DirtyFilesHolder<JavaSourceRootDescriptor, ModuleBuildTarget> dirtyFilesHolder,
            OutputConsumer outputConsumer
    ) throws ProjectBuildException, IOException {
        IceConfig iceConfig = IceConfig.getSettings(context.getProjectDescriptor().getProject());

        File frameworkHome = Utils.ideaUrlToFile(iceConfig.getFrameworkHomeUrl());

        final Map<ModuleBuildTarget, List<File>> toCompile = collectChangedFiles(context, dirtyFilesHolder);
        if (toCompile.isEmpty()) {
            return ExitCode.NOTHING_DONE;
        }

        for (Map.Entry<ModuleBuildTarget, List<File>> e : toCompile.entrySet()) {
            final ModuleBuildTarget target = e.getKey();
            final JpsModule module = target.getModule();
            final SliceCompilerSettings facetConfig = SliceCompilerSettings.getSettings(module);
            final List<File> sourceFiles = e.getValue();

            Set<IceComponent> translators = facetConfig.getConfiguredComponents();

            if (translators.isEmpty()) {
                context.processMessage(
                        new CompilerMessage(
                                getPresentableName(),
                                BuildMessage.Kind.WARNING,
                                "No valid translators found for module " +
                                        module.getName() +
                                        ". Check facet configuration."
                        )
                );

                continue;
            }

            // Translate files

            for (IceComponent c : translators) {
                compileFiles(context, frameworkHome, target, sourceFiles, facetConfig, c);
            }
        }

        return ExitCode.OK;
    }

    private void compileFiles(
            CompileContext context,
            File frameworkHome,
            ModuleBuildTarget buildTarget,
            List<File> sourceFiles,
            SliceCompilerSettings facetConfig,
            IceComponent target
    ) {
        File outputDir = facetConfig.getOutputDir(target);
        final JpsModule module = buildTarget.getModule();

        if (outputDir == null) {
            context.processMessage(
                    new CompilerMessage(
                            getPresentableName(),
                            BuildMessage.Kind.WARNING,
                            "Output folder is not specified for " +
                                    target.getTranslatorName() +
                                    " in module " +
                                    module.getName() +
                                    ". Check facet configuration."
                    )
            );

            return;
        }

        List<String> command = new ArrayList<>();
        command.add(target.getTranslatorPath(frameworkHome).getAbsolutePath());
        command.add("--output-dir");
        command.add(outputDir.getAbsolutePath());
        for (JpsModuleSourceRoot contentRoot : module.getSourceRoots()) {
            command.add("-I" + contentRoot.getFile().getAbsolutePath());
        }
        for (File source : sourceFiles) {
            command.add(source.getAbsolutePath());
        }

        try {
            Process process = new ProcessBuilder()
                    .command(command)
                    .redirectErrorStream(true)
                    .start();

            try (InputStream out = process.getInputStream()) {
                String result = StreamUtil.readText(out, "UTF-8");
                int code = 0;

                try {
                    code = process.waitFor();
                } catch (InterruptedException e) {
                    context.processMessage(
                            new CompilerMessage(
                                    getPresentableName(),
                                    BuildMessage.Kind.WARNING,
                                    "Translator " + target.getTranslatorName() + " was interrupted"
                            )
                    );
                }
                context.processMessage(
                        new CompilerMessage(
                                getPresentableName(),
                                BuildMessage.Kind.INFO,
                                result
                        )
                );
                if (code != 0) {
                    context.processMessage(
                            new CompilerMessage(
                                    getPresentableName(),
                                    BuildMessage.Kind.ERROR,
                                    "Failed to translate files " +
                                            target.getTranslatorName() +
                                            ". Process returns error code " +
                                            code
                            )
                    );
                }
            }
        } catch (IOException e) {
            context.processMessage(
                    new CompilerMessage(
                            getPresentableName(),
                            BuildMessage.Kind.ERROR,
                            "Failed to translate files with " +
                                    target.getTranslatorName() +
                                    ". Error: " +
                                    e.getMessage()
                    )
            );
        }
    }

    private Map<ModuleBuildTarget, List<File>> collectChangedFiles(
            CompileContext context,
            DirtyFilesHolder<JavaSourceRootDescriptor, ModuleBuildTarget> dirtyFilesHolder
    ) throws IOException {
        final Map<ModuleBuildTarget, List<File>> toCompile = new HashMap<>();
        dirtyFilesHolder.processDirtyFiles(
                new FileProcessor<JavaSourceRootDescriptor, ModuleBuildTarget>() {
                    public boolean apply(
                            ModuleBuildTarget target,
                            File file,
                            JavaSourceRootDescriptor sourceRoot
                    ) throws IOException {
                        if (file.getName().endsWith(".ice")) {
                            List<File> files = toCompile.get(target);
                            if (files == null) {
                                files = new ArrayList<>();
                                toCompile.put(target, files);
                            }
                            files.add(file);
                        }
                        return true;
                    }
                }
        );
        return toCompile;
    }

    @NotNull
    @Override
    public String getPresentableName() {
        return "Slice compiler";
    }
}
