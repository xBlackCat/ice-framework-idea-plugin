package org.xblackcat.frozenidea.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import org.xblackcat.frozenidea.IceFileType;
import org.xblackcat.frozenidea.integration.SliceHelper;
import org.xblackcat.frozenidea.psi.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 24.03.2016 15:12
 *
 * @author xBlackCat
 */
public class SliceUtil {
    public static List<SliceDataTypeElement> findDataTypes(Project project, String fqn) {
        List<SliceDataTypeElement> result = null;
        Collection<VirtualFile> virtualFiles = FileTypeIndex.getFiles(
                IceFileType.INSTANCE,
                GlobalSearchScope.allScope(project)
        );
        for (VirtualFile virtualFile : virtualFiles) {
            SliceFile simpleFile = (SliceFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (simpleFile != null) {
                SliceDataTypeElement[] properties = PsiTreeUtil.getChildrenOfType(simpleFile, SliceDataTypeElement.class);
                if (properties != null) {
                    for (SliceDataTypeElement dataTypeElement : properties) {
                        if (fqn.equals(SliceHelper.getFQN(dataTypeElement))) {
                            if (result == null) {
                                result = new ArrayList<>();
                            }
                            result.add(dataTypeElement);
                        }
                    }
                }
            }
        }
        return result != null ? result : Collections.emptyList();
    }

    public static List<SliceDataTypeElement> findDataTypes(Project project) {
        List<SliceDataTypeElement> result = new ArrayList<>();
        Collection<VirtualFile> virtualFiles = FileTypeIndex.getFiles(
                IceFileType.INSTANCE,
                GlobalSearchScope.allScope(project)
        );
        for (VirtualFile virtualFile : virtualFiles) {
            SliceFile simpleFile = (SliceFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (simpleFile != null) {
                SliceDataTypeElement[] dataTypeElement = PsiTreeUtil.getChildrenOfType(simpleFile, SliceDataTypeElement.class);
                if (dataTypeElement != null) {
                    Collections.addAll(result, dataTypeElement);
                }
            }
        }
        return result;
    }

    public static List<SliceNamedElement> findNamedItems(Project project, String fqn) {
        List<SliceNamedElement> result = null;
        Collection<VirtualFile> virtualFiles = FileTypeIndex.getFiles(
                IceFileType.INSTANCE,
                GlobalSearchScope.allScope(project)
        );
        for (VirtualFile virtualFile : virtualFiles) {
            SliceFile simpleFile = (SliceFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (simpleFile != null) {
                SliceNamedElement[] properties = PsiTreeUtil.getChildrenOfType(simpleFile, SliceNamedElement.class);
                if (properties != null) {
                    for (SliceNamedElement dataTypeElement : properties) {
                        if (fqn.equals(dataTypeElement.getName())) {
                            if (result == null) {
                                result = new ArrayList<>();
                            }
                            result.add(dataTypeElement);
                        }
                    }
                }
            }
        }
        return result != null ? result : Collections.emptyList();
    }

    public static List<SliceNamedElement> findNamedItems(Project project) {
        List<SliceNamedElement> result = new ArrayList<>();
        Collection<VirtualFile> virtualFiles = FileTypeIndex.getFiles(
                IceFileType.INSTANCE,
                GlobalSearchScope.allScope(project)
        );
        for (VirtualFile virtualFile : virtualFiles) {
            SliceFile simpleFile = (SliceFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (simpleFile != null) {
                SliceNamedElement[] dataTypeElement = PsiTreeUtil.getChildrenOfType(simpleFile, SliceNamedElement.class);
                if (dataTypeElement != null) {
                    Collections.addAll(result, dataTypeElement);
                }
            }
        }
        return result;
    }

    public static List<SliceMethodDef> getMethodList(SliceDataTypeElement aClass) {
        if (aClass instanceof SliceClassDef) {
            final SliceClassBody body = ((SliceClassDef) aClass).getClassBody();
            if (body != null) {
                return body.getMethodDefList();
            } else {
                return null;
            }
        } else if (aClass instanceof SliceInterfaceDef) {
            final SliceInterfaceBody body = ((SliceInterfaceDef) aClass).getInterfaceBody();
            if (body != null) {
                return body.getMethodDefList();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}