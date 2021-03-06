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

package org.xblackcat.frozenidea.integration.java;

import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassReferenceProvider;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.psi.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author gregsh
 */
public class JavaModuleHelper {
    protected Module module;

    public JavaModuleHelper(Module module) {
        this.module = module;
    }

    public static JavaModuleHelper getJavaHelper(Module module) {
        JavaModuleHelper service = module.getService(JavaModuleHelper.class);
        return service == null ? new JavaModuleHelper(module) : service;
    }

    public static JavaModuleHelper getJavaHelper(PsiElement element) {
        Module module = ModuleUtil.findModuleForPsiElement(element);

        if (module != null) {
            JavaModuleHelper service = module.getService(JavaModuleHelper.class);

            if (service != null) {
                return service;
            }
        }

        return new JavaModuleHelper(module);
    }

    @Nullable
    public PsiReferenceProvider getClassReferenceProvider() {
        return null;
    }

    @Nullable
    public PsiClass findClass(SliceDataTypeElement element) {
        return null;
    }

    @Nullable
    public PsiClass findClass(SliceModule element, String name) {
        return null;
    }

    @Nullable
    public NavigationItem findPackage(String packageName) {
        return null;
    }

    @NotNull
    public List<String> getMethodTypes(NavigatablePsiElement method) {
        return Collections.singletonList("void");
    }

    @NotNull
    public List<String> getAnnotations(NavigatablePsiElement element) {
        return Collections.emptyList();
    }

    public PsiElement[] findClassMethod(SliceMethodDef methodDef) {
        return null;
    }

    public PsiElement findClassField(SliceDataTypeElement className, String name) {
        return null;
    }

    private static class Impl extends JavaModuleHelper {
        private final JavaPsiFacade myFacade;

        private Impl(Module module, JavaPsiFacade facade) {
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
        public @Nullable PsiClass findClass(SliceDataTypeElement element) {
            final String packageName = JavaPsiUtil.getJavaPackageName(element.getModule());
            if (packageName == null) {
                return null;
            }
            return findClass(packageName + "." + element.getName());
        }

        @Override
        public @Nullable PsiClass findClass(SliceModule element, String name) {
            final String packageName = JavaPsiUtil.getJavaPackageName(element);
            if (packageName == null) {
                return null;
            }
            return findClass(packageName + "." + name);
        }

        public @Nullable PsiClass findClass(String className) {
            if (className == null) {
                return null;
            }
            return myFacade.findClass(className, GlobalSearchScope.moduleWithDependentsScope(module));
        }

        @Override
        public NavigationItem findPackage(String packageName) {
            return myFacade.findPackage(packageName);
        }

        @Override
        public PsiElement findClassField(SliceDataTypeElement className, String name) {
            PsiClass aClass = findClass(className);
            if (aClass == null) {
                return null;
            }

            return aClass.findFieldByName(name, false);
        }

        @Override
        public PsiElement[] findClassMethod(SliceMethodDef methodDef) {
            PsiClass iceClass = myFacade.findClass(
                    "com.zeroc.Ice.Current",
                    GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module)
            );
            if (iceClass == null) {
                return null;
            }

            final List<PsiElement> result = new ArrayList<>();
            final SliceDataTypeElement containingClass = methodDef.getContainingClass();
            PsiClass aClass = findClass(containingClass);
            if (aClass != null) {
                checkObjectMethods(methodDef, iceClass, result, aClass, "");
            }

            PsiClass aClassPrx = findClass(containingClass.getModule(), containingClass.getName() + "Prx");
            if (aClassPrx != null) {
                checkProxyMethods(methodDef, iceClass, result, aClassPrx, "");
                checkProxyMethods(methodDef, iceClass, result, aClassPrx, "Async");
            }

            return result.toArray(PsiElement.EMPTY_ARRAY);
        }

        private void checkObjectMethods(
                SliceMethodDef methodDef,
                PsiClass iceClass,
                List<PsiElement> result,
                PsiClass aClass,
                String suffix
        ) {
            PsiMethod[] methods = aClass.findMethodsByName(methodDef.getName() + suffix, true);
            final SliceParametersList parametersList = methodDef.getParametersList();
            final int paramsCount = parametersList == null ? 0 : parametersList.getParameterDefList().size();


            for (PsiMethod method : methods) {
                final PsiParameter[] javaParameters = method.getParameterList().getParameters();
                if (javaParameters.length == paramsCount + 1) {
                    if (!javaParameters[paramsCount].getType().getCanonicalText().equals(iceClass.getQualifiedName())) {
                        continue;
                    }

                    if (checkMethodParams(iceClass, parametersList, paramsCount, javaParameters)) {
                        continue;
                    }

                    result.add(method);
                }
            }
        }

        private void checkProxyMethods(
                SliceMethodDef methodDef,
                PsiClass iceClass,
                List<PsiElement> result,
                PsiClass aClassPrx,
                String suffix
        ) {
            PsiMethod[] methods = aClassPrx.findMethodsByName(methodDef.getName() + suffix, true);
            final SliceParametersList parametersList = methodDef.getParametersList();
            final int paramsCount = parametersList == null ? 0 : parametersList.getParameterDefList().size();


            for (PsiMethod method : methods) {
                final PsiParameter[] javaParameters = method.getParameterList().getParameters();
                if (javaParameters.length == paramsCount + 1) {
                    if (!javaParameters[paramsCount].getType().getCanonicalText().equals("java.util.Map<java.lang.String,java.lang.String>")) {
                        continue;
                    }
                } else if (javaParameters.length != paramsCount) {
                    continue;
                }

                if (checkMethodParams(iceClass, parametersList, paramsCount, javaParameters)) {
                    continue;
                }

                result.add(method);
            }
        }

        private boolean checkMethodParams(
                PsiClass iceClass,
                SliceParametersList parametersList,
                int paramsCount,
                PsiParameter[] javaParameters
        ) {
            for (int i = 0; i < paramsCount; i++) {
                final SliceDataType dataType = parametersList.getParameterDefList().get(i).getDataType();
                final String type = JavaHelper.getJavaHelper(iceClass.getProject()).toJavaParameter(dataType);

                if (!javaParameters[i].getType().getCanonicalText().equals(type)) {
                    return true;
                }
            }
            return false;
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
