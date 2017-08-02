package org.xblackcat.frozenidea.parser;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static org.xblackcat.frozenidea.psi.SliceTypes.*;

%%

%{
  public _SliceLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _SliceLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

END_OF_LINE_COMMENT="//".*
DOC_STYLE_COMMENT="/"\*\*([^*]|\*+[^*/])*(\*+"/")?
C_STYLE_COMMENT="/"\*[^*]([^*]|\*+[^*/])*(\*+"/")?
ESCAPE_SEQUENCE=\\[^\n\r]
BAD_STRING=\"([^\\\"\r\n]|{ESCAPE_SEQUENCE})*
STRING_VALUE={BAD_STRING}\"
ID=\\?[:letter:][a-zA-Z_0-9]*
FLOAT_VALUE=(((\.[0-9]+)|([0-9]+\.[0-9]*))([Ee][+-]?[0-9]+)?[FfDd]?)|([0-9]+([Ee][+-]?[0-9]+)[FfDd]?)|([0-9]+[FfDd])
INTEGER_VALUE=((0|([1-9][0-9]*))|(0x[0-9A-Fa-f]+)|(0[0-7]+))(L|l)?
DIRECTIVE=#.+

%%
<YYINITIAL> {
  {WHITE_SPACE}              { return WHITE_SPACE; }

  "="                        { return ICE_EQ; }
  ";"                        { return ICE_SEMICOLON; }
  ","                        { return ICE_COMA; }
  "*"                        { return ICE_ASTERISK; }
  "<"                        { return ICE_LT; }
  ">"                        { return ICE_GT; }
  "{"                        { return ICE_LEFT_BRACE; }
  "}"                        { return ICE_RIGHT_BRACE; }
  "[["                       { return ICE_OPEN_GL_MD; }
  "]]"                       { return ICE_CLOSE_GL_MD; }
  "["                        { return ICE_LEFT_BRACKET; }
  "]"                        { return ICE_RIGHT_BRACKET; }
  "("                        { return ICE_LEFT_PARENTH; }
  ")"                        { return ICE_RIGHT_PARENTH; }
  "::"                       { return ICE_NAME_SEPARATOR; }
  "void"                     { return ICE_KW_VOID; }
  "bool"                     { return ICE_KW_BOOL; }
  "byte"                     { return ICE_KW_BYTE; }
  "short"                    { return ICE_KW_SHORT; }
  "int"                      { return ICE_KW_INT; }
  "long"                     { return ICE_KW_LONG; }
  "float"                    { return ICE_KW_FLOAT; }
  "double"                   { return ICE_KW_DOUBLE; }
  "LocalObject"              { return ICE_KW_LOCAL_OBJECT; }
  "string"                   { return ICE_KW_STRING; }
  "Object"                   { return ICE_KW_OBJECT; }
  "class"                    { return ICE_KW_CLASS; }
  "exception"                { return ICE_KW_EXCEPTION; }
  "interface"                { return ICE_KW_INTERFACE; }
  "sequence"                 { return ICE_KW_SEQUENCE; }
  "struct"                   { return ICE_KW_STRUCT; }
  "dictionary"               { return ICE_KW_DICTIONARY; }
  "enum"                     { return ICE_KW_ENUM; }
  "false"                    { return ICE_KW_FALSE; }
  "true"                     { return ICE_KW_TRUE; }
  "out"                      { return ICE_KW_OUT; }
  "local"                    { return ICE_KW_LOCAL; }
  "idempotent"               { return ICE_KW_IDEMPOTENT; }
  "module"                   { return ICE_KW_MODULE; }
  "const"                    { return ICE_KW_CONST; }
  "throws"                   { return ICE_KW_THROWS; }
  "extends"                  { return ICE_KW_EXTENDS; }
  "implements"               { return ICE_KW_IMPLEMENTS; }
  "implements"               { return ICE_IMPLEMENTS; }

  {END_OF_LINE_COMMENT}      { return ICE_END_OF_LINE_COMMENT; }
  {DOC_STYLE_COMMENT}        { return ICE_DOC_STYLE_COMMENT; }
  {C_STYLE_COMMENT}          { return ICE_C_STYLE_COMMENT; }
  {ESCAPE_SEQUENCE}          { return ICE_ESCAPE_SEQUENCE; }
  {BAD_STRING}               { return ICE_BAD_STRING; }
  {STRING_VALUE}             { return ICE_STRING_VALUE; }
  {ID}                       { return ICE_ID; }
  {FLOAT_VALUE}              { return ICE_FLOAT_VALUE; }
  {INTEGER_VALUE}            { return ICE_INTEGER_VALUE; }
  {DIRECTIVE}                { return ICE_DIRECTIVE; }

}

[^] { return BAD_CHARACTER; }
