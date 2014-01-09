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

import com.intellij.icons.AllIcons;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenice.psi.*;

import javax.swing.*;

public abstract class SliceNamedElementImpl extends SliceCompositeElementImpl implements SliceNamedElement {

    private volatile String myCachedName;

    public SliceNamedElementImpl(ASTNode node) {
        super(node);
    }

    @Override
    public void subtreeChanged() {
        super.subtreeChanged();
        myCachedName = null;
    }

    @Override
    public String getName() {
        if (myCachedName == null) {
            final PsiElement id = getId();
            if (id != null) {
                myCachedName = id.getText();
            } else {
                return "!! " + getClass().getName();
            }
        }
        return myCachedName;
    }

    @Override
    public PsiElement setName(@NonNls @NotNull String s) throws IncorrectOperationException {
//    getId().replace(BnfElementFactory.createLeafFromText(getProject(), s));
        return this;
    }

    @Override
    public int getTextOffset() {
        final PsiElement id = getId();
        if (id != null) {
            return id.getTextOffset();
        } else {
            return -1;
        }
    }

    @NotNull
    @Override
    public SearchScope getUseScope() {
        return new LocalSearchScope(getContainingFile());
    }

    @Nullable
    @Override
    protected Icon getElementIcon(@IconFlags int flags) {
        if (this instanceof SliceModule) {
            return AllIcons.Nodes.Package;
        } else if (this instanceof SliceMethodDef) {
            return AllIcons.Nodes.AbstractMethod;
        } else if (this instanceof SliceFieldDef) {
            return AllIcons.Nodes.Field;
        } else if (this instanceof SliceConstant) {
            return AllIcons.Nodes.Field;
        } else if (this instanceof SliceClassDef) {
            return AllIcons.Nodes.Class;
        } else if (this instanceof SliceInterfaceDef) {
            return AllIcons.Nodes.Interface;
        } else if (this instanceof SliceExceptionDef) {
            return AllIcons.Nodes.ExceptionClass;
        } else if (this instanceof SliceStructDef) {
            return AllIcons.Nodes.AnonymousClass;
        } else if (this instanceof SliceEnumDef) {
            return AllIcons.Nodes.Enum;
        } else if (this instanceof SliceDictionaryDef) {
            return AllIcons.Nodes.Advice;
        } else if (this instanceof SliceSequenceDef) {
            return AllIcons.Nodes.Advice;
        }

        return super.getElementIcon(flags);
    }

    @Nullable
    @Override
    public PsiElement getId() {
        ASTNode child = getNode().findChildByType(SliceTypes.ICE_ID);
        return child == null ? null : child.getPsi();
    }

    @Override
    public PsiElement getNameIdentifier() {
        return getId();
    }

    @Override
    public String toString() {
        return super.toString() + ":" + getName();
    }
}
