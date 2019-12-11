/*
 * Copyright 2011-2011 Gregory Shrago
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xblackcat.frozenidea.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.psi.*;

import javax.swing.*;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: gregory
 * Date: 14.07.11
 * Time: 20:04
 */
public abstract class SliceInnerElementImpl extends SliceNamedElementImpl implements SliceInnerElement {
    protected SliceInnerElementImpl(ASTNode node) {
        super(node);
    }

    @Override
    public SliceDataTypeElement getDeclarationType() {
        return PsiTreeUtil.getParentOfType(this, SliceDataTypeElement.class);
    }

    @Override
    public ItemPresentation getPresentation() {
        if (!(SliceInnerElementImpl.this instanceof SliceMethodDef)) {
            return super.getPresentation();
        }
        return new ItemPresentation() {
            @Override
            public String getPresentableText() {
                final SliceParametersList list = ((SliceMethodDef) SliceInnerElementImpl.this).getParametersList();
                final String params;
                if (list == null || list.getParameterList().isEmpty()) {
                    params = "";
                } else {
                    params = list.getParameterList().stream().map(p -> p.getDataType().getText()).collect(Collectors.joining(", "));
                }
                return getName() + "(" + params + ")";
            }

            @Nullable
            @Override
            public String getLocationString() {
                final FQN path = FQN.buildFQN(SliceInnerElementImpl.this).getPath();
                return path == null ? null : path.getFQN();
            }

            @Nullable
            @Override
            public Icon getIcon(boolean unused) {
                return getElementIcon(0);
            }
        };
    }

}
