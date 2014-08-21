package org.xblackcat.frozenidea.completion;

import com.intellij.codeInsight.TailType;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.lookup.TailTypeDecorator;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * 09.02.12 12:11
 *
 * @author xBlackCat
 */
public class SliceMetadataCompletionContributor extends CompletionContributor {
    public SliceMetadataCompletionContributor() {
        extend(CompletionType.BASIC, PsiIcePatterns.metadataDirective(), new MetadataDirectivesProvider());
    }

    @Override
    public void fillCompletionVariants(CompletionParameters parameters, CompletionResultSet result) {
        super.fillCompletionVariants(parameters, result);
    }

    private static class MetadataDirectivesProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(
                @NotNull CompletionParameters parameters,
                ProcessingContext context,
                @NotNull CompletionResultSet result
        ) {
            for (MetadataDirective d : MetadataDirective.DIRECTIVES) {
                if (d.isHasArgument()) {
                    result.addElement(
                            TailTypeDecorator.withTail(
                                    LookupElementBuilder.create(d.getDirective()),
                                    TailType.CASE_COLON
                            )
                    );
                } else {
                    result.addElement(
                            TailTypeDecorator.withTail(
                                    LookupElementBuilder.create(d.getDirective()),
                                    TailType.createSimpleTailType('"')
                            )
                    );
                }
            }
        }
    }
}
