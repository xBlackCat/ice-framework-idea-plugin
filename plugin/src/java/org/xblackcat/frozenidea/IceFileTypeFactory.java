package org.xblackcat.frozenidea;

import com.intellij.codeInsight.editorActions.TypedHandler;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

/**
 * @author xBlackCat
 */
public class IceFileTypeFactory extends FileTypeFactory {
    @Override
    public void createFileTypes(@NotNull FileTypeConsumer consumer) {
        consumer.consume(IceFileType.INSTANCE, "ice" + FileTypeConsumer.EXTENSION_DELIMITER + "slice");
        TypedHandler.registerBaseLanguageQuoteHandler(
                SliceLanguage.class,
                TypedHandler.getQuoteHandlerForType(IceFileType.INSTANCE)
        );
    }
}
