package org.xblackcat.frozenidea.psi;

import org.jetbrains.annotations.Nullable;

public interface SliceVariableElement extends SliceNamedElement {
    @Nullable
    default SliceDataType getDataType() {
        return null;
    }

    @Nullable
    default SliceFieldInitializer getFieldInitializer() {
        return null;
    }
}
