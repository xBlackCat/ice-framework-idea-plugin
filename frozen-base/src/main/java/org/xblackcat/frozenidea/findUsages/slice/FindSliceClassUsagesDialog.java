package org.xblackcat.frozenidea.findUsages.slice;

import com.intellij.find.FindBundle;
import com.intellij.find.findUsages.FindUsagesHandler;
import com.intellij.find.findUsages.FindUsagesOptions;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.StateRestoringCheckBox;
import org.xblackcat.frozenidea.psi.SliceDataTypeElement;
import org.xblackcat.frozenidea.psi.SliceNamedElement;
import org.xblackcat.frozenidea.util.SliceBundle;

import javax.swing.*;

/**
 * 06.07.12 15:01
 *
 * @author xBlackCat
 */
public class FindSliceClassUsagesDialog extends SliceFindUsagesDialog<SliceClassFindUsagesOptions> {
    private StateRestoringCheckBox myCbUsages;
    private StateRestoringCheckBox myCbMethodsUsages;
    private StateRestoringCheckBox myCbFieldsUsages;
    private StateRestoringCheckBox myCbImplementingClasses;
    private StateRestoringCheckBox myCbDerivedInterfaces;
    private StateRestoringCheckBox myCbDerivedClasses;

    public FindSliceClassUsagesDialog(
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
    public JComponent getPreferredFocusedControl() {
        return myCbUsages;
    }

    @Override
    public void calcFindUsagesOptions(SliceClassFindUsagesOptions options) {
        super.calcFindUsagesOptions(options);

        if (isToChange(myCbUsages)) {
            options.isUsages = isSelected(myCbUsages);
        }
        if (isToChange(myCbMethodsUsages)) {
            options.isMethodsUsages = isSelected(myCbMethodsUsages);
        }
        if (isToChange(myCbFieldsUsages)) {
            options.isFieldsUsages = isSelected(myCbFieldsUsages);
        }
        if (isToChange(myCbDerivedClasses)) {
            options.isDerivedClasses = isSelected(myCbDerivedClasses);
        }
        if (isToChange(myCbImplementingClasses)) {
            options.isImplementingClasses = isSelected(myCbImplementingClasses);
        }
        if (isToChange(myCbDerivedInterfaces)) {
            options.isDerivedInterfaces = isSelected(myCbDerivedInterfaces);
        }
        options.isCheckDeepInheritance = true;
        options.isIncludeInherited = false;
     }

    @Override
    protected JPanel createFindWhatPanel() {
        JPanel findWhatPanel = new JPanel();

        findWhatPanel.setBorder(IdeBorderFactory.createTitledBorder(FindBundle.message("find.what.group")));
        findWhatPanel.setLayout(new BoxLayout(findWhatPanel, BoxLayout.Y_AXIS));

        myCbUsages = addCheckboxToPanel(
                FindBundle.message("find.what.usages.checkbox"),
                getFindUsagesOptions().isUsages,
                findWhatPanel,
                true
        );

        SliceNamedElement psiClass = getPsiElement();
        myCbMethodsUsages = addCheckboxToPanel(
                SliceBundle.message("find.what.methods.usages.checkbox"),
                getFindUsagesOptions().isMethodsUsages,
                findWhatPanel,
                true
        );

        if (psiClass instanceof SliceDataTypeElement) {
            SliceDataTypeElement type = (SliceDataTypeElement) psiClass;
            if (type.isClass() || type.isInterface() || type.isStruct() || type.isException()) {
                myCbFieldsUsages = addCheckboxToPanel(
                        SliceBundle.message("find.what.fields.usages.checkbox"),
                        getFindUsagesOptions().isFieldsUsages,
                        findWhatPanel,
                        true
                );
            }

            if (type.isInterface()) {
                myCbImplementingClasses = addCheckboxToPanel(
                        SliceBundle.message("find.what.implementing.classes.checkbox"),
                        getFindUsagesOptions().isImplementingClasses,
                        findWhatPanel,
                        true
                );
                myCbDerivedInterfaces = addCheckboxToPanel(
                        SliceBundle.message("find.what.derived.interfaces.checkbox"),
                        getFindUsagesOptions().isDerivedInterfaces,
                        findWhatPanel,
                        true
                );
            }
            if (type.isClass() || type.isException())
                myCbDerivedClasses = addCheckboxToPanel(
                        SliceBundle.message("find.what.derived.classes.checkbox"),
                        getFindUsagesOptions().isDerivedClasses,
                        findWhatPanel,
                        true
                );
        }
        return findWhatPanel;
    }

    @Override
    protected void update() {
        if (myCbToSearchForTextOccurrences != null) {
            if (isSelected(myCbUsages)) {
                myCbToSearchForTextOccurrences.makeSelectable();
            } else {
                myCbToSearchForTextOccurrences.makeUnselectable(false);
            }
        }

        boolean hasSelected = isSelected(myCbUsages) ||
                isSelected(myCbFieldsUsages) ||
                isSelected(myCbMethodsUsages) ||
                isSelected(myCbImplementingClasses) ||
                isSelected(myCbDerivedInterfaces) ||
                isSelected(myCbDerivedClasses);
        setOKActionEnabled(hasSelected);
    }

}
