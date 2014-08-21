package org.xblackcat.frozenidea.parser;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.LookAheadLexer;

/**
 * 12.06.12 11:24
 *
 * @author xBlackCat
 */
public class IceLexer extends LookAheadLexer {
    public IceLexer() {
        super(new FlexAdapter(new _IceLexer()));
    }
}
