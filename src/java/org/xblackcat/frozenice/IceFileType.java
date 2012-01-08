package org.xblackcat.frozenice;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.util.Icons;

import javax.swing.*;

/**
 * @author xBlackCat
 */
public class IceFileType extends LanguageFileType {
    public static final IceFileType INSTANCE = new IceFileType();

    private static final String DEFAULT_EXTENSION = "ice";

    protected IceFileType() {
        super(SliceLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "SLICE";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "ICE definition file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    @Override
    public Icon getIcon() {
        return Icons.FILE_ICON;
    }

    @Override
    public String getCharset(@NotNull VirtualFile file, byte[] content) {
        return super.getCharset(file, content);
    }
}
