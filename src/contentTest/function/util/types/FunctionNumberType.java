package contentTest.function.util.types;

import contentTest.function.util.FunctionType;

public abstract class FunctionNumberType extends FunctionType {

    public boolean isNumber() {
        return true;
    }

    public boolean isSimple() {
        return true;
    }

}
