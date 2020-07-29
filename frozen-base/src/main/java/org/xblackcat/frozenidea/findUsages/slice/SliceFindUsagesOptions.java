package org.xblackcat.frozenidea.findUsages.slice;

import com.intellij.find.FindBundle;
import com.intellij.find.findUsages.FindUsagesOptions;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.search.SearchScope;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;

/**
 * @author peter
 */
public class SliceFindUsagesOptions extends FindUsagesOptions {
    public SliceFindUsagesOptions(@NotNull Project project) {
        super(project, null);

        isUsages = true;
    }

    public SliceFindUsagesOptions(@NotNull SearchScope searchScope) {
        super(searchScope);
    }

    protected void addUsageTypes(LinkedHashSet<String> to) {
        if (this.isUsages) {
            to.add(FindBundle.message("find.usages.panel.title.usages"));
        }
    }
    
    @Override
    public final String generateUsagesString() {
        String suffix = " " + FindBundle.message("find.usages.panel.title.separator") + " ";
        LinkedHashSet<String> strings = new LinkedHashSet<>();
        addUsageTypes(strings);
        if (strings.isEmpty()) {
            strings.add(FindBundle.message("find.usages.panel.title.usages"));
        }
        return StringUtil.join(strings, suffix);
    }


}
