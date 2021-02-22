package ZLang.function.util.ElementHandler;

import com.sun.beans.decoder.ValueObject;
import com.sun.beans.finder.MethodFinder;
import ZLang.function.util.Object.FuncObject;
import sun.reflect.misc.MethodUtil;

import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FuncPropertyElementHandler extends FuncAccessorElementHandler{

    static final String GETTER = "get";
    static final String SETTER = "set";

    private Integer index;

    @Override
    public void addAttribute(String name, String value) {
        if (name.equals("index")) { // NON-NLS: the attribute name
            this.index = Integer.valueOf(value);
        } else {
            super.addAttribute(name, value);
        }
    }

    @Override
    protected boolean isArgument() {
        return false;
    }

    @Override
    protected ValueObject getFuncValueObject() {
        return null;
    }

    @Override
    protected FuncObject getValue(String name) {
        try {
            return (FuncObject) getPropertyValue((FuncObject) getContextBean(), name, this.index);
        }
        catch (Exception exception) {
            getOwner().handleException(exception);
        }
        return null;
    }

    @Override
    protected void setValue(String name, Object value) {

    }

    public void setValue(String name, FuncObject value) {
        try {
            setPropertyValue((FuncObject) getContextBean(), name, this.index, value);
        }
        catch (Exception exception) {
            getOwner().handleException(exception);
        }
    }

    private static FuncObject getPropertyValue(FuncObject bean, String name, Integer index) throws IllegalAccessException, IntrospectionException, InvocationTargetException, NoSuchMethodException {
        Class<?> type = bean.getClass();
        if (index == null) {
            return (FuncObject) MethodUtil.invoke(findGetter(type, name), bean, new Object[] {});
        } else if (type.isArray() && (name == null)) {
            return (FuncObject) Array.get(bean, index);
        } else {
            return (FuncObject) MethodUtil.invoke(findGetter(type, name, int.class), bean, new Object[] {index});
        }
    }

    private static void setPropertyValue(FuncObject bean, String name, Integer index, Object value) throws IllegalAccessException, IntrospectionException, InvocationTargetException, NoSuchMethodException {
        Class<?> type = bean.getClass();
        Class<?> param = (value != null)
                ? value.getClass()
                : null;

        if (index == null) {
            MethodUtil.invoke(findSetter(type, name, param), bean, new Object[] {value});
        } else if (type.isArray() && (name == null)) {
            Array.set(bean, index, value);
        } else {
            MethodUtil.invoke(findSetter(type, name, int.class, param), bean, new Object[] {index, value});
        }
    }

    private static Method findGetter(Class<?> type, String name, Class<?>...args) throws IntrospectionException, NoSuchMethodException {
        if (name == null) {
            return MethodFinder.findInstanceMethod(type, GETTER, args);
        }
        PropertyDescriptor pd = getProperty(type, name);
        if (args.length == 0) {
            Method method = pd.getReadMethod();
            if (method != null) {
                return method;
            }
        } else if (pd instanceof IndexedPropertyDescriptor) {
            IndexedPropertyDescriptor ipd = (IndexedPropertyDescriptor) pd;
            Method method = ipd.getIndexedReadMethod();
            if (method != null) {
                return method;
            }
        }
        throw new IntrospectionException("Could not find getter for the " + name + " property");
    }

    private static Method findSetter(Class<?> type, String name, Class<?>...args) throws IntrospectionException, NoSuchMethodException {
        if (name == null) {
            return MethodFinder.findInstanceMethod(type, SETTER, args);
        }
        PropertyDescriptor pd = getProperty(type, name);
        if (args.length == 1) {
            Method method = pd.getWriteMethod();
            if (method != null) {
                return method;
            }
        } else if (pd instanceof IndexedPropertyDescriptor) {
            IndexedPropertyDescriptor ipd = (IndexedPropertyDescriptor) pd;
            Method method = ipd.getIndexedWriteMethod();
            if (method != null) {
                return method;
            }
        }
        throw new IntrospectionException("Could not find setter for the " + name + " property");
    }

    private static PropertyDescriptor getProperty(Class<?> type, String name) throws IntrospectionException {
        for (PropertyDescriptor pd : Introspector.getBeanInfo(type).getPropertyDescriptors()) {
            if (name.equals(pd.getName())) {
                return pd;
            }
        }
        throw new IntrospectionException("Could not find the " + name + " property descriptor");
    }
}
