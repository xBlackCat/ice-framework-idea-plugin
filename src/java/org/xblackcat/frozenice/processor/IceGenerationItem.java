package org.xblackcat.frozenice.processor;

import com.intellij.openapi.compiler.GeneratingCompiler;
import com.intellij.openapi.compiler.ValidityState;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;

/**
* 16.01.12 15:32
*
* @author xBlackCat
*/
class IceGenerationItem implements GeneratingCompiler.GenerationItem {
    private final VirtualFile file;
    private final boolean inTestSourceContent;
    private final Module module;

    public IceGenerationItem(Module module, VirtualFile file, boolean inTestSourceContent) {
        this.module = module;
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
        return module;
    }

    @Override
    public boolean isTestSource() {
        return inTestSourceContent;
    }

    public VirtualFile getSource() {
        return file;
    }
}
