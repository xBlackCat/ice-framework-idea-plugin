package org.xblackcat.frozenidea.find.usages;

import com.intellij.find.FindBundle;
import com.intellij.find.findUsages.FindUsagesOptions;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;

/**
 * @author peter
 */
public abstract class SliceFindUsagesOptions extends FindUsagesOptions {
    public boolean skipImplementationClasses = false;

    public SliceFindUsagesOptions(@NotNull Project project) {
        super(project, null);

        isUsages = true;
    }

    protected void addUsageTypes(LinkedHashSet<String> to) {
        if (this.isUsages) {
            to.add(FindBundle.message("find.usages.panel.title.usages"));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SliceFindUsagesOptions that = (SliceFindUsagesOptions) o;

        return skipImplementationClasses == that.skipImplementationClasses;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (skipImplementationClasses ? 1 : 0);
        return result;
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
