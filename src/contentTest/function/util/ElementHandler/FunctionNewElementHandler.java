package contentTest.function.util.ElementHandler;

import com.sun.beans.finder.ConstructorFinder;
import contentTest.function.util.Object.FuncObject;
import contentTest.function.util.ValueObject.FuncValueObject;
import contentTest.function.util.ValueObject.FuncValueObjectImpl;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

abstract class FunctionNewElementHandler extends FuncElementHandler{

    private List<FuncObject> arguments = new ArrayList<FuncObject>();
    private FuncValueObject value = FuncValueObjectImpl.FUNC;

    private Class<?> type;

    @Override
    public void addAttribute(String name, String value) {
        if (name.equals("class")) { // NON-NLS: the attribute name
            this.type = getOwner().findClass(value);
        } else {
            super.addAttribute(name, value);
        }
    }

    protected final void addArgument(Object argument) {
        if (this.arguments == null) {
            throw new IllegalStateException("Could not add argument to evaluated element");
        }
        this.arguments.add((FuncObject) argument);
    }

    @Override
    protected final Object getContextBean() {
        return (this.type != null)
                ? this.type
                : (FuncObject) super.getContextBean();
    }

    protected final FuncValueObject getValueObject() {
        if (this.arguments != null) {
            try {
                this.value = getValueObject(this.type, (FuncObject[]) this.arguments.toArray());
            }
            catch (Exception exception) {
                getOwner().handleException(exception);
            }
            finally {
                this.arguments = null;
            }
        }
        return this.value;
    }

    FuncValueObject getValueObject(Class<?> type, FuncObject[] args) throws Exception {
        if (type == null) {
            throw new IllegalArgumentException("Class name is not set");
        }
        Class<?>[] types = getArgumentTypes(args);
        Constructor<?> constructor = ConstructorFinder.findConstructor(type, types);
        if (constructor.isVarArgs()) {
            args = (FuncObject[]) getArguments(args, constructor.getParameterTypes());
        }
        return FuncValueObjectImpl.funcCreate((FuncValueObject) constructor.newInstance(args));
    }

    static Class<?>[] getArgumentTypes(Object[] arguments) {
        Class<?>[] types = new Class<?>[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            if (arguments[i] != null) {
                types[i] = arguments[i].getClass();
            }
        }
        return types;
    }

    static Object[] getArguments(Object[] arguments, Class<?>[] types) {
        int index = types.length - 1;
        if (types.length == arguments.length) {
            Object argument = arguments[index];
            if (argument == null) {
                return arguments;
            }
            Class<?> type = types[index];
            if (type.isAssignableFrom(argument.getClass())) {
                return arguments;
            }
        }
        int length = arguments.length - index;
        Class<?> type = types[index].getComponentType();
        Object array = Array.newInstance(type, length);
        System.arraycopy(arguments, index, array, 0, length);

        Object[] args = new Object[types.length];
        System.arraycopy(arguments, 0, args, 0, index);
        args[index] = array;
        return args;
    }

}
