// This is a generated file. Not intended for manual editing.
package org.xblackcat.frozenidea.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import org.xblackcat.frozenidea.psi.impl.*;

public interface SliceTypes {

  IElementType ICE_BOOL_LITERAL = new SliceCompositeElementType("ICE_BOOL_LITERAL");
  IElementType ICE_CLASS_BODY = new SliceCompositeElementType("ICE_CLASS_BODY");
  IElementType ICE_CLASS_DEF = new SliceCompositeElementType("ICE_CLASS_DEF");
  IElementType ICE_CONSTANT_DEF = new SliceCompositeElementType("ICE_CONSTANT_DEF");
  IElementType ICE_CONSTANT_VALUE = new SliceCompositeElementType("ICE_CONSTANT_VALUE");
  IElementType ICE_DATA_TYPE = new SliceCompositeElementType("ICE_DATA_TYPE");
  IElementType ICE_DICTIONARY_DEF = new SliceCompositeElementType("ICE_DICTIONARY_DEF");
  IElementType ICE_DICTIONARY_TYPE_LIST = new SliceCompositeElementType("ICE_DICTIONARY_TYPE_LIST");
  IElementType ICE_ELEMENT_TYPE = new SliceCompositeElementType("ICE_ELEMENT_TYPE");
  IElementType ICE_ENUM_CONSTANT = new SliceCompositeElementType("ICE_ENUM_CONSTANT");
  IElementType ICE_ENUM_CONSTANT_INITIALIZER = new SliceCompositeElementType("ICE_ENUM_CONSTANT_INITIALIZER");
  IElementType ICE_ENUM_CONSTANT_LIST = new SliceCompositeElementType("ICE_ENUM_CONSTANT_LIST");
  IElementType ICE_ENUM_CONSTANT_REFERENCE = new SliceCompositeElementType("ICE_ENUM_CONSTANT_REFERENCE");
  IElementType ICE_ENUM_DEF = new SliceCompositeElementType("ICE_ENUM_DEF");
  IElementType ICE_EXCEPTION_DEF = new SliceCompositeElementType("ICE_EXCEPTION_DEF");
  IElementType ICE_EXTENDS_DEF = new SliceCompositeElementType("ICE_EXTENDS_DEF");
  IElementType ICE_EXTENDS_LIST = new SliceCompositeElementType("ICE_EXTENDS_LIST");
  IElementType ICE_FIELD_DEF = new SliceCompositeElementType("ICE_FIELD_DEF");
  IElementType ICE_FIELD_INITIALIZER = new SliceCompositeElementType("ICE_FIELD_INITIALIZER");
  IElementType ICE_FLOAT_LITERAL = new SliceCompositeElementType("ICE_FLOAT_LITERAL");
  IElementType ICE_GLOBAL_METADATA = new SliceCompositeElementType("ICE_GLOBAL_METADATA");
  IElementType ICE_IMPLEMENTS_DEF = new SliceCompositeElementType("ICE_IMPLEMENTS_DEF");
  IElementType ICE_INTEGER_LITERAL = new SliceCompositeElementType("ICE_INTEGER_LITERAL");
  IElementType ICE_INTERFACE_BODY = new SliceCompositeElementType("ICE_INTERFACE_BODY");
  IElementType ICE_INTERFACE_DEF = new SliceCompositeElementType("ICE_INTERFACE_DEF");
  IElementType ICE_KEY_TYPE = new SliceCompositeElementType("ICE_KEY_TYPE");
  IElementType ICE_METADATA = new SliceCompositeElementType("ICE_METADATA");
  IElementType ICE_METADATA_ELEMENT = new SliceCompositeElementType("ICE_METADATA_ELEMENT");
  IElementType ICE_METHOD_DEF = new SliceCompositeElementType("ICE_METHOD_DEF");
  IElementType ICE_METHOD_RETURN_TYPE = new SliceCompositeElementType("ICE_METHOD_RETURN_TYPE");
  IElementType ICE_MODIFIER = new SliceCompositeElementType("ICE_MODIFIER");
  IElementType ICE_MODULE = new SliceCompositeElementType("ICE_MODULE");
  IElementType ICE_MODULE_PATH = new SliceCompositeElementType("ICE_MODULE_PATH");
  IElementType ICE_NUMBER_LITERAL = new SliceCompositeElementType("ICE_NUMBER_LITERAL");
  IElementType ICE_PARAMETER = new SliceCompositeElementType("ICE_PARAMETER");
  IElementType ICE_PARAMETERS_LIST = new SliceCompositeElementType("ICE_PARAMETERS_LIST");
  IElementType ICE_PARAMETER_MODIFIER = new SliceCompositeElementType("ICE_PARAMETER_MODIFIER");
  IElementType ICE_PROXY_TYPE = new SliceCompositeElementType("ICE_PROXY_TYPE");
  IElementType ICE_SEQUENCE_DEF = new SliceCompositeElementType("ICE_SEQUENCE_DEF");
  IElementType ICE_SEQUENCE_TYPE_LIST = new SliceCompositeElementType("ICE_SEQUENCE_TYPE_LIST");
  IElementType ICE_STRING_LITERAL = new SliceCompositeElementType("ICE_STRING_LITERAL");
  IElementType ICE_STRUCT_DEF = new SliceCompositeElementType("ICE_STRUCT_DEF");
  IElementType ICE_THROWS_DEF = new SliceCompositeElementType("ICE_THROWS_DEF");
  IElementType ICE_THROWS_LIST = new SliceCompositeElementType("ICE_THROWS_LIST");
  IElementType ICE_TYPE_REFERENCE = new SliceCompositeElementType("ICE_TYPE_REFERENCE");
  IElementType ICE_VARIABLE_TYPE = new SliceCompositeElementType("ICE_VARIABLE_TYPE");

