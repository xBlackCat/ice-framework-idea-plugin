/*
 * Copyright 2000-2009 JetBrains s.r.o.
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
package org.xblackcat.frozenidea.findUsages.slice;

import com.intellij.find.findUsages.CommonFindUsagesDialog;
import com.intellij.find.findUsages.FindUsagesHandler;
import com.intellij.find.findUsages.FindUsagesOptions;
import com.intellij.internal.statistic.eventLog.FeatureUsageData;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.ui.SimpleColoredComponent;
import com.intellij.usageView.UsageViewUtil;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.psi.SliceNamedElement;

public abstract class SliceFindUsagesDialog<T extends SliceFindUsagesOptions> extends CommonFindUsagesDialog {
    protected static final String EVENT_LOG_GROUP = "slice.find.usages";

    protected SliceFindUsagesDialog(
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
    protected void init() {
        super.init();
    }

    public void calcFindUsagesOptions(T options) {
    }

    @Override
    public void calcFindUsagesOptions(FindUsagesOptions options) {
        super.calcFindUsagesOptions(options);
        calcFindUsagesOptions((T) options);
    }

    protected SliceNamedElement getPsiElement() {
        return (SliceNamedElement) myPsiElement;
    }

    @Override
    public void configureLabelComponent(@NotNull final SimpleColoredComponent coloredComponent) {
        coloredComponent.append(StringUtil.capitalize(UsageViewUtil.getType(myPsiElement)));
        coloredComponent.append(" ");
        coloredComponent.append(UsageViewUtil.getLongName(myPsiElement));
    }

    @SuppressWarnings("unchecked")
    protected T getFindUsagesOptions() {
        return (T) myFindUsagesOptions;
    }
}
