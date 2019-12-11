package org.xblackcat.frozenidea;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.util.SliceIcons;

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
        return "ZeroC ICE definition file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    @Override
    public Icon getIcon() {
        return SliceIcons.FILE_ICON;
    }

    @Override
    public String getCharset(@NotNull VirtualFile file, @NotNull byte[] content) {
        return CharsetToolkit.UTF8;
    }
}
