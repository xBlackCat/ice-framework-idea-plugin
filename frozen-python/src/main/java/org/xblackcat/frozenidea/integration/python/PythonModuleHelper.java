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

package org.xblackcat.frozenidea.integration.python;

import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.*;
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
public class PythonModuleHelper {
    protected Module module;

    public PythonModuleHelper(Module module) {
        this.module = module;
    }

    public static PythonModuleHelper getPythonHelper(Module module) {
        PythonModuleHelper service = module.getService(PythonModuleHelper.class);
        return service == null ? new PythonModuleHelper(module) : service;
    }

    public static PythonModuleHelper getPythonHelper(PsiElement element) {
        Module module = ModuleUtil.findModuleForPsiElement(element);

        if (module != null) {
            PythonModuleHelper service = module.getService(PythonModuleHelper.class);

            if (service != null) {
                return service;
            }
        }

        return new PythonModuleHelper(module);
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
    public List<String> getAnnotations(NavigatablePsiElement element) {
        return Collections.emptyList();
    }

    public PsiElement[] findClassMethod(SliceMethodDef methodDef) {
        return null;
    }

    public PsiElement findClassField(SliceDataTypeElement className, String name) {
        return null;
    }

    private static class Impl extends PythonModuleHelper {
        private final JavaPsiFacade myFacade;

        private Impl(Module module, JavaPsiFacade facade) {
            super(module);
            myFacade = facade;
        }


        @Override
        public @Nullable PsiClass findClass(SliceDataTypeElement element) {
            final String packageName = PythonPsiUtil.getPythonPackageName(element.getModule());
            if (packageName == null) {
                return null;
            }
            return findClass(packageName + "." + element.getName());
        }

        @Override
        public @Nullable PsiClass findClass(SliceModule element, String name) {
            final String packageName = PythonPsiUtil.getPythonPackageName(element);
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
                final PsiParameter[] pythonParameters = method.getParameterList().getParameters();
                if (pythonParameters.length == paramsCount + 1) {
                    if (!pythonParameters[paramsCount].getType().getCanonicalText().equals(iceClass.getQualifiedName())) {
                        continue;
                    }

                    if (checkMethodParams(iceClass, parametersList, paramsCount, pythonParameters)) {
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
                final PsiParameter[] pythonParameters = method.getParameterList().getParameters();
                if (pythonParameters.length == paramsCount + 1) {
                    if (!pythonParameters[paramsCount].getType().getCanonicalText().equals(
                            "python.util.Map<python.lang.String,python.lang.String>")) {
                        continue;
                    }
                } else if (pythonParameters.length != paramsCount) {
                    continue;
                }

                if (checkMethodParams(iceClass, parametersList, paramsCount, pythonParameters)) {
                    continue;
                }

                result.add(method);
            }
        }

        private boolean checkMethodParams(
                PsiClass iceClass,
                SliceParametersList parametersList,
                int paramsCount,
                PsiParameter[] pythonParameters
        ) {
            for (int i = 0; i < paramsCount; i++) {
                final SliceDataType dataType = parametersList.getParameterDefList().get(i).getDataType();
                final String type = "";

                if (!pythonParameters[i].getType().getCanonicalText().equals(type)) {
                    return true;
                }
            }
            return false;
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
