package org.xblackcat.frozenidea.code.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.psi.SliceMethodDef;
import org.xblackcat.frozenidea.util.PsiFormatUtil;

import java.util.Collection;
import java.util.Collections;

import static com.intellij.psi.util.PsiFormatUtilBase.*;

/**
 *
 */
public class SliceMethodTreeElement extends PsiTreeElementBase<SliceMethodDef> {
    public SliceMethodTreeElement(SliceMethodDef psiElement) {
        super(psiElement);
    }

    @Override
    public @NotNull Collection<StructureViewTreeElement> getChildrenBase() {
        return Collections.emptyList();
    }

    @Override
    public @Nullable String getPresentableText() {
        @Nullable SliceMethodDef psiMethod = getElement();
        if (psiMethod == null) {
            return "";
        }
        String method = PsiFormatUtil.formatMethod(
                psiMethod,
                SHOW_NAME | TYPE_AFTER | SHOW_PARAMETERS | SHOW_TYPE,
                SHOW_TYPE
        );
        return StringUtil.replace(method, ":", ": ");
    }
}
