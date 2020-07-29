package org.xblackcat.frozenidea.util;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * 08.01.12 14:15
 *
 * @author xBlackCat
 */
public interface SliceIcons {
    Icon FACET_ICON = IconLoader.getIcon("/icons/facet.svg");
    Icon FILE_ICON = IconLoader.getIcon("/icons/file.svg");

    Icon CLASS_ICON = IconLoader.getIcon("/icons/class.svg");
    Icon EXCEPTION_ICON = IconLoader.getIcon("/icons/exceptionClass.svg");
    Icon ENUM_ICON = IconLoader.getIcon("/icons/enum.svg");
    Icon FIELD_ICON = IconLoader.getIcon("/icons/field.svg");
    Icon INTERFACE_ICON = IconLoader.getIcon("/icons/interface.svg");
    Icon METHOD_ICON = IconLoader.getIcon("/icons/method.svg");
    Icon PROPERTY_ICON = IconLoader.getIcon("/icons/property.svg");


    Icon TRANSLATED_JAVA_CLASS = IconLoader.getIcon("/icons/translateJavaClass.svg");
    Icon TRANSLATED_JAVA_METHOD = IconLoader.getIcon("/icons/translateJavaMethod.svg");
    Icon IMPLEMENTED_SLICE_METHOD = IconLoader.getIcon("/icons/implementedSliceMethod.svg");
    Icon IMPLEMENTED_SLICE_CLASS = IconLoader.getIcon("/icons/implementedSliceClass.svg");
    Icon IMPLEMENTED = IconLoader.getIcon("/gutter/implementedMethod.svg");
    Icon IMPLEMENTING = IconLoader.getIcon("/gutter/implementingMethod.svg");
    Icon OVERRIDDEN = IconLoader.getIcon("/gutter/overridenMethod.svg");
    Icon OVERRIDING = IconLoader.getIcon("/gutter/overridingMethod.svg");
}
