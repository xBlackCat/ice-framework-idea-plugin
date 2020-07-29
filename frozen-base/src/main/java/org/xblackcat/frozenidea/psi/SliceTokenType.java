package org.xblackcat.frozenidea.psi;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.SliceLanguage;

/**
 * 08.06.12 14:49
 *
 * @author xBlackCat
 */
public class SliceTokenType extends IElementType {
    public SliceTokenType(@NotNull @NonNls String debugName) {
        super(debugName, SliceLanguage.INSTANCE);
    }
}
