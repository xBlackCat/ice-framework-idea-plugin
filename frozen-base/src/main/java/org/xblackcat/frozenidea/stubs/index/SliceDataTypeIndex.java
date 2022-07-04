package org.xblackcat.frozenidea.stubs.index;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.*;
import com.intellij.util.io.EnumeratorStringDescriptor;
import com.intellij.util.io.KeyDescriptor;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.IceFileType;
import org.xblackcat.frozenidea.psi.SliceDataTypeElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 24.03.2016 15:43
 *
 * @author xBlackCat
 */
public class SliceDataTypeIndex extends ScalarIndexExtension<String> {
    public static final ID<String, Void> NAME = ID.create("FrozenIdea.DataTypesIndex");
    private final EnumeratorStringDescriptor keyDescriptor = new EnumeratorStringDescriptor();

    @NotNull
    @Override
    public ID<String, Void> getName() {
        return NAME;
    }

    @NotNull
    @Override
    public FileBasedIndex.InputFilter getInputFilter() {
        return file -> file.getFileType() == IceFileType.INSTANCE;
    }

    @Override
    public boolean dependsOnFileContent() {
        return true;
    }

    @NotNull
    @Override
    public DataIndexer<String, Void, FileContent> getIndexer() {
        return SliceDataTypeIndex::indexer;
    }

    @NotNull
    @Override
    public KeyDescriptor<String> getKeyDescriptor() {
        return keyDescriptor;
    }

    @Override
    public int getVersion() {
        return 1;
    }

    public static List<String> findAllKeys(Project project, GlobalSearchScope scope) {
        final List<String> result = new ArrayList<>();
        FileBasedIndex.getInstance().processAllKeys(
                NAME,
                name -> {
                    result.add(name);
                    return true;
                },
                scope,
                null
        );
        return result;
    }

    public static List<SliceDataTypeElement> findDeclaration(
            @NotNull final String name,
            @NotNull Project project,
            @NotNull GlobalSearchScope scope
    ) {
        final List<SliceDataTypeElement> result = new ArrayList<>();
        final PsiManager manager = PsiManager.getInstance(project);
        FileBasedIndex.getInstance().getFilesWithKey(
                NAME,
                Collections.singleton(name),
                file -> {
                    PsiFile psiFile = manager.findFile(file);
                    if (psiFile != null) {
                        for (PsiElement child : psiFile.getChildren()) {
                            if (child instanceof SliceDataTypeElement && name.equals(((SliceDataTypeElement) child).getName())) {
                                result.add((SliceDataTypeElement) child);
                            }
                        }
                    }
                    return true;
                },
                scope
        );
        return result;
    }

    @NotNull
    private static Map<String, Void> indexer(@NotNull FileContent inputData) {
        Map<String, Void> result = new THashMap<>();
        for (PsiElement child : inputData.getPsiFile().getChildren()) {
            if (child instanceof SliceDataTypeElement) {
                String name = ((SliceDataTypeElement) child).getName();
                if (name != null) {
                    result.put(name, null);
                }
            }
        }

        return result;
    }
}