  IElementType ICE_ASTERISK = new SliceTokenType("*");
  IElementType ICE_BAD_STRING = new SliceTokenType("bad_string");
  IElementType ICE_CLOSE_GL_MD = new SliceTokenType("]]");
  IElementType ICE_COMA = new SliceTokenType(",");
  IElementType ICE_C_STYLE_COMMENT = new SliceTokenType("c_style_comment");
  IElementType ICE_DIRECTIVE = new SliceTokenType("directive");
  IElementType ICE_DOC_STYLE_COMMENT = new SliceTokenType("doc_style_comment");
  IElementType ICE_END_OF_LINE_COMMENT = new SliceTokenType("end_of_line_comment");
  IElementType ICE_EQ = new SliceTokenType("=");
  IElementType ICE_ESCAPE_SEQUENCE = new SliceTokenType("escape_sequence");
  IElementType ICE_FLOAT_VALUE = new SliceTokenType("float_value");
  IElementType ICE_GT = new SliceTokenType(">");
  IElementType ICE_ID = new SliceTokenType("id");
  IElementType ICE_INTEGER_VALUE = new SliceTokenType("integer_value");
  IElementType ICE_KW_BOOL = new SliceTokenType("bool");
  IElementType ICE_KW_BYTE = new SliceTokenType("byte");
  IElementType ICE_KW_CLASS = new SliceTokenType("class");
  IElementType ICE_KW_CONST = new SliceTokenType("const");
  IElementType ICE_KW_DICTIONARY = new SliceTokenType("dictionary");
  IElementType ICE_KW_DOUBLE = new SliceTokenType("double");
  IElementType ICE_KW_ENUM = new SliceTokenType("enum");
  IElementType ICE_KW_EXCEPTION = new SliceTokenType("exception");
  IElementType ICE_KW_EXTENDS = new SliceTokenType("extends");
  IElementType ICE_KW_FALSE = new SliceTokenType("false");
  IElementType ICE_KW_FLOAT = new SliceTokenType("float");
  IElementType ICE_KW_IDEMPOTENT = new SliceTokenType("idempotent");
  IElementType ICE_KW_IMPLEMENTS = new SliceTokenType("implements");
  IElementType ICE_KW_INT = new SliceTokenType("int");
  IElementType ICE_KW_INTERFACE = new SliceTokenType("interface");
  IElementType ICE_KW_LOCAL = new SliceTokenType("local");
  IElementType ICE_KW_LOCAL_OBJECT = new SliceTokenType("LocalObject");
  IElementType ICE_KW_LONG = new SliceTokenType("long");
  IElementType ICE_KW_MODULE = new SliceTokenType("module");
  IElementType ICE_KW_OBJECT = new SliceTokenType("Object");
  IElementType ICE_KW_OUT = new SliceTokenType("out");
  IElementType ICE_KW_SEQUENCE = new SliceTokenType("sequence");
  IElementType ICE_KW_SHORT = new SliceTokenType("short");
  IElementType ICE_KW_STRING = new SliceTokenType("string");
  IElementType ICE_KW_STRUCT = new SliceTokenType("struct");
  IElementType ICE_KW_THROWS = new SliceTokenType("throws");
  IElementType ICE_KW_TRUE = new SliceTokenType("true");
  IElementType ICE_KW_VOID = new SliceTokenType("void");
  IElementType ICE_LEFT_BRACE = new SliceTokenType("{");
  IElementType ICE_LEFT_BRACKET = new SliceTokenType("[");
  IElementType ICE_LEFT_PARENTH = new SliceTokenType("(");
  IElementType ICE_LT = new SliceTokenType("<");
  IElementType ICE_NAME_SEPARATOR = new SliceTokenType("::");
  IElementType ICE_OPEN_GL_MD = new SliceTokenType("[[");
  IElementType ICE_RIGHT_BRACE = new SliceTokenType("}");
  IElementType ICE_RIGHT_BRACKET = new SliceTokenType("]");
  IElementType ICE_RIGHT_PARENTH = new SliceTokenType(")");
  IElementType ICE_SEMICOLON = new SliceTokenType(";");
  IElementType ICE_STRING_VALUE = new SliceTokenType("string_value");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ICE_BOOL_LITERAL) {
        return new SliceBoolLiteralImpl(node);
      }
      else if (type == ICE_CLASS_BODY) {
        return new SliceClassBodyImpl(node);
      }
      else if (type == ICE_CLASS_DEF) {
        return new SliceClassDefImpl(node);
      }
      else if (type == ICE_CONSTANT_DEF) {
        return new SliceConstantDefImpl(node);
      }
      else if (type == ICE_CONSTANT_VALUE) {
        return new SliceConstantValueImpl(node);
      }
      else if (type == ICE_DATA_TYPE) {
        return new SliceDataTypeImpl(node);
      }
      else if (type == ICE_DICTIONARY_DEF) {
        return new SliceDictionaryDefImpl(node);
      }
      else if (type == ICE_DICTIONARY_TYPE_LIST) {
        return new SliceDictionaryTypeListImpl(node);
      }
      else if (type == ICE_ELEMENT_TYPE) {
        return new SliceElementTypeImpl(node);
      }
      else if (type == ICE_ENUM_CONSTANT) {
        return new SliceEnumConstantImpl(node);
      }
      else if (type == ICE_ENUM_CONSTANT_INITIALIZER) {
        return new SliceEnumConstantInitializerImpl(node);
      }
      else if (type == ICE_ENUM_CONSTANT_LIST) {
        return new SliceEnumConstantListImpl(node);
      }
      else if (type == ICE_ENUM_CONSTANT_REFERENCE) {
        return new SliceEnumConstantReferenceImpl(node);
      }
      else if (type == ICE_ENUM_DEF) {
        return new SliceEnumDefImpl(node);
      }
      else if (type == ICE_EXCEPTION_DEF) {
        return new SliceExceptionDefImpl(node);
      }
      else if (type == ICE_EXTENDS_DEF) {
        return new SliceExtendsDefImpl(node);
      }
      else if (type == ICE_EXTENDS_LIST) {
        return new SliceExtendsListImpl(node);
      }
      else if (type == ICE_FIELD_DEF) {
        return new SliceFieldDefImpl(node);
      }
      else if (type == ICE_FIELD_INITIALIZER) {
        return new SliceFieldInitializerImpl(node);
      }
      else if (type == ICE_FLOAT_LITERAL) {
        return new SliceFloatLiteralImpl(node);
      }
      else if (type == ICE_GLOBAL_METADATA) {
        return new SliceGlobalMetadataImpl(node);
      }
      else if (type == ICE_IMPLEMENTS_DEF) {
        return new SliceImplementsDefImpl(node);
      }
      else if (type == ICE_INTEGER_LITERAL) {
        return new SliceIntegerLiteralImpl(node);
      }
      else if (type == ICE_INTERFACE_BODY) {
        return new SliceInterfaceBodyImpl(node);
      }
      else if (type == ICE_INTERFACE_DEF) {
        return new SliceInterfaceDefImpl(node);
      }
      else if (type == ICE_KEY_TYPE) {
        return new SliceKeyTypeImpl(node);
      }
      else if (type == ICE_METADATA) {
        return new SliceMetadataImpl(node);
      }
      else if (type == ICE_METADATA_ELEMENT) {
        return new SliceMetadataElementImpl(node);
      }
      else if (type == ICE_METHOD_DEF) {
        return new SliceMethodDefImpl(node);
      }
      else if (type == ICE_METHOD_RETURN_TYPE) {
        return new SliceMethodReturnTypeImpl(node);
      }
      else if (type == ICE_MODIFIER) {
        return new SliceModifierImpl(node);
      }
      else if (type == ICE_MODULE) {
        return new SliceModuleImpl(node);
      }
      else if (type == ICE_MODULE_PATH) {
        return new SliceModulePathImpl(node);
      }
      else if (type == ICE_NUMBER_LITERAL) {
        return new SliceNumberLiteralImpl(node);
      }
      else if (type == ICE_PARAMETER) {
        return new SliceParameterImpl(node);
      }
      else if (type == ICE_PARAMETERS_LIST) {
        return new SliceParametersListImpl(node);
      }
      else if (type == ICE_PARAMETER_MODIFIER) {
        return new SliceParameterModifierImpl(node);
      }
      else if (type == ICE_PROXY_TYPE) {
        return new SliceProxyTypeImpl(node);
      }
      else if (type == ICE_SEQUENCE_DEF) {
        return new SliceSequenceDefImpl(node);
      }
      else if (type == ICE_SEQUENCE_TYPE_LIST) {
        return new SliceSequenceTypeListImpl(node);
      }
      else if (type == ICE_STRING_LITERAL) {
        return new SliceStringLiteralImpl(node);
      }
      else if (type == ICE_STRUCT_DEF) {
        return new SliceStructDefImpl(node);
      }
      else if (type == ICE_THROWS_DEF) {
        return new SliceThrowsDefImpl(node);
      }
      else if (type == ICE_THROWS_LIST) {
        return new SliceThrowsListImpl(node);
      }
      else if (type == ICE_TYPE_REFERENCE) {
        return new SliceTypeReferenceImpl(node);
      }
      else if (type == ICE_VARIABLE_TYPE) {
        return new SliceVariableTypeImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
