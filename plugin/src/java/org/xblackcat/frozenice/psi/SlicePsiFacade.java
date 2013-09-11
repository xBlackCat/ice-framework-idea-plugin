package org.xblackcat.frozenice.psi;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NotNullLazyKey;
import com.intellij.psi.search.GlobalSearchScope;

/**
 * 10.09.13 10:06
 *
 * @author xBlackCat
 */
public abstract class SlicePsiFacade {
    private static final NotNullLazyKey<SlicePsiFacade, Project> INSTANCE_KEY = ServiceManager.createLazyKey(
            SlicePsiFacade.class
    );

    public static SlicePsiFacade getInstance(Project project) {
        return INSTANCE_KEY.getValue(project);
    }

    public abstract SliceDataTypeElement findClass(String className, GlobalSearchScope globalSearchScope);

    protected abstract Project getProject();

    public abstract SliceModule findPackage(String packageName);
}
