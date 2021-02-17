package org.xblackcat.frozenidea.util;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.util.PsiFormatUtilBase;
import com.intellij.util.BitUtil;
import com.intellij.util.ObjectUtils;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.psi.*;

import java.util.List;
import java.util.StringJoiner;

public class PsiFormatUtil extends PsiFormatUtilBase {
    @MagicConstant(flags = {
            SHOW_TYPE, TYPE_AFTER, SHOW_CONTAINING_CLASS, SHOW_FQ_NAME, SHOW_NAME,
            SHOW_INITIALIZER, SHOW_RAW_TYPE, SHOW_RAW_NON_TOP_TYPE, SHOW_FQ_CLASS_NAMES, USE_INTERNAL_CANONICAL_TEXT})
    public @interface FormatVariableOptions {
    }

    @MagicConstant(flags = {
            SHOW_TYPE, TYPE_AFTER, SHOW_CONTAINING_CLASS, SHOW_FQ_NAME, SHOW_NAME,
            SHOW_PARAMETERS, SHOW_THROWS, SHOW_RAW_TYPE, SHOW_RAW_NON_TOP_TYPE, SHOW_FQ_CLASS_NAMES, USE_INTERNAL_CANONICAL_TEXT})
    public @interface FormatMethodOptions {
    }

    @MagicConstant(flags = {
            SHOW_NAME, SHOW_FQ_NAME,
            SHOW_EXTENDS_IMPLEMENTS, SHOW_REDUNDANT_MODIFIERS, JAVADOC_MODIFIERS_ONLY, SHOW_RAW_TYPE})
    public @interface FormatClassOptions {
    }

    public static String formatVariable(SliceVariableElement variable, @FormatVariableOptions int options) {
        StringBuilder buffer = new StringBuilder();
        formatVariable(variable, options, buffer);
        return buffer.toString();
    }

    private static void formatVariable(
            @NotNull SliceVariableElement variable,
            @FormatVariableOptions int options,
            @NotNull StringBuilder buffer
    ) {
        if (BitUtil.isSet(options, SHOW_TYPE) && !BitUtil.isSet(options, TYPE_AFTER)) {
            appendSpaceIfNeeded(buffer);
            buffer.append(formatVariableType(variable, options));
        }
        if (variable instanceof SliceFieldDef && BitUtil.isSet(options, SHOW_CONTAINING_CLASS)) {
            SliceDataTypeElement aClass = ((SliceFieldDef) variable).getContainingClass();
            if (aClass != null) {
                String className = aClass.getName();
                if (className != null) {
                    appendSpaceIfNeeded(buffer);
                    if (BitUtil.isSet(options, SHOW_FQ_NAME)) {
                        buffer.append(ObjectUtils.notNull(aClass.getQualifiedName(), className));
                    } else {
                        buffer.append(className);
                    }
                    buffer.append('.');
                }
            }
            if (BitUtil.isSet(options, SHOW_NAME)) {
                buffer.append(variable.getName());
            }
        } else {
            if (BitUtil.isSet(options, SHOW_NAME)) {
                String name = variable.getName();
                if (StringUtil.isNotEmpty(name)) {
                    appendSpaceIfNeeded(buffer);
                    buffer.append(name);
                }
            }
        }
        if (BitUtil.isSet(options, SHOW_TYPE) && BitUtil.isSet(options, TYPE_AFTER)) {
            if (BitUtil.isSet(options, SHOW_NAME) && variable.getName() != null) {
                buffer.append(':');
            }
            buffer.append(formatVariableType(variable, options));
        }
        if (BitUtil.isSet(options, SHOW_INITIALIZER)) {
            @Nullable SliceFieldInitializer initializer = variable.getFieldInitializer();
            if (initializer != null && initializer.getConstantValue() != null) {
                buffer.append(" = ");
                String text = initializer.getConstantValue().getText();
                int index1 = text.lastIndexOf('\n');
                if (index1 < 0) index1 = text.length();
                int index2 = text.lastIndexOf('\r');
                if (index2 < 0) index2 = text.length();
                int index = Math.min(index1, index2);
                buffer.append(text, 0, index);
                if (index < text.length()) {
                    buffer.append(" ...");
                }
            }
        }
    }

    private static String formatVariableType(@NotNull SliceVariableElement variable, @FormatVariableOptions int options) {
        final SliceDataType type = variable.getDataType();
        if (type != null) {
            return formatType(type, options);
        } else if (variable instanceof SliceEnumConstant) {
            final SliceDataTypeElement containingClass = ((SliceEnumConstant) variable).getContainingClass();
            if (containingClass.isEnum()) {
                return formatClass(containingClass, options);
            }
        }
        return "null";
    }

    public static String formatMethod(
            @NotNull SliceMethodDef method,
            @FormatMethodOptions int options,
            @FormatVariableOptions int parameterOptions
    ) {
        return formatMethod(method, options, parameterOptions, MAX_PARAMS_TO_SHOW);
    }

    public static String formatMethod(
            @NotNull SliceMethodDef method,
            @FormatMethodOptions int options,
            @FormatVariableOptions int parameterOptions,
            int maxParametersToShow
    ) {
        StringBuilder buffer = new StringBuilder();
        formatMethod(method, options, parameterOptions, maxParametersToShow, buffer);
        return buffer.toString();
    }

