package org.xblackcat.frozenice.psi;

import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.tree.ILightStubFileElementType;
import org.xblackcat.frozenice.SliceLanguage;

/**
 * 05.01.12 14:40
 *
 * @author xBlackCat
 */
public class IceFileElementType extends ILightStubFileElementType<PsiIceFileStub> {
    public IceFileElementType() {
        super("ice.FILE", SliceLanguage.INSTANCE);
    }

    @Override
    public void indexStub(PsiIceFileStub stub, IndexSink sink) {
    }

    @Override
    public String getExternalId() {
        return "ice.FILE";
    }
}
