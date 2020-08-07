package org.xblackcat.frozenidea.code.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xblackcat.frozenidea.psi.SliceTypes;

import java.util.ArrayList;
import java.util.List;

import static org.xblackcat.frozenidea.psi.SliceTypes.*;

/**
 *
 */
public abstract class AbstractSliceBlock extends AbstractBlock {
    protected final SpacingBuilder spacingBuilder;

    public AbstractSliceBlock(
            @NotNull ASTNode node,
            @Nullable Wrap wrap,
            @Nullable Alignment alignment,
            SpacingBuilder spacingBuilder
    ) {
        super(node, wrap, alignment);
        this.spacingBuilder = spacingBuilder;
    }

    @Override
    public @Nullable Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
        return spacingBuilder.getSpacing(this, child1, child2);
    }

    @Override
    public boolean isLeaf() {
        return myNode.getFirstChildNode() == null;
    }
}
