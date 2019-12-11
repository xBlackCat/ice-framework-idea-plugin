package org.xblackcat.frozenidea.jps;

import com.intellij.execution.process.BaseOSProcessHandler;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.ModuleChunk;
import org.jetbrains.jps.builders.DirtyFilesHolder;
import org.jetbrains.jps.builders.java.JavaSourceRootDescriptor;
import org.jetbrains.jps.incremental.*;
import org.jetbrains.jps.incremental.messages.BuildMessage;
import org.jetbrains.jps.incremental.messages.CompilerMessage;
import org.jetbrains.jps.model.module.JpsModule;
import org.jetbrains.jps.util.JpsPathUtil;
import org.xblackcat.frozenidea.config.IceComponent;
import org.xblackcat.frozenidea.config.IceConfig;
import org.xblackcat.frozenidea.config.SliceCompilerSettings;
import org.xblackcat.frozenidea.config.Target;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 10.09.13 17:01
 *
 * @author xBlackCat
 */
public class SliceBuilder extends ModuleLevelBuilder {
    static final String BUILDER_NAME = "SliceTranslator";

    protected SliceBuilder() {
        super(BuilderCategory.SOURCE_GENERATOR);
    }

    @Override
    public List<String> getCompilableFileExtensions() {
        return Arrays.asList("ice", "slice");
    }

    @Override
    public ExitCode build(
            CompileContext context,
            ModuleChunk chunk,
            DirtyFilesHolder<JavaSourceRootDescriptor, ModuleBuildTarget> dirtyFilesHolder,
            OutputConsumer outputConsumer
    ) throws ProjectBuildException, IOException {
        final IceConfig config = context.getProjectDescriptor().getProject().getContainer().getChild(IceConfig.ROLE);
        IceConfig iceConfig = config == null ? new IceConfig("") : config;

        File frameworkHome = JpsPathUtil.urlToFile(iceConfig.getFrameworkHomeUrl());
        List<String> includes = new ArrayList<>();
        for (String include : iceConfig.getIncludeUrls()) {
            includes.add(JpsPathUtil.urlToFile(include).getAbsolutePath());
        }

        final Map<ModuleBuildTarget, List<File>> toCompile = collectChangedFiles(context, dirtyFilesHolder);
        if (toCompile.isEmpty()) {
            return ExitCode.NOTHING_DONE;
        }

        for (Map.Entry<ModuleBuildTarget, List<File>> e : toCompile.entrySet()) {
            final ModuleBuildTarget target = e.getKey();
            final JpsModule module = target.getModule();
            SliceCompilerSettings settings = module.getContainer().getChild(SliceCompilerSettings.ROLE);
            final SliceCompilerSettings facetConfig = settings == null ? new SliceCompilerSettings() : settings;
            final List<File> sourceFiles = e.getValue();

            List<Target> translators = facetConfig.getComponents();

            if (translators.isEmpty()) {
                context.processMessage(
                        new CompilerMessage(
                                getPresentableName(), BuildMessage.Kind.WARNING,
                                "No valid translators found for module " + module.getName() + ". Check facet configuration."
                        )
                );

                continue;
            }

            // Translate files
            for (Target c : translators) {
                final File outputDir = c.getOutputFile();

                if (!outputDir.isDirectory() && !outputDir.mkdirs()) {
                    context.processMessage(
                            new CompilerMessage(
                                    getPresentableName(),
                                    BuildMessage.Kind.ERROR,
                                    "Failed to create output directory " + outputDir.getAbsolutePath() + " for file output in module " + module.getName()
                            )
                    );
                    return ExitCode.ABORT;
                }


                if (facetConfig.isCleanOutput()) {
                    try {
                        FileUtils.cleanDirectory(outputDir);
                    } catch (IOException ex) {
                        context.processMessage(
                                new CompilerMessage(
                                        getPresentableName(), BuildMessage.Kind.ERROR,
                                        "Failed to empty target directory: " + outputDir.getPath() + " . Error: " + ex.getMessage()
                                )
                        );
                        return ExitCode.ABORT;
                    }
                }

                compileFiles(context, frameworkHome, includes, target, sourceFiles, c.getComponent(), outputDir);
            }
        }

        return ExitCode.OK;
    }

    private void compileFiles(
            final CompileContext context,
            File frameworkHome,
            List<String> includes,
            ModuleBuildTarget buildTarget,
            List<File> sourceFiles,
            IceComponent target,
            File outputDir
    ) throws StopBuildException {
        final JpsModule module = buildTarget.getModule();

        final String translatorName = target.getTranslatorName();
        if (outputDir == null) {
            context.processMessage(
                    new CompilerMessage(
                            getPresentableName(), BuildMessage.Kind.WARNING,
                            "Output folder is not specified for " + translatorName + " in module " + module.getName() +
                                    ". Check facet configuration."
                    )
            );

            return;
        }

        List<String> command = new ArrayList<>();
        command.add(target.getTranslatorPath(frameworkHome).getAbsolutePath());
        if (target == IceComponent.Java) {
            command.add("--list-generated");
        }
        command.add("--output-dir");
        final String outputDirPath = outputDir.getAbsolutePath();
        command.add(outputDirPath);
        includes.stream().map(include -> "-I" + include).forEachOrdered(command::add);

        module.getSourceRoots().stream().map(contentRoot -> "-I" + contentRoot.getFile().getAbsolutePath()).forEachOrdered(command::add);

        sourceFiles.stream().map(File::getAbsolutePath).forEachOrdered(command::add);

        try {
            Process process = new ProcessBuilder()
                    .command(command)
                    .start();


            BaseOSProcessHandler handler = new BaseOSProcessHandler(process, StringUtil.join(command, " "), CharsetToolkit.UTF8_CHARSET);
            final ATranslatorProcessAdapter processAdapter;
            if (target == IceComponent.Java) {
                processAdapter = new JavaProcessAdapter(context, target, module, outputDirPath);
            } else {
                processAdapter = new SliceProcessAdapter(context, target, module, outputDirPath, sourceFiles);
            }
            handler.addProcessListener(processAdapter);
            handler.startNotify();
            handler.waitFor();
            if (processAdapter.hasErrors()) {
                throw new StopBuildException();
            }

        } catch (IOException e) {
            context.processMessage(
                    new CompilerMessage(
                            getPresentableName(),
                            BuildMessage.Kind.ERROR,
                            "Failed to translate files with " + translatorName + ". Error: " + e.getMessage()
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
                (target, file, sourceRoot) -> {
                    final String fileName = file.getName();

                    if (fileName.endsWith(".ice") || fileName.endsWith(".slice")) {
                        List<File> files = toCompile.get(target);
                        if (files == null) {
                            files = new ArrayList<>();
                            toCompile.put(target, files);
                        }
                        files.add(file);
                    }
                    return true;
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
