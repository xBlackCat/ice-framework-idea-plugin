package org.xblackcat.frozenice.psi;

import com.intellij.psi.tree.IElementType;
import org.xblackcat.frozenice.SliceLanguage;

/**
 * 08.06.12 14:49
 *
 * @author xBlackCat
 */
public class SliceCompositeElementType extends IElementType {
    public SliceCompositeElementType(@org.jetbrains.annotations.NotNull @org.jetbrains.annotations.NonNls String debugName) {
        super(debugName, SliceLanguage.INSTANCE);
    }
}
