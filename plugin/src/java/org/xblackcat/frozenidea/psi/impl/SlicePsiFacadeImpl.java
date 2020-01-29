package org.xblackcat.frozenidea.psi.impl;

import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.xblackcat.frozenidea.IceFileType;
import org.xblackcat.frozenidea.config.IceComponent;
import org.xblackcat.frozenidea.integration.SliceHelper;
import org.xblackcat.frozenidea.psi.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 10.09.13 10:07
 *
 * @author xBlackCat
 */
public class SlicePsiFacadeImpl extends SlicePsiFacade {
    private final Project myProject;

    public SlicePsiFacadeImpl(Project myProject) {
        this.myProject = myProject;
    }


    @Override
    public SliceDataTypeElement findClass(FQN className, GlobalSearchScope scope) {
        ProgressIndicatorProvider.checkCanceled(); // We hope this method is being called often enough to cancel daemon processes smoothly

        List<SliceDataTypeElement> result = new ArrayList<>();

        Collection<VirtualFile> virtualFiles =
                FileTypeIndex.getFiles(IceFileType.INSTANCE, scope);
        for (VirtualFile virtualFile : virtualFiles) {
            SliceFile sliceFile = (SliceFile) PsiManager.getInstance(myProject).findFile(virtualFile);
            if (sliceFile != null) {
                final String javaPackageName = SliceHelper.getPackageName(sliceFile, IceComponent.Java);
                int idx = 0;
                if (javaPackageName != null) {
                    final String[] path = StringUtils.split(javaPackageName, ".");
                    if (!className.startWith(path)) {
                        continue;
                    }
                    idx = path.length;
                }

                final String[] modules = className.getModules();
                final PsiElement module = findModule(sliceFile, modules, idx);
                if (module != null) {
                    final SliceDataTypeElement[] types = PsiTreeUtil.getChildrenOfType(module, SliceDataTypeElement.class);
                    if (types != null) {
                        for (SliceDataTypeElement e : types) {
                            if (className.getName().equals(e.getName())) {
                                result.add(e);
                            }
                        }
                    }
                }
            }
        }

        for (SliceDataTypeElement e : result) {
            if (e instanceof SliceClassDef) {
                if (((SliceClassDef) e).getClassBody() != null) {
                    return e;
                }
            } else if (e instanceof SliceInterfaceDef) {
                if (((SliceInterfaceDef) e).getInterfaceBody() != null) {
                    return e;
                }
            }
        }
        return null;
    }

    private PsiElement findModule(PsiElement root, String[] parts, int idx) {
        if (idx >= parts.length) {
            return root;
        }
        String part = parts[idx];
        final SliceModule[] types = PsiTreeUtil.getChildrenOfType(root, SliceModule.class);
        if (types != null) {
            for (SliceModule e : types) {
                if (part.equals(e.getName())) {
                    return findModule(e, parts, idx + 1);
                }
            }
        }
        return null;
    }

    @Override
    protected Project getProject() {
        return myProject;
    }

    @Override
    public SliceModule findPackage(String packageName) {
        return null;
    }

}
