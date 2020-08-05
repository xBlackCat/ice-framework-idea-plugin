package org.xblackcat.frozenidea.code.completion;

import com.intellij.codeInsight.TailType;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.AutoCompletionPolicy;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.lookup.TailTypeDecorator;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.psi.SliceModule;
import org.xblackcat.frozenidea.psi.SliceNamedElement;
import org.xblackcat.frozenidea.util.FQN;
import org.xblackcat.frozenidea.util.SlicePsiUtil;

import java.util.HashMap;
import java.util.Map;

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

    private static final String moduleKeyword = "module";
    private static final String constKeyword = "const";
    private static final String[] typeDefineKeywords = new String[]{
            "class", "interface", "exception", "struct", "enum", "sequence", "dictionary"};
    private static final String[] primitiveTypeKeywords = new String[]{
            "bool", "byte", "short", "int", "long", "float", "double", "string", "Object", "LocalObject"
    };

    public SliceBasicCompletionContributor() {
        extend(CompletionType.BASIC, SlicePatterns.parameterList(), new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(
                    @NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result
            ) {
                for (String kw : primitiveTypeKeywords) {
                    result.addElement(
                            TailTypeDecorator.withTail(
                                    LookupElementBuilder.create(kw)
                                            .withBoldness(true)
                                            .withAutoCompletionPolicy(AutoCompletionPolicy.GIVE_CHANCE_TO_OVERWRITE),
                                    TailType.SPACE
                            )
                    );
                }
            }
        });
        extend(CompletionType.BASIC, SlicePatterns.classBody(), new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(
                    @NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result
            ) {
                result.addElement(
                        TailTypeDecorator.withTail(
                                LookupElementBuilder.create("void")
                                        .withBoldness(true)
                                        .withAutoCompletionPolicy(AutoCompletionPolicy.GIVE_CHANCE_TO_OVERWRITE),
                                TailType.SPACE
                        )
                );
                for (String kw : primitiveTypeKeywords) {
                    result.addElement(
                            TailTypeDecorator.withTail(
                                    LookupElementBuilder.create(kw)
                                            .withBoldness(true)
                                            .withAutoCompletionPolicy(AutoCompletionPolicy.GIVE_CHANCE_TO_OVERWRITE),
                                    TailType.SPACE
                            )
                    );
                }
            }
        });
        extend(CompletionType.BASIC, SlicePatterns.classBodyNotReference(), new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(
                    @NotNull CompletionParameters parameters,
                    ProcessingContext context,
                    @NotNull CompletionResultSet result
            ) {
                final PsiElement position = parameters.getPosition();
                final SliceModule module = PsiTreeUtil.getParentOfType(position, SliceModule.class);
                final FQN moduleFQN;
                if (module != null) {
                    moduleFQN = FQN.buildFQN(module);
                } else {
                    moduleFQN = null;
                }

                Map<FQN, SliceNamedElement> references = new HashMap<>();
                SlicePsiUtil.findAll(position.getProject(), references, SlicePsiUtil.SLICE_DATA_DECLARATION_PREDICATE);

                references.forEach((key, value) -> result.addElement(TailTypeDecorator.withTail(
                        SlicePsiUtil.buildLookupElement(moduleFQN, key, value),
                        TailType.SPACE
                )));
            }
        });
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
                for (String kw : typeDefineKeywords) {
                    result.addElement(
                            TailTypeDecorator.withTail(
                                    LookupElementBuilder.create(kw)
                                            .withBoldness(true)
                                            .withAutoCompletionPolicy(AutoCompletionPolicy.GIVE_CHANCE_TO_OVERWRITE),
                                    TailType.SPACE
                            )
                    );
                }
                result.addElement(
                        TailTypeDecorator.withTail(
                                LookupElementBuilder.create(moduleKeyword)
                                        .withBoldness(true)
                                        .withAutoCompletionPolicy(AutoCompletionPolicy.GIVE_CHANCE_TO_OVERWRITE),
                                GROUP_TAIL_TYPE
                        )
                );
                result.addElement(
                        TailTypeDecorator.withTail(
                                LookupElementBuilder.create(constKeyword)
                                        .withBoldness(true)
                                        .withAutoCompletionPolicy(AutoCompletionPolicy.GIVE_CHANCE_TO_OVERWRITE),
                                GROUP_TAIL_TYPE
                        )
                );
            }
        });
        extend(CompletionType.BASIC, SlicePatterns.emptySliceFile(), new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(
                    @NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result
            ) {
                result.addElement(
                        TailTypeDecorator.withTail(
                                LookupElementBuilder.create(moduleKeyword)
                                        .withBoldness(true)
                                        .withAutoCompletionPolicy(AutoCompletionPolicy.GIVE_CHANCE_TO_OVERWRITE),
                                TailType.SPACE
                        )
                );
            }
        });
    }
}
