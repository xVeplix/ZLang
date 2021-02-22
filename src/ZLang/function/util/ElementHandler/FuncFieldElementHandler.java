package ZLang.function.util.ElementHandler;

import com.sun.beans.decoder.ValueObject;
import com.sun.beans.finder.FieldFinder;
import ZLang.function.util.Object.FuncObject;

import java.lang.reflect.Field;

public class FuncFieldElementHandler extends FuncAccessorElementHandler{
    private Class<?> type;

    @Override
    public void addAttribute(String name, String value) {
        if (name.equals("class")) { // NON-NLS: the attribute name
            this.type = getOwner().findClass(value);
        } else {
            super.addAttribute(name, value);
        }
    }

    @Override
    protected boolean isArgument() {
        return super.isArgument() && (this.type != null); // only static accessor can be used an argument
    }

    @Override
    protected ValueObject getFuncValueObject() {
        return null;
    }

    @Override
    protected Object getContextBean() {
        return (this.type != null)
                ? this.type
                : super.getContextBean();
    }

    @Override
    protected Object getValue(String name) {
        try {
            return getFieldValue((FuncObject) getContextBean(), name);
        }
        catch (Exception exception) {
            getOwner().handleException(exception);
        }
        return null;
    }

    @Override
    protected void setValue(String name, Object value) {
        try {
            setFieldValue(getContextBean(), name, value);
        }
        catch (Exception exception) {
            getOwner().handleException(exception);
        }
    }

    static FuncObject getFieldValue(FuncObject bean, String name) throws IllegalAccessException, NoSuchFieldException {
        return (FuncObject) findField(bean, name).get(bean);
    }

    private static void setFieldValue(Object bean, String name, Object value) throws IllegalAccessException, NoSuchFieldException {
        findField(bean, name).set(bean, value);
    }

    private static Field findField(Object bean, String name) throws NoSuchFieldException {
        return (bean instanceof Class<?>)
                ? FieldFinder.findStaticField((Class<?>) bean, name)
                : FieldFinder.findField(bean.getClass(), name);
    }

}
