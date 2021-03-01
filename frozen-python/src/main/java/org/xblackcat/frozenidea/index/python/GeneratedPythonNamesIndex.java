package org.xblackcat.frozenidea.index.python;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.*;
import com.intellij.util.io.DataExternalizer;
import com.intellij.util.io.EnumeratorStringDescriptor;
import com.intellij.util.io.KeyDescriptor;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.IceFileType;
import org.xblackcat.frozenidea.integration.SliceHelper;
import org.xblackcat.frozenidea.integration.python.PythonPsiUtil;
import org.xblackcat.frozenidea.psi.SliceDataTypeElement;
import org.xblackcat.frozenidea.psi.SliceFile;
import org.xblackcat.frozenidea.psi.SliceModule;
import org.xblackcat.frozenidea.util.FQN;

import java.util.*;

/**
 *
 */
public class GeneratedPythonNamesIndex extends FileBasedIndexExtension<String, String> {
    public static final ID<String, String> GENERATED_PYTHON_CLASSES_INDEX = ID.create("SliceGeneratedPythonNamesIndex");
    private static final DataIndexer<String, String, FileContent> INDEXER = new MyIndexer();
    private final DefaultFileTypeSpecificInputFilter myInputFilter = new DefaultFileTypeSpecificInputFilter(IceFileType.INSTANCE);
    private final KeyDescriptor<String> myKeyDescriptor = new EnumeratorStringDescriptor();

    public static SliceDataTypeElement findDeclaration(String pythonClassName, Project project, GlobalSearchScope scope) {
        List<SliceDataTypeElement> result = new ArrayList<>();
        final PsiManager manager = PsiManager.getInstance(project);

        final FileBasedIndex fileBasedIndex = FileBasedIndex.getInstance();
        final List<String> values = fileBasedIndex.getValues(GENERATED_PYTHON_CLASSES_INDEX, pythonClassName, scope);
        if (values.isEmpty()) {
            return null;
        }
        final FQN fqn = FQN.buildFQN(values.get(0));
        fileBasedIndex.getFilesWithKey(
                GENERATED_PYTHON_CLASSES_INDEX,
                Collections.singleton(pythonClassName),
                file -> {
                    PsiFile psiFile = manager.findFile(file);
                    if (psiFile != null) {
                        final Collection<SliceDataTypeElement> elements = PsiTreeUtil.findChildrenOfType(
                                psiFile,
                                SliceDataTypeElement.class
                        );
                        for (SliceDataTypeElement element : elements) {
                            if (element.isSequence() || element.isDictionary()) {
                                continue;
                            }

                            if (fqn.getName().equals(element.getName())) {
                                if (fqn.equals(SliceHelper.getFQN(element))) {
                                    result.add(element);
                                }
                            }
                        }
                    }
                    return true;
                },
                scope
        );
        final Optional<SliceDataTypeElement> def = result.stream().filter(declaration -> declaration.getBodyBlock() != null).findFirst();
        return def.orElse(null);
    }

    @Override
    public @NotNull ID<String, String> getName() {
        return GENERATED_PYTHON_CLASSES_INDEX;
    }

    @Override
    public @NotNull FileBasedIndex.InputFilter getInputFilter() {
        return myInputFilter;
    }

    @Override
    public boolean dependsOnFileContent() {
        return true;
    }

    @Override
    public @NotNull DataIndexer<String, String, FileContent> getIndexer() {
        return INDEXER;
    }

    @Override
    public @NotNull KeyDescriptor<String> getKeyDescriptor() {
        return myKeyDescriptor;
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public @NotNull DataExternalizer<String> getValueExternalizer() {
        return new EnumeratorStringDescriptor();
    }

    private static class MyIndexer implements DataIndexer<String, String, FileContent> {
        @Override
        public @NotNull Map<String, String> map(@NotNull FileContent inputData) {
            final THashMap<String, String> result = new THashMap<>();
            if (inputData.getPsiFile() instanceof SliceFile) {
                final Collection<SliceModule> modules = PsiTreeUtil.findChildrenOfType(inputData.getPsiFile(), SliceModule.class);

                final String fileName = inputData.getPsiFile().getName().replaceAll("\\.", "_");

                for (SliceModule module : modules) {
                    final String pythonPackageName = PythonPsiUtil.getPythonPackageName(module);
                    final FQN fqn = SliceHelper.getFQN(module);
                    if (fqn == null) {
                        continue;
                    }
                    final String moduleFQN = fqn.getFQN();

                    for (SliceDataTypeElement e : module.getTypeDeclarations()) {
                        if (!e.isDictionary() && !e.isSequence()) {
                            final String name = e.getName();
                            result.put(pythonPackageName + "." + name, moduleFQN + "::" + name);
                            result.put(fileName + "." + name, moduleFQN + "::" + name);
                        }
                    }
                }
            }
            return result;
        }
    }
}
