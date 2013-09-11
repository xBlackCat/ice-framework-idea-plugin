package org.xblackcat.frozenice.processor;

import com.intellij.openapi.compiler.Compiler;
import com.intellij.openapi.compiler.CompilerFactory;
import com.intellij.openapi.compiler.CompilerManager;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenice.IceFileType;

/**
 * 05.09.13 13:40
 *
 * @author xBlackCat
 */
public class SliceCompilerFactory implements CompilerFactory {
    @Override
    public Compiler[] createCompilers(@NotNull CompilerManager compilerManager) {
//        Slice2Xxx translator = new Slice2Xxx();
//        final HashSet<FileType> outputTypes = new HashSet<FileType>(Arrays.asList(StdFileTypes.JAVA));
//
//        compilerManager.addTranslatingCompiler(
//                translator,
//                new HashSet<FileType>(Arrays.asList(IceFileType.INSTANCE)),
//                outputTypes
//        );
        compilerManager.addCompilableFileType(IceFileType.INSTANCE);
        compilerManager.addCompiler(new Slice2Xxx());
        return new Compiler[0];
    }
}
