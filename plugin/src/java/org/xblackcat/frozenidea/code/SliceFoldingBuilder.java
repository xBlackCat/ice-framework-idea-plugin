package org.xblackcat.frozenidea.code;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.jsp.jspJava.JspHolderMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.psi.impl.SliceFileImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * 19.01.12 15:08
 *
 * @author xBlackCat
 */
public class SliceFoldingBuilder extends FoldingBuilderEx implements DumbAware {
    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
        if (!(root instanceof SliceFileImpl)) {
            return FoldingDescriptor.EMPTY;
        }

        SliceFileImpl file = (SliceFileImpl) root;

        List<FoldingDescriptor> result = new ArrayList<FoldingDescriptor>();


        return result.toArray(new FoldingDescriptor[result.size()]);
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return false;
    }

    @Override
    public String getPlaceholderText(@NotNull ASTNode node) {
        return "...";
    }


    @Nullable
    private static TextRange getFileHeader(PsiJavaFile file) {
        PsiElement first = file.getFirstChild();
        if (first instanceof PsiWhiteSpace) {
            first = first.getNextSibling();
        }
        PsiElement element = first;
        while (element instanceof PsiComment) {
            element = element.getNextSibling();
            if (element instanceof PsiWhiteSpace) {
                element = element.getNextSibling();
            } else {
                break;
            }
        }
        if (element == null) {
            return null;
        }
        if (element.getPrevSibling() instanceof PsiWhiteSpace) {
            element = element.getPrevSibling();
        }
        if (element == null || element.equals(first)) {
            return null;
        }
        return new TextRange(first.getTextOffset(), element.getTextOffset());
    }

    @Nullable
    public static TextRange getRangeToFold(PsiElement element) {
        if (element instanceof PsiMethod) {
            if (element instanceof JspHolderMethod) {
                return null;
            }
            PsiCodeBlock body = ((PsiMethod) element).getBody();
            if (body == null) {
                return null;
            }
            return body.getTextRange();
        }
        if (element instanceof PsiClassInitializer) {
            return ((PsiClassInitializer) element).getBody().getTextRange();
        }
        if (element instanceof PsiClass) {
            PsiClass aClass = (PsiClass) element;
            PsiElement lBrace = aClass.getLBrace();
            if (lBrace == null) {
                return null;
            }
            PsiElement rBrace = aClass.getRBrace();
            if (rBrace == null) {
                return null;
            }
            return new TextRange(lBrace.getTextOffset(), rBrace.getTextOffset() + 1);
        }
        if (element instanceof PsiJavaFile) {
            return getFileHeader((PsiJavaFile) element);
        }
        if (element instanceof PsiImportList) {
            PsiImportList list = (PsiImportList) element;
            PsiImportStatementBase[] statements = list.getAllImportStatements();
            if (statements.length == 0) {
                return null;
            }
            final PsiElement importKeyword = statements[0].getFirstChild();
            if (importKeyword == null) {
                return null;
            }
            int startOffset = importKeyword.getTextRange().getEndOffset() + 1;
            int endOffset = statements[statements.length - 1].getTextRange().getEndOffset();
            return new TextRange(startOffset, endOffset);
        }
        if (element instanceof PsiDocComment) {
            return element.getTextRange();
        }
        if (element instanceof PsiAnnotation) {
            int startOffset = element.getTextRange().getStartOffset();
            PsiElement last = element;
            while (element instanceof PsiAnnotation) {
                last = element;
                element = PsiTreeUtil.skipSiblingsForward(element, PsiWhiteSpace.class, PsiComment.class);
            }

            return new TextRange(startOffset, last.getTextRange().getEndOffset());
        }
        return null;
    }

    private static boolean addToFold(
            List<FoldingDescriptor> list,
            PsiElement elementToFold,
            Document document,
            boolean allowOneLiners
    ) {
        TextRange range = getRangeToFold(elementToFold);
        if (range == null) {
            return false;
        }

        return addFoldRegion(list, elementToFold, document, allowOneLiners, range);
    }

    private static boolean addFoldRegion(
            final List<FoldingDescriptor> list, final PsiElement elementToFold, final Document document,
            final boolean allowOneLiners,
            final TextRange range
    ) {
        final TextRange fileRange = elementToFold.getContainingFile().getTextRange();
        if (range.equals(fileRange)) {
            return false;
        }

        // PSI element text ranges may be invalid because of reparse exception (see, for example, IDEA-10617)
        if (range.getStartOffset() < 0 || range.getEndOffset() > fileRange.getEndOffset()) {
            return false;
        }
        if (!allowOneLiners) {
            int startLine = document.getLineNumber(range.getStartOffset());
            int endLine = document.getLineNumber(range.getEndOffset() - 1);
            if (startLine < endLine && range.getLength() > 1) {
                list.add(new FoldingDescriptor(elementToFold, range));
                return true;
            }
            return false;
        } else {
            if (range.getLength() > 3) {
                list.add(new FoldingDescriptor(elementToFold, range));
                return true;
            }
            return false;
        }
    }

}
