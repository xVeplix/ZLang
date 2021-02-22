package contentTest.function.util.ElementHandler;

import com.sun.beans.decoder.DocumentHandler;
import com.sun.beans.decoder.ElementHandler;
import com.sun.beans.decoder.ValueObject;

public abstract class FuncElementHandler {

    private FuncDocumentHandler owner;
    private FuncElementHandler parent;

    private String id;

    public final FuncDocumentHandler getOwner() {
        return this.owner;
    }

    final void setOwner(FuncDocumentHandler owner) {
        if (owner == null) {
            throw new IllegalArgumentException("Every element should have owner");
        }
        this.owner = owner;
    }

    public final FuncElementHandler getParent() {
        return this.parent;
    }

    final void setParent(FuncElementHandler parent) {
        this.parent = parent;
    }

    protected final Object getVariable(String id) {
        if (id.equals(this.id)) {
            ValueObject value = getFuncValueObject();
            if (value.isVoid()) {
                throw new IllegalStateException("The element does not return value");
            }
            return value.getValue();
        }
        return (this.parent != null)
                ? this.parent.getVariable(id)
                : this.owner.getVariable(id);
    }

    protected Object getContextBean() {
        if (this.parent != null) {
            ValueObject value = this.parent.getFuncValueObject();
            if (!value.isVoid()) {
                return value.getValue();
            }
            throw new IllegalStateException("The outer element does not return value");
        } else {
            Object value = this.owner.getOwner();
            if (value != null) {
                return value;
            }
            throw new IllegalStateException("The topmost element does not have context");
        }
    }

    public void addAttribute(String name, String value) {
        if (name.equals("id")) { // NON-NLS: the attribute name
            this.id = value;
        } else {
            throw new IllegalArgumentException("Unsupported attribute: " + name);
        }
    }

    public void startElement() {
    }

    public void endElement() {
        // do nothing if no value returned
        ValueObject value = getFuncValueObject();
        if (!value.isVoid()) {
            if (this.id != null) {
                this.owner.setVariable(this.id, value.getValue());
            }
            if (isArgument()) {
                if (this.parent != null) {
                    this.parent.addFuncArgument(value.getValue());
                } else {
                    this.owner.addObject(value.getValue());
                }
            }
        }
    }

    public void addCharacter(char ch) {
        if ((ch != ' ') && (ch != '\n') && (ch != '\t') && (ch != '\r')) {
            throw new IllegalStateException("Illegal character with code " + (int) ch);
        }
    }

    protected void addFuncArgument(Object argument) {
        throw new IllegalStateException("Could not add argument to simple element");
    }

    protected boolean isArgument() {
        return this.id == null;
    }

    protected abstract ValueObject getFuncValueObject();

}
