package org.xblackcat.frozenice.psi.element;

import org.jetbrains.annotations.NotNull;

/**
 * 06.02.12 15:12
 *
 * @author xBlackCat
 */
public interface SliceModule extends SliceNamedElement {
    @NotNull
    SliceTypeDeclaration[] getContainedTypes();

}
