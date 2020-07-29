package org.xblackcat.frozenidea;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LanguageUtil;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.parser.SliceLexer;
import org.xblackcat.frozenidea.parser.SliceParser;
import org.xblackcat.frozenidea.psi.SliceTypes;
import org.xblackcat.frozenidea.psi.impl.SliceFileImpl;

/**
 * 04.01.12 16:31
 *
 * @author xBlackCat
 */
public class SliceParserDefinition implements ParserDefinition {

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new SliceLexer();
    }

    @Override
    public PsiParser createParser(Project project) {
        return new SliceParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return ElementType.FILE_ELEMENT_TYPE;
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return ElementType.WHITESPACE_BIT_SET;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return ElementType.COMMENT_BIT_SET;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return ElementType.LITERALS;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return SliceTypes.Factory.createElement(node);
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new SliceFileImpl(viewProvider);
    }

    @Override
    public SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
        final Lexer lexer = createLexer(left.getPsi().getProject());
        return LanguageUtil.canStickTokensTogetherByLexer(left, right, lexer);
    }
}
