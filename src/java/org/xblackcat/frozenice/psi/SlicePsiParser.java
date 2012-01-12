package org.xblackcat.frozenice.psi;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

/**
 * 05.01.12 16:33
 *
 * @author xBlackCat
 */
public class SlicePsiParser implements PsiParser {
    @NotNull
    @Override
    public ASTNode parse(IElementType root, PsiBuilder builder) {
        final PsiBuilder.Marker rootMarker = builder.mark();

        final SliceParser parser = new SliceParser(builder);
        while (!builder.eof()) {
            parser.parseBlock();
        }

        rootMarker.done(root);
        return builder.getTreeBuilt();
    }
}
