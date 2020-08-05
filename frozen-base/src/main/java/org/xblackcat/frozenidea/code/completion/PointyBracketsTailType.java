/*
 * Copyright 2000-2009 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.xblackcat.frozenidea.code.completion;

import com.intellij.application.options.CodeStyle;
import com.intellij.codeInsight.TailType;
import com.intellij.lang.Language;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.util.PsiUtilCore;

/**
 * @author peter
 */
public abstract class PointyBracketsTailType extends TailType {
    protected abstract boolean isSpaceBeforeParentheses(CommonCodeStyleSettings styleSettings, Editor editor, final int tailOffset);

    protected abstract boolean isSpaceWithinParentheses(CommonCodeStyleSettings styleSettings, Editor editor, final int tailOffset);

    @Override
    public int processTail(final Editor editor, int tailOffset) {
        CommonCodeStyleSettings styleSettings = getLocalCodeStyleSettings(editor, tailOffset);
        if (isSpaceBeforeParentheses(styleSettings, editor, tailOffset)) {
            tailOffset = insertChar(editor, tailOffset, ' ');
        }
        tailOffset = insertChar(editor, tailOffset, '<');
        if (isSpaceWithinParentheses(styleSettings, editor, tailOffset)) {
            tailOffset = insertChar(editor, tailOffset, ' ');
            tailOffset = insertChar(editor, tailOffset, ' ');
            tailOffset = insertChar(editor, tailOffset, '>');
            moveCaret(editor, tailOffset, -2);
        } else {
            tailOffset = insertChar(editor, tailOffset, '>');
            moveCaret(editor, tailOffset, -1);
        }
        return tailOffset;
    }

    protected static CommonCodeStyleSettings getLocalCodeStyleSettings(Editor editor, int tailOffset) {
        final Project project = editor.getProject();
        assert project != null;
        final PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
        assert psiFile != null;
        Language language = PsiUtilCore.getLanguageAtOffset(psiFile, tailOffset);

        return CodeStyle.getLanguageSettings(psiFile, language);
    }

}