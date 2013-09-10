package org.xblackcat.frozenice.integration;

import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.util.PsiTreeUtil;
import org.xblackcat.frozenice.psi.*;
import org.xblackcat.frozenice.util.IceComponent;

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
            SliceMetadataBody body = md.getMetadataBody();
            for (SliceMetadataElement el : body.getMetadataElementList()) {
                String packageString = target.extractPackageName(el.getStringLiteral().getText());

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

        collectName(res, PsiTreeUtil.getParentOfType(module, SliceModule.class));

        res.append(module.getId().getText());
        res.append(".");
    }

    public static String getFQN(SliceNamedElement element) {
        String name = element.getId().getText();

        if (name == null) {
            return null;
        }

        SliceModule module;
        if (element instanceof SliceModule) {
            module = PsiTreeUtil.getParentOfType(element, SliceModule.class);
        } else if (element instanceof SliceDataTypeElement) {
            module = ((SliceDataTypeElement) element).getModule();
        } else {
            return null;
        }
        String packageName = SliceHelper.getPackageName(element.getContainingFile(), IceComponent.Java);

        StringBuilder fqn = new StringBuilder();
        if (packageName != null) {
            fqn.append(packageName);
            fqn.append(".");
        }

        SliceHelper.collectName(fqn, module);

        fqn.append(name);

        return fqn.toString();

    }

    public static PsiClass searchClassImplementation(SliceClassDef element) {
        if (element == null) {
            return null;
        }

        String implFQN = getFQN(element);

        return checkJavaClass(element, implFQN);
    }

    private static PsiClass checkJavaClass(SliceDataTypeElement element, String implFQN) {
        JavaModuleHelper javaHelper = JavaModuleHelper.getJavaHelper(element);
        NavigatablePsiElement aClass = javaHelper.findClass(implFQN);

        if (!(aClass instanceof PsiClass) || !((PsiClass) aClass).hasModifierProperty(PsiModifier.ABSTRACT)) {
            return null;
        }

        return (PsiClass) aClass;
    }

    public static PsiClass searchInterfaceImplementation(SliceInterfaceDef element) {
        if (element == null) {
            return null;
        }

        String moduleFQN = getFQN(element.getModule());
        String name = element.getName();

        String implFQN = moduleFQN + "._" + name + "Disp";

        return checkJavaClass(element, implFQN);
    }
}
