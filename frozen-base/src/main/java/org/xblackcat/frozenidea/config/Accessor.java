package org.xblackcat.frozenidea.config;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *
 */
public interface Accessor<T> extends Supplier<T>, Consumer<T> {
}
