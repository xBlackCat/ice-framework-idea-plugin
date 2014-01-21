package org.xblackcat.frozenice.processor;

import com.intellij.compiler.CompilerConfiguration;
import com.intellij.facet.FacetManager;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.GeneratingCompiler;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import org.xblackcat.frozenice.IceFileType;
import org.xblackcat.frozenice.facet.IceFacet;

import java.util.ArrayList;
import java.util.List;

/**
 * 08.02.12 10:47
 *
 * @author xBlackCat
 */
final class PrepareAction implements Computable<GeneratingCompiler.GenerationItem[]> {
    private CompileContext context;

    PrepareAction(CompileContext context) {
        this.context = context;
    }

    @Override
    public GeneratingCompiler.GenerationItem[] compute() {
        ProjectFileIndex fileIndex = ProjectRootManager.getInstance(context.getProject()).getFileIndex();
        final CompileScope compileScope = context.getCompileScope();

        VirtualFile[] files = compileScope.getFiles(IceFileType.INSTANCE, false);
        List<GeneratingCompiler.GenerationItem> items = new ArrayList<GeneratingCompiler.GenerationItem>(files.length);
        CompilerConfiguration compilerConfiguration = CompilerConfiguration.getInstance(context.getProject());


        for (VirtualFile file : files) {
            if (context.isMake() && compilerConfiguration.isExcludedFromCompilation(file)) {
                continue;
            }

            final Module moduleByFile = context.getModuleByFile(file);
            if (moduleByFile != null) {
                IceFacet iceFacet = FacetManager.getInstance(moduleByFile).getFacetByType(IceFacet.ID);

                if (iceFacet != null) {
                    IceGenerationItem generationItem = new IceGenerationItem(
                            iceFacet,
                            file,
                            fileIndex.isInTestSourceContent(file)
                    );
                    items.add(generationItem);
                }
            }
        }

        return items.toArray(new GeneratingCompiler.GenerationItem[items.size()]);
    }
}
