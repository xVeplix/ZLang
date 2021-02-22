package ZLang.function.util.ElementHandler;

import com.sun.beans.decoder.ValueObject;
import ZLang.function.util.Object.FuncObject;
import ZLang.function.util.ValueObject.FuncValueObject;
import ZLang.function.util.ValueObject.FuncValueObjectImpl;

import java.beans.Expression;

import static java.util.Locale.ENGLISH;

public class FunctionObjectElementHandler extends FunctionNewElementHandler{

    private String idref;
    private String field;
    private Integer index;
    private String property;
    private String method;

    @Override
    public final void addAttribute(String name, String value) {
        if (name.equals("idref")) { // NON-NLS: the attribute name
            this.idref = value;
        } else if (name.equals("field")) { // NON-NLS: the attribute name
            this.field = value;
        } else if (name.equals("index")) { // NON-NLS: the attribute name
            this.index = Integer.valueOf(value);
            addArgument(this.index); // hack for compatibility
        } else if (name.equals("property")) { // NON-NLS: the attribute name
            this.property = value;
        } else if (name.equals("method")) { // NON-NLS: the attribute name
            this.method = value;
        } else {
            super.addAttribute(name, value);
        }
    }

    @Override
    public final void startElement() {
        if ((this.field != null) || (this.idref != null)) {
            getValueObject();
        }
    }

    @Override
    protected boolean isArgument() {
        return true; // hack for compatibility
    }

    @Override
    protected ValueObject getFuncValueObject() {
        return null;
    }

    protected final FuncValueObject getValueObject(Class<?> type, FuncObject[] args) throws Exception {
        if (this.field != null) {
            return (FuncValueObject) FuncValueObjectImpl.funcCreate((FuncValueObject) FuncFieldElementHandler.getFieldValue((FuncObject) getContextBean(), this.field));
        }
        if (this.idref != null) {
            return FuncValueObjectImpl.funcCreate((FuncValueObject) getVariable(this.idref));
        }
        FuncObject bean = (FuncObject) getContextBean();
        String name;
        if (this.index != null) {
            name = (args.length == 2)
                    ? FuncPropertyElementHandler.SETTER
                    : FuncPropertyElementHandler.GETTER;
        } else if (this.property != null) {
            name = (args.length == 1)
                    ? FuncPropertyElementHandler.SETTER
                    : FuncPropertyElementHandler.GETTER;

            if (0 < this.property.length()) {
                name += this.property.substring(0, 1).toUpperCase(ENGLISH) + this.property.substring(1);
            }
        } else {
            name = (this.method != null) && (0 < this.method.length())
                    ? this.method
                    : "new"; // NON-NLS: the constructor marker
        }
        Expression expression = new Expression(bean, name, args);
        return FuncValueObjectImpl.funcCreate((FuncValueObject) expression.getValue());
    }
}
