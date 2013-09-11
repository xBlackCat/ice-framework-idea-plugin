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
package org.xblackcat.frozenice.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import org.xblackcat.frozenice.psi.SliceDataFwTypeElement;
import org.xblackcat.frozenice.psi.SliceDataTypeElement;
import org.xblackcat.frozenice.psi.SliceModule;

/**
 * Created by IntelliJ IDEA.
 * User: gregory
 * Date: 14.07.11
 * Time: 20:04
 */
public abstract class SliceDataFwTypeElementImpl extends SliceNamedElementImpl implements SliceDataFwTypeElement {
    protected SliceDataFwTypeElementImpl(ASTNode node) {
        super(node);
    }

    @Override
    public SliceModule getModule() {
        return PsiTreeUtil.getParentOfType(this, SliceModule.class);
    }

    @Override
    public SliceDataTypeElement getDeclaration() {
        // TODO: implement search real declaration
        return null;
    }
}
