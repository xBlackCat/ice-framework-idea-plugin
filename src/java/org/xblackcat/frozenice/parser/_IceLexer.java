/* The following code was generated by JFlex 1.4.3 on 22.06.12 11:51 */

/* It's an automatically generated code. Do not modify it. */
package org.xblackcat.frozenice.parser;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.xblackcat.frozenice.SliceParserDefinition;
import org.xblackcat.frozenice.psi.SliceTypes;

@SuppressWarnings({"ALL"})

/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.3
 * on 22.06.12 11:51 from the specification file
 * <tt>P:/learn.projects/FrozenIdea/src/java/org/xblackcat/frozenice/parser/_IceLexer.flex</tt>
 */
class _IceLexer implements FlexLexer {
  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0, 0
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\4\1\1\1\7\1\0\1\1\1\7\16\4\4\0\1\1\1\0"+
    "\1\11\1\10\1\3\3\0\1\56\1\57\1\6\1\24\1\65\1\24"+
    "\1\21\1\5\1\14\7\17\2\16\1\0\1\64\1\54\1\66\1\55"+
    "\2\0\3\20\1\25\1\22\1\25\5\3\1\12\2\3\1\46\13\3"+
    "\1\62\1\2\1\63\1\0\1\3\1\0\1\30\1\35\1\37\1\27"+
    "\1\23\1\26\1\50\1\45\1\41\1\47\1\3\1\13\1\51\1\42"+
    "\1\36\1\40\1\43\1\33\1\31\1\32\1\34\1\53\1\52\1\15"+
    "\1\44\1\3\1\60\1\0\1\61\1\0\41\4\2\0\4\3\4\0"+
    "\1\3\2\0\1\4\7\0\1\3\4\0\1\3\5\0\27\3\1\0"+
    "\37\3\1\0\u013f\3\31\0\162\3\4\0\14\3\16\0\5\3\11\0"+
    "\1\3\21\0\130\4\5\0\23\4\12\0\1\3\13\0\1\3\1\0"+
    "\3\3\1\0\1\3\1\0\24\3\1\0\54\3\1\0\46\3\1\0"+
    "\5\3\4\0\202\3\1\0\4\4\3\0\105\3\1\0\46\3\2\0"+
    "\2\3\6\0\20\3\41\0\46\3\2\0\1\3\7\0\47\3\11\0"+
    "\21\4\1\0\27\4\1\0\3\4\1\0\1\4\1\0\2\4\1\0"+
    "\1\4\13\0\33\3\5\0\3\3\15\0\4\4\14\0\6\4\13\0"+
    "\32\3\5\0\13\3\16\4\7\0\12\4\4\0\2\3\1\4\143\3"+
    "\1\0\1\3\10\4\1\0\6\4\2\3\2\4\1\0\4\4\2\3"+
    "\12\4\3\3\2\0\1\3\17\0\1\4\1\3\1\4\36\3\33\4"+
    "\2\0\3\3\60\0\46\3\13\4\1\3\u014f\0\3\4\66\3\2\0"+
    "\1\4\1\3\20\4\2\0\1\3\4\4\3\0\12\3\2\4\2\0"+
    "\12\4\21\0\3\4\1\0\10\3\2\0\2\3\2\0\26\3\1\0"+
    "\7\3\1\0\1\3\3\0\4\3\2\0\1\4\1\3\7\4\2\0"+
    "\2\4\2\0\3\4\11\0\1\4\4\0\2\3\1\0\3\3\2\4"+
    "\2\0\12\4\4\3\15\0\3\4\1\0\6\3\4\0\2\3\2\0"+
    "\26\3\1\0\7\3\1\0\2\3\1\0\2\3\1\0\2\3\2\0"+
    "\1\4\1\0\5\4\4\0\2\4\2\0\3\4\13\0\4\3\1\0"+
    "\1\3\7\0\14\4\3\3\14\0\3\4\1\0\11\3\1\0\3\3"+
    "\1\0\26\3\1\0\7\3\1\0\2\3\1\0\5\3\2\0\1\4"+
    "\1\3\10\4\1\0\3\4\1\0\3\4\2\0\1\3\17\0\2\3"+
    "\2\4\2\0\12\4\1\0\1\3\17\0\3\4\1\0\10\3\2\0"+
    "\2\3\2\0\26\3\1\0\7\3\1\0\2\3\1\0\5\3\2\0"+
    "\1\4\1\3\6\4\3\0\2\4\2\0\3\4\10\0\2\4\4\0"+
    "\2\3\1\0\3\3\4\0\12\4\1\0\1\3\20\0\1\4\1\3"+
    "\1\0\6\3\3\0\3\3\1\0\4\3\3\0\2\3\1\0\1\3"+
    "\1\0\2\3\3\0\2\3\3\0\3\3\3\0\10\3\1\0\3\3"+
    "\4\0\5\4\3\0\3\4\1\0\4\4\11\0\1\4\17\0\11\4"+
    "\11\0\1\3\7\0\3\4\1\0\10\3\1\0\3\3\1\0\27\3"+
    "\1\0\12\3\1\0\5\3\4\0\7\4\1\0\3\4\1\0\4\4"+
    "\7\0\2\4\11\0\2\3\4\0\12\4\22\0\2\4\1\0\10\3"+
    "\1\0\3\3\1\0\27\3\1\0\12\3\1\0\5\3\2\0\1\4"+
    "\1\3\7\4\1\0\3\4\1\0\4\4\7\0\2\4\7\0\1\3"+
    "\1\0\2\3\4\0\12\4\22\0\2\4\1\0\10\3\1\0\3\3"+
    "\1\0\27\3\1\0\20\3\4\0\6\4\2\0\3\4\1\0\4\4"+
    "\11\0\1\4\10\0\2\3\4\0\12\4\22\0\2\4\1\0\22\3"+
    "\3\0\30\3\1\0\11\3\1\0\1\3\2\0\7\3\3\0\1\4"+
    "\4\0\6\4\1\0\1\4\1\0\10\4\22\0\2\4\15\0\60\3"+
    "\1\4\2\3\7\4\4\0\10\3\10\4\1\0\12\4\47\0\2\3"+
    "\1\0\1\3\2\0\2\3\1\0\1\3\2\0\1\3\6\0\4\3"+
    "\1\0\7\3\1\0\3\3\1\0\1\3\1\0\1\3\2\0\2\3"+
    "\1\0\4\3\1\4\2\3\6\4\1\0\2\4\1\3\2\0\5\3"+
    "\1\0\1\3\1\0\6\4\2\0\12\4\2\0\2\3\42\0\1\3"+
    "\27\0\2\4\6\0\12\4\13\0\1\4\1\0\1\4\1\0\1\4"+
    "\4\0\2\4\10\3\1\0\42\3\6\0\24\4\1\0\2\4\4\3"+
    "\4\0\10\4\1\0\44\4\11\0\1\4\71\0\42\3\1\0\5\3"+
    "\1\0\2\3\1\0\7\4\3\0\4\4\6\0\12\4\6\0\6\3"+
    "\4\4\106\0\46\3\12\0\51\3\7\0\132\3\5\0\104\3\5\0"+
    "\122\3\6\0\7\3\1\0\77\3\1\0\1\3\1\0\4\3\2\0"+
    "\7\3\1\0\1\3\1\0\4\3\2\0\47\3\1\0\1\3\1\0"+
    "\4\3\2\0\37\3\1\0\1\3\1\0\4\3\2\0\7\3\1\0"+
    "\1\3\1\0\4\3\2\0\7\3\1\0\7\3\1\0\27\3\1\0"+
    "\37\3\1\0\1\3\1\0\4\3\2\0\7\3\1\0\47\3\1\0"+
    "\23\3\16\0\11\4\56\0\125\3\14\0\u026c\3\2\0\10\3\12\0"+
    "\32\3\5\0\113\3\3\0\3\3\17\0\15\3\1\0\4\3\3\4"+
    "\13\0\22\3\3\4\13\0\22\3\2\4\14\0\15\3\1\0\3\3"+
    "\1\0\2\4\14\0\64\3\40\4\3\0\1\3\3\0\2\3\1\4"+
    "\2\0\12\4\41\0\3\4\2\0\12\4\6\0\130\3\10\0\51\3"+
    "\1\4\126\0\35\3\3\0\14\4\4\0\14\4\12\0\12\4\36\3"+
    "\2\0\5\3\u038b\0\154\3\224\0\234\3\4\0\132\3\6\0\26\3"+
    "\2\0\6\3\2\0\46\3\2\0\6\3\2\0\10\3\1\0\1\3"+
    "\1\0\1\3\1\0\1\3\1\0\37\3\2\0\65\3\1\0\7\3"+
    "\1\0\1\3\3\0\3\3\1\0\7\3\3\0\4\3\2\0\6\3"+
    "\4\0\15\3\5\0\3\3\1\0\7\3\17\0\4\4\32\0\5\4"+
    "\20\0\2\3\23\0\1\3\13\0\4\4\6\0\6\4\1\0\1\3"+
    "\15\0\1\3\40\0\22\3\36\0\15\4\4\0\1\4\3\0\6\4"+
    "\27\0\1\3\4\0\1\3\2\0\12\3\1\0\1\3\3\0\5\3"+
    "\6\0\1\3\1\0\1\3\1\0\1\3\1\0\4\3\1\0\3\3"+
    "\1\0\7\3\3\0\3\3\5\0\5\3\26\0\44\3\u0e81\0\3\3"+
    "\31\0\11\3\6\4\1\0\5\3\2\0\5\3\4\0\126\3\2\0"+
    "\2\4\2\0\3\3\1\0\137\3\5\0\50\3\4\0\136\3\21\0"+
    "\30\3\70\0\20\3\u0200\0\u19b6\3\112\0\u51a6\3\132\0\u048d\3\u0773\0"+
    "\u2ba4\3\u215c\0\u012e\3\2\0\73\3\225\0\7\3\14\0\5\3\5\0"+
    "\1\3\1\4\12\3\1\0\15\3\1\0\5\3\1\0\1\3\1\0"+
    "\2\3\1\0\2\3\1\0\154\3\41\0\u016b\3\22\0\100\3\2\0"+
    "\66\3\50\0\15\3\3\0\20\4\20\0\4\4\17\0\2\3\30\0"+
    "\3\3\31\0\1\3\6\0\5\3\1\0\207\3\2\0\1\4\4\0"+
    "\1\3\13\0\12\4\7\0\32\3\4\0\1\3\1\0\32\3\12\0"+
    "\132\3\3\0\6\3\2\0\6\3\2\0\6\3\2\0\3\3\3\0"+
    "\2\3\3\0\2\3\22\0\3\4\4\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\1\2\1\1\1\3\1\1\1\4\2\1"+
    "\2\3\2\5\1\1\14\3\1\6\1\7\1\10\1\11"+
    "\1\12\1\13\1\14\1\15\1\16\1\17\1\20\1\21"+
    "\1\22\1\23\1\0\1\24\2\3\2\5\2\0\1\25"+
    "\1\0\1\25\26\3\1\26\1\27\1\22\3\3\1\5"+
    "\1\25\1\0\16\3\1\30\3\3\1\31\4\3\1\0"+
    "\2\3\1\32\2\3\1\33\10\3\1\34\1\3\1\35"+
    "\1\36\7\3\1\37\1\22\1\3\1\40\2\3\1\41"+
    "\1\42\5\3\1\43\1\3\1\44\1\45\10\3\1\46"+
    "\2\3\1\47\1\50\1\51\3\3\1\52\1\53\1\3"+
    "\1\54\11\3\1\55\4\3\1\56\2\3\1\57\2\3"+
    "\1\60\1\61\1\62\1\63";

