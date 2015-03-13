package org.xblackcat.frozenidea.code;

import com.intellij.lang.CodeDocumentationAwareCommenter;
import com.intellij.psi.PsiComment;
import com.intellij.psi.impl.source.tree.JavaDocElementType;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.psi.SliceDocComment;
import org.xblackcat.frozenidea.psi.SliceTypes;

/**
 * 06.02.12 17:25
 *
 * @author xBlackCat
 */
public class SliceCommenter implements CodeDocumentationAwareCommenter {
    @Override
    public String getLineCommentPrefix() {
        return "//";
    }

    @Override
    public String getBlockCommentPrefix() {
        return "/*";
    }

    @Override
    public String getBlockCommentSuffix() {
        return "*/";
    }

    @Override
    public String getCommentedBlockCommentPrefix() {
        return null;
    }

    @Override
    public String getCommentedBlockCommentSuffix() {
        return null;
    }

    @Override
    @Nullable
    public IElementType getDocumentationCommentTokenType() {
        return SliceTypes.ICE_DOC_STYLE_COMMENT;
    }

    @Nullable
    @Override
    public IElementType getLineCommentTokenType() {
        return SliceTypes.ICE_END_OF_LINE_COMMENT;
    }

    @Nullable
    @Override
    public IElementType getBlockCommentTokenType() {
        return SliceTypes.ICE_C_STYLE_COMMENT;
    }

    @Override
    public String getDocumentationCommentPrefix() {
        return "/**";
    }

    @Override
    public String getDocumentationCommentLinePrefix() {
        return "*";
    }

    @Override
    public String getDocumentationCommentSuffix() {
        return "**/";
    }

    @Override
    public boolean isDocumentationComment(final PsiComment element) {
        return element instanceof SliceDocComment;
    }

}
