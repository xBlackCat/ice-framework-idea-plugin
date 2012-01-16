package org.xblackcat.frozenice.util;

import com.intellij.openapi.util.SystemInfo;

/**
 * 16.01.12 16:09
 *
 * @author xBlackCat
 */
public interface Constants {
    String JAVA_TRANSLATOR_NAME = SystemInfo.isWindows ? "slice2java.exe" : "slice2java";
}
