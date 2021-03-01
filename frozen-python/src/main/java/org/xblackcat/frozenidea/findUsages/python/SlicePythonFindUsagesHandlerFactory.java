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
package org.xblackcat.frozenidea.findUsages.python;

import com.intellij.find.findUsages.FindUsagesHandler;
import com.intellij.find.findUsages.FindUsagesHandlerFactory;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.findUsages.slice.SliceClassFindUsagesOptions;
import org.xblackcat.frozenidea.findUsages.slice.SliceFindUsagesProvider;

/**
 * @author peter
 */
public class SlicePythonFindUsagesHandlerFactory extends FindUsagesHandlerFactory {
    private final SliceClassFindUsagesOptions myFindClassOptions;

    public static SlicePythonFindUsagesHandlerFactory getInstance(@NotNull Project project) {
        return ContainerUtil.findInstance(
                EP_NAME.getExtensions(project),
                SlicePythonFindUsagesHandlerFactory.class
        );
    }

    public SlicePythonFindUsagesHandlerFactory(Project project) {
        myFindClassOptions = new SliceClassFindUsagesOptions(project);
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
//            final PsiMethod[] methods = SuperMethodWarningUtil.checkSuperMethods((PsiMethod) element, PythonFindUsagesHandler.ACTION_STRING);
//            if (methods.length > 1) {
//                return new PythonFindUsagesHandler(element, methods, this);
//            }
//            if (methods.length == 1) {
//                return new PythonFindUsagesHandler(methods[0], this);
//            }
//            return FindUsagesHandler.NULL_HANDLER;
//        }

        return new SlicePythonFindUsagesHandler(element, this);
    }

    public SliceClassFindUsagesOptions getFindClassOptions() {
        return myFindClassOptions;
    }
}
