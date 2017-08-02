package org.xblackcat.frozenidea.code;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.psi.SliceNamedElement;
import org.xblackcat.frozenidea.util.SliceUtil;

import java.util.List;

/**
 * 24.03.2016 15:06
 *
 * @author xBlackCat
 */
public class SliceGotoSymbolContributor implements ChooseByNameContributor {
    @NotNull
    @Override
    public String[] getNames(Project project, boolean includeNonProjectItems) {
        final List<SliceNamedElement> dataTypes = SliceUtil.findNamedItems(project);
        return dataTypes.stream().map(SliceNamedElement::getName).filter(n -> n != null && n.length() > 0).toArray(String[]::new);
    }

    @NotNull
    @Override
    public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems) {
        final List<SliceNamedElement> items = SliceUtil.findNamedItems(project, name);
        return items.stream().toArray(NavigationItem[]::new);
    }
}
