package org.xblackcat.frozenice;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.ElementType;
import com.intellij.psi.impl.source.tree.JavaElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

/**
 * 04.01.12 16:31
 *
 * @author xBlackCat
 */
public class IceParserDefinition implements ParserDefinition {
    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new FlexAdapter(new _IceLexer());
    }

    @Override
    public PsiParser createParser(Project project) {
        return new PsiParser() {
            @NotNull
            @Override
            public ASTNode parse(IElementType root, PsiBuilder builder) {
                // TODO: implement
                return null;
            }
        };
    }

    @Override
    public IFileElementType getFileNodeType() {
        return SliceElementTypes.FILE_ELEMENT_TYPE;
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return ElementType.JAVA_WHITESPACE_BIT_SET;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return ElementType.JAVA_COMMENT_BIT_SET;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return TokenSet.create(JavaElementType.LITERAL_EXPRESSION);
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return null;
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new IcePsiFile(viewProvider, SliceLanguage.INSTANCE);
    }

    @Override
    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }
}
