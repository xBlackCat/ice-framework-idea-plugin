package org.xblackcat.frozenidea.code.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.xblackcat.frozenidea.psi.SliceTypes.ICE_GLOBAL_METADATA;
import static org.xblackcat.frozenidea.psi.SliceTypes.ICE_MODULE;

/**
 *
 */
public class SliceFileBlock extends AbstractSliceBlock {

    public SliceFileBlock(@NotNull ASTNode node, SpacingBuilder spacingBuilder) {
        super(node, null, null, spacingBuilder);
    }

    @Override
    public Indent getIndent() {
        return Indent.getNoneIndent();
    }

    @Override
    protected List<Block> buildChildren() {
        List<Block> blocks = new ArrayList<>();
        ASTNode child = myNode.getFirstChildNode();
        while (child != null) {
            final IElementType elementType = child.getElementType();
            if (elementType == ICE_MODULE) {
                Block block = new SliceModuleBlock(
                        child,
                        Wrap.createWrap(WrapType.NONE, false),
                        Alignment.createAlignment(),
                        spacingBuilder
                );
                blocks.add(block);
            } else if (elementType == ICE_GLOBAL_METADATA) {
                Block block = new AbstractSliceBlock(
                        child,
                        Wrap.createWrap(WrapType.NONE, false),
                        Alignment.createAlignment(),
                        spacingBuilder
                ) {
                    @Override
                    protected List<Block> buildChildren() {
                        return Collections.emptyList();
                    }

                    @Override
                    public boolean isLeaf() {
                        return true;
                    }
                };
                blocks.add(block);
            }
            child = child.getTreeNext();
        }
        return blocks;
    }
}
