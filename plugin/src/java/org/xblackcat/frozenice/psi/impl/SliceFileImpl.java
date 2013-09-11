package org.xblackcat.frozenice.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.IceFileType;
import org.xblackcat.frozenice.SliceLanguage;
import org.xblackcat.frozenice.psi.SliceFile;

/**
 * 04.01.12 16:36
 *
 * @author xBlackCat
 */
public class SliceFileImpl extends PsiFileBase implements SliceFile {
    public SliceFileImpl(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, SliceLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return IceFileType.INSTANCE;
    }
}
