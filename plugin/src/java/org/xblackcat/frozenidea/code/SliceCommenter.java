package org.xblackcat.frozenidea.code;

import com.intellij.lang.Commenter;

/**
 * 06.02.12 17:25
 *
 * @author xBlackCat
 */
public class SliceCommenter implements Commenter {
    @Override
    public String getLineCommentPrefix() {
        return "//";
    }

    @Override
    public String getBlockCommentPrefix() {
        return "/*";
    }

    @Override
    public String getBlockCommentSuffix() {
        return "*/";
    }

    @Override
    public String getCommentedBlockCommentPrefix() {
        return null;
    }

    @Override
    public String getCommentedBlockCommentSuffix() {
        return null;
    }
}
