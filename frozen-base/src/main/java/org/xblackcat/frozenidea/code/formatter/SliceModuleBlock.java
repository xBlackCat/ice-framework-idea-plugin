package org.xblackcat.frozenidea.code.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.xblackcat.frozenidea.psi.SliceModule;
import org.xblackcat.frozenidea.psi.SliceModuleBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.xblackcat.frozenidea.psi.SliceTypes.ICE_MODULE;

/**
 *
 */
public class SliceModuleBlock extends AbstractSliceBlock {

    public SliceModuleBlock(@NotNull ASTNode node, Wrap wrap, Alignment alignment, SpacingBuilder spacingBuilder) {
        super(node, wrap, alignment, spacingBuilder);
    }

    @Override
    public Indent getIndent() {
        return Indent.getNoneIndent();
    }

    @Override
    protected List<Block> buildChildren() {
        SliceModule module = ((SliceModule) myNode.getPsi());
        if (module == null) {
            return Collections.emptyList();
        }

        final SliceModuleBody body = module.getModuleBody();
        if (body == null) {
            return Collections.emptyList();
        }

        List<Block> blocks = new ArrayList<>();
        for (SliceModule subModule : body.getModuleList()) {
            blocks.add(new SliceModuleBlock(subModule.getNode(), myWrap, myAlignment, spacingBuilder));
        }

        return blocks;
    }
}
