package org.xblackcat.frozenidea.findUsages.slice;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * 06.07.12 15:01
 *
 * @author xBlackCat
 */
public class SliceClassFindUsagesOptions extends SliceFindUsagesOptions {
    public boolean isMethodsUsages;
    public boolean isFieldsUsages;
    public boolean isDerivedClasses;
    public boolean isImplementingClasses;
    public boolean isDerivedInterfaces;
    public boolean isCheckDeepInheritance = true;
    public boolean isIncludeInherited;


    public SliceClassFindUsagesOptions(@NotNull Project project) {
        super(project);
    }


}
