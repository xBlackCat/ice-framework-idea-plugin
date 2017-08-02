package org.xblackcat.frozenidea.find.usages;

import com.intellij.find.findUsages.AbstractFindUsagesDialog;
import com.intellij.find.findUsages.FindUsagesHandler;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.integration.JavaModuleHelper;
import org.xblackcat.frozenidea.psi.SliceDataTypeElement;
import org.xblackcat.frozenidea.psi.impl.FQN;

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

        if (element instanceof SliceDataTypeElement) {
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

            PsiElement javaClass = javaHelper.findClass(FQN.buildFQN(classDef).getJavaFQN());
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
