package contentTest.function.util.ValueObject;

import contentTest.function.util.Object.FuncObject;

public class FuncValueObjectImpl implements FuncValueObject {

    static final FuncValueObject NULL = new FuncValueObjectImpl(null);
    public static final FuncValueObject FUNC = new FuncValueObjectImpl();


    public static FuncValueObject funcCreate(FuncValueObject value) {
        return (value != null)
                ? new FuncValueObjectImpl((FuncObject) value)
                : NULL;
    }

    private FuncObject value;
    private boolean isFunc;

    private FuncValueObjectImpl() {
        this.isFunc = true;
    }

    private FuncValueObjectImpl(FuncObject value) {
        this.value = value;
    }

    public FuncObject getValue() {
        return this.value;
    }

    public boolean isFunc() {
        return this.isFunc();
    }

}
