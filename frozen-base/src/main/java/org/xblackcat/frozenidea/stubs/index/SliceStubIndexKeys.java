package org.xblackcat.frozenidea.stubs.index;

import com.intellij.psi.stubs.StubIndexKey;
import org.xblackcat.frozenidea.psi.SliceDataTypeElement;
import org.xblackcat.frozenidea.psi.SliceMethodDef;

/**
 * 24.03.2016 15:45
 *
 * @author xBlackCat
 */
public class SliceStubIndexKeys {
    public static final StubIndexKey<String, SliceDataTypeElement> DATA_TYPE_NAMES = StubIndexKey.createIndexKey("slice.datatypes");
    public static final StubIndexKey<String, SliceMethodDef> METHODS = StubIndexKey.createIndexKey("slice.interface.method.names");
    public static final StubIndexKey<String, SliceDataTypeElement> FIELDS = StubIndexKey.createIndexKey("slice.field.name");

    private SliceStubIndexKeys() {
    }
}
