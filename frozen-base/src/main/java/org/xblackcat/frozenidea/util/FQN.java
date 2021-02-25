package org.xblackcat.frozenidea.util;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.psi.SliceModule;
import org.xblackcat.frozenidea.psi.SliceNamedElement;
import org.xblackcat.frozenidea.psi.SliceTypeReference;

import java.util.*;

public class FQN {
    public static final FQN EMPTY = new FQN();
    private final String[] elements;

    private FQN(String... elements) {
        this.elements = elements;
    }

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
            return new FQN(copy);
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

    public static FQN buildFQN(String qualifiedName) {
        return buildFQN(qualifiedName, "::");
    }

    public static FQN buildFQN(String qualifiedName, String separator) {
        if (qualifiedName == null) {
            return null;
        }
        return new FQN(StringUtil.split(qualifiedName, separator).toArray(new String[0]));
    }

    public static FQN of(Collection<String> parts) {
        return new FQN(parts.toArray(new String[0]));
    }

    public String[] getModules() {
        return Arrays.copyOf(elements, elements.length - 1);
    }

    public String getName() {
        return elements[elements.length - 1];
    }

    public boolean startWith(FQN path) {
        return path != null && startWith(path.elements);
    }

    public boolean startWith(String[] path) {
        if (path == null) {
            return false;
        }
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
        return getFQN();
    }

    public String getFQN() {
        return String.join("::", elements);
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
        return new FQN(getModules());
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

    public FQN with(String name) {
        final String[] parts = Arrays.copyOf(elements, elements.length + 1);
        parts[elements.length] = name;
        return new FQN(parts);
    }
}
