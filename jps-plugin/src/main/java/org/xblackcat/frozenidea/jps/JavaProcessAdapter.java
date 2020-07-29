package org.xblackcat.frozenidea.jps;

import com.intellij.execution.process.ProcessEvent;
import com.intellij.openapi.util.JDOMUtil;
import org.jdom.Element;
import org.jetbrains.jps.builders.java.JavaModuleBuildTargetType;
import org.jetbrains.jps.incremental.CompileContext;
import org.jetbrains.jps.incremental.ModuleBuildTarget;
import org.jetbrains.jps.incremental.messages.BuildMessage;
import org.jetbrains.jps.incremental.messages.CompilerMessage;
import org.jetbrains.jps.incremental.messages.FileGeneratedEvent;
import org.jetbrains.jps.model.module.JpsModule;
import org.xblackcat.frozenidea.config.IceComponent;

/**
 * 25.09.2015 13:20
 *
 * @author xBlackCat
 */
class JavaProcessAdapter extends ATranslatorProcessAdapter {
    private final IceComponent target;

    public JavaProcessAdapter(
            CompileContext context,
            IceComponent translatorName,
            JpsModule module,
            String outputDirPath
    ) {
        super(module, outputDirPath, context);
        target = translatorName;
    }

    @Override
    public void processTerminated(ProcessEvent event) {
        Element res;
        final String stdout = stdOutput.toString();
        try {
            res = JDOMUtil.load(stdout);
        } catch (Exception e) {
            context.processMessage(
                    new CompilerMessage(
                            SliceBuilder.BUILDER_NAME,
                            BuildMessage.Kind.ERROR,
                            "Can't process compiler output: " + stdout
                    )
            );
            hasErrors.set(true);
            return;
        }


        int exitCode = event.getExitCode();
        if (exitCode != 0) {
            for (Element source : res.getChildren("source")) {
                final Element output = source.getChild("output");
                if (output != null) {
                    String message = output.getTextTrim();

                    for (String line : message.split("\n")) {
                        context.processMessage(parseErrorLine(line));
                    }
                }
            }

            final String stdErr = errorOutput.toString();
            if (stdErr.length() > 0) {
                context.processMessage(new CompilerMessage(SliceBuilder.BUILDER_NAME, BuildMessage.Kind.ERROR, stdErr));
            }
            context.processMessage(
                    new CompilerMessage(
                            SliceBuilder.BUILDER_NAME,
                            BuildMessage.Kind.ERROR,
                            "translator '" + target.getTranslatorName() + "' for '" + module.getName() +
                                    "' finished with exit code " + exitCode
                    )
            );
            hasErrors.set(true);
        } else {
            final FileGeneratedEvent msg = new FileGeneratedEvent(new ModuleBuildTarget(module, JavaModuleBuildTargetType.PRODUCTION));

            for (Element source : res.getChildren("source")) {
                for (Element file : source.getChildren("file")) {
                    final String fileName = file.getAttributeValue("name");

                    if (fileName.startsWith(outputDirPath)) {
                        msg.add(outputDirPath, fileName.substring(outputDirPath.length() + 1));
                    }
                }
            }

            context.processMessage(msg);

        }
    }
}
