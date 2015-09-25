package org.xblackcat.frozenidea.jps;

import com.intellij.execution.process.ProcessEvent;
import org.jetbrains.jps.incremental.CompileContext;
import org.jetbrains.jps.incremental.messages.FileGeneratedEvent;
import org.jetbrains.jps.model.module.JpsModule;
import org.xblackcat.frozenidea.config.IceComponent;

import java.io.File;
import java.util.List;

/**
 * 25.09.2015 13:20
 *
 * @author xBlackCat
 */
class SliceProcessAdapter extends ATranslatorProcessAdapter {
    private final IceComponent iceComponent;
    private final List<File> sourceFiles;

    public SliceProcessAdapter(
            CompileContext context,
            IceComponent iceComponent,
            JpsModule module,
            String outputDirPath,
            List<File> sourceFiles
    ) {
        super(module, outputDirPath, context);
        this.iceComponent = iceComponent;
        this.sourceFiles = sourceFiles;
    }

    @Override
    public void processTerminated(ProcessEvent event) {
        int exitCode = event.getExitCode();
        if (exitCode != 0) {
            String message = errorOutput.toString();

            for (String line : message.split("\n")) {
                context.processMessage(parseErrorLine(line));
                hasErrors.set(true);
            }
        } else {
            final FileGeneratedEvent msg = new FileGeneratedEvent();

            for (File source : sourceFiles) {
                final String name = source.getName();
                final String cleanName;
                final int indexOf = name.lastIndexOf('.');
                if (indexOf > 0) {
                    cleanName = name.substring(0, indexOf);
                } else {
                    cleanName = name;
                }
                msg.add(outputDirPath, cleanName + "." + iceComponent.getFileExtension());
            }

            context.processMessage(msg);

        }
    }
}