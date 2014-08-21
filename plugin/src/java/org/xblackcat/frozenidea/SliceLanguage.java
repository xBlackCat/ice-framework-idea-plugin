package org.xblackcat.frozenidea;

import com.intellij.lang.Language;

/**
 * @author xBlackCat
 */
public class SliceLanguage extends Language {
    public static SliceLanguage INSTANCE = new SliceLanguage();

    public SliceLanguage() {
        super("SLICE", "text/slice", "application/x-slice", "text/x-slice");
    }

    @Override
    public String getDisplayName() {
        return "Slice";
    }

    @Override
    public boolean isCaseSensitive() {
        return true;
    }
}
