package org.xblackcat.frozenidea.psi.impl;

import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import org.xblackcat.frozenidea.psi.SliceDataTypeElement;
import org.xblackcat.frozenidea.psi.SliceModule;
import org.xblackcat.frozenidea.psi.SlicePsiFacade;

/**
 * 10.09.13 10:07
 *
 * @author xBlackCat
 */
public class SlicePsiFacadeImpl extends SlicePsiFacade {
    private final Project myProject;

    public SlicePsiFacadeImpl(Project myProject) {
        this.myProject = myProject;
    }


    @Override
    public SliceDataTypeElement findClass(String className, GlobalSearchScope scope) {
        ProgressIndicatorProvider.checkCanceled(); // We hope this method is being called often enough to cancel daemon processes smoothly



        return null;
    }


    @Override
    protected Project getProject() {
        return myProject;
    }

    @Override
    public SliceModule findPackage(String packageName) {
        return null;
    }

}
