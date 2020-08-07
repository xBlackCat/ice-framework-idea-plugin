package org.xblackcat.frozenidea.code.folding;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.CustomFoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.jsp.jspJava.JspHolderMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.psi.*;
import org.xblackcat.frozenidea.util.SlicePsiUtil;

import java.util.Collections;
import java.util.List;

/**
 * 19.01.12 15:08
 *
 * @author xBlackCat
 */
public class SliceFoldingBuilder extends CustomFoldingBuilder implements DumbAware {
    @Override
    protected void buildLanguageFoldRegions(
            @NotNull List<FoldingDescriptor> descriptors,
            @NotNull PsiElement root,
            @NotNull Document document,
            boolean quick
    ) {
        if (!(root instanceof SliceFile)) {
            return;
        }
        final SliceFile sliceFile = (SliceFile) root;

        addFoldsForGlobalMetaData(descriptors, sliceFile);

        for (SliceModule module : PsiTreeUtil.getChildrenOfType(sliceFile, SliceModule.class)) {
            ProgressIndicatorProvider.checkCanceled();

            if (module != null) {
                addFoldsForModule(descriptors, module, false);
            }
        }
    }

    private void addFoldsForModule(List<FoldingDescriptor> descriptors, SliceModule module, boolean collapseByDefault) {
        addFoldsForMetaData(descriptors, module.getMetadata());

        TextRange foldRange = foldRangeForModule(module);
        if (foldRange != null && foldRange.getLength() > 1) {
            final FoldingDescriptor foldingDescriptor = new FoldingDescriptor(
                    module.getNode(),
                    foldRange,
                    null,
                    SlicePsiUtil.isEmpty(module) ? "{}" : "{...}",
                    collapseByDefault,
                    Collections.emptySet()
            );

            descriptors.add(foldingDescriptor);
        }

        final SliceModuleBody moduleBody = module.getModuleBody();
        if (moduleBody == null) {
            return;
        }

        for (PsiElement child = moduleBody.getFirstChild(); child != null; child = child.getNextSibling()) {
            ProgressIndicatorProvider.checkCanceled();

            if (child instanceof SliceModule) {
                addFoldsForModule(descriptors, (SliceModule) child, true);
            } else if (child instanceof SliceDataTypeElement) {
                addFoldsForDataType(descriptors, (SliceDataTypeElement) child);
            }
        }
    }

    private void addFoldsForDataType(List<FoldingDescriptor> descriptors, SliceDataTypeElement type) {
        addFoldsForMetaData(descriptors, type.getMetadata());

        TextRange foldRange = foldRangeForType(type);
        if (foldRange != null && foldRange.getLength() > 1) {
            final FoldingDescriptor foldingDescriptor = new FoldingDescriptor(
                    type.getNode(),
                    foldRange,
                    null,
                    SlicePsiUtil.isEmpty(type) ? "{}" : "{...}",
                    true,
                    Collections.emptySet()
            );

            descriptors.add(foldingDescriptor);
        }

        final @Nullable SliceBodyBlock codeBlock = type.getBodyBlock();
        if (codeBlock == null) {
            return;
        }

        for (PsiElement child = codeBlock.getFirstChild(); child != null; child = child.getNextSibling()) {
            ProgressIndicatorProvider.checkCanceled();

            if (child instanceof SliceInnerElement) {
                addFoldsForMetaData(descriptors, ((SliceInnerElement) child).getMetadata());
            }
        }
    }

    private static TextRange foldRangeForModule(SliceModule module) {
        final SliceModuleBody body = module.getModuleBody();

        if (body == null) {
            return null;
        }

        final PsiElement firstElement = body.getFirstChild();
        final PsiElement lastElement = body.getLastChild();
        if (firstElement == null || lastElement == null) {
            return null;
        }

        return new TextRange(firstElement.getTextRange().getStartOffset(), lastElement.getTextRange().getEndOffset());
    }

    private static TextRange foldRangeForType(SliceDataTypeElement type) {
        final @Nullable SliceBodyBlock body = type.getBodyBlock();

        if (body == null) {
            return null;
        }

        final PsiElement firstElement = body.getFirstChild();
        final PsiElement lastElement = body.getLastChild();
        if (firstElement == null || lastElement == null) {
            return null;
        }

        return new TextRange(firstElement.getTextRange().getStartOffset(), lastElement.getTextRange().getEndOffset());
    }

    private void addFoldsForGlobalMetaData(List<FoldingDescriptor> descriptors, SliceFile file) {
        final SliceGlobalMetadata globalMetadata = PsiTreeUtil.findChildOfType(file, SliceGlobalMetadata.class);

        if (globalMetadata == null) {
            return;
        }

        final TextRange range = getGlobalMetadataRange(globalMetadata);
        if (range != null && range.getLength() > 1) {
            final FoldingDescriptor foldingDescriptor = new FoldingDescriptor(
                    globalMetadata.getNode(),
                    range,
                    null,
                    "...",
                    true,
                    Collections.emptySet()
            );

            descriptors.add(foldingDescriptor);
        }

    }

    private void addFoldsForMetaData(List<FoldingDescriptor> descriptors, SliceMetadata metadata) {
        if (metadata == null) {
            return;
        }

        final TextRange range = getMetadataRange(metadata);
        if (range != null && range.getLength() > 1) {
            final FoldingDescriptor foldingDescriptor = new FoldingDescriptor(
                    metadata.getNode(),
                    range,
                    null,
                    "...",
                    true,
                    Collections.emptySet()
            );

            descriptors.add(foldingDescriptor);
        }

    }

    @Nullable
    private static TextRange getGlobalMetadataRange(SliceGlobalMetadata globalMetadata) {
        final List<SliceGlobalMetadataStatement> statementList = globalMetadata.getGlobalMetadataStatementList();
        if (statementList.isEmpty()) {
            return null;
        }

        final PsiElement firstElement = statementList.get(0).getFirstChild();
        final PsiElement lastElement = statementList.get(statementList.size() - 1).getLastChild();
        if (firstElement == null || lastElement == null) {
            return null;
        }

        return new TextRange(firstElement.getTextRange().getEndOffset(), lastElement.getTextRange().getStartOffset());
    }

    @Nullable
    private static TextRange getMetadataRange(SliceMetadata metadata) {
        final @NotNull List<SliceMetadataStatement> statementList = metadata.getMetadataStatementList();
        if (statementList.isEmpty()) {
            return null;
        }

        final PsiElement firstElement = statementList.get(0).getFirstChild();
        final PsiElement lastElement = statementList.get(statementList.size() - 1).getLastChild();
        if (firstElement == null || lastElement == null) {
            return null;
        }

        return new TextRange(firstElement.getTextRange().getEndOffset(), lastElement.getTextRange().getStartOffset());
    }

    @Override
    protected String getLanguagePlaceholderText(@NotNull ASTNode node, @NotNull TextRange range) {
        return null;
    }

    @Override
    protected boolean isRegionCollapsedByDefault(@NotNull ASTNode node) {
        return false;
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
        } else {
            if (range.getLength() > 3) {
                list.add(new FoldingDescriptor(elementToFold, range));
                return true;
            }
        }
        return false;
    }

}
