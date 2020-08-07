package org.xblackcat.frozenidea.psi;

import org.jetbrains.annotations.Nullable;

public interface SliceInnerElement extends SliceNamedElement {
    SliceDataTypeElement getContainingClass();

    @Nullable
    default SliceMetadata getMetadata() {
        return null;
    }
}
