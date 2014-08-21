package org.xblackcat.frozenidea.find.usages;

import com.intellij.find.findUsages.FindUsagesHandler;
import com.intellij.find.findUsages.FindUsagesOptions;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;

import javax.swing.*;

/**
 * 06.07.12 15:01
 *
 * @author xBlackCat
 */
public class FindClassUsagesDialog extends SliceFindUsagesDialog<SliceFindClassUsagesOptions> {
    protected FindClassUsagesDialog(
            PsiElement element,
            Project project,
            FindUsagesOptions findUsagesOptions,
            boolean toShowInNewTab,
            boolean mustOpenInNewTab,
            boolean isSingleFile,
            FindUsagesHandler handler
    ) {
        super(element, project, findUsagesOptions, toShowInNewTab, mustOpenInNewTab, isSingleFile, handler);
    }

    @Override
    protected JPanel createFindWhatPanel() {
        return null;
    }
}
