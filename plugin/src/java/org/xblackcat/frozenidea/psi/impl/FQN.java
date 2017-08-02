package org.xblackcat.frozenidea.psi.impl;

import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.psi.SliceModule;
import org.xblackcat.frozenidea.psi.SliceNamedElement;
import org.xblackcat.frozenidea.psi.SliceTypeReference;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * 02.08.2017 14:52
 *
 * @author xBlackCat
 */
public class FQN {
    public static FQN buildFQN(String ref, @NotNull SliceModule module) {
        if (ref.contains("::")) {
            return new FQN(ref.split("::"));
        } else {
            Deque<String> fqn = new ArrayDeque<>(10);
            fqn.addFirst(ref);

            SliceModule m = module;
            do {
                fqn.addFirst(m.getName());
                m = PsiTreeUtil.getParentOfType(m, SliceModule.class);
            } while (m != null);

            return new FQN(fqn.toArray(new String[0]));
        }
    }

    public static FQN buildFQN(SliceTypeReference ref) {
        final String text = ref.getModulePath().getText();
        if (text != null && !text.isEmpty()) {
            final String[] elements = text.split("::");
            final String[] copy = Arrays.copyOf(elements, elements.length + 1);
            copy[copy.length - 1] = ref.getId().getText();
            return new FQN(elements);
        }

        return new FQN(ref.getId().getText());
    }

    public static FQN buildFQN(SliceNamedElement element) {
        Deque<String> fqn = new ArrayDeque<>(10);
        fqn.addFirst(element.getName());

        SliceModule m = PsiTreeUtil.getParentOfType(element, SliceModule.class);
        while (m != null) {
            fqn.addFirst(m.getName());
            m = PsiTreeUtil.getParentOfType(m, SliceModule.class);
        }

        return new FQN(fqn.toArray(new String[0]));
    }

    private final String[] elements;

    public FQN(String... elements) {
        this.elements = elements;
    }

    public String[] getModules() {
        return Arrays.copyOf(elements, elements.length - 1);
    }

    public String getName() {
        return elements[elements.length - 1];
    }


    @Override
    public String toString() {
        return "FQN: " + getFQN();
    }

    public String getFQN() {
        return String.join("::", elements);
    }

    public String getJavaFQN() {
        return String.join(".", elements);
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
}
