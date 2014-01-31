package org.xblackcat.frozenice.integration;

import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.config.IceComponent;
import org.xblackcat.frozenice.psi.*;

import java.util.List;

/**
 * 14.06.12 16:23
 *
 * @author xBlackCat
 */
public class SliceHelper {
    public static String getPackageName(PsiFile file, IceComponent target) {
        List<SliceGlobalMetadata> globalMetadatas = PsiTreeUtil.getChildrenOfTypeAsList(
                file,
                SliceGlobalMetadata.class
        );
        for (SliceGlobalMetadata md : globalMetadatas) {
            for (SliceMetadataElement el : md.getMetadataElementList()) {
                final String text = el.getStringLiteral().getText();
                String packageString = target.extractPackageName(text.substring(1, text.length() - 1));

                if (packageString != null) {
                    return packageString;
                }
            }
        }

        return null;
    }

    public static void collectName(StringBuilder res, SliceModule module) {
        if (module == null) {
            return;
        }
        final PsiElement id = module.getId();
        if (id == null) {
            return;
        }

        collectName(res, PsiTreeUtil.getParentOfType(module, SliceModule.class));

        res.append(id.getText());
        res.append(".");
    }

    public static String getFQN(SliceNamedElement element) {
        final PsiElement id = element.getId();
        if (id == null) {
            return null;
        }
        String name = id.getText();

        if (name == null) {
            return null;
        }

        SliceModule module = getContainerSliceModule(element);
        if (module == null) {
            return null;
        }
        return buildFQN(name, module);
    }

    public static @NotNull String buildFQN(String name, SliceModule module) {
        String packageName = SliceHelper.getPackageName(module.getContainingFile(), IceComponent.Java);

        StringBuilder fqn = new StringBuilder();
        if (packageName != null) {
            fqn.append(packageName);
            fqn.append(".");
        }

        SliceHelper.collectName(fqn, module);

        fqn.append(name);

        return fqn.toString();
    }

    public static SliceModule getContainerSliceModule(SliceNamedElement element) {
        SliceModule module;
        if (element instanceof SliceModule) {
            module = PsiTreeUtil.getParentOfType(element, SliceModule.class);
        } else if (element instanceof SliceDataTypeElement) {
            module = ((SliceDataTypeElement) element).getModule();
        } else {
            module = null;
        }
        return module;
    }

    public static PsiClass searchImplementation(SliceDataTypeElement element) {
        if (element == null) {
            return null;
        }

        final PsiElement id = element.getId();
        if (id == null) {
            return null;
        }
        String name = id.getText();

        if (name == null) {
            return null;
        }

        SliceModule module = getContainerSliceModule(element);
        if (module == null) {
            return null;
        }
        final String implFQN = buildFQN("_" + name + "Operations", module);

        return checkJavaClass(element, implFQN);
    }

    private static PsiClass checkJavaClass(SliceDataTypeElement element, String implFQN) {
        JavaModuleHelper javaHelper = JavaModuleHelper.getJavaHelper(element);
        NavigatablePsiElement aClass = javaHelper.findClass(implFQN);

        if ((aClass instanceof PsiClass)) {
            return (PsiClass) aClass;
        }
        return null;

    }

}
