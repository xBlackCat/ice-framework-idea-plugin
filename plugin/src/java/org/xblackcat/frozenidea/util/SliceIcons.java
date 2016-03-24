package org.xblackcat.frozenidea.util;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * 08.01.12 14:15
 *
 * @author xBlackCat
 */
public interface SliceIcons {
    Icon FACET_ICON = IconLoader.getIcon("/icons/facet.png");
    Icon FILE_ICON = IconLoader.getIcon("/icons/file.png");

    Icon CLASS_ICON = IconLoader.getIcon("/icons/class.png");
    Icon EXCEPTION_ICON = IconLoader.getIcon("/icons/exceptionClass.png");
    Icon ENUM_ICON = IconLoader.getIcon("/icons/enum.png");
    Icon FIELD_ICON = IconLoader.getIcon("/icons/field.png");
    Icon INTERFACE_ICON = IconLoader.getIcon("/icons/interface.png");
    Icon METHOD_ICON = IconLoader.getIcon("/icons/method.png");
    Icon PROPERTY_ICON = IconLoader.getIcon("/icons/property.png");


    Icon TRANSLATED = IconLoader.getIcon("/actions/compile.png");
    Icon IMPLEMENTED = IconLoader.getIcon("/gutter/implementedMethod.png");
    Icon IMPLEMENTING = IconLoader.getIcon("/gutter/implementingMethod.png");
    Icon OVERRIDDEN = IconLoader.getIcon("/gutter/overridenMethod.png");
    Icon OVERRIDING = IconLoader.getIcon("/gutter/overridingMethod.png");
}
