package org.xblackcat.frozenidea;

import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;

/**
 * @author xBlackCat
 */
public class SliceLanguage extends Language {
    public static SliceLanguage INSTANCE = new SliceLanguage();

    private SliceLanguage() {
        super("SLICE", "text/slice", "application/x-slice", "text/x-slice");
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Slice";
    }

    @Override
    public boolean isCaseSensitive() {
        return true;
    }
}
