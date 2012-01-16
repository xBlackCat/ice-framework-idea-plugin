package org.xblackcat.frozenice;

import com.intellij.facet.FacetManager;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.SourceGeneratingCompiler;
import com.intellij.openapi.compiler.ValidityState;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.facet.IceFacet;
import org.xblackcat.frozenice.facet.IceFacetConfiguration;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 16.01.12 10:02
 *
 * @author xBlackCat
 */
public class Slice2Xxx implements SourceGeneratingCompiler {
    @Override
    public VirtualFile getPresentableFile(CompileContext context, Module module, VirtualFile outputRoot, VirtualFile generatedFile) {
        return null;
    }

    @Override
    public GenerationItem[] getGenerationItems(CompileContext context) {
        List<GenerationItem> res = new ArrayList<GenerationItem>();

        final Module[] modules = ModuleManager.getInstance(context.getProject()).getModules();
        for (Module m : modules) {
            final IceFacet iceFacet = FacetManager.getInstance(m).getFacetByType(IceFacet.ID);

            res.add(new GenerationItem() {
                @Override
                public String getPath() {
                    return null;
                }

                @Override
                public ValidityState getValidityState() {
                    return null;
                }

                @Override
                public Module getModule() {
                    return null;
                }

                @Override
                public boolean isTestSource() {
                    return false;
                }
            });
        }

        return res.toArray(new GenerationItem[res.size()]);
    }

    @Override
    public GenerationItem[] generate(CompileContext context, GenerationItem[] items, VirtualFile outputRootDirectory) {
        return new GenerationItem[0];
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

            final IceFacetConfiguration facetConfiguration = iceFacet.getConfiguration();

        }

        return false;
    }

    @Override
    public ValidityState createValidityState(DataInput in) throws IOException {
        return null;
    }
}
