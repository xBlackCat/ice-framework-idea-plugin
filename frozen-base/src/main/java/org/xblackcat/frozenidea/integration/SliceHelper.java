package org.xblackcat.frozenidea.integration;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.usageView.UsageViewBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.config.IceComponent;
import org.xblackcat.frozenidea.psi.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 14.06.12 16:23
 *
 * @author xBlackCat
 */
public class SliceHelper {
    private static final String DEFAULT_FQN = UsageViewBundle.message("default.package.presentable.name");

    public static String packageName(SliceModulePath path) {
        final PsiElement[] children = path.getChildren();
        if (children.length == 0) {
            return null;
        }

        return "";
    }

    public static String getPackageName(PsiFile file, IceComponent target) {
        if (file == null) {
            return null;
        }
        final @Nullable SliceGlobalMetadata globalMetadata = PsiTreeUtil.getChildOfType(file, SliceGlobalMetadata.class);
        if (globalMetadata == null) {
            return null;
        }
        List<SliceGlobalMetadataStatement> globalMetadatas = globalMetadata.getGlobalMetadataStatementList();
        for (SliceGlobalMetadataStatement md : globalMetadatas) {
            for (SliceMetadataElement el : md.getMetadataElementList()) {
                final SliceStringLiteral literal = el.getStringLiteral();
                if (literal == null) {
                    continue;
                }
                final String text = literal.getText();
                String packageString = target.extractPackageName(text.substring(1, text.length() - 1));

                if (packageString != null) {
                    return packageString;
                }
            }
        }

        return null;
    }

    public static void collectName(StringBuilder res, SliceModule module) {
        if (module == null) {
            return;
        }
        final PsiElement id = module.getId();
        if (id == null) {
            return;
        }

        collectName(res, PsiTreeUtil.getParentOfType(module, SliceModule.class));

        res.append(id.getText());
        res.append(".");
    }

    public static String getFQN(SliceNamedElement element) {
        final PsiElement id = element.getId();
        if (id == null) {
            return DEFAULT_FQN;
        }
        String name = id.getText();

        if (name == null) {
            return DEFAULT_FQN;
        }

        SliceModule module = getContainerSliceModule(element);
        if (module == null) {
            return DEFAULT_FQN;
        }
        return buildFQN(name, module);
    }

    public static
    @NotNull
    String buildFQN(String name, SliceModule module) {
        String packageName = SliceHelper.getPackageName(module.getContainingFile(), IceComponent.Java);

        StringBuilder fqn = new StringBuilder();
        if (packageName != null) {
            fqn.append(packageName);
            fqn.append(".");
        }

        SliceHelper.collectName(fqn, module);

        fqn.append(name);

        return fqn.toString();
    }

    public static SliceModule getContainerSliceModule(SliceNamedElement element) {
        SliceModule module;
        if (element instanceof SliceModule) {
            module = PsiTreeUtil.getParentOfType(element, SliceModule.class);
        } else if (element instanceof SliceDataTypeElement) {
            module = ((SliceDataTypeElement) element).getModule();
        } else {
            module = null;
        }
        return module;
    }

    public static void findAllSubclasses(
            Set<SliceDataTypeElement> elements,
            SliceModule module,
            SliceDataTypeElement element
    ) {
        final HashSet<SliceDataTypeElement> set = new HashSet<>();
        set.add(element);
        findAllSubclasses(elements, module, set);
    }

    private static void findAllSubclasses(Set<SliceDataTypeElement> elements, SliceModule module, Set<SliceDataTypeElement> parents) {
        final SliceModuleBody body = module.getModuleBody();
        if (body == null) {
            return;
        }
        for (SliceDataTypeElement clazz : body.getDataTypeElementList()) {
            if (!clazz.isClass()) {
                continue;
            }

            if (clazz.getBodyBlock() == null) {
                // Ignore forward definitions
                continue;
            }

            if (parents.contains(clazz)) {
                continue;
            }

            SliceExtendsBlock extendsDef = clazz.getExtendsBlock();
            if (extendsDef == null) {
                continue;
            }
            for (SliceTypeReference tr : extendsDef.getTypeReferenceList()) {
                final PsiElement psiElement = tr.getReference().resolve();

                if (psiElement instanceof SliceDataTypeElement) {
                    if (parents.contains(psiElement)) {
                        elements.add(clazz);
                        parents.add(clazz);
                        break;
                    }
                }
            }
        }
    }

    public static void findAllImplementations(Set<SliceDataTypeElement> elements, SliceModule module, SliceDataTypeElement element) {
        Set<SliceDataTypeElement> supers = new HashSet<>();

        Set<SliceDataTypeElement> hierarchy = new HashSet<>();
        hierarchy.add(element);

        final SliceModuleBody body = module.getModuleBody();
        if (body == null) {
            return;
        }

        final List<SliceDataTypeElement> typeDefList = body.getDataTypeElementList();
        for (SliceDataTypeElement type : typeDefList) {
            if (type.getBodyBlock() == null) {
                // Ignore forward definitions
                continue;
            }

            if (type.isInterface()) {
                if (type.equals(element)) {
                    continue;
                }

                SliceExtendsBlock extendsDef = type.getExtendsBlock();
                searchInReferenceList(elements, hierarchy, hierarchy, type, extendsDef);
            }

            if (type.isClass()) {
                SliceImplementsBlock implementsDef = type.getImplementsBlock();
                searchInReferenceList(elements, supers, hierarchy, type, implementsDef);
            }
        }

        findAllSubclasses(elements, module, supers);
    }

    private static void searchInReferenceList(
            Set<SliceDataTypeElement> elements,
            Set<SliceDataTypeElement> supers,
            Set<SliceDataTypeElement> hierarchy,
            SliceDataTypeElement type,
            SliceReferenceListElement element
    ) {
        if (element != null) {
            for (SliceTypeReference tr : element.getTypeReferenceList()) {
                final PsiElement psiElement = tr.getReference().resolve();

                if (psiElement instanceof SliceDataTypeElement) {
                    if (hierarchy.contains(psiElement)) {
                        elements.add(type);
                        supers.add(type);
                    }
                }
            }
        }
    }
}
