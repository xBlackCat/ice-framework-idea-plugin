package org.xblackcat.frozenice.processor;

import com.intellij.openapi.compiler.GeneratingCompiler;
import com.intellij.openapi.compiler.ValidityState;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import org.xblackcat.frozenice.facet.IceFacet;

/**
 * 16.01.12 15:32
 *
 * @author xBlackCat
 */
class IceGenerationItem implements GeneratingCompiler.GenerationItem {
    private final VirtualFile file;
    private final boolean inTestSourceContent;
    private final IceFacet facet;

    public IceGenerationItem(IceFacet facet, VirtualFile file, boolean inTestSourceContent) {
        this.facet = facet;
        this.file = file;
        this.inTestSourceContent = inTestSourceContent;
    }

    @Override
    public String getPath() {
        return file.getPath();
    }

    @Override
    public ValidityState getValidityState() {
        return null;
    }

    @Override
    public Module getModule() {
        return facet.getModule();
    }

    public IceFacet getFacet() {
        return facet;
    }

    @Override
    public boolean isTestSource() {
        return inTestSourceContent;
    }

    public VirtualFile getSource() {
        return file;
    }
}
