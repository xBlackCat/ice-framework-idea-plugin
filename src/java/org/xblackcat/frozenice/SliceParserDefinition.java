package org.xblackcat.frozenice;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LanguageUtil;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.parser.IceLexer;
import org.xblackcat.frozenice.parser.SliceParser;
import org.xblackcat.frozenice.psi.SliceTokenType;
import org.xblackcat.frozenice.psi.SliceTypes;
import org.xblackcat.frozenice.psi.impl.SliceFileImpl;

/**
 * 04.01.12 16:31
 *
 * @author xBlackCat
 */
public class SliceParserDefinition implements ParserDefinition {
    public static final IFileElementType FILE_ELEMENT_TYPE = new IFileElementType("SLICE", SliceLanguage.INSTANCE);
    public static final IElementType C_STYLE_COMMENT = new SliceTokenType("Ice.C_Comment");
    public static final IElementType END_OF_LINE_COMMENT = new SliceTokenType("Ice.EnfOfLineComment");
    public static final IElementType ICE_DIRECTIVE = new SliceTokenType("Ice.MacrosLine");
    public static final TokenSet KEYWORD_BIT_SET = TokenSet.create(
            SliceTypes.ICE_KW_BOOL,
            SliceTypes.ICE_KW_BYTE,
            SliceTypes.ICE_KW_CLASS,
            SliceTypes.ICE_KW_CONST,
            SliceTypes.ICE_KW_DICTIONARY,
            SliceTypes.ICE_KW_DOUBLE,
            SliceTypes.ICE_KW_ENUM,
            SliceTypes.ICE_KW_EXCEPTION,
            SliceTypes.ICE_KW_EXTENDS,
            SliceTypes.ICE_KW_FALSE,
            SliceTypes.ICE_KW_FLOAT,
            SliceTypes.ICE_KW_IDEMPOTENT,
            SliceTypes.ICE_KW_IMPLEMENTS,
            SliceTypes.ICE_KW_INT,
            SliceTypes.ICE_KW_INTERFACE,
            SliceTypes.ICE_KW_LOCAL,
            SliceTypes.ICE_KW_LOCAL_OBJECT,
            SliceTypes.ICE_KW_LONG,
            SliceTypes.ICE_KW_MODULE,
            SliceTypes.ICE_KW_OBJECT,
            SliceTypes.ICE_KW_OUT,
            SliceTypes.ICE_KW_SEQUENCE,
            SliceTypes.ICE_KW_SHORT,
            SliceTypes.ICE_KW_STRING,
            SliceTypes.ICE_KW_STRUCT,
            SliceTypes.ICE_KW_THROWS,
            SliceTypes.ICE_KW_TRUE,
            SliceTypes.ICE_KW_VOID
    );

    public static final TokenSet LITERALS = TokenSet.create(SliceTypes.ICE_STRING);
    public static final TokenSet WHITESPACE_BIT_SET = TokenSet.create(TokenType.WHITE_SPACE);
    public static final TokenSet COMMENT_BIT_SET = TokenSet.create(C_STYLE_COMMENT, END_OF_LINE_COMMENT, ICE_DIRECTIVE);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new IceLexer();
    }

    @Override
    public PsiParser createParser(Project project) {
        return new SliceParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE_ELEMENT_TYPE;
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return WHITESPACE_BIT_SET;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return COMMENT_BIT_SET;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return LITERALS;
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
    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        final Lexer lexer = createLexer(left.getPsi().getProject());
        return LanguageUtil.canStickTokensTogetherByLexer(left, right, lexer);
    }
}
