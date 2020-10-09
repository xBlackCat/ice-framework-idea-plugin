package org.xblackcat.frozenidea.code.formatter;

import com.intellij.formatting.*;
import com.intellij.json.JsonLanguage;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.SliceLanguage;

import static org.xblackcat.frozenidea.psi.SliceTypes.*;

/**
 *
 */
public class SliceFormattingModelBuilder implements FormattingModelBuilder {
    @Override
    public @NotNull FormattingModel createModel(@NotNull FormattingContext formattingContext) {
        final CodeStyleSettings codeStyleSettings = formattingContext.getCodeStyleSettings();

        SpacingBuilder spacingBuilder = createSpacingBuilder(codeStyleSettings);
        return FormattingModelProvider.createFormattingModelForPsiFile(
                formattingContext.getContainingFile(),
                new SliceFileBlock(formattingContext.getNode(), spacingBuilder),
                codeStyleSettings
        );
    }

    private SpacingBuilder createSpacingBuilder(CodeStyleSettings settings) {
        final CommonCodeStyleSettings commonSettings = settings.getCommonSettings(JsonLanguage.INSTANCE);

        return new SpacingBuilder(settings, SliceLanguage.INSTANCE)
                .withinPair(ICE_LEFT_BRACKET, ICE_RIGHT_BRACKET).spaceIf(commonSettings.SPACE_WITHIN_BRACKETS, true)
                .withinPair(ICE_LEFT_BRACE, ICE_RIGHT_BRACE).spaceIf(commonSettings.SPACE_WITHIN_BRACES, true)
                .withinPair(ICE_LEFT_PARENTH, ICE_RIGHT_PARENTH).spaceIf(commonSettings.SPACE_WITHIN_METHOD_PARENTHESES, true)
                .before(ICE_COMMA).spaceIf(commonSettings.SPACE_BEFORE_COMMA)
                .after(ICE_COMMA).spaceIf(commonSettings.SPACE_AFTER_COMMA);
    }
}
