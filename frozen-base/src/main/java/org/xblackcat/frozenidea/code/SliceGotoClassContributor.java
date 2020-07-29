package org.xblackcat.frozenidea.code;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * 24.03.2016 15:06
 *
 * @author xBlackCat
 */
public class SliceGotoClassContributor implements ChooseByNameContributor {
    @NotNull
    @Override
    public String[] getNames(Project project, boolean includeNonProjectItems) {
        return new String[0];
    }

    @NotNull
    @Override
    public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems) {
        return new NavigationItem[0];
    }
}
