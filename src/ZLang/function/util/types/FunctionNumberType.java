package ZLang.function.util.types;

import ZLang.function.util.FunctionType;

public abstract class FunctionNumberType extends FunctionType {

    public boolean isNumber() {
        return true;
    }

    public boolean isSimple() {
        return true;
    }

}
