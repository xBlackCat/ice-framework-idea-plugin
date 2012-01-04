/* It's an automatically generated code. Do not modify it. */
package org.xblackcat.frozenice;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;

@SuppressWarnings({"ALL"})
%%

%{
  public _IceLexer(){
    this((java.io.Reader)null);
  }

  public void goTo(int offset) {
    zzCurrentPos = zzMarkedPos = zzStartRead = offset;
    zzPushbackPos = 0;
    zzAtEOF = offset < zzEndRead;
  }
%}

%unicode
%class _IceLexer
%implements FlexLexer
%function advance
%type IElementType
%eof{  return;
%eof}

WHITE_SPACE_CHAR=[\ \n\r\t\f]

IDENTIFIER=\\?[:jletter:] [:jletterdigit:]*

C_STYLE_COMMENT=("/*"[^"*"]{COMMENT_TAIL})|"/*"
COMMENT_TAIL=([^"*"]*("*"+[^"*""/"])?)*("*"+"/")?
END_OF_LINE_COMMENT="/""/"[^\r\n]*
MACROS_LINE="#"[^\r\n]*

%%

<YYINITIAL> {WHITE_SPACE_CHAR}+ { return SliceTokenTypes.WHITE_SPACE; }

<YYINITIAL> {MACROS_LINE} { return SliceTokenTypes.MACROS_LINE; }
<YYINITIAL> {C_STYLE_COMMENT} { return SliceTokenTypes.C_STYLE_COMMENT; }
<YYINITIAL> {END_OF_LINE_COMMENT} { return SliceTokenTypes.END_OF_LINE_COMMENT; }

<YYINITIAL> "true" { return SliceTokenTypes.KEYWORD_TRUE; }
<YYINITIAL> "false" { return SliceTokenTypes.KEYWORD_FALSE; }

<YYINITIAL> "false" { return SliceTokenTypes.KEYWORD_FALSE; } 
<YYINITIAL> "true" { return SliceTokenTypes.KEYWORD_TRUE; } 

<YYINITIAL> "bool" { return SliceTokenTypes.KEYWORD_BOOL; } 
<YYINITIAL> "exception" { return SliceTokenTypes.KEYWORD_EXCEPTION; } 
<YYINITIAL> "interface" { return SliceTokenTypes.KEYWORD_INTERFACE; } 
<YYINITIAL> "sequence" { return SliceTokenTypes.KEYWORD_SEQUENCE; } 
<YYINITIAL> "byte" { return SliceTokenTypes.KEYWORD_BYTE; } 
<YYINITIAL> "extends" { return SliceTokenTypes.KEYWORD_EXTENDS; } 
<YYINITIAL> "local" { return SliceTokenTypes.KEYWORD_LOCAL; } 
<YYINITIAL> "short" { return SliceTokenTypes.KEYWORD_SHORT; } 
<YYINITIAL> "class" { return SliceTokenTypes.KEYWORD_CLASS; } 
<YYINITIAL> "LocalObject" { return SliceTokenTypes.KEYWORD_LOCALOBJECT; } 
<YYINITIAL> "string" { return SliceTokenTypes.KEYWORD_STRING; } 
<YYINITIAL> "const" { return SliceTokenTypes.KEYWORD_CONST; } 
<YYINITIAL> "float" { return SliceTokenTypes.KEYWORD_FLOAT; } 
<YYINITIAL> "long" { return SliceTokenTypes.KEYWORD_LONG; } 
<YYINITIAL> "struct" { return SliceTokenTypes.KEYWORD_STRUCT; } 
<YYINITIAL> "dictionary" { return SliceTokenTypes.KEYWORD_DICTIONARY; } 
<YYINITIAL> "idempotent" { return SliceTokenTypes.KEYWORD_IDEMPOTENT; } 
<YYINITIAL> "module" { return SliceTokenTypes.KEYWORD_MODULE; } 
<YYINITIAL> "throws" { return SliceTokenTypes.KEYWORD_THROWS; } 
<YYINITIAL> "double" { return SliceTokenTypes.KEYWORD_DOUBLE; } 
<YYINITIAL> "implements" { return SliceTokenTypes.KEYWORD_IMPLEMENTS; } 
<YYINITIAL> "Object" { return SliceTokenTypes.KEYWORD_OBJECT; } 
<YYINITIAL> "enum" { return SliceTokenTypes.KEYWORD_ENUM; } 
<YYINITIAL> "int" { return SliceTokenTypes.KEYWORD_INT; } 
<YYINITIAL> "out" { return SliceTokenTypes.KEYWORD_OUT; } 
<YYINITIAL> "void" { return SliceTokenTypes.KEYWORD_VOID; } 

<YYINITIAL> {IDENTIFIER} { return SliceTokenTypes.IDENTIFIER; }

<YYINITIAL> "<" { return SliceTokenTypes.LT; }
<YYINITIAL> ">" { return SliceTokenTypes.GT; }
<YYINITIAL> "("   { return SliceTokenTypes.LPARENTH; }
<YYINITIAL> ")"   { return SliceTokenTypes.RPARENTH; }
<YYINITIAL> "{"   { return SliceTokenTypes.LBRACE; }
<YYINITIAL> "}"   { return SliceTokenTypes.RBRACE; }
<YYINITIAL> "["   { return SliceTokenTypes.LBRACKET; }
<YYINITIAL> "]"   { return SliceTokenTypes.RBRACKET; }
<YYINITIAL> ";"   { return SliceTokenTypes.SEMICOLON; }
<YYINITIAL> ","   { return SliceTokenTypes.COMMA; }
<YYINITIAL> "..." { return SliceTokenTypes.ELLIPSIS; }
<YYINITIAL> "."   { return SliceTokenTypes.DOT; }

<YYINITIAL> . { return SliceTokenTypes.BAD_CHARACTER; }

