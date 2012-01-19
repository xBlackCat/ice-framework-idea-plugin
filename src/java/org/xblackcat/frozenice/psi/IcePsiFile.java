package org.xblackcat.frozenice.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.IceFileType;
import org.xblackcat.frozenice.SliceLanguage;

/**
 * 04.01.12 16:36
 *
 * @author xBlackCat
 */
public class IcePsiFile extends PsiFileBase {
    public IcePsiFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, SliceLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return IceFileType.INSTANCE;
    }
}
