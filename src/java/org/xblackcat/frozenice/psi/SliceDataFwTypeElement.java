package org.xblackcat.frozenice.psi;

/**
 * 14.06.12 14:34
 *
 * @author xBlackCat
 */
public interface SliceDataFwTypeElement extends SliceNamedElement {
    SliceModule getModule();

    SliceDataTypeElement getDeclaration();
}
