package contentTest.function.util.ElementHandler;

import contentTest.function.util.Object.FuncObject;
import contentTest.function.util.ValueObject.FuncValueObject;
import contentTest.function.util.ValueObject.FuncValueObjectImpl;

abstract class FuncAccessorElementHandler extends FuncElementHandler{

    private String name;
    private FuncValueObject value;

    @Override
    public void addAttribute(String name, String value) {
        if (name.equals("name")) {
            this.name = value;
        } else {
            super.addAttribute(name, value);
        }
    }


    protected final void addArgument(FuncObject argument) {
        if (this.value != null) {
            throw new IllegalStateException("Could not add argument to evaluated element");
        }
        setValue(this.name, argument);
        this.value = FuncValueObjectImpl.FUNC;
    }

    protected final FuncValueObject getValueObject() {
        if (this.value == null) {
            this.value = FuncValueObjectImpl.funcCreate((FuncValueObject) getValue(this.name));
        }
        return (FuncValueObject) this.value;
    }

    protected abstract Object getValue(String name);

    protected abstract void setValue(String name, Object value);

}

