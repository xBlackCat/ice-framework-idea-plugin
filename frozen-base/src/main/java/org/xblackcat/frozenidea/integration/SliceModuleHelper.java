/*
 * Copyright 2011-2012 Gregory Shrago
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

package org.xblackcat.frozenidea.integration;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassReferenceProvider;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.psi.*;
import org.xblackcat.frozenidea.util.FQN;
import org.xblackcat.frozenidea.util.SliceUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author gregsh
 */
public class SliceModuleHelper {
    protected Module module;

    public SliceModuleHelper(Module module) {
        this.module = module;
    }

    public static SliceModuleHelper getSliceHelper(Module module) {
        SliceModuleHelper service = module.getService(SliceModuleHelper.class);
        return service == null ? new SliceModuleHelper(module) : service;
    }

    public static SliceModuleHelper getSliceHelper(PsiElement element) {
        Module module = ModuleUtil.findModuleForPsiElement(element);

        if (module != null) {
            SliceModuleHelper service = module.getService(SliceModuleHelper.class);

            if (service != null) {
                return service;
            }
        }

        return new SliceModuleHelper(module);
    }

    @Nullable
    public PsiReferenceProvider getClassReferenceProvider() {
        return null;
    }

    @Nullable
    public SliceDataTypeElement findClass(FQN className) {
        return null;
    }

    @Nullable
    public SliceModule findPackage(String packageName) {
        return null;
    }

    @Nullable
    public SliceMethodDef findClassMethod(FQN className, String methodName, int paramCount) {
        return null;
    }

    @NotNull
    public List<NavigatablePsiElement> getClassMethods(FQN className, boolean staticMethods) {
        return Collections.emptyList();
    }

    @NotNull
    public List<String> getMethodTypes(NavigatablePsiElement method) {
        return Collections.singletonList("void");
    }

    @NotNull
    public List<String> getAnnotations(NavigatablePsiElement element) {
        return Collections.emptyList();
    }

    private static class Impl extends SliceModuleHelper {
        private final SlicePsiFacade myFacade;

        private Impl(Module module, SlicePsiFacade facade) {
            super(module);
            myFacade = facade;
        }

        @Override
        public PsiReferenceProvider getClassReferenceProvider() {
            JavaClassReferenceProvider provider = new JavaClassReferenceProvider();
            provider.setSoft(false);
            return provider;
        }

        @Override
        public SliceDataTypeElement findClass(FQN className) {
            if (className == null) {
                return null;
            }
            return myFacade.findClass(className, GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module));
        }

        @Override
        public SliceModule findPackage(String packageName) {
            return myFacade.findPackage(packageName);
        }

        @Override
        public SliceMethodDef findClassMethod(FQN className, String methodName, int paramCount) {
            SliceDataTypeElement aClass = findClass(className);
            if (aClass == null) {
                return null;
            }

            final List<SliceMethodDef> methods = SliceUtil.getMethodList(aClass);
            if (methods == null) {
                return null;
            }

            for (SliceMethodDef method : methods) {
                if (paramCount < 0) {
                    return method;
                } else {
                    final int paramAmount;
                    final SliceParametersList list = method.getParametersList();
                    if (list != null) {
                        paramAmount = list.getParameterDefList().size();
                    } else {
                        paramAmount = 0;
                    }
                    if (paramCount == paramAmount) {
                        return method;
                    }
                }
            }
            return !methods.isEmpty() ? methods.get(0) : null;
        }

        @NotNull
        @Override
        public List<NavigatablePsiElement> getClassMethods(FQN className, boolean staticMethods) {
            SliceDataTypeElement aClass = findClass(className);
            if (aClass == null) return Collections.emptyList();
            final ArrayList<NavigatablePsiElement> result = new ArrayList<>();
//            for (PsiMethod method : aClass.getAllMethods()) {
//                PsiModifierList modifierList = method.getModifierList();
//                if (modifierList.hasExplicitModifier(PsiModifier.PUBLIC) &&
//                        staticMethods == modifierList.hasExplicitModifier(PsiModifier.STATIC)) {
//                    result.add(method);
//                }
//            }
            return result;
        }

        @NotNull
        @Override
        public List<String> getMethodTypes(NavigatablePsiElement method) {
            if (method == null) {
                return Collections.emptyList();
            }
            PsiMethod psiMethod = (PsiMethod) method;
            PsiType returnType = psiMethod.getReturnType();
            List<String> strings = new ArrayList<>();
            strings.add(returnType == null ? "void" : returnType.getCanonicalText());
            for (PsiParameter parameter : psiMethod.getParameterList().getParameters()) {
                strings.add(parameter.getType().getCanonicalText());
                strings.add(parameter.getName());
            }
            return strings;
        }

        @NotNull
        @Override
        public List<String> getAnnotations(NavigatablePsiElement element) {
            if (element == null) {
                return Collections.emptyList();
            }
            PsiModifierList modifierList = ((PsiModifierListOwner) element).getModifierList();
            if (modifierList == null) {
                return super.getAnnotations(element);
            }
            List<String> strings = new ArrayList<>();
            for (PsiAnnotation annotation : modifierList.getAnnotations()) {
                if (annotation.getParameterList().getAttributes().length > 0) {
                    continue;
                }
                strings.add(annotation.getQualifiedName());
            }
            return strings;
        }
    }
}
