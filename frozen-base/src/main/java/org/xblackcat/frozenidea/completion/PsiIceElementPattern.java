package org.xblackcat.frozenidea.completion;

import com.intellij.patterns.InitialPatternCondition;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * 09.02.12 12:16
 *
 * @author xBlackCat
 */
public class PsiIceElementPattern<T extends PsiElement, Self extends PsiIceElementPattern<T, Self>> extends PsiElementPattern<T, Self> {
    public PsiIceElementPattern(Class<T> tClass) {
        super(tClass);
    }

    public PsiIceElementPattern(@NotNull InitialPatternCondition<T> tInitialPatternCondition) {
        super(tInitialPatternCondition);
    }

    public static class Capture<T extends PsiElement> extends PsiIceElementPattern<T, Capture<T>> {
        public Capture(final Class<T> aClass) {
            super(aClass);
        }

        public Capture(@NotNull final InitialPatternCondition<T> condition) {
            super(condition);
        }
    }
}
