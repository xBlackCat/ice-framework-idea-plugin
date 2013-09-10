package org.xblackcat.frozenice.find.usages;

import com.intellij.find.findUsages.AbstractFindUsagesDialog;
import com.intellij.find.findUsages.FindUsagesHandler;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.integration.JavaModuleHelper;
import org.xblackcat.frozenice.integration.SliceHelper;
import org.xblackcat.frozenice.psi.SliceDataFwTypeElement;
import org.xblackcat.frozenice.psi.SliceDataTypeElement;

/**
 * 06.07.12 15:07
 *
 * @author xBlackCat
 */
public class SliceFindUsagesHandler extends FindUsagesHandler {
    private final SliceFindUsagesHandlerFactory factory;
    private final PsiElement[] myElementsToSearch;

    public SliceFindUsagesHandler(@NotNull PsiElement psiElement, SliceFindUsagesHandlerFactory factory) {
        this(psiElement, PsiElement.EMPTY_ARRAY, factory);
    }

    public SliceFindUsagesHandler(
            @NotNull PsiElement psiElement,
            @NotNull PsiElement[] elementsToSearch,
            SliceFindUsagesHandlerFactory factory
    ) {
        super(psiElement);
        this.factory = factory;
        myElementsToSearch = elementsToSearch;
    }

    @NotNull
    @Override
    public AbstractFindUsagesDialog getFindUsagesDialog(
            boolean isSingleFile,
            boolean toShowInNewTab,
            boolean mustOpenInNewTab
    ) {
        PsiElement element = getPsiElement();

        if (element instanceof SliceDataTypeElement || element instanceof SliceDataFwTypeElement) {
            return new FindClassUsagesDialog(
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
    public PsiElement[] getPrimaryElements() {
        PsiElement element = getPsiElement();

        if (element instanceof SliceDataTypeElement) {
            SliceDataTypeElement classDef = (SliceDataTypeElement) element;

            JavaModuleHelper javaHelper = JavaModuleHelper.getJavaHelper(classDef);

            NavigatablePsiElement javaClass = javaHelper.findClass(SliceHelper.getFQN(classDef));
            if (javaClass != null) {
                return new PsiElement[]{
                        element,
                        javaClass
                };
            }
        }

        return myElementsToSearch.length == 0 ? new PsiElement[]{element} : myElementsToSearch;
    }
}
