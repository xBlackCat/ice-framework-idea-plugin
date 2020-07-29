package org.xblackcat.frozenidea.findUsages.java;

import com.intellij.find.findUsages.AbstractFindUsagesDialog;
import com.intellij.find.findUsages.FindUsagesHandler;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.findUsages.slice.FindSliceClassUsagesDialog;
import org.xblackcat.frozenidea.integration.java.JavaModuleHelper;
import org.xblackcat.frozenidea.psi.*;
import org.xblackcat.frozenidea.util.FQN;

/**
 * 06.07.12 15:07
 *
 * @author xBlackCat
 */
public class SliceJavaFindUsagesHandler extends FindUsagesHandler {
    private final SliceJavaFindUsagesHandlerFactory factory;

    public SliceJavaFindUsagesHandler(@NotNull PsiElement psiElement, SliceJavaFindUsagesHandlerFactory factory) {
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

            JavaModuleHelper javaHelper = JavaModuleHelper.getJavaHelper(classDef);

            PsiElement javaClass = javaHelper.findClass(FQN.buildFQN(classDef).getJavaFQN());
            if (javaClass != null) {
                return new PsiElement[]{
                        javaClass
                };
            }
        } else if (element instanceof SliceMethodDef) {
            SliceMethodDef methodDef = (SliceMethodDef) element;
            JavaModuleHelper javaHelper = JavaModuleHelper.getJavaHelper(methodDef);

            return javaHelper.findClassMethod(methodDef);
        }  else if (element instanceof SliceFieldDef) {
            SliceFieldDef fieldDef = (SliceFieldDef) element;
            JavaModuleHelper javaHelper = JavaModuleHelper.getJavaHelper(fieldDef);

            PsiElement javaField = javaHelper.findClassField(FQN.buildFQN(fieldDef.getDeclarationType()).getJavaFQN(), fieldDef.getName());
            if (javaField != null) {
                return new PsiElement[]{
                        javaField
                };
            }
        }  else if (element instanceof SliceEnumConstant) {
            SliceEnumConstant fieldDef = (SliceEnumConstant) element;
            JavaModuleHelper javaHelper = JavaModuleHelper.getJavaHelper(fieldDef);

            PsiElement javaField = javaHelper.findClassField(FQN.buildFQN(fieldDef.getDeclarationType()).getJavaFQN(), fieldDef.getName());
            if (javaField != null) {
                return new PsiElement[]{
                        javaField
                };
            }
        }
        return super.getSecondaryElements();
    }
}
