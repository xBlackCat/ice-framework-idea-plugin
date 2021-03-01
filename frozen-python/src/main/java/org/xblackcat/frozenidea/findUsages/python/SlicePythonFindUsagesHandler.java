package org.xblackcat.frozenidea.findUsages.python;

import com.intellij.find.findUsages.AbstractFindUsagesDialog;
import com.intellij.find.findUsages.FindUsagesHandler;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.findUsages.slice.FindSliceClassUsagesDialog;
import org.xblackcat.frozenidea.integration.python.PythonModuleHelper;
import org.xblackcat.frozenidea.psi.SliceDataTypeElement;
import org.xblackcat.frozenidea.psi.SliceEnumConstant;
import org.xblackcat.frozenidea.psi.SliceFieldDef;
import org.xblackcat.frozenidea.psi.SliceMethodDef;

/**
 * 06.07.12 15:07
 *
 * @author xBlackCat
 */
public class SlicePythonFindUsagesHandler extends FindUsagesHandler {
    private final SlicePythonFindUsagesHandlerFactory factory;

    public SlicePythonFindUsagesHandler(@NotNull PsiElement psiElement, SlicePythonFindUsagesHandlerFactory factory) {
        super(psiElement);
        this.factory = factory;
    }

    @NotNull
    @Override
    public AbstractFindUsagesDialog getFindUsagesDialog(
            boolean isSingleFile,
            boolean toShowInNewTab,
            boolean mustOpenInNewTab
    ) {
        PsiElement element = getPsiElement();

        if (element instanceof SliceDataTypeElement) {
            return new FindSliceClassUsagesDialog(
                    element,
                    getProject(),
                    factory.getFindClassOptions(),
                    toShowInNewTab,
                    mustOpenInNewTab,
                    isSingleFile,
                    this
            );
        }

        return super.getFindUsagesDialog(isSingleFile, toShowInNewTab, mustOpenInNewTab);
    }

    @NotNull
    @Override
    public PsiElement[] getSecondaryElements() {
        PsiElement element = getPsiElement();

        if (element instanceof SliceDataTypeElement) {
            SliceDataTypeElement classDef = (SliceDataTypeElement) element;

            PythonModuleHelper pythonHelper = PythonModuleHelper.getPythonHelper(classDef);

            PsiElement pythonClass = pythonHelper.findClass(classDef);
            if (pythonClass != null) {
                return new PsiElement[]{
                        pythonClass
                };
            }
        } else if (element instanceof SliceMethodDef) {
            SliceMethodDef methodDef = (SliceMethodDef) element;
            PythonModuleHelper pythonHelper = PythonModuleHelper.getPythonHelper(methodDef);

            return pythonHelper.findClassMethod(methodDef);
        } else if (element instanceof SliceFieldDef) {
            SliceFieldDef fieldDef = (SliceFieldDef) element;
            PythonModuleHelper pythonHelper = PythonModuleHelper.getPythonHelper(fieldDef);

            PsiElement pythonField = pythonHelper.findClassField(fieldDef.getContainingClass(), fieldDef.getName());
            if (pythonField != null) {
                return new PsiElement[]{
                        pythonField
                };
            }
        } else if (element instanceof SliceEnumConstant) {
            SliceEnumConstant fieldDef = (SliceEnumConstant) element;
            PythonModuleHelper pythonHelper = PythonModuleHelper.getPythonHelper(fieldDef);

            PsiElement pythonField = pythonHelper.findClassField(fieldDef.getContainingClass(), fieldDef.getName());
            if (pythonField != null) {
                return new PsiElement[]{
                        pythonField
                };
            }
        }
        return super.getSecondaryElements();
    }
}
