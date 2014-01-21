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
package org.xblackcat.frozenice.find.usages;

import com.intellij.find.findUsages.FindUsagesHandler;
import com.intellij.find.findUsages.FindUsagesHandlerFactory;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author peter
 */
public class SliceFindUsagesHandlerFactory extends FindUsagesHandlerFactory {
    private final SliceFindClassUsagesOptions myFindClassOptions;

    public static SliceFindUsagesHandlerFactory getInstance(@NotNull Project project) {
        return ContainerUtil.findInstance(
                Extensions.getExtensions(EP_NAME, project),
                SliceFindUsagesHandlerFactory.class
        );
    }

    public SliceFindUsagesHandlerFactory(Project project) {
        myFindClassOptions = new SliceFindClassUsagesOptions(project);
    }

    @Override
    public boolean canFindUsages(@NotNull final PsiElement element) {
        return new SliceFindUsagesProvider().canFindUsagesFor(element);
    }

    @Override
    public FindUsagesHandler createFindUsagesHandler(
            @NotNull final PsiElement element,
            final boolean forHighlightUsages
    ) {
//        if (element instanceof PsiMethod && !forHighlightUsages) {
//            final PsiMethod[] methods = SuperMethodWarningUtil.checkSuperMethods((PsiMethod) element, JavaFindUsagesHandler.ACTION_STRING);
//            if (methods.length > 1) {
//                return new JavaFindUsagesHandler(element, methods, this);
//            }
//            if (methods.length == 1) {
//                return new JavaFindUsagesHandler(methods[0], this);
//            }
//            return FindUsagesHandler.NULL_HANDLER;
//        }

        return new SliceFindUsagesHandler(element, this);
    }

    public SliceFindClassUsagesOptions getFindClassOptions() {
        return myFindClassOptions;
    }
}
