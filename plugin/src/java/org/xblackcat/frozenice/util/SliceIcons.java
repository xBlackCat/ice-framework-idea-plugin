package org.xblackcat.frozenice.util;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * 08.01.12 14:15
 *
 * @author xBlackCat
 */
public interface SliceIcons {
    Icon FACET_ICON_32 = IconLoader.getIcon("/icons/facet-32.png");
    Icon FACET_ICON = IconLoader.getIcon("/icons/facet.png");
    Icon FILE_ICON = IconLoader.getIcon("/icons/file.png");

    Icon IMPLEMENTED = IconLoader.getIcon("/gutter/implementedMethod.png");
    Icon OVERRIDDEN = IconLoader.getIcon("/gutter/overridenMethod.png");
}
