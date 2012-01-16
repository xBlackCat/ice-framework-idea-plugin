package org.xblackcat.frozenice.processor;

import com.intellij.compiler.CompilerConfiguration;
import com.intellij.compiler.impl.CompilerUtil;
import com.intellij.facet.FacetManager;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.compiler.*;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.io.StreamUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.FrozenIdea;
import org.xblackcat.frozenice.IceFileType;
import org.xblackcat.frozenice.config.IceConfig;
import org.xblackcat.frozenice.facet.IceFacet;
import org.xblackcat.frozenice.facet.IceFacetConfiguration;
import org.xblackcat.frozenice.util.Constants;

import java.io.DataInput;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

            Collection<File> folders = new ArrayList<File>();
            for (GenerationItem item : generationItems) {
                VirtualFile outputDir = getOutputDir((IceGenerationItem) item);
                if (outputDir != null) {
                    folders.add(VfsUtil.virtualToIoFile(outputDir));
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
                IceFacetConfiguration facetConfiguration = iceFacet.getConfiguration();
                if (facetConfiguration.getConfig().getOutputDir() == null) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public ValidityState createValidityState(DataInput in) throws IOException {
        return TimestampValidityState.load(in);
    }

    private final class PrepareAction implements Computable<GenerationItem[]> {
        private CompileContext context;

        private PrepareAction(CompileContext context) {
            this.context = context;
        }

        @Override
        public GenerationItem[] compute() {
            ProjectFileIndex fileIndex = ProjectRootManager.getInstance(context.getProject()).getFileIndex();
            final CompileScope compileScope = context.getCompileScope();

            VirtualFile[] files = compileScope.getFiles(IceFileType.INSTANCE, false);
            List<GenerationItem> items = new ArrayList<GenerationItem>(files.length);
            CompilerConfiguration compilerConfiguration = CompilerConfiguration.getInstance(context.getProject());


            for (VirtualFile file : files) {
                if (context.isMake() && compilerConfiguration.isExcludedFromCompilation(file)) {
                    continue;
                }

                IceGenerationItem generationItem = new IceGenerationItem(context.getModuleByFile(file), file, fileIndex.isInTestSourceContent(file));
                items.add(generationItem);
            }

            return items.toArray(new GenerationItem[items.size()]);
        }
    }

    private class GenerateAction implements Computable<GenerationItem[]> {
        private final CompileContext context;
        private final GenerationItem[] items;
        private final VirtualFile outputRootDirectory;
        private final Sdk projectSdk;

        public GenerateAction(CompileContext context, GenerationItem[] items, VirtualFile outputRootDirectory, Sdk projectSdk) {
            this.context = context;
            this.items = items;
            this.outputRootDirectory = outputRootDirectory;
            this.projectSdk = projectSdk;
        }

        @Override
        public GenerationItem[] compute() {
            Project project = context.getProject();

            VirtualFile baseDir = project.getBaseDir();
            File projectBaseDir = baseDir != null ? VfsUtil.virtualToIoFile(baseDir) : null;

            FrozenIdea plugin = ServiceManager.getService(project, FrozenIdea.class);
            IceConfig config = plugin.getConfig();

            final VirtualFile binFolder = config.getFrameworkHome().findChild("bin");
            if (binFolder == null) {
                context.addMessage(CompilerMessageCategory.ERROR, "Facet home is invalid", null, -1, -1);
                return NO_ITEMS;
            }
            VirtualFile javaTranslator = binFolder.findChild(Constants.JAVA_TRANSLATOR_NAME);
            if (javaTranslator == null) {
                context.addMessage(CompilerMessageCategory.ERROR, "Facet home is invalid", null, -1, -1);
                return NO_ITEMS;
            }

            File javaTranslatorFile = VfsUtil.virtualToIoFile(javaTranslator);

            List<GenerationItem> results = new ArrayList<GenerationItem>(items.length);
            for (GenerationItem item : items) {
                IceGenerationItem iceItem = (IceGenerationItem) item;
                VirtualFile source = iceItem.getSource();

                if (source != null && source.isValid()) {
                    VirtualFile outputDir = getOutputDir(iceItem);

                    if (outputDir == null) {
                        context.addMessage(CompilerMessageCategory.ERROR, "Facet output folder is not specified", iceItem.getSource().getUrl(), -1, -1);
                        continue;
                    }

                    File realOutputDir = VfsUtil.virtualToIoFile(outputDir);
                    File sourceFile = VfsUtil.virtualToIoFile(iceItem.getSource());

                    try {
                        String sourceFileRelative = FileUtil.getRelativePath(projectBaseDir, realOutputDir);
                        String[] command = new String[]{
                                '"' + javaTranslatorFile.getAbsolutePath() + '"',
                                "--output-dir",
                                '"' + sourceFileRelative + '"',
                                '"' + FileUtil.getRelativePath(projectBaseDir, sourceFile) + '"',
                        };

                        Process process = new ProcessBuilder()
                                .directory(projectBaseDir)
                                .command(command)
                                .redirectErrorStream(true)
                                .start();

                        InputStream out = process.getInputStream();
                        try {
                            String result = StreamUtil.readText(out);
                            int code = 0;

                            try {
                                code = process.waitFor();
                            } catch (InterruptedException e) {
                                context.addMessage(CompilerMessageCategory.WARNING, "Translator " + Constants.JAVA_TRANSLATOR_NAME + " was interrupted", null, -1, -1);
                            }
                            context.addMessage(CompilerMessageCategory.INFORMATION, result, null, -1, -1);
                            if (code != 0) {
                                context.addMessage(CompilerMessageCategory.ERROR, "Failed to translate file " + sourceFileRelative + ". Process returns error code " + code, null, -1, -1);
                            } else {
                                results.add(iceItem);
                            }
                        } finally {
                            out.close();
                        }

                    } catch (IOException e) {
                        context.addMessage(CompilerMessageCategory.ERROR, e.getMessage(), source.getUrl(), -1, -1);
                    }
                }
            }

            return results.toArray(new GenerationItem[results.size()]);
        }
    }

    private static VirtualFile getOutputDir(IceGenerationItem iceItem) {
        IceFacet iceFacet = FacetManager.getInstance(iceItem.getModule()).getFacetByType(IceFacet.ID);

        IceFacetConfiguration.Config facetConfig = iceFacet.getConfiguration().getConfig();
        return facetConfig.getOutputDir();
    }
}
