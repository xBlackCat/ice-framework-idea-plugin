package org.xblackcat.frozenice.psi;

import com.intellij.psi.tree.IFileElementType;
import org.xblackcat.frozenice.SliceLanguage;

/**
 * 04.01.12 16:40
 *
 * @author xBlackCat
 */
public interface SliceElementTypes {
    IFileElementType FILE_ELEMENT_TYPE = new IFileElementType("SLICE", SliceLanguage.INSTANCE);
}
