package org.xblackcat.frozenice.psi;

import com.intellij.psi.impl.source.tree.FileElement;

/**
 * 05.01.12 14:37
 *
 * @author xBlackCat
 */
public class IceFileElement extends FileElement {
    public IceFileElement(CharSequence text) {
        super(IceStubElementTypes.ICE_FILE, text);
    }
}
