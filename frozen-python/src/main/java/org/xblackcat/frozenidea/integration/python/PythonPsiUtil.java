package org.xblackcat.frozenidea.integration.python;

import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.config.IceComponent;
import org.xblackcat.frozenidea.integration.SliceHelper;
import org.xblackcat.frozenidea.psi.SliceDataTypeElement;
import org.xblackcat.frozenidea.psi.SliceModule;

/**
 *
 */
public class PythonPsiUtil {
    @Nullable
    public static String getPythonFQN(SliceDataTypeElement element) {
        if (element.isSequence() || element.isDictionary()) {
            return null;
        }
        final String packageName = PythonPsiUtil.getPythonPackageName(element.getModule());
        if (packageName == null) {
            return null;
        }
        return packageName + "." + element.getName();
    }

    @Nullable
    public static String getPythonPackageName(SliceModule module) {
        final String modulePackageName = SliceHelper.getPackageNameMetadata(module, IceComponent.Python);
        if (modulePackageName != null) {
            return modulePackageName + "." + module.getName();
        }

        final SliceModule parentModule = PsiTreeUtil.getParentOfType(module, SliceModule.class);
        if (parentModule != null) {
            return getPythonPackageName(parentModule) + "." + module.getName();
        }

        final String packageName = SliceHelper.getPackageNameMetadata(module.getContainingFile(), IceComponent.Python);
        if (packageName != null) {
            return packageName + "." + module.getName();
        }

        return module.getName();
    }
}
