package org.xblackcat.frozenidea.completion;

import com.intellij.codeInsight.TailType;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.AutoCompletionPolicy;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.lookup.TailTypeDecorator;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * 30.01.12 16:59
 *
 * @author xBlackCat
 */
public class SliceBasicCompletionContributor extends CompletionContributor {
    private static final TailType GROUP_TAIL_TYPE = new PointyBracketsTailType() {
        @Override
        protected boolean isSpaceBeforeParentheses(
                CommonCodeStyleSettings styleSettings, Editor editor, int tailOffset
        ) {
            return false;
        }

        @Override
        protected boolean isSpaceWithinParentheses(
                CommonCodeStyleSettings styleSettings, Editor editor, int tailOffset
        ) {
            return false;
        }
    };

    private static final String[] keywords = new String[]{
            "class", "interface", "exception", "struct", "enum", "module"};
    private static final String[] keywordsGR = new String[]{
            "sequence", "dictionary"};

    public SliceBasicCompletionContributor() {
        extend(CompletionType.BASIC, SlicePatterns.elementDef(), new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(
                    @NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result
            ) {
                result.addElement(
                        TailTypeDecorator.withTail(
                                LookupElementBuilder.create("extends")
                                                    .withBoldness(true)
                                                    .withAutoCompletionPolicy(AutoCompletionPolicy.GIVE_CHANCE_TO_OVERWRITE),
                                TailType.SPACE
                        )
                );
                result.addElement(
                        TailTypeDecorator.withTail(
                                LookupElementBuilder.create("implements")
                                                    .withBoldness(true)
                                                    .withAutoCompletionPolicy(AutoCompletionPolicy.GIVE_CHANCE_TO_OVERWRITE),
                                TailType.SPACE
                        )
                );
            }
        });
        extend(CompletionType.BASIC, SlicePatterns.moduleBody(), new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(
                    @NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result
            ) {
                for (String kw : keywords) {
                    result.addElement(
                            TailTypeDecorator.withTail(
                                    LookupElementBuilder.create(kw)
                                                        .withBoldness(true)
                                                        .withAutoCompletionPolicy(AutoCompletionPolicy.GIVE_CHANCE_TO_OVERWRITE),
                                    TailType.SPACE
                            )
                    );
                }
                for (String kw : keywordsGR) {
                    result.addElement(
                            TailTypeDecorator.withTail(
                                    LookupElementBuilder.create(kw)
                                                        .withBoldness(true)
                                                        .withAutoCompletionPolicy(AutoCompletionPolicy.GIVE_CHANCE_TO_OVERWRITE),
                                    GROUP_TAIL_TYPE
                            )
                    );
                }
            }
        });
    }
}
