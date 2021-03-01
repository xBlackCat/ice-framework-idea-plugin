package org.xblackcat.frozenidea.psi.impl;

import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.xblackcat.frozenidea.IceFileType;
import org.xblackcat.frozenidea.config.IceComponent;
import org.xblackcat.frozenidea.integration.SliceHelper;
import org.xblackcat.frozenidea.psi.SliceDataTypeElement;
import org.xblackcat.frozenidea.psi.SliceFile;
import org.xblackcat.frozenidea.psi.SliceModule;
import org.xblackcat.frozenidea.psi.SlicePsiFacade;
import org.xblackcat.frozenidea.util.FQN;

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
                final String javaPackageName = SliceHelper.getPackageNameMetadata(sliceFile, IceComponent.Java);
                int idx = 0;
                if (javaPackageName != null) {
                    final String[] path = StringUtils.split(javaPackageName, ".");
                    if (!className.startWith(path)) {
                        continue;
                    }
                    idx = path.length;
                }

                SliceModule[] modules = PsiTreeUtil.getChildrenOfType(sliceFile, SliceModule.class);
                collectDataTypes(className, result, idx, modules);
            }
        }

        for (SliceDataTypeElement e : result) {
            if (e.getBodyBlock() != null) {
                if (e.isClass() || e.isInterface()) {
                    return e;
                }
            }
        }
        return null;
    }

    private void collectDataTypes(FQN className, List<SliceDataTypeElement> result, int idx, SliceModule[] modules) {
        final String[] fqnParts = className.getModules();
        PathLoop:
        while (idx < fqnParts.length) {
            if (modules == null) {
                return;
            }
            for (SliceModule module : modules) {
                if (fqnParts[idx].equals(module.getName())) {
                    idx++;
                    if (idx == fqnParts.length) {
                        for (SliceDataTypeElement e : module.getTypeDeclarations()) {
                            if (className.getName().equals(e.getName())) {
                                result.add(e);
                            }
                        }
                        return;
                    } else {
                        modules = module.getSubModules().toArray(SliceModule[]::new);
                        continue PathLoop;
                    }
                }
            }
            break;
        }
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
