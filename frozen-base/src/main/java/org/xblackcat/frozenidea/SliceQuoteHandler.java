package org.xblackcat.frozenidea;

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler;

/**
 * 11.02.14 10:09
 *
 * @author xBlackCat
 */
public class SliceQuoteHandler extends SimpleTokenSetQuoteHandler {
    public SliceQuoteHandler() {
        super(ElementType.LITERALS);
    }
}
