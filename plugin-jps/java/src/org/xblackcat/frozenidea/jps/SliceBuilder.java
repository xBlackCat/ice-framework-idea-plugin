package org.xblackcat.frozenidea.jps;

import com.intellij.execution.process.BaseOSProcessHandler;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import org.jdom.Document;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.ModuleChunk;
import org.jetbrains.jps.builders.DirtyFilesHolder;
import org.jetbrains.jps.builders.FileProcessor;
import org.jetbrains.jps.builders.java.JavaSourceRootDescriptor;
import org.jetbrains.jps.incremental.*;
import org.jetbrains.jps.incremental.messages.BuildMessage;
import org.jetbrains.jps.incremental.messages.CompilerMessage;
import org.jetbrains.jps.incremental.messages.FileGeneratedEvent;
import org.jetbrains.jps.model.module.JpsModule;
import org.jetbrains.jps.model.module.JpsModuleSourceRoot;
import org.xblackcat.frozenidea.config.IceComponent;
import org.xblackcat.frozenidea.config.IceConfig;
import org.xblackcat.frozenidea.config.SliceCompilerSettings;
import org.xblackcat.frozenidea.config.Target;
import org.xblackcat.frozenidea.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 10.09.13 17:01
 *
 * @author xBlackCat
 */
public class SliceBuilder extends ModuleLevelBuilder {
    private static final String BUILDER_NAME = "SliceTranslator";

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

            List<Target> translators = facetConfig.getConfiguredComponents();

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
            for (Target c : translators) {
                final File outputDir = c.getOutputFile();
                compileFiles(context, frameworkHome, target, sourceFiles, c.getComponent(), outputDir);
            }
        }

        return ExitCode.OK;
    }

    private void compileFiles(
            final CompileContext context,
            File frameworkHome,
            ModuleBuildTarget buildTarget,
            List<File> sourceFiles,
            IceComponent target, File outputDir
    ) throws StopBuildException {
        final JpsModule module = buildTarget.getModule();

        final String translatorName = target.getTranslatorName();
        if (outputDir == null) {
            context.processMessage(
                    new CompilerMessage(
                            getPresentableName(),
                            BuildMessage.Kind.WARNING,
                            "Output folder is not specified for " +
                                    translatorName +
                                    " in module " +
                                    module.getName() +
                                    ". Check facet configuration."
                    )
            );

            return;
        }

        List<String> command = new ArrayList<String>();
        command.add(target.getTranslatorPath(frameworkHome).getAbsolutePath());
        command.add("--list-generated");
        command.add("--output-dir");
        final String outputDirPath = outputDir.getAbsolutePath();
        command.add(outputDirPath);
        command.add("-I" + new File(frameworkHome, "slice").getAbsolutePath());
        for (JpsModuleSourceRoot contentRoot : module.getSourceRoots()) {
            command.add("-I" + contentRoot.getFile().getAbsolutePath());
        }
        for (File source : sourceFiles) {
            command.add(source.getAbsolutePath());
        }

        try {
            Process process = new ProcessBuilder()
                    .command(command)
                    .start();


            BaseOSProcessHandler handler = new BaseOSProcessHandler(process, StringUtil.join(command, " "), CharsetToolkit.UTF8_CHARSET);
            final AtomicBoolean hasErrors = new AtomicBoolean();
            handler.addProcessListener(
                    new ProcessAdapter() {
                        final StringBuilder errorOutput = new StringBuilder();
                        final StringBuilder stdOutput = new StringBuilder();

                        @Override
                        public void onTextAvailable(ProcessEvent event, Key outputType) {
                            if (outputType == ProcessOutputTypes.STDERR) {
                                errorOutput.append(event.getText());
                            } else if (outputType == ProcessOutputTypes.STDOUT) {
                                stdOutput.append(event.getText());
                            }
                        }

                        @Override
                        public void processTerminated(ProcessEvent event) {
                            Document res;
                            final String stdout = stdOutput.toString();
                            try {
                                res = JDOMUtil.loadDocument(stdout);
                            } catch (Exception e) {
                                context.processMessage(
                                        new CompilerMessage(
                                                BUILDER_NAME,
                                                BuildMessage.Kind.ERROR,
                                                "Can't process compiler output: " + stdout
                                        )
                                );
                                hasErrors.set(true);
                                return;
                            }


                            int exitCode = event.getExitCode();
                            if (exitCode != 0) {
                                for (Element source : res.getRootElement().getChildren("source")) {
                                    final Element output = source.getChild("output");
                                    if (output != null) {
                                        String message = output.getTextTrim();

                                        for (String line : message.split("\n")) {
                                            int separatorIndex = line.indexOf(": ");
                                            final String path;
                                            final long lineNumber;
                                            if (separatorIndex <= 0) {
                                                path = null;
                                                lineNumber = -1L;
                                            } else {
                                                int lineSep = line.lastIndexOf(':', separatorIndex - 1);
                                                if (lineSep == -1) {
                                                    path = null;
                                                    lineNumber = -1L;
                                                } else {
                                                    path = line.substring(0, lineSep);
                                                    long l;
                                                    try {
                                                        l = Long.parseLong(line.substring(lineSep + 1, separatorIndex));
                                                    } catch (NumberFormatException e) {
                                                        l = -1L;
                                                    }
                                                    lineNumber = l;
                                                }
                                            }

                                            context.processMessage(
                                                    new CompilerMessage(
                                                            BUILDER_NAME,
                                                            BuildMessage.Kind.ERROR,
                                                            line,
                                                            path,
                                                            -1L,
                                                            -1L,
                                                            -1L,
                                                            lineNumber,
                                                            -1L
                                                    )
                                            );
                                        }
                                    }
                                }

                                final String stdErr = errorOutput.toString();
                                if (stdErr.length() > 0) {
                                    context.processMessage(new CompilerMessage(BUILDER_NAME, BuildMessage.Kind.ERROR, stdErr));
                                }
                                context.processMessage(
                                        new CompilerMessage(
                                                BUILDER_NAME,
                                                BuildMessage.Kind.ERROR,
                                                "translator '" + translatorName + "' for '" + module.getName() +
                                                        "' finished with exit code " + exitCode
                                        )
                                );
                                hasErrors.set(true);
                            } else {
                                final FileGeneratedEvent msg = new FileGeneratedEvent();

                                for (Element source : res.getRootElement().getChildren("source")) {
                                    for (Element file : source.getChildren("file")) {
                                        final String fileName = file.getAttributeValue("name");

                                        if (fileName.startsWith(outputDirPath)) {
                                            msg.add(outputDirPath, fileName.substring(outputDirPath.length() + 1));
                                        }
                                    }
                                }

                                context.processMessage(msg);

                            }
                        }
                    }
            );
            handler.startNotify();
            handler.waitFor();
            if (hasErrors.get()) {
                throw new StopBuildException();
            }

        } catch (IOException e) {
            context.processMessage(
                    new CompilerMessage(
                            getPresentableName(),
                            BuildMessage.Kind.ERROR,
                            "Failed to translate files with " +
                                    translatorName +
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
        final Map<ModuleBuildTarget, List<File>> toCompile = new HashMap<ModuleBuildTarget, List<File>>();
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
                                files = new ArrayList<File>();
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
