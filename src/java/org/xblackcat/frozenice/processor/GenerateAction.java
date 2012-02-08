package org.xblackcat.frozenice.processor;

import com.intellij.facet.FacetManager;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompilerMessageCategory;
import com.intellij.openapi.compiler.GeneratingCompiler;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.io.StreamUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.xblackcat.frozenice.FrozenIdea;
import org.xblackcat.frozenice.config.IceConfig;
import org.xblackcat.frozenice.facet.Config;
import org.xblackcat.frozenice.facet.IceFacet;
import org.xblackcat.frozenice.util.Constants;
import org.xblackcat.frozenice.util.IceComponent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

    public GenerateAction(CompileContext context, GeneratingCompiler.GenerationItem[] items, VirtualFile outputRootDirectory, Sdk projectSdk) {
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

        final VirtualFile binFolder = pluginConfig.getFrameworkHome().findChild("bin");
        if (binFolder == null) {
            context.addMessage(CompilerMessageCategory.ERROR, "Facet home is invalid", null, -1, -1);
            return Slice2Xxx.NO_ITEMS;
        }
        VirtualFile javaTranslator = binFolder.findChild(Constants.JAVA_TRANSLATOR_NAME);
        if (javaTranslator == null) {
            context.addMessage(CompilerMessageCategory.ERROR, "Facet home is invalid", null, -1, -1);
            return Slice2Xxx.NO_ITEMS;
        }

        File javaTranslatorFile = VfsUtil.virtualToIoFile(javaTranslator);

        List<GeneratingCompiler.GenerationItem> results = new ArrayList<GeneratingCompiler.GenerationItem>(items.length);
        for (GeneratingCompiler.GenerationItem item : items) {
            IceGenerationItem iceItem = (IceGenerationItem) item;
            VirtualFile source = iceItem.getSource();

            if (source != null && source.isValid()) {
                final IceFacet iceFacet = FacetManager.getInstance(iceItem.getModule()).getFacetByType(IceFacet.ID);

                if (iceFacet == null) {
                    continue;
                }

                Config facetConfig = iceFacet.getConfiguration().getConfig();
                if (!facetConfig.isValid()) {
                    context.addMessage(CompilerMessageCategory.ERROR, "Facet output folder is not specified", iceItem.getSource().getUrl(), -1, -1);
                    continue;
                }

                for (IceComponent c : facetConfig.getConfiguredComponents()) {
                    VirtualFile outputDir = facetConfig.getOutputDir(c);

                    if (outputDir == null) {
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
        }

        return results.toArray(new GeneratingCompiler.GenerationItem[results.size()]);
    }
}
