package org.xblackcat.frozenidea.psi.impl;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.config.IceComponent;
import org.xblackcat.frozenidea.integration.SliceHelper;
import org.xblackcat.frozenidea.psi.SliceModule;
import org.xblackcat.frozenidea.psi.SliceNamedElement;
import org.xblackcat.frozenidea.psi.SliceTypeReference;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.StringJoiner;

/**
 * 02.08.2017 14:52
 *
 * @author xBlackCat
 */
public class FQN {
    public static FQN buildFQN(String ref, @NotNull SliceModule module) {
        if (ref.contains("::")) {
            return new FQN(module.getContainingFile(), ref.split("::"));
        } else {
            Deque<String> fqn = new ArrayDeque<>(10);
            fqn.addFirst(ref);

            SliceModule m = module;
            do {
                fqn.addFirst(m.getName());
                m = PsiTreeUtil.getParentOfType(m, SliceModule.class);
            } while (m != null);

            return new FQN(module.getContainingFile(), fqn.toArray(new String[0]));
        }
    }

    public static FQN buildFQN(SliceTypeReference ref) {
        final String text = ref.getModulePath().getText();
        if (text != null && !text.isEmpty()) {
            final String[] elements = text.split("::");
            final String[] copy = Arrays.copyOf(elements, elements.length + 1);
            copy[copy.length - 1] = ref.getId().getText();
            return new FQN(ref.getContainingFile(), copy);
        }

        return new FQN(ref.getContainingFile(), ref.getId().getText());
    }

    public static FQN buildFQN(SliceNamedElement element) {
        Deque<String> fqn = new ArrayDeque<>(10);
        fqn.addFirst(element.getName());

        SliceModule m = PsiTreeUtil.getParentOfType(element, SliceModule.class);
        while (m != null) {
            fqn.addFirst(m.getName());
            m = PsiTreeUtil.getParentOfType(m, SliceModule.class);
        }

        return new FQN(element.getContainingFile(), fqn.toArray(new String[0]));
    }

    @Nullable
    public static FQN buildFQN(@NotNull PsiClass javaPsiClass) {
        final String qualifiedName = javaPsiClass.getQualifiedName();
        if (qualifiedName == null) {
            return null;
        }
        return new FQN(null, StringUtil.split(qualifiedName, ".").toArray(new String[0]));
    }

    private final PsiFile psiFile;
    private final String[] elements;

    private FQN(PsiFile psiFile, String... elements) {
        this.psiFile = psiFile;
        this.elements = elements;
    }

    public String[] getModules() {
        return Arrays.copyOf(elements, elements.length - 1);
    }

    public String getName() {
        return elements[elements.length - 1];
    }

    public FQN withNewName(String newName) {
        final FQN fqn = new FQN(psiFile, elements.clone());
        fqn.elements[fqn.elements.length - 1] = newName;
        return fqn;
    }

    public boolean startWith(FQN path) {
        return startWith(path.elements);
    }

    public boolean startWith(String[] path) {
        if (path.length >= this.elements.length) {
            return false;
        }

        int i = path.length - 1;
        while (i >= 0) {
            if (!path[i].equals(this.elements[i])) {
                return false;
            }
            i--;
        }

        return true;
    }

    @Override
    public String toString() {
        return "FQN: " + getFQN();
    }

    public String getFQN() {
        return String.join("::", elements);
    }

    public String getJavaFQN() {
        final String packageName = SliceHelper.getPackageName(psiFile, IceComponent.Java);
        if (packageName != null && !packageName.isEmpty()) {
            return packageName + "." + String.join(".", elements);
        } else {
            return String.join(".", elements);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FQN fqn = (FQN) o;
        return Arrays.equals(elements, fqn.elements);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(elements);
    }

    /**
     * Returns a path object. If the FQN object represents a root element null will be returned
     *
     * @return path FQN object or null if this object is top level object
     */
    public FQN getPath() {
        if (elements.length == 1) {
            return null;
        }
        return new FQN(psiFile, getModules());
    }

    public String getPathString() {
        // Number of elements not likely worth Arrays.stream overhead.
        StringJoiner joiner = new StringJoiner("::");
        int i = 0, elementsLength = elements.length - 1;
        while (i < elementsLength) {
            final String cs = elements[i];
            joiner.add(cs);
            i++;
        }
        return joiner.toString();
    }
}
