package org.xblackcat.frozenidea.util;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public interface SliceIcons {
    Icon FACET_ICON = IconLoader.getIcon("/icons/facet.svg", SliceIcons.class);
    Icon FILE_ICON = IconLoader.getIcon("/icons/file.svg", SliceIcons.class);

    Icon CLASS_ICON = IconLoader.getIcon("/icons/class.svg", SliceIcons.class);
    Icon EXCEPTION_ICON = IconLoader.getIcon("/icons/exceptionClass.svg", SliceIcons.class);
    Icon ENUM_ICON = IconLoader.getIcon("/icons/enum.svg", SliceIcons.class);
    Icon FIELD_ICON = IconLoader.getIcon("/icons/field.svg", SliceIcons.class);
    Icon INTERFACE_ICON = IconLoader.getIcon("/icons/interface.svg", SliceIcons.class);
    Icon METHOD_ICON = IconLoader.getIcon("/icons/method.svg", SliceIcons.class);
    Icon PROPERTY_ICON = IconLoader.getIcon("/icons/property.svg", SliceIcons.class);


    Icon TRANSLATED_CLASS = IconLoader.getIcon("/icons/toTranslatedClass.svg", SliceIcons.class);
    Icon TRANSLATED_METHOD = IconLoader.getIcon("/icons/toTranslatedMethod.svg", SliceIcons.class);
    Icon DECLARED_ICE_METHOD = IconLoader.getIcon("/icons/toDeclaredMethod.svg", SliceIcons.class);
    Icon IMPLEMENTED_SLICE_METHOD = IconLoader.getIcon("/icons/implementedSliceMethod.svg", SliceIcons.class);
    Icon IMPLEMENTED_SLICE_CLASS = IconLoader.getIcon("/icons/implementedSliceClass.svg", SliceIcons.class);
    Icon IMPLEMENTED = IconLoader.getIcon("/gutter/implementedMethod.svg", SliceIcons.class);
    Icon IMPLEMENTING = IconLoader.getIcon("/gutter/implementingMethod.svg", SliceIcons.class);
    Icon OVERRIDDEN = IconLoader.getIcon("/gutter/overridenMethod.svg", SliceIcons.class);
    Icon OVERRIDING = IconLoader.getIcon("/gutter/overridingMethod.svg", SliceIcons.class);
}
