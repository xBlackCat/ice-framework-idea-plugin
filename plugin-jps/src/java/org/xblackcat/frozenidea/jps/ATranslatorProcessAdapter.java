package org.xblackcat.frozenidea.jps;

import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.incremental.CompileContext;
import org.jetbrains.jps.incremental.messages.BuildMessage;
import org.jetbrains.jps.incremental.messages.CompilerMessage;
import org.jetbrains.jps.model.module.JpsModule;
import org.xblackcat.frozenidea.config.IceComponent;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 25.09.2015 13:21
 *
 * @author xBlackCat
 */
public class ATranslatorProcessAdapter extends ProcessAdapter {
    protected final CompileContext context;
    protected final AtomicBoolean hasErrors = new AtomicBoolean();
    protected final JpsModule module;
    protected final String outputDirPath;
    protected final StringBuilder errorOutput = new StringBuilder();
    protected final StringBuilder stdOutput = new StringBuilder();

    public ATranslatorProcessAdapter(
            JpsModule module,
            String outputDirPath,
            CompileContext context
    ) {
        this.module = module;
        this.outputDirPath = outputDirPath;
        this.context = context;
    }

    @NotNull
    protected static BuildMessage parseErrorLine(String line) {
        int separatorIndex = line.indexOf(": ");
        final String path;
        final long lineNumber;
        if (separatorIndex <= 0) {
            path = null;
            lineNumber = -1L;
        } else {
            int lineSep = line.lastIndexOf(':', separatorIndex - 1);
            if (lineSep == -1) {
                path = null;
                lineNumber = -1L;
            } else {
                path = line.substring(0, lineSep);
                long l;
                try {
                    l = Long.parseLong(line.substring(lineSep + 1, separatorIndex));
                } catch (NumberFormatException e) {
                    l = -1L;
                }
                lineNumber = l;
            }
        }

        return new CompilerMessage(
                SliceBuilder.BUILDER_NAME,
                BuildMessage.Kind.ERROR,
                line,
                path,
                -1L,
                -1L,
                -1L,
                lineNumber,
                -1L
        );
    }

    @Override
    public void onTextAvailable(ProcessEvent event, Key outputType) {
        if (outputType == ProcessOutputTypes.STDERR) {
            errorOutput.append(event.getText());
        } else if (outputType == ProcessOutputTypes.STDOUT) {
            stdOutput.append(event.getText());
        }
    }

    public boolean hasErrors() {
        return hasErrors.get();
    }
}