  private static int [] zzUnpackAction() {
    int [] result = new int[193];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\67\0\156\0\245\0\334\0\u0113\0\67\0\u014a"+
    "\0\u0181\0\u01b8\0\u01ef\0\u0226\0\u025d\0\u0294\0\u02cb\0\u0302"+
    "\0\u0339\0\u0370\0\u03a7\0\u03de\0\u0415\0\u044c\0\u0483\0\u04ba"+
    "\0\u04f1\0\u0528\0\67\0\67\0\67\0\67\0\67\0\67"+
    "\0\u055f\0\u0596\0\67\0\67\0\67\0\u05cd\0\u0604\0\u063b"+
    "\0\u0672\0\67\0\u06a9\0\u06e0\0\67\0\u0717\0\u074e\0\u0785"+
    "\0\u07bc\0\u07f3\0\67\0\u082a\0\u0861\0\u0898\0\u08cf\0\u0906"+
    "\0\u093d\0\u0974\0\u09ab\0\u09e2\0\u0a19\0\u0a50\0\u0a87\0\u0abe"+
    "\0\u0af5\0\u0b2c\0\u0b63\0\u0b9a\0\u0bd1\0\u0c08\0\u0c3f\0\u0c76"+
    "\0\u0cad\0\67\0\67\0\u0ce4\0\u0d1b\0\u0d52\0\u0d89\0\u0dc0"+
    "\0\u0df7\0\u0e2e\0\u0e65\0\u0e9c\0\u0ed3\0\u0f0a\0\u0f41\0\u0f78"+
    "\0\u0faf\0\u0fe6\0\u101d\0\u1054\0\u108b\0\u10c2\0\u10f9\0\u1130"+
    "\0\334\0\u1167\0\u119e\0\u11d5\0\u120c\0\u1243\0\u127a\0\u12b1"+
    "\0\u12e8\0\u131f\0\u1356\0\u138d\0\334\0\u13c4\0\u13fb\0\334"+
    "\0\u1432\0\u1469\0\u14a0\0\u14d7\0\u150e\0\u1545\0\u157c\0\u15b3"+
    "\0\334\0\u15ea\0\334\0\334\0\u1621\0\u1658\0\u168f\0\u16c6"+
    "\0\u16fd\0\u1734\0\u176b\0\334\0\67\0\u17a2\0\334\0\u17d9"+
    "\0\u1810\0\334\0\334\0\u1847\0\u187e\0\u18b5\0\u18ec\0\u1923"+
    "\0\334\0\u195a\0\334\0\334\0\u1991\0\u19c8\0\u19ff\0\u1a36"+
    "\0\u1a6d\0\u1aa4\0\u1adb\0\u1b12\0\334\0\u1b49\0\u1b80\0\334"+
    "\0\334\0\334\0\u1bb7\0\u1bee\0\u1c25\0\334\0\334\0\u1c5c"+
    "\0\334\0\u1c93\0\u1cca\0\u1d01\0\u1d38\0\u1d6f\0\u1da6\0\u1ddd"+
    "\0\u1e14\0\u1e4b\0\334\0\u1e82\0\u1eb9\0\u1ef0\0\u1f27\0\334"+
    "\0\u1f5e\0\u1f95\0\334\0\u1fcc\0\u2003\0\334\0\334\0\334"+
    "\0\334";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[193];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\2\1\3\1\4\1\5\1\2\1\6\1\7\1\3"+
    "\1\10\1\11\1\12\1\13\1\14\1\5\2\15\1\5"+
    "\1\16\1\5\1\17\1\2\1\5\1\20\1\21\1\5"+
    "\1\22\1\23\2\5\1\24\1\25\1\26\1\5\1\27"+
    "\4\5\1\30\2\5\1\31\1\5\1\32\1\33\1\34"+
    "\1\35\1\36\1\37\1\40\1\41\1\42\1\43\1\44"+
    "\1\45\70\0\1\3\5\0\1\3\62\0\1\5\6\0"+
    "\2\5\1\0\1\5\2\0\1\5\1\0\2\5\1\0"+
    "\27\5\16\0\2\5\5\0\7\5\1\0\2\5\1\0"+
    "\27\5\20\0\1\46\1\47\63\0\1\50\6\0\2\50"+
    "\1\0\1\50\2\0\1\50\1\0\2\50\1\0\27\50"+
    "\13\0\2\11\1\51\4\11\1\0\1\11\1\52\55\11"+
    "\3\0\2\5\5\0\7\5\1\0\2\5\1\0\11\5"+
    "\1\53\15\5\16\0\2\5\5\0\7\5\1\0\2\5"+
    "\1\0\11\5\1\54\15\5\25\0\2\55\1\56\1\57"+
    "\1\60\1\56\1\0\1\61\2\62\1\0\3\63\51\0"+
    "\2\55\1\15\1\0\2\15\1\0\1\61\2\62\1\0"+
    "\3\63\53\0\1\61\1\0\2\61\52\0\2\5\5\0"+
    "\3\5\1\64\3\5\1\0\2\5\1\0\15\5\1\65"+
    "\11\5\16\0\2\5\5\0\1\5\1\66\5\5\1\0"+
    "\2\5\1\0\3\5\1\67\23\5\16\0\2\5\5\0"+
    "\7\5\1\0\2\5\1\0\11\5\1\70\2\5\1\71"+
    "\12\5\16\0\2\5\5\0\7\5\1\0\1\5\1\72"+
    "\1\0\5\5\1\73\12\5\1\74\6\5\16\0\2\5"+
    "\5\0\7\5\1\0\2\5\1\0\6\5\1\75\11\5"+
    "\1\76\6\5\16\0\2\5\5\0\7\5\1\0\2\5"+
    "\1\0\11\5\1\77\5\5\1\100\7\5\16\0\2\5"+
    "\5\0\7\5\1\0\2\5\1\0\7\5\1\101\17\5"+
    "\16\0\2\5\5\0\1\5\1\102\5\5\1\0\2\5"+
    "\1\0\11\5\1\103\15\5\16\0\2\5\5\0\7\5"+
    "\1\0\2\5\1\0\2\5\1\104\12\5\1\105\6\5"+
    "\1\106\2\5\16\0\2\5\5\0\7\5\1\0\2\5"+
    "\1\0\10\5\1\107\16\5\16\0\2\5\5\0\7\5"+
    "\1\0\2\5\1\0\11\5\1\110\15\5\16\0\2\5"+
    "\5\0\7\5\1\0\2\5\1\0\11\5\1\111\15\5"+
    "\75\0\1\112\67\0\1\113\3\0\7\46\1\0\57\46"+
    "\6\114\1\0\60\114\7\50\1\0\57\50\7\11\1\0"+
    "\57\11\3\0\2\5\5\0\7\5\1\0\2\5\1\0"+
    "\12\5\1\115\14\5\16\0\2\5\5\0\7\5\1\0"+
    "\2\5\1\0\12\5\1\116\2\5\1\117\11\5\25\0"+
    "\2\55\1\56\1\0\1\60\1\56\1\0\1\61\2\62"+
    "\1\0\3\63\53\0\1\120\1\0\3\120\1\0\2\120"+
    "\1\0\4\120\4\0\1\120\1\0\1\120\43\0\1\60"+
    "\1\0\2\60\1\0\1\61\2\62\1\0\3\63\53\0"+
    "\1\61\1\0\2\61\2\0\2\62\1\0\3\63\53\0"+
    "\1\121\1\0\2\121\4\0\1\122\45\0\2\5\5\0"+
    "\7\5\1\0\2\5\1\0\5\5\1\123\4\5\1\124"+
    "\14\5\16\0\2\5\5\0\7\5\1\0\2\5\1\0"+
    "\7\5\1\125\17\5\16\0\2\5\5\0\7\5\1\0"+
    "\2\5\1\0\11\5\1\126\15\5\16\0\2\5\5\0"+
    "\1\5\1\127\5\5\1\0\2\5\1\0\27\5\16\0"+
    "\2\5\5\0\7\5\1\0\2\5\1\0\7\5\1\130"+
    "\17\5\16\0\2\5\5\0\7\5\1\0\2\5\1\0"+
    "\12\5\1\131\14\5\16\0\2\5\5\0\7\5\1\0"+
    "\2\5\1\0\16\5\1\132\10\5\16\0\2\5\5\0"+
    "\7\5\1\0\2\5\1\0\6\5\1\133\20\5\16\0"+
    "\2\5\5\0\7\5\1\0\2\5\1\0\11\5\1\134"+
    "\15\5\16\0\2\5\5\0\7\5\1\0\2\5\1\0"+
    "\7\5\1\135\17\5\16\0\2\5\5\0\7\5\1\0"+
    "\2\5\1\0\6\5\1\136\20\5\16\0\2\5\5\0"+
    "\7\5\1\0\2\5\1\0\11\5\1\137\15\5\16\0"+
    "\2\5\5\0\7\5\1\0\2\5\1\0\5\5\1\140"+
    "\21\5\16\0\2\5\5\0\7\5\1\0\2\5\1\0"+
    "\5\5\1\141\21\5\16\0\2\5\5\0\7\5\1\0"+
    "\2\5\1\0\3\5\1\142\23\5\16\0\2\5\5\0"+
    "\7\5\1\0\2\5\1\0\15\5\1\143\11\5\16\0"+
    "\2\5\5\0\7\5\1\0\1\5\1\144\1\0\27\5"+
    "\16\0\2\5\5\0\7\5\1\0\2\5\1\0\5\5"+
    "\1\145\21\5\16\0\2\5\5\0\7\5\1\0\2\5"+
    "\1\0\13\5\1\146\13\5\16\0\2\5\5\0\7\5"+
    "\1\0\2\5\1\0\22\5\1\147\4\5\16\0\2\5"+
    "\5\0\7\5\1\0\2\5\1\0\2\5\1\150\24\5"+
    "\16\0\2\5\5\0\7\5\1\0\2\5\1\0\14\5"+
    "\1\151\12\5\13\0\6\114\1\152\60\114\3\0\2\5"+
    "\5\0\7\5\1\0\2\5\1\0\3\5\1\153\23\5"+
    "\16\0\2\5\5\0\7\5\1\0\2\5\1\0\3\5"+
    "\1\154\23\5\16\0\2\5\5\0\7\5\1\0\2\5"+
    "\1\0\23\5\1\155\3\5\25\0\2\55\1\120\1\0"+
    "\3\120\1\0\2\120\1\0\4\120\4\0\1\120\1\0"+
    "\1\120\43\0\1\121\1\0\2\121\5\0\3\63\53\0"+
    "\1\121\1\0\2\121\52\0\2\5\5\0\7\5\1\0"+
    "\1\5\1\156\1\0\27\5\16\0\2\5\5\0\7\5"+
    "\1\0\1\5\1\157\1\0\27\5\16\0\2\5\5\0"+
    "\7\5\1\0\2\5\1\0\24\5\1\160\2\5\16\0"+
    "\2\5\5\0\7\5\1\0\2\5\1\0\3\5\1\161"+
    "\23\5\16\0\2\5\5\0\7\5\1\0\2\5\1\0"+
    "\4\5\1\162\22\5\16\0\2\5\5\0\7\5\1\0"+
    "\2\5\1\0\10\5\1\163\16\5\16\0\2\5\5\0"+
    "\7\5\1\0\2\5\1\0\5\5\1\164\21\5\16\0"+
    "\2\5\5\0\7\5\1\0\2\5\1\0\7\5\1\165"+
    "\17\5\16\0\2\5\5\0\7\5\1\0\2\5\1\0"+
    "\7\5\1\166\4\5\1\167\12\5\16\0\2\5\5\0"+
    "\7\5\1\0\2\5\1\0\6\5\1\170\20\5\16\0"+
    "\2\5\5\0\7\5\1\0\1\5\1\171\1\0\27\5"+
    "\16\0\2\5\5\0\7\5\1\0\2\5\1\0\11\5"+
    "\1\172\15\5\16\0\2\5\5\0\1\5\1\173\5\5"+
    "\1\0\2\5\1\0\27\5\16\0\2\5\5\0\7\5"+
    "\1\0\1\5\1\174\1\0\27\5\16\0\2\5\5\0"+
    "\7\5\1\0\2\5\1\0\4\5\1\175\22\5\16\0"+
    "\2\5\5\0\7\5\1\0\2\5\1\0\4\5\1\176"+
    "\22\5\16\0\2\5\5\0\7\5\1\0\2\5\1\0"+
    "\24\5\1\177\2\5\16\0\2\5\5\0\7\5\1\0"+
    "\1\5\1\200\1\0\27\5\16\0\2\5\5\0\1\5"+
    "\1\201\5\5\1\0\2\5\1\0\27\5\16\0\2\5"+
    "\5\0\7\5\1\0\1\5\1\202\1\0\27\5\16\0"+
    "\2\5\5\0\7\5\1\0\2\5\1\0\7\5\1\203"+
    "\17\5\16\0\2\5\5\0\7\5\1\0\2\5\1\0"+
    "\2\5\1\204\24\5\13\0\5\114\1\205\1\152\60\114"+
    "\3\0\2\5\5\0\1\5\1\206\5\5\1\0\2\5"+
    "\1\0\27\5\16\0\2\5\5\0\1\5\1\207\5\5"+
    "\1\0\2\5\1\0\27\5\16\0\2\5\5\0\7\5"+
    "\1\0\2\5\1\0\15\5\1\210\11\5\16\0\2\5"+
    "\5\0\7\5\1\0\2\5\1\0\13\5\1\211\13\5"+
    "\16\0\2\5\5\0\7\5\1\0\2\5\1\0\5\5"+
    "\1\212\21\5\16\0\2\5\5\0\7\5\1\0\1\5"+
    "\1\213\1\0\27\5\16\0\2\5\5\0\1\5\1\214"+
    "\5\5\1\0\2\5\1\0\27\5\16\0\2\5\5\0"+
    "\7\5\1\0\2\5\1\0\14\5\1\215\12\5\16\0"+
    "\2\5\5\0\7\5\1\0\1\5\1\216\1\0\27\5"+
    "\16\0\2\5\5\0\7\5\1\0\2\5\1\0\12\5"+
    "\1\217\14\5\16\0\2\5\5\0\7\5\1\0\2\5"+
    "\1\0\15\5\1\220\11\5\16\0\2\5\5\0\7\5"+
    "\1\0\2\5\1\0\5\5\1\221\21\5\16\0\2\5"+
    "\5\0\7\5\1\0\2\5\1\0\25\5\1\222\1\5"+
    "\16\0\2\5\5\0\7\5\1\0\2\5\1\0\4\5"+
    "\1\223\22\5\16\0\2\5\5\0\7\5\1\0\2\5"+
    "\1\0\5\5\1\224\21\5\16\0\2\5\5\0\7\5"+
    "\1\0\2\5\1\0\13\5\1\225\13\5\16\0\2\5"+
    "\5\0\7\5\1\0\2\5\1\0\6\5\1\226\20\5"+
    "\16\0\2\5\5\0\7\5\1\0\1\5\1\227\1\0"+
    "\27\5\16\0\2\5\5\0\7\5\1\0\2\5\1\0"+
    "\12\5\1\230\14\5\16\0\2\5\5\0\1\5\1\231"+
    "\5\5\1\0\2\5\1\0\27\5\16\0\2\5\5\0"+
    "\7\5\1\0\2\5\1\0\21\5\1\232\5\5\16\0"+
    "\2\5\5\0\7\5\1\0\2\5\1\0\2\5\1\233"+
    "\24\5\16\0\2\5\5\0\7\5\1\0\2\5\1\0"+
    "\5\5\1\234\21\5\16\0\2\5\5\0\7\5\1\0"+
    "\1\5\1\235\1\0\27\5\16\0\2\5\5\0\7\5"+
    "\1\0\2\5\1\0\11\5\1\236\15\5\16\0\2\5"+
    "\5\0\7\5\1\0\2\5\1\0\15\5\1\237\11\5"+
    "\16\0\2\5\5\0\7\5\1\0\2\5\1\0\5\5"+
    "\1\240\21\5\16\0\2\5\5\0\7\5\1\0\2\5"+
    "\1\0\23\5\1\241\3\5\16\0\2\5\5\0\7\5"+
    "\1\0\2\5\1\0\4\5\1\242\22\5\16\0\2\5"+
    "\5\0\7\5\1\0\2\5\1\0\11\5\1\243\15\5"+
    "\16\0\2\5\5\0\7\5\1\0\2\5\1\0\1\5"+
    "\1\244\25\5\16\0\2\5\5\0\7\5\1\0\2\5"+
    "\1\0\24\5\1\245\2\5\16\0\2\5\5\0\7\5"+
    "\1\0\2\5\1\0\5\5\1\246\21\5\16\0\2\5"+
    "\5\0\7\5\1\0\1\5\1\247\1\0\27\5\16\0"+
    "\2\5\5\0\7\5\1\0\2\5\1\0\10\5\1\250"+
    "\16\5\16\0\2\5\5\0\7\5\1\0\2\5\1\0"+
    "\4\5\1\251\22\5\16\0\2\5\5\0\7\5\1\0"+
    "\2\5\1\0\14\5\1\252\12\5\16\0\2\5\5\0"+
    "\7\5\1\0\2\5\1\0\15\5\1\253\11\5\16\0"+
    "\2\5\5\0\7\5\1\0\2\5\1\0\12\5\1\254"+
    "\14\5\16\0\2\5\5\0\7\5\1\0\2\5\1\0"+
    "\5\5\1\255\21\5\16\0\2\5\5\0\7\5\1\0"+
    "\2\5\1\0\3\5\1\256\23\5\16\0\2\5\5\0"+
    "\7\5\1\0\1\5\1\257\1\0\27\5\16\0\2\5"+
    "\5\0\7\5\1\0\2\5\1\0\22\5\1\260\4\5"+
    "\16\0\2\5\5\0\7\5\1\0\2\5\1\0\11\5"+
    "\1\261\15\5\16\0\2\5\5\0\7\5\1\0\2\5"+
    "\1\0\3\5\1\262\23\5\16\0\2\5\5\0\7\5"+
    "\1\0\1\5\1\263\1\0\27\5\16\0\2\5\5\0"+
    "\7\5\1\0\1\5\1\264\1\0\27\5\16\0\2\5"+
    "\5\0\7\5\1\0\2\5\1\0\12\5\1\265\14\5"+
    "\16\0\2\5\5\0\7\5\1\0\2\5\1\0\15\5"+
    "\1\266\11\5\16\0\2\5\5\0\7\5\1\0\1\5"+
    "\1\267\1\0\27\5\16\0\2\5\5\0\7\5\1\0"+
    "\2\5\1\0\15\5\1\270\11\5\16\0\2\5\5\0"+
    "\7\5\1\0\2\5\1\0\6\5\1\271\20\5\16\0"+
    "\2\5\5\0\7\5\1\0\2\5\1\0\15\5\1\272"+
    "\11\5\16\0\2\5\5\0\7\5\1\0\1\5\1\273"+
    "\1\0\27\5\16\0\2\5\5\0\7\5\1\0\2\5"+
    "\1\0\5\5\1\274\21\5\16\0\2\5\5\0\7\5"+
    "\1\0\2\5\1\0\12\5\1\275\14\5\16\0\2\5"+
    "\5\0\7\5\1\0\2\5\1\0\17\5\1\276\7\5"+
    "\16\0\2\5\5\0\7\5\1\0\2\5\1\0\5\5"+
    "\1\277\21\5\16\0\2\5\5\0\7\5\1\0\2\5"+
    "\1\0\4\5\1\300\22\5\16\0\2\5\5\0\7\5"+
    "\1\0\2\5\1\0\5\5\1\301\21\5\13\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[8250];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;
  private static final char[] EMPTY_BUFFER = new char[0];
  private static final int YYEOF = -1;
  private static java.io.Reader zzReader = null; // Fake

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\1\11\4\1\1\11\23\1\6\11\2\1\3\11"+
    "\3\1\1\0\1\11\2\1\1\11\1\1\2\0\1\1"+
    "\1\0\1\11\26\1\2\11\6\1\1\0\27\1\1\0"+
    "\32\1\1\11\74\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[193];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private CharSequence zzBuffer = "";

  /** this buffer may contains the current text array to be matched when it is cheap to acquire it */
  private char[] zzBufferArray;

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the textposition at the last state to be included in yytext */
  private int zzPushbackPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /**
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */
  public _IceLexer(){
    this((java.io.Reader)null);
  }

  public void goTo(int offset) {
    zzCurrentPos = zzMarkedPos = zzStartRead = offset;
    zzPushbackPos = 0;
    zzAtEOF = offset < zzEndRead;
  }


  _IceLexer(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  _IceLexer(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 1772) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }

  public final int getTokenStart(){
    return zzStartRead;
  }

  public final int getTokenEnd(){
    return getTokenStart() + yylength();
  }

  public void reset(CharSequence buffer, int start, int end,int initialState){
    zzBuffer = buffer;
    zzBufferArray = com.intellij.util.text.CharArrayUtil.fromSequenceWithoutCopying(buffer);
    zzCurrentPos = zzMarkedPos = zzStartRead = start;
    zzPushbackPos = 0;
    zzAtEOF  = false;
    zzAtBOL = true;
    zzEndRead = end;
    yybegin(initialState);
  }

  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   *
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {
    return true;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final CharSequence yytext() {
    return zzBuffer.subSequence(zzStartRead, zzMarkedPos);
  }


  /**
   * Returns the character at position <tt>pos</tt> from the
   * matched text.
   *
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch.
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBufferArray != null ? zzBufferArray[zzStartRead+pos]:zzBuffer.charAt(zzStartRead+pos);
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of
   * yypushback(int) and a match-all fallback rule) this method
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  }


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() {
    if (!zzEOFDone) {
      zzEOFDone = true;
    
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public IElementType advance() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    CharSequence zzBufferL = zzBuffer;
    char[] zzBufferArrayL = zzBufferArray;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

      zzState = ZZ_LEXSTATE[zzLexicalState];


      zzForAction: {
        while (true) {

          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL.charAt(zzCurrentPosL++);
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL.charAt(zzCurrentPosL++);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 49: 
          { return SliceTypes.ICE_KW_IDEMPOTENT;
          }
        case 52: break;
        case 35: 
          { return SliceTypes.ICE_KW_SHORT;
          }
        case 53: break;
        case 8: 
          { return SliceTypes.ICE_LEFT_PARENTH;
          }
        case 54: break;
        case 47: 
          { return SliceTypes.ICE_KW_INTERFACE;
          }
        case 55: break;
        case 17: 
          { return SliceParserDefinition.END_OF_LINE_COMMENT;
          }
        case 56: break;
        case 39: 
          { return SliceTypes.ICE_KW_STRUCT;
          }
        case 57: break;
        case 36: 
          { return SliceTypes.ICE_KW_CLASS;
          }
        case 58: break;
        case 32: 
          { return SliceTypes.ICE_KW_LOCAL;
          }
        case 59: break;
        case 6: 
          { return SliceTypes.ICE_LT;
          }
        case 60: break;
        case 9: 
          { return SliceTypes.ICE_RIGHT_PARENTH;
          }
        case 61: break;
        case 20: 
          { return SliceTypes.ICE_STRING_LITERAL;
          }
        case 62: break;
        case 40: 
          { return SliceTypes.ICE_KW_STRING;
          }
        case 63: break;
        case 45: 
          { return SliceTypes.ICE_KW_SEQUENCE;
          }
        case 64: break;
        case 33: 
          { return SliceTypes.ICE_KW_FLOAT;
          }
        case 65: break;
        case 43: 
          { return SliceTypes.ICE_KW_MODULE;
          }
        case 66: break;
        case 18: 
          { return SliceParserDefinition.C_STYLE_COMMENT;
          }
        case 67: break;
        case 10: 
          { return SliceTypes.ICE_LEFT_BRACE;
          }
        case 68: break;
        case 46: 
          { return SliceTypes.ICE_KW_EXCEPTION;
          }
        case 69: break;
        case 37: 
          { return SliceTypes.ICE_KW_CONST;
          }
        case 70: break;
        case 12: 
          { return SliceTypes.ICE_LEFT_BRACKET;
          }
        case 71: break;
        case 5: 
          { return SliceTypes.ICE_INTEGER_VALUE;
          }
        case 72: break;
        case 24: 
          { return SliceTypes.ICE_KW_OUT;
          }
        case 73: break;
        case 42: 
          { return SliceTypes.ICE_KW_OBJECT;
          }
        case 74: break;
        case 13: 
          { return SliceTypes.ICE_RIGHT_BRACKET;
          }
        case 75: break;
        case 48: 
          { return SliceTypes.ICE_KW_DICTIONARY;
          }
        case 76: break;
        case 41: 
          { return SliceTypes.ICE_KW_THROWS;
          }
        case 77: break;
        case 44: 
          { return SliceTypes.ICE_KW_EXTENDS;
          }
        case 78: break;
        case 21: 
          { return SliceTypes.ICE_FLOAT_VALUE;
          }
        case 79: break;
        case 14: 
          { return SliceTypes.ICE_SEMICOLON;
          }
        case 80: break;
        case 22: 
          { return SliceTypes.ICE_OPEN_GL_MD;
          }
        case 81: break;
        case 11: 
          { return SliceTypes.ICE_RIGHT_BRACE;
          }
        case 82: break;
        case 27: 
          { return SliceTypes.ICE_KW_ENUM;
          }
        case 83: break;
        case 3: 
          { return SliceTypes.ICE_ID;
          }
        case 84: break;
        case 7: 
          { return SliceTypes.ICE_GT;
          }
        case 85: break;
        case 38: 
          { return SliceTypes.ICE_KW_DOUBLE;
          }
        case 86: break;
        case 30: 
          { return SliceTypes.ICE_KW_BYTE;
          }
        case 87: break;
        case 23: 
          { return SliceTypes.ICE_CLOSE_GL_MD;
          }
        case 88: break;
        case 25: 
          { return SliceTypes.ICE_KW_INT;
          }
        case 89: break;
        case 34: 
          { return SliceTypes.ICE_KW_FALSE;
          }
        case 90: break;
        case 31: 
          { return SliceTypes.ICE_KW_VOID;
          }
        case 91: break;
        case 4: 
          { return SliceTypes.ICE_ASTERISK;
          }
        case 92: break;
        case 28: 
          { return SliceTypes.ICE_KW_TRUE;
          }
        case 93: break;
        case 2: 
          { return com.intellij.psi.TokenType.WHITE_SPACE;
          }
        case 94: break;
        case 15: 
          { return SliceTypes.ICE_COMA;
          }
        case 95: break;
        case 19: 
          { return SliceParserDefinition.ICE_DIRECTIVE;
          }
        case 96: break;
        case 50: 
          { return SliceTypes.ICE_KW_IMPLEMENTS;
          }
        case 97: break;
        case 16: 
          { return SliceTypes.ICE_EQ;
          }
        case 98: break;
        case 29: 
          { return SliceTypes.ICE_KW_BOOL;
          }
        case 99: break;
        case 26: 
          { return SliceTypes.ICE_KW_LONG;
          }
        case 100: break;
        case 1: 
          { return com.intellij.psi.TokenType.BAD_CHARACTER;
          }
        case 101: break;
        case 51: 
          { return SliceTypes.ICE_KW_LOCAL_OBJECT;
          }
        case 102: break;
        default:
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            zzDoEOF();
            return null;
          }
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
