package org.xblackcat.frozenidea.code.highlight;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.util.SliceBundle;
import org.xblackcat.frozenidea.util.SliceIcons;

import javax.swing.*;
import java.util.Map;

/**
 * 23.09.2015 12:15
 *
 * @author xBlackCat
 */
public class SliceColorSettingsPage implements ColorSettingsPage {
    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor(SliceBundle.message("highlight.key.keyword"), SliceHighlighterColors.ICE_KEYWORD),
            new AttributesDescriptor(SliceBundle.message("highlight.key.string_literal"), SliceHighlighterColors.ICE_STRING_LITERAL),
            new AttributesDescriptor(SliceBundle.message("highlight.key.numeric_literal"), SliceHighlighterColors.ICE_NUMERIC_LITERAL),
            new AttributesDescriptor(SliceBundle.message("highlight.key.doc_comment"), SliceHighlighterColors.ICE_DOC_STYLE_COMMENT),
            new AttributesDescriptor(SliceBundle.message("highlight.key.block_comment"), SliceHighlighterColors.ICE_C_STYLE_COMMENT),
            new AttributesDescriptor(SliceBundle.message("highlight.key.line_comment"), SliceHighlighterColors.ICE_END_OF_LINE_COMMENT),
            new AttributesDescriptor(SliceBundle.message("highlight.key.directive"), SliceHighlighterColors.ICE_DIRECTIVE),
            new AttributesDescriptor(SliceBundle.message("highlight.key.parentheses"), SliceHighlighterColors.ICE_PARENTHESES),
            new AttributesDescriptor(SliceBundle.message("highlight.key.braces"), SliceHighlighterColors.ICE_BRACES),
            new AttributesDescriptor(SliceBundle.message("highlight.key.brackets"), SliceHighlighterColors.ICE_BRACKETS),
    };

    @Nullable
    @Override
    public Icon getIcon() {
        return SliceIcons.FACET_ICON;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new SliceSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return "#pragma once\n" +
                "\n" +
                "/**\n" +
                " * Documentation \n" +
                " **/\n" +
                "[[\"java:package:org.example\"]]\n" +
                "module ExternalModule {\n" +
                "  exception SomeException {\n" +
                "    /* multi-line comment */\n" +
                "    string reason;\n" +
                "  };\n" +
                " \n" +
                "  enum SomeEnum { One, Two, Three };\n" +
                " \n" +
                "  const string SomeString = \"This is a string\";\n" +
                "  const int SomeInt = 10;\n" +
                "\n" +
                "  [\"deprecated\"]\n" +
                "  class DataClass {\n" +
                "    int field;\n" +
                "  };\n" +
                "  \n" +
                "  // Single-line comment\n" +
                "  class AnyClass extends DataClass {\n" +
                "    string method(int parameter) throws SomeException;\n" +
                "  };\n" +
                "};";
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Slice (ZeroC ICE)";
    }
}
