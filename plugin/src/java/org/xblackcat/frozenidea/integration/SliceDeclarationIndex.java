package org.xblackcat.frozenidea.integration;

import com.intellij.util.indexing.*;
import com.intellij.util.io.EnumeratorStringDescriptor;
import com.intellij.util.io.KeyDescriptor;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.IceFileType;

/**
 * 12.08.2015 14:18
 *
 * @author xBlackCat
 */
public class SliceDeclarationIndex extends ScalarIndexExtension<String> {
    private static final ID<String, Void> SLICE_DECLARATION_INDEX = ID.create("SliceDeclarationIndex");
    private final EnumeratorStringDescriptor myKeyDescriptor = new EnumeratorStringDescriptor();
    private final FileBasedIndex.InputFilter myFilter = file -> file.getFileType() == IceFileType.INSTANCE;

    @NotNull
    @Override
    public ID<String, Void> getName() {
        return SLICE_DECLARATION_INDEX;
    }

    @NotNull
    @Override
    public DataIndexer<String, Void, FileContent> getIndexer() {
        return null;
    }

    @NotNull
    @Override
    public KeyDescriptor<String> getKeyDescriptor() {
        return myKeyDescriptor;
    }

    @NotNull
    @Override
    public FileBasedIndex.InputFilter getInputFilter() {
        return myFilter;
    }

    @Override
    public boolean dependsOnFileContent() {
        return false;
    }

    @Override
    public int getVersion() {
        return 0;
    }
}
