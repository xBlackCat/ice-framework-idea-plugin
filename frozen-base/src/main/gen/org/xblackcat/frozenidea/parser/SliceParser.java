// This is a generated file. Not intended for manual editing.
package org.xblackcat.frozenidea.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static org.xblackcat.frozenidea.psi.SliceTypes.*;
import static org.xblackcat.frozenidea.parser.SliceParserUtil.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class SliceParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return root(b, l + 1);
  }

  /* ********************************************************** */
  // '{' body_element_list '}'
  public static boolean body_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "body_block")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ICE_BODY_BLOCK, "<body block>");
    r = consumeToken(b, ICE_LEFT_BRACE);
    p = r; // pin = 1
    r = r && report_error_(b, body_element_list(b, l + 1));
    r = p && consumeToken(b, ICE_RIGHT_BRACE) && r;
    exit_section_(b, l, m, r, p, body_block_recovery_parser_);
    return r || p;
  }

  /* ********************************************************** */
  // !(';')
  static boolean body_block_recovery(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "body_block_recovery")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, ICE_SEMICOLON);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ';' | ',' | method_def | field_def | enum_constant
  static boolean body_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "body_element")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, ICE_SEMICOLON);
    if (!r) r = consumeToken(b, ICE_COMA);
    if (!r) r = method_def(b, l + 1);
    if (!r) r = field_def(b, l + 1);
    if (!r) r = enum_constant(b, l + 1);
    exit_section_(b, l, m, r, false, body_element_recovery_parser_);
    return r;
  }

  /* ********************************************************** */
  // body_element*
  static boolean body_element_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "body_element_list")) return false;
    Marker m = enter_section_(b, l, _NONE_);
    while (true) {
      int c = current_position_(b);
      if (!body_element(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "body_element_list", c)) break;
    }
    exit_section_(b, l, m, true, false, body_element_list_recovery_parser_);
    return true;
  }

  /* ********************************************************** */
  // !('}')
  static boolean body_element_list_recovery(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "body_element_list_recovery")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, ICE_RIGHT_BRACE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !(field_def | method_def | enum_constant | constant_def | '}' | '[')
  static boolean body_element_recovery(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "body_element_recovery")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !body_element_recovery_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // field_def | method_def | enum_constant | constant_def | '}' | '['
  private static boolean body_element_recovery_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "body_element_recovery_0")) return false;
    boolean r;
    r = field_def(b, l + 1);
    if (!r) r = method_def(b, l + 1);
    if (!r) r = enum_constant(b, l + 1);
    if (!r) r = constant_def(b, l + 1);
    if (!r) r = consumeToken(b, ICE_RIGHT_BRACE);
    if (!r) r = consumeToken(b, ICE_LEFT_BRACKET);
    return r;
  }

  /* ********************************************************** */
  // 'false' | 'true'
  public static boolean bool_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bool_literal")) return false;
    if (!nextTokenIs(b, "<bool literal>", ICE_KW_FALSE, ICE_KW_TRUE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ICE_BOOL_LITERAL, "<bool literal>");
    r = consumeToken(b, ICE_KW_FALSE);
    if (!r) r = consumeToken(b, ICE_KW_TRUE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // metadata* 'const' variable_type id field_initializer ';'
  public static boolean constant_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constant_def")) return false;
    if (!nextTokenIs(b, "<constant def>", ICE_KW_CONST, ICE_LEFT_BRACKET)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ICE_CONSTANT_DEF, "<constant def>");
    r = constant_def_0(b, l + 1);
    r = r && consumeToken(b, ICE_KW_CONST);
    p = r; // pin = 2
    r = r && report_error_(b, variable_type(b, l + 1));
    r = p && report_error_(b, consumeToken(b, ICE_ID)) && r;
    r = p && report_error_(b, field_initializer(b, l + 1)) && r;
    r = p && consumeToken(b, ICE_SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // metadata*
  private static boolean constant_def_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constant_def_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!metadata(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "constant_def_0", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // number_literal | bool_literal | string_literal | enum_constant_reference
  public static boolean constant_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constant_value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ICE_CONSTANT_VALUE, "<constant value>");
    r = number_literal(b, l + 1);
    if (!r) r = bool_literal(b, l + 1);
    if (!r) r = string_literal(b, l + 1);
    if (!r) r = enum_constant_reference(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // proxy_type | type_reference | primitive_data_types | simple_data_types
  public static boolean data_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ICE_DATA_TYPE, "<data type>");
    r = proxy_type(b, l + 1);
    if (!r) r = type_reference(b, l + 1);
    if (!r) r = primitive_data_types(b, l + 1);
    if (!r) r = simple_data_types(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // metadata* type_word generic_type? id extends_def? implements_def? body_block? ';'
  public static boolean data_type_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_type_element")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ICE_DATA_TYPE_ELEMENT, "<data type element>");
    r = data_type_element_0(b, l + 1);
    r = r && type_word(b, l + 1);
    p = r; // pin = 2
    r = r && report_error_(b, data_type_element_2(b, l + 1));
    r = p && report_error_(b, consumeToken(b, ICE_ID)) && r;
    r = p && report_error_(b, data_type_element_4(b, l + 1)) && r;
    r = p && report_error_(b, data_type_element_5(b, l + 1)) && r;
    r = p && report_error_(b, data_type_element_6(b, l + 1)) && r;
    r = p && consumeToken(b, ICE_SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // metadata*
  private static boolean data_type_element_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_type_element_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!metadata(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "data_type_element_0", c)) break;
    }
    return true;
  }

  // generic_type?
  private static boolean data_type_element_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_type_element_2")) return false;
    generic_type(b, l + 1);
    return true;
  }

  // extends_def?
  private static boolean data_type_element_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_type_element_4")) return false;
    extends_def(b, l + 1);
    return true;
  }

  // implements_def?
  private static boolean data_type_element_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_type_element_5")) return false;
    implements_def(b, l + 1);
    return true;
  }

  // body_block?
  private static boolean data_type_element_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_type_element_6")) return false;
    body_block(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // !('[' | 'const' | 'module' | 'class' | 'exception' | 'interface' | 'sequence' | 'struct' | 'dictionary' | 'enum' | '}' | <<eof>>)
  static boolean element_recovery(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "element_recovery")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !element_recovery_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '[' | 'const' | 'module' | 'class' | 'exception' | 'interface' | 'sequence' | 'struct' | 'dictionary' | 'enum' | '}' | <<eof>>
  private static boolean element_recovery_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "element_recovery_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ICE_LEFT_BRACKET);
    if (!r) r = consumeToken(b, ICE_KW_CONST);
    if (!r) r = consumeToken(b, ICE_KW_MODULE);
    if (!r) r = consumeToken(b, ICE_KW_CLASS);
    if (!r) r = consumeToken(b, ICE_KW_EXCEPTION);
    if (!r) r = consumeToken(b, ICE_KW_INTERFACE);
    if (!r) r = consumeToken(b, ICE_KW_SEQUENCE);
    if (!r) r = consumeToken(b, ICE_KW_STRUCT);
    if (!r) r = consumeToken(b, ICE_KW_DICTIONARY);
    if (!r) r = consumeToken(b, ICE_KW_ENUM);
    if (!r) r = consumeToken(b, ICE_RIGHT_BRACE);
    if (!r) r = eof(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // id enum_constant_initializer?
  public static boolean enum_constant(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_constant")) return false;
    if (!nextTokenIs(b, ICE_ID)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ICE_ENUM_CONSTANT, null);
    r = consumeToken(b, ICE_ID);
    p = r; // pin = 1
    r = r && enum_constant_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // enum_constant_initializer?
  private static boolean enum_constant_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_constant_1")) return false;
    enum_constant_initializer(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '=' number_literal
  public static boolean enum_constant_initializer(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_constant_initializer")) return false;
    if (!nextTokenIs(b, ICE_EQ)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ICE_ENUM_CONSTANT_INITIALIZER, null);
    r = consumeToken(b, ICE_EQ);
    p = r; // pin = 1
    r = r && number_literal(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // id
  public static boolean enum_constant_reference(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_constant_reference")) return false;
    if (!nextTokenIs(b, ICE_ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ICE_ID);
    exit_section_(b, m, ICE_ENUM_CONSTANT_REFERENCE, r);
    return r;
  }

  /* ********************************************************** */
  // 'extends' extends_list
  public static boolean extends_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extends_def")) return false;
    if (!nextTokenIs(b, ICE_KW_EXTENDS)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ICE_EXTENDS_DEF, null);
    r = consumeToken(b, ICE_KW_EXTENDS);
    p = r; // pin = 1
    r = r && extends_list(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // type_reference (',' type_reference) *
  public static boolean extends_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extends_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ICE_EXTENDS_LIST, "<extends list>");
    r = type_reference(b, l + 1);
    r = r && extends_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, extends_list_recovery_parser_);
    return r;
  }

  // (',' type_reference) *
  private static boolean extends_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extends_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!extends_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "extends_list_1", c)) break;
    }
    return true;
  }

  // ',' type_reference
  private static boolean extends_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extends_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ICE_COMA);
    r = r && type_reference(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !('implements' | ';' | '{')
  static boolean extends_list_recovery(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extends_list_recovery")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !extends_list_recovery_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // 'implements' | ';' | '{'
  private static boolean extends_list_recovery_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "extends_list_recovery_0")) return false;
    boolean r;
    r = consumeToken(b, ICE_KW_IMPLEMENTS);
    if (!r) r = consumeToken(b, ICE_SEMICOLON);
    if (!r) r = consumeToken(b, ICE_LEFT_BRACE);
    return r;
  }

  /* ********************************************************** */
  // metadata* variable_type id (field_initializer )? ';'
  public static boolean field_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "field_def")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ICE_FIELD_DEF, "<field def>");
    r = field_def_0(b, l + 1);
    r = r && variable_type(b, l + 1);
    p = r; // pin = 2
    r = r && report_error_(b, consumeToken(b, ICE_ID));
    r = p && report_error_(b, field_def_3(b, l + 1)) && r;
    r = p && consumeToken(b, ICE_SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // metadata*
  private static boolean field_def_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "field_def_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!metadata(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "field_def_0", c)) break;
    }
    return true;
  }

  // (field_initializer )?
  private static boolean field_def_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "field_def_3")) return false;
    field_def_3_0(b, l + 1);
    return true;
  }

  // (field_initializer )
  private static boolean field_def_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "field_def_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = field_initializer(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '=' constant_value
  public static boolean field_initializer(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "field_initializer")) return false;
    if (!nextTokenIs(b, ICE_EQ)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ICE_FIELD_INITIALIZER, null);
    r = consumeToken(b, ICE_EQ);
    p = r; // pin = 1
    r = r && constant_value(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // float_value
  public static boolean float_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "float_literal")) return false;
    if (!nextTokenIs(b, ICE_FLOAT_VALUE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ICE_FLOAT_VALUE);
    exit_section_(b, m, ICE_FLOAT_LITERAL, r);
    return r;
  }

  /* ********************************************************** */
  // '<' generic_types '>'
  public static boolean generic_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_type")) return false;
    if (!nextTokenIs(b, ICE_LT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ICE_GENERIC_TYPE, null);
    r = consumeToken(b, ICE_LT);
    p = r; // pin = 1
    r = r && report_error_(b, generic_types(b, l + 1));
    r = p && consumeToken(b, ICE_GT) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // data_type second_generic_type?
  static boolean generic_types(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_types")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = data_type(b, l + 1);
    r = r && generic_types_1(b, l + 1);
    exit_section_(b, l, m, r, false, type_recovery_parser_);
    return r;
  }

  // second_generic_type?
  private static boolean generic_types_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_types_1")) return false;
    second_generic_type(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '[[' global_metadata_body? ']]'
  public static boolean global_metadata(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "global_metadata")) return false;
    if (!nextTokenIs(b, ICE_OPEN_GL_MD)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ICE_GLOBAL_METADATA, null);
    r = consumeToken(b, ICE_OPEN_GL_MD);
    p = r; // pin = 1
    r = r && report_error_(b, global_metadata_1(b, l + 1));
    r = p && consumeToken(b, ICE_CLOSE_GL_MD) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // global_metadata_body?
  private static boolean global_metadata_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "global_metadata_1")) return false;
    global_metadata_body(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // metadata_element (',' metadata_element)*
  static boolean global_metadata_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "global_metadata_body")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = metadata_element(b, l + 1);
    r = r && global_metadata_body_1(b, l + 1);
    exit_section_(b, l, m, r, false, global_metadata_element_recovery_parser_);
    return r;
  }

  // (',' metadata_element)*
  private static boolean global_metadata_body_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "global_metadata_body_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!global_metadata_body_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "global_metadata_body_1", c)) break;
    }
    return true;
  }

  // ',' metadata_element
  private static boolean global_metadata_body_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "global_metadata_body_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ICE_COMA);
    r = r && metadata_element(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(']]' | global_metadata | module)
  static boolean global_metadata_element_recovery(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "global_metadata_element_recovery")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !global_metadata_element_recovery_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ']]' | global_metadata | module
  private static boolean global_metadata_element_recovery_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "global_metadata_element_recovery_0")) return false;
    boolean r;
    r = consumeToken(b, ICE_CLOSE_GL_MD);
    if (!r) r = global_metadata(b, l + 1);
    if (!r) r = module(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // (global_metadata)*
  static boolean global_metadata_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "global_metadata_list")) return false;
    Marker m = enter_section_(b, l, _NONE_);
    while (true) {
      int c = current_position_(b);
      if (!global_metadata_list_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "global_metadata_list", c)) break;
    }
    exit_section_(b, l, m, true, false, global_metadata_recovery_parser_);
    return true;
  }

  // (global_metadata)
  private static boolean global_metadata_list_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "global_metadata_list_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = global_metadata(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(module | global_metadata | metadata)
  static boolean global_metadata_recovery(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "global_metadata_recovery")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !global_metadata_recovery_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // module | global_metadata | metadata
  private static boolean global_metadata_recovery_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "global_metadata_recovery_0")) return false;
    boolean r;
    r = module(b, l + 1);
    if (!r) r = global_metadata(b, l + 1);
    if (!r) r = metadata(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // 'implements' extends_list
  public static boolean implements_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "implements_def")) return false;
    if (!nextTokenIs(b, ICE_KW_IMPLEMENTS)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ICE_IMPLEMENTS_DEF, null);
    r = consumeToken(b, ICE_KW_IMPLEMENTS);
    p = r; // pin = 1
    r = r && extends_list(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // integer_value
  public static boolean integer_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "integer_literal")) return false;
    if (!nextTokenIs(b, ICE_INTEGER_VALUE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ICE_INTEGER_VALUE);
    exit_section_(b, m, ICE_INTEGER_LITERAL, r);
    return r;
  }

  /* ********************************************************** */
  // '[' metadata_body? ']'
  public static boolean metadata(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "metadata")) return false;
    if (!nextTokenIs(b, ICE_LEFT_BRACKET)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ICE_METADATA, null);
    r = consumeToken(b, ICE_LEFT_BRACKET);
    p = r; // pin = 1
    r = r && report_error_(b, metadata_1(b, l + 1));
    r = p && consumeToken(b, ICE_RIGHT_BRACKET) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // metadata_body?
  private static boolean metadata_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "metadata_1")) return false;
    metadata_body(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // metadata_element (',' metadata_element)*
  static boolean metadata_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "metadata_body")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = metadata_element(b, l + 1);
    r = r && metadata_body_1(b, l + 1);
    exit_section_(b, l, m, r, false, metadata_element_recovery_parser_);
    return r;
  }

  // (',' metadata_element)*
  private static boolean metadata_body_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "metadata_body_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!metadata_body_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "metadata_body_1", c)) break;
    }
    return true;
  }

  // ',' metadata_element
  private static boolean metadata_body_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "metadata_body_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ICE_COMA);
    r = r && metadata_element(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // string_literal | bad_string
  public static boolean metadata_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "metadata_element")) return false;
    if (!nextTokenIs(b, "<metadata element>", ICE_BAD_STRING, ICE_STRING_VALUE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ICE_METADATA_ELEMENT, "<metadata element>");
    r = string_literal(b, l + 1);
    if (!r) r = consumeToken(b, ICE_BAD_STRING);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !(']' | module | element_recovery)
  static boolean metadata_element_recovery(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "metadata_element_recovery")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !metadata_element_recovery_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ']' | module | element_recovery
  private static boolean metadata_element_recovery_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "metadata_element_recovery_0")) return false;
    boolean r;
    r = consumeToken(b, ICE_RIGHT_BRACKET);
    if (!r) r = module(b, l + 1);
    if (!r) r = element_recovery(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // metadata* modifier* method_return_type id '(' parameters_list ')' (throws_def)? ';'
  public static boolean method_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method_def")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ICE_METHOD_DEF, "<method def>");
    r = method_def_0(b, l + 1);
    r = r && method_def_1(b, l + 1);
    r = r && method_return_type(b, l + 1);
    r = r && consumeTokens(b, 2, ICE_ID, ICE_LEFT_PARENTH);
    p = r; // pin = 5
    r = r && report_error_(b, parameters_list(b, l + 1));
    r = p && report_error_(b, consumeToken(b, ICE_RIGHT_PARENTH)) && r;
    r = p && report_error_(b, method_def_7(b, l + 1)) && r;
    r = p && consumeToken(b, ICE_SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // metadata*
  private static boolean method_def_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method_def_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!metadata(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "method_def_0", c)) break;
    }
    return true;
  }

  // modifier*
  private static boolean method_def_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method_def_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!modifier(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "method_def_1", c)) break;
    }
    return true;
  }

  // (throws_def)?
  private static boolean method_def_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method_def_7")) return false;
    method_def_7_0(b, l + 1);
    return true;
  }

  // (throws_def)
  private static boolean method_def_7_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method_def_7_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = throws_def(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // data_type | 'void'
  public static boolean method_return_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method_return_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ICE_METHOD_RETURN_TYPE, "<method return type>");
    r = data_type(b, l + 1);
    if (!r) r = consumeToken(b, ICE_KW_VOID);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'idempotent' | 'local'
  public static boolean modifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modifier")) return false;
    if (!nextTokenIs(b, "<modifier>", ICE_KW_IDEMPOTENT, ICE_KW_LOCAL)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ICE_MODIFIER, "<modifier>");
    r = consumeToken(b, ICE_KW_IDEMPOTENT);
    if (!r) r = consumeToken(b, ICE_KW_LOCAL);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // metadata? 'module' id '{' module_body '}' ';'
  public static boolean module(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module")) return false;
    if (!nextTokenIs(b, "<module>", ICE_KW_MODULE, ICE_LEFT_BRACKET)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ICE_MODULE, "<module>");
    r = module_0(b, l + 1);
    r = r && consumeTokens(b, 1, ICE_KW_MODULE, ICE_ID, ICE_LEFT_BRACE);
    p = r; // pin = 2
    r = r && report_error_(b, module_body(b, l + 1));
    r = p && report_error_(b, consumeTokens(b, -1, ICE_RIGHT_BRACE, ICE_SEMICOLON)) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // metadata?
  private static boolean module_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_0")) return false;
    metadata(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // (';' | module_element) *
  static boolean module_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_body")) return false;
    Marker m = enter_section_(b, l, _NONE_);
    while (true) {
      int c = current_position_(b);
      if (!module_body_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "module_body", c)) break;
    }
    exit_section_(b, l, m, true, false, body_element_list_recovery_parser_);
    return true;
  }

  // ';' | module_element
  private static boolean module_body_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_body_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ICE_SEMICOLON);
    if (!r) r = module_element(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // constant_def | data_type_element | module
  static boolean module_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_element")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = constant_def(b, l + 1);
    if (!r) r = data_type_element(b, l + 1);
    if (!r) r = module(b, l + 1);
    exit_section_(b, l, m, r, false, element_recovery_parser_);
    return r;
  }

  /* ********************************************************** */
  // (module)*
  static boolean module_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_list")) return false;
    Marker m = enter_section_(b, l, _NONE_);
    while (true) {
      int c = current_position_(b);
      if (!module_list_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "module_list", c)) break;
    }
    exit_section_(b, l, m, true, false, element_recovery_parser_);
    return true;
  }

  // (module)
  private static boolean module_list_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_list_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = module(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // path_element*
  public static boolean module_path(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_path")) return false;
    Marker m = enter_section_(b, l, _NONE_, ICE_MODULE_PATH, "<module path>");
    while (true) {
      int c = current_position_(b);
      if (!path_element(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "module_path", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // integer_literal | float_literal
  public static boolean number_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "number_literal")) return false;
    if (!nextTokenIs(b, "<number literal>", ICE_FLOAT_VALUE, ICE_INTEGER_VALUE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ICE_NUMBER_LITERAL, "<number literal>");
    r = integer_literal(b, l + 1);
    if (!r) r = float_literal(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // metadata* parameter_modifier* data_type id
  public static boolean parameter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ICE_PARAMETER, "<parameter>");
    r = parameter_0(b, l + 1);
    r = r && parameter_1(b, l + 1);
    r = r && data_type(b, l + 1);
    p = r; // pin = 3
    r = r && consumeToken(b, ICE_ID);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // metadata*
  private static boolean parameter_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!metadata(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "parameter_0", c)) break;
    }
    return true;
  }

  // parameter_modifier*
  private static boolean parameter_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!parameter_modifier(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "parameter_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // !(')')
  static boolean parameter_list_recovery(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_list_recovery")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, ICE_RIGHT_PARENTH);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'out'
  public static boolean parameter_modifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_modifier")) return false;
    if (!nextTokenIs(b, ICE_KW_OUT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ICE_KW_OUT);
    exit_section_(b, m, ICE_PARAMETER_MODIFIER, r);
    return r;
  }

  /* ********************************************************** */
  // (parameter (',' parameter) *)?
  public static boolean parameters_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameters_list")) return false;
    Marker m = enter_section_(b, l, _NONE_, ICE_PARAMETERS_LIST, "<parameters list>");
    parameters_list_0(b, l + 1);
    exit_section_(b, l, m, true, false, parameter_list_recovery_parser_);
    return true;
  }

  // parameter (',' parameter) *
  private static boolean parameters_list_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameters_list_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parameter(b, l + 1);
    r = r && parameters_list_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (',' parameter) *
  private static boolean parameters_list_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameters_list_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!parameters_list_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "parameters_list_0_1", c)) break;
    }
    return true;
  }

  // ',' parameter
  private static boolean parameters_list_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameters_list_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ICE_COMA);
    r = r && parameter(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // id '::'
  static boolean path_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_element")) return false;
    if (!nextTokenIs(b, ICE_ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 2, ICE_ID, ICE_NAME_SEPARATOR);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'float' | 'double' | 'bool' | 'byte' | 'short' | 'int' | 'long'
  static boolean primitive_data_types(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primitive_data_types")) return false;
    boolean r;
    r = consumeToken(b, ICE_KW_FLOAT);
    if (!r) r = consumeToken(b, ICE_KW_DOUBLE);
    if (!r) r = consumeToken(b, ICE_KW_BOOL);
    if (!r) r = consumeToken(b, ICE_KW_BYTE);
    if (!r) r = consumeToken(b, ICE_KW_SHORT);
    if (!r) r = consumeToken(b, ICE_KW_INT);
    if (!r) r = consumeToken(b, ICE_KW_LONG);
    return r;
  }

  /* ********************************************************** */
  // (type_reference | simple_data_types) '*'
  public static boolean proxy_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "proxy_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ICE_PROXY_TYPE, "<proxy type>");
    r = proxy_type_0(b, l + 1);
    r = r && consumeToken(b, ICE_ASTERISK);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // type_reference | simple_data_types
  private static boolean proxy_type_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "proxy_type_0")) return false;
    boolean r;
    r = type_reference(b, l + 1);
    if (!r) r = simple_data_types(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // slice_block
  static boolean root(PsiBuilder b, int l) {
    return slice_block(b, l + 1);
  }

  /* ********************************************************** */
  // ',' data_type
  public static boolean second_generic_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "second_generic_type")) return false;
    if (!nextTokenIs(b, ICE_COMA)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ICE_SECOND_GENERIC_TYPE, null);
    r = consumeToken(b, ICE_COMA);
    p = r; // pin = 1
    r = r && data_type(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // 'LocalObject' | 'string' | 'Object'
  static boolean simple_data_types(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_data_types")) return false;
    boolean r;
    r = consumeToken(b, ICE_KW_LOCAL_OBJECT);
    if (!r) r = consumeToken(b, ICE_KW_STRING);
    if (!r) r = consumeToken(b, ICE_KW_OBJECT);
    return r;
  }

  /* ********************************************************** */
  // !<<eof>> global_metadata_list module_list
  static boolean slice_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "slice_block")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = slice_block_0(b, l + 1);
    r = r && global_metadata_list(b, l + 1);
    r = r && module_list(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !<<eof>>
  private static boolean slice_block_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "slice_block_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !eof(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // string_value
  public static boolean string_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_literal")) return false;
    if (!nextTokenIs(b, ICE_STRING_VALUE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ICE_STRING_VALUE);
    exit_section_(b, m, ICE_STRING_LITERAL, r);
    return r;
  }

  /* ********************************************************** */
  // 'throws' throws_list
  public static boolean throws_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "throws_def")) return false;
    if (!nextTokenIs(b, ICE_KW_THROWS)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ICE_THROWS_DEF, null);
    r = consumeToken(b, ICE_KW_THROWS);
    p = r; // pin = 1
    r = r && throws_list(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // type_reference (',' type_reference) *
  public static boolean throws_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "throws_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ICE_THROWS_LIST, "<throws list>");
    r = type_reference(b, l + 1);
    r = r && throws_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, throws_list_recovery_parser_);
    return r;
  }

  // (',' type_reference) *
  private static boolean throws_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "throws_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!throws_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "throws_list_1", c)) break;
    }
    return true;
  }

  // ',' type_reference
  private static boolean throws_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "throws_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ICE_COMA);
    r = r && type_reference(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(';' | '{')
  static boolean throws_list_recovery(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "throws_list_recovery")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !throws_list_recovery_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ';' | '{'
  private static boolean throws_list_recovery_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "throws_list_recovery_0")) return false;
    boolean r;
    r = consumeToken(b, ICE_SEMICOLON);
    if (!r) r = consumeToken(b, ICE_LEFT_BRACE);
    return r;
  }

  /* ********************************************************** */
  // !('>')
  static boolean type_recovery(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_recovery")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, ICE_GT);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // module_path id
  public static boolean type_reference(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_reference")) return false;
    if (!nextTokenIs(b, ICE_ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = module_path(b, l + 1);
    r = r && consumeToken(b, ICE_ID);
    exit_section_(b, m, ICE_TYPE_REFERENCE, r);
    return r;
  }

  /* ********************************************************** */
  // 'class'|'interface'|'exception'|'struct'|'enum'|'sequence'|'dictionary'
  public static boolean type_word(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_word")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ICE_TYPE_WORD, "<type word>");
    r = consumeToken(b, ICE_KW_CLASS);
    if (!r) r = consumeToken(b, ICE_KW_INTERFACE);
    if (!r) r = consumeToken(b, ICE_KW_EXCEPTION);
    if (!r) r = consumeToken(b, ICE_KW_STRUCT);
    if (!r) r = consumeToken(b, ICE_KW_ENUM);
    if (!r) r = consumeToken(b, ICE_KW_SEQUENCE);
    if (!r) r = consumeToken(b, ICE_KW_DICTIONARY);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // data_type
  public static boolean variable_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ICE_VARIABLE_TYPE, "<variable type>");
    r = data_type(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  static final Parser body_block_recovery_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return body_block_recovery(b, l + 1);
    }
  };
  static final Parser body_element_list_recovery_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return body_element_list_recovery(b, l + 1);
    }
  };
  static final Parser body_element_recovery_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return body_element_recovery(b, l + 1);
    }
  };
  static final Parser element_recovery_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return element_recovery(b, l + 1);
    }
  };
  static final Parser extends_list_recovery_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return extends_list_recovery(b, l + 1);
    }
  };
  static final Parser global_metadata_element_recovery_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return global_metadata_element_recovery(b, l + 1);
    }
  };
  static final Parser global_metadata_recovery_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return global_metadata_recovery(b, l + 1);
    }
  };
  static final Parser metadata_element_recovery_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return metadata_element_recovery(b, l + 1);
    }
  };
  static final Parser parameter_list_recovery_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return parameter_list_recovery(b, l + 1);
    }
  };
  static final Parser throws_list_recovery_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return throws_list_recovery(b, l + 1);
    }
  };
  static final Parser type_recovery_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return type_recovery(b, l + 1);
    }
  };
}
