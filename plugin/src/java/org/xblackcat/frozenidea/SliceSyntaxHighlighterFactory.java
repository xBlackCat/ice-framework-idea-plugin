package org.xblackcat.frozenidea;

import com.intellij.openapi.fileTypes.SingleLazyInstanceSyntaxHighlighterFactory;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.syntax.SliceSyntaxHighlighter;

/**
 * 04.01.12 12:07
 *
 * @author xBlackCat
 */
public class SliceSyntaxHighlighterFactory extends SingleLazyInstanceSyntaxHighlighterFactory {
    @NotNull
    @Override
    protected SyntaxHighlighter createHighlighter() {
        return new SliceSyntaxHighlighter();
    }
}
