package org.xblackcat.frozenice.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * 30.01.12 16:59
 *
 * @author xBlackCat
 */
public class SliceCompletionContributor extends CompletionContributor {
    public SliceCompletionContributor() {
        extend(CompletionType.BASIC, PsiIcePatterns.moduleBody(), new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(
                    @NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result
            ) {

            }
        });

    }
}
