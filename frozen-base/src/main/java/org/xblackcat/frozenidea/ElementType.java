package org.xblackcat.frozenidea;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.xblackcat.frozenidea.psi.SliceTypes;

/**
 * 29.04.2014 15:47
 *
 * @author xBlackCat
 */
public interface ElementType {
    IFileElementType FILE_ELEMENT_TYPE = new IFileElementType("SLICE", SliceLanguage.INSTANCE);
    TokenSet KEYWORD_BIT_SET = TokenSet.create(
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
    TokenSet LITERALS = TokenSet.create(SliceTypes.ICE_STRING_LITERAL);
    TokenSet WHITESPACE_BIT_SET = TokenSet.create(TokenType.WHITE_SPACE);
    TokenSet PLAIN_COMMENT_BIT_SET = TokenSet.create(
            SliceTypes.ICE_C_STYLE_COMMENT,
            SliceTypes.ICE_END_OF_LINE_COMMENT,
            SliceTypes.ICE_DOC_STYLE_COMMENT
    );
    TokenSet COMMENT_BIT_SET = TokenSet.orSet(PLAIN_COMMENT_BIT_SET, TokenSet.create(SliceTypes.ICE_DIRECTIVE));

/*
    ILazyParseableElementType DOC_COMMENT = new IReparseableElementType("DOC_COMMENT", SliceLanguage.INSTANCE) {
        private final JavaParserUtil.ParserWrapper myParser = JavadocParser::parseDocCommentText;

        @Override
        public ASTNode createNode(final CharSequence text) {
            return new PsiDocCommentImpl(text);
        }

        @Nullable
        @Override
        public ASTNode parseContents(final ASTNode chameleon) {
            return JavaParserUtil.parseFragment(chameleon, myParser);
        }

        @Override
        public boolean isParsable(final CharSequence buffer, Language fileLanguage, final Project project) {
            Lexer lexer = JavaParserDefinition.createLexer(LanguageLevelProjectExtension.getInstance(project).getLanguageLevel());
            lexer.start(buffer);
            if (lexer.getTokenType() == DOC_COMMENT) {
                lexer.advance();
                if (lexer.getTokenType() == null) {
                    return true;
                }
            }
            return false;
        }
    };
*/

}
