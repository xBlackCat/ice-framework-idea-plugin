package org.xblackcat.frozenidea.integration.java;

import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.config.IceComponent;
import org.xblackcat.frozenidea.integration.SliceHelper;
import org.xblackcat.frozenidea.psi.SliceDataTypeElement;
import org.xblackcat.frozenidea.psi.SliceModule;

/**
 *
 */
public class JavaPsiUtil {
    @Nullable
    public static String getJavaFQN(SliceDataTypeElement element) {
        if (element.isSequence() || element.isDictionary()) {
            return null;
        }
        final String packageName = JavaPsiUtil.getJavaPackageName(element.getModule());
        if (packageName == null) {
            return null;
        }
        return packageName + "." + element.getName();
    }

    @Nullable
    public static String getJavaPackageName(SliceModule module) {
        final String modulePackageName = SliceHelper.getPackageName(module, IceComponent.Java);
        if (modulePackageName != null) {
            return modulePackageName + "." + module.getName();
        }

        final SliceModule parentModule = PsiTreeUtil.getParentOfType(module, SliceModule.class);
        if (parentModule != null) {
            return getJavaPackageName(parentModule) + "." + module.getName();
        }

        final String packageName = SliceHelper.getPackageName(module.getContainingFile(), IceComponent.Java);
        if (packageName != null) {
            return packageName + "." + module.getName();
        }

        return module.getName();
    }
}
