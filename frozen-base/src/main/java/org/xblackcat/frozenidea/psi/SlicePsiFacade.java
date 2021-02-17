package org.xblackcat.frozenidea.psi;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import org.xblackcat.frozenidea.util.FQN;

/**
 * 10.09.13 10:06
 *
 * @author xBlackCat
 */
public abstract class SlicePsiFacade {
    public static SlicePsiFacade getInstance(Project project) {
        return project.getService((Class<? extends SlicePsiFacade>) SlicePsiFacade.class);
    }

    public abstract SliceDataTypeElement findClass(FQN className, GlobalSearchScope globalSearchScope);

    protected abstract Project getProject();

    public abstract SliceModule findPackage(String packageName);
}