    private static void formatMethod(
            @NotNull SliceMethodDef method,
            @FormatMethodOptions int options,
            @FormatVariableOptions int parameterOptions,
            int maxParametersToShow,
            StringBuilder buffer
    ) {
        if (BitUtil.isSet(options, SHOW_TYPE) && !BitUtil.isSet(options, TYPE_AFTER)) {
            @Nullable SliceDataType type = method.getMethodReturnType().getDataType();
            if (type != null) {
                appendSpaceIfNeeded(buffer);
                buffer.append(formatType(type, options));
            }
        }
        if (BitUtil.isSet(options, SHOW_CONTAINING_CLASS)) {
            SliceDataTypeElement aClass = method.getContainingClass();
            if (aClass != null) {
                appendSpaceIfNeeded(buffer);
                String name = aClass.getName();
                if (name != null) {
                    if (BitUtil.isSet(options, SHOW_FQ_NAME)) {
                        buffer.append(ObjectUtils.notNull(aClass.getQualifiedName(), name));
                    } else {
                        buffer.append(name);
                    }
                    buffer.append('.');
                }
            }
            if (BitUtil.isSet(options, SHOW_NAME)) {
                buffer.append(method.getName());
            }
        } else {
            if (BitUtil.isSet(options, SHOW_NAME)) {
                appendSpaceIfNeeded(buffer);
                buffer.append(method.getName());
            }
        }
        if (BitUtil.isSet(options, SHOW_PARAMETERS)) {
            buffer.append('(');
            @NotNull List<SliceParameterDef> params = method.getParametersList().getParameterDefList();
            int i = 0;
            for (SliceParameterDef param : params) {
                if (i > 0) {
                    buffer.append(", ");
                }
                if (i >= maxParametersToShow) {
                    buffer.append("...");
                    break;
                }
                buffer.append(formatVariable(param, parameterOptions));
                i++;
            }
            buffer.append(')');
        }
        if (BitUtil.isSet(options, SHOW_TYPE) && BitUtil.isSet(options, TYPE_AFTER)) {
            @Nullable SliceDataType type = method.getMethodReturnType().getDataType();
            if (type != null) {
                if (buffer.length() > 0) buffer.append(':');
                buffer.append(formatType(type, options));
            }
        }
        if (BitUtil.isSet(options, SHOW_THROWS)) {
            String throwsText = formatReferenceList(method.getThrowsBlock(), options);
            if (!throwsText.isEmpty()) {
                appendSpaceIfNeeded(buffer);
                buffer.append("throws ").append(throwsText);
            }
        }
    }

    @NotNull
    public static String formatClass(@NotNull SliceDataTypeElement aClass, @FormatClassOptions int options) {
        StringBuilder buffer = new StringBuilder();

        if (BitUtil.isSet(options, SHOW_NAME)) {
            String name = aClass.getName();
            if (name != null) {
                appendSpaceIfNeeded(buffer);
                if (BitUtil.isSet(options, SHOW_FQ_NAME)) {
                    buffer.append(aClass.getQualifiedName().getFQN());
                } else {
                    buffer.append(aClass.getName());
                }
            }
        }

        if (BitUtil.isSet(options, SHOW_EXTENDS_IMPLEMENTS)) {
            @Nullable SliceExtendsBlock extendsList = aClass.getExtendsBlock();
            if (extendsList != null) {
                String extendsText = formatReferenceList(extendsList, options);
                if (!extendsText.isEmpty()) {
                    appendSpaceIfNeeded(buffer);
                    buffer.append("extends ").append(extendsText);
                }
            }

            @Nullable SliceImplementsBlock implementsList = aClass.getImplementsBlock();
            if (implementsList != null) {
                String implementsText = formatReferenceList(implementsList, options);
                if (!implementsText.isEmpty()) {
                    appendSpaceIfNeeded(buffer);
                    buffer.append("implements ").append(implementsText);
                }
            }
        }

        return buffer.toString();
    }

    private static String formatReferenceList(@Nullable SliceReferenceListElement list, int options) {
        if (list == null) {
            return "";
        }
        StringJoiner buffer = new StringJoiner(", ");
        @NotNull List<SliceTypeReference> refs = list.getTypeReferenceList();
        for (SliceTypeReference ref : refs) {
            buffer.add(formatReference(ref, options));
        }
        return buffer.toString();
    }

    public static String formatType(@Nullable SliceDataType type, int options) {
        if (type == null) {
            return "null";
        }
        final SliceProxyType proxyType = type.getProxyType();
        final SliceTypeReference typeReference = type.getTypeReference();
        if (proxyType != null && proxyType.getTypeReference() != null) {
            return FQN.buildFQN(proxyType.getTypeReference()).getFQN();
        } else if (typeReference != null) {
            return FQN.buildFQN(typeReference).getFQN();
        }
        return type.getText();
    }

    public static String formatReference(SliceTypeReference ref, int options) {
        return BitUtil.isSet(options, SHOW_FQ_CLASS_NAMES) ? ref.getReference().getCanonicalText() : ref.getText();
    }
}