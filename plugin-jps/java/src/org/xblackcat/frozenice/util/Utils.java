package org.xblackcat.frozenice.util;

import java.io.File;
import java.net.URI;

/**
 * 11.09.13 13:53
 *
 * @author xBlackCat
 */
public class Utils {
    public static File ideaUrlToFile(String url) {
        if (url != null) {
            final URI uri = URI.create(url);
            return new File(
                    uri.getAuthority() != null ?
                            uri.getAuthority() + uri.getPath() :
                            uri.getPath()
            );
        }
        return null;
    }
}
