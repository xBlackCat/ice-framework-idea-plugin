/* It's an automatically generated code. Do not modify it. */
package org.xblackcat.frozenice.parser;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.xblackcat.frozenice.psi.SliceTypes;
import org.xblackcat.frozenice.SliceParserDefinition;

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

ID=\\?[:jletter:] [:jletterdigit:]*

C_STYLE_COMMENT=("/*"[^"*"]{COMMENT_TAIL})|"/*"
COMMENT_TAIL=([^"*"]*("*"+[^"*""/"])?)*("*"+"/")?
END_OF_LINE_COMMENT="/""/"[^\r\n]*
MACROS_LINE="#"[:jletter:][^\r\n]*
ESCAPE_SEQUENCE=\\[^\n\r]
BAD_STRING=\"([^\\\"\r\n]|{ESCAPE_SEQUENCE})*
STRING_LITERAL={BAD_STRING}\"


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////      integers and floats     /////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

integerLiteral = ({decimalNumeral}|{hexNumeral}|{octalNumeral})(L|l)?
decimalNumeral = 0|{nonZeroDigit}{digit}*
hexNumeral = 0x{hexDigit}+
octalNumeral = 0{octalDigit}+
digit = [0-9]
nonZeroDigit = [1-9]
octalDigit = [0-7]
hexDigit = [0-9A-Fa-f]

floatingPointLiteral = {digit}+"."{digit}*{exponentPart}?{floatType}?|"."{digit}+{exponentPart}?{floatType}?|{digit}+{exponentPart}{floatType}?|{digit}+{exponentPart}?{floatType}

exponentPart = (E|e)("+"|"-")?{digit}+
floatType = F|f|D|d

digit = [0-9]
octalDigit = [0-7]

%%

<YYINITIAL> {WHITE_SPACE_CHAR}+ { return com.intellij.psi.TokenType.WHITE_SPACE; }

<YYINITIAL> {MACROS_LINE} { return SliceParserDefinition.ICE_DIRECTIVE; }
<YYINITIAL> {C_STYLE_COMMENT} { return SliceParserDefinition.C_STYLE_COMMENT; }
<YYINITIAL> {END_OF_LINE_COMMENT} { return SliceParserDefinition.END_OF_LINE_COMMENT; }

<YYINITIAL> {STRING_LITERAL} { return SliceTypes.ICE_STRING; }

<YYINITIAL> {floatingPointLiteral} { return SliceTypes.ICE_FLOAT_VALUE; }
<YYINITIAL> {integerLiteral} { return SliceTypes.ICE_INTEGER_VALUE; }

<YYINITIAL> "false" { return SliceTypes.ICE_KW_FALSE; } 
<YYINITIAL> "true" { return SliceTypes.ICE_KW_TRUE; } 

<YYINITIAL> "bool" { return SliceTypes.ICE_KW_BOOL; } 
<YYINITIAL> "exception" { return SliceTypes.ICE_KW_EXCEPTION; } 
<YYINITIAL> "interface" { return SliceTypes.ICE_KW_INTERFACE; } 
<YYINITIAL> "sequence" { return SliceTypes.ICE_KW_SEQUENCE; } 
<YYINITIAL> "byte" { return SliceTypes.ICE_KW_BYTE; } 
<YYINITIAL> "extends" { return SliceTypes.ICE_KW_EXTENDS; } 
<YYINITIAL> "local" { return SliceTypes.ICE_KW_LOCAL; } 
<YYINITIAL> "short" { return SliceTypes.ICE_KW_SHORT; } 
<YYINITIAL> "class" { return SliceTypes.ICE_KW_CLASS; } 
<YYINITIAL> "LocalObject" { return SliceTypes.ICE_KW_LOCAL_OBJECT; }
<YYINITIAL> "string" { return SliceTypes.ICE_KW_STRING; } 
<YYINITIAL> "const" { return SliceTypes.ICE_KW_CONST; } 
<YYINITIAL> "float" { return SliceTypes.ICE_KW_FLOAT; } 
<YYINITIAL> "long" { return SliceTypes.ICE_KW_LONG; } 
<YYINITIAL> "struct" { return SliceTypes.ICE_KW_STRUCT; } 
<YYINITIAL> "dictionary" { return SliceTypes.ICE_KW_DICTIONARY; } 
<YYINITIAL> "idempotent" { return SliceTypes.ICE_KW_IDEMPOTENT; } 
<YYINITIAL> "module" { return SliceTypes.ICE_KW_MODULE; } 
<YYINITIAL> "throws" { return SliceTypes.ICE_KW_THROWS; } 
<YYINITIAL> "double" { return SliceTypes.ICE_KW_DOUBLE; } 
<YYINITIAL> "implements" { return SliceTypes.ICE_KW_IMPLEMENTS; } 
<YYINITIAL> "Object" { return SliceTypes.ICE_KW_OBJECT; } 
<YYINITIAL> "enum" { return SliceTypes.ICE_KW_ENUM; } 
<YYINITIAL> "int" { return SliceTypes.ICE_KW_INT; } 
<YYINITIAL> "out" { return SliceTypes.ICE_KW_OUT; } 
<YYINITIAL> "void" { return SliceTypes.ICE_KW_VOID; } 

<YYINITIAL> {ID} { return SliceTypes.ICE_ID; }

<YYINITIAL> "<" { return SliceTypes.ICE_LT; }
<YYINITIAL> ">" { return SliceTypes.ICE_GT; }
<YYINITIAL> "("   { return SliceTypes.ICE_LEFT_PARENTH; }
<YYINITIAL> ")"   { return SliceTypes.ICE_RIGHT_PARENTH; }
<YYINITIAL> "{"   { return SliceTypes.ICE_LEFT_BRACE; }
<YYINITIAL> "}"   { return SliceTypes.ICE_RIGHT_BRACE; }
<YYINITIAL> "[["   { return SliceTypes.ICE_OPEN_GL_MD; }
<YYINITIAL> "]]"   { return SliceTypes.ICE_CLOSE_GL_MD; }
<YYINITIAL> "["   { return SliceTypes.ICE_LEFT_BRACKET; }
<YYINITIAL> "]"   { return SliceTypes.ICE_RIGHT_BRACKET; }
<YYINITIAL> ";"   { return SliceTypes.ICE_SEMICOLON; }
<YYINITIAL> ","   { return SliceTypes.ICE_COMA; }
<YYINITIAL> "="   { return SliceTypes.ICE_EQ; }

<YYINITIAL> {BAD_STRING} { return com.intellij.psi.TokenType.BAD_CHARACTER; }
<YYINITIAL> . { return com.intellij.psi.TokenType.BAD_CHARACTER; }

