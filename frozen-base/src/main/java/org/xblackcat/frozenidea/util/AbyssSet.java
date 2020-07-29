package org.xblackcat.frozenidea.util;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * Always empty set with consuming all elements (methods {@linkplain #add(Object)} and {@linkplain #addAll(Collection)} always returns true)
 */
public class AbyssSet<E> extends AbstractSet<E> {
    @NotNull
    @Override
    public Iterator<E> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public boolean add(E e) {
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return true;
    }

    @Override
    public int size() {
        return 0;
    }
}
