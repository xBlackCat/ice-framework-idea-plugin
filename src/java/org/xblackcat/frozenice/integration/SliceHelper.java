package org.xblackcat.frozenice.integration;

import com.intellij.psi.PsiFile;
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
        List<SliceGlobalMetadata> globalMetadatas = PsiTreeUtil.getChildrenOfTypeAsList(file, SliceGlobalMetadata.class);
        for (SliceGlobalMetadata md : globalMetadatas) {
            for (SliceMetadataElement el : md.getMetadataBody().getMetadataElementList()) {
                String packageString = target.extractPackageName(el.getString().getText());

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
}
