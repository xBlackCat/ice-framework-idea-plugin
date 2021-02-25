package org.xblackcat.frozenidea.findUsages.slice;

import com.intellij.find.findUsages.FindUsagesOptions;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.search.SearchScope;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.util.SliceBundle;

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
            to.add(SliceBundle.message("find.usages.panel.title.usages"));
        }
    }

    @Override
    public final String generateUsagesString() {
        String separator = " " + SliceBundle.message("find.usages.panel.title.separator") + " ";
        LinkedHashSet<String> strings = new LinkedHashSet<>();
        addUsageTypes(strings);
        if (strings.isEmpty()) {
            strings.add(SliceBundle.message("find.usages.panel.title.usages"));
        }
        return StringUtil.join(strings, separator);
    }


}
