package org.xblackcat.frozenidea.completion;

import com.intellij.codeInsight.TailType;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.AutoCompletionPolicy;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.lookup.TailTypeDecorator;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * 30.01.12 16:59
 *
 * @author xBlackCat
 */
public class SliceBasicCompletionContributor extends CompletionContributor {
    private final String[] keywords = new String[]{
            "class", "interface", "exception", "struct", "enum", "sequence", "dictionary", "module"};

    public SliceBasicCompletionContributor() {
        extend(CompletionType.BASIC, SlicePatterns.moduleBody(), new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(
                    @NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result
            ) {
                for (String kw : keywords) {
                    result.addElement(
                            TailTypeDecorator.withTail(
                                    LookupElementBuilder.create(kw).withBoldness(true).withAutoCompletionPolicy(AutoCompletionPolicy.GIVE_CHANCE_TO_OVERWRITE),
                                    TailType.SPACE
                            )
                    );
                }
            }
        });
    }
}
