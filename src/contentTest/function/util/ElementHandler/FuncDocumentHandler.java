package contentTest.function.util.ElementHandler;

import com.sun.beans.decoder.DocumentHandler;
import com.sun.beans.decoder.ElementHandler;
import com.sun.beans.finder.ClassFinder;
import jdk.internal.access.SharedSecrets;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.beans.ExceptionListener;
import java.io.IOException;
import java.io.StringReader;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuncDocumentHandler extends DefaultHandler {

    private final AccessControlContext acc = AccessController.getContext();
    private final Map<String, Class<? extends FuncElementHandler>> handlers = new HashMap<>();
    private final Map<String, Object> environment = new HashMap<>();
    private final List<Object> objects = new ArrayList<>();

    private Reference<ClassLoader> loader;
    private ExceptionListener listener;
    private Object owner;

    public FuncElementHandler Funchandler;

    public FuncDocumentHandler() {

    }

    public ClassLoader getClassLoader() {
        return (this.loader != null)
                ? this.loader.get()
                : null;
    }

    public void setClassLoader(ClassLoader loader) {
        this.loader = new WeakReference<ClassLoader>(loader);
    }

    public ExceptionListener getExceptionListener() {
        return this.listener;
    }

    public void setExceptionListener(ExceptionListener listener) {
        this.listener = listener;
    }

    public Object getOwner() {
        return this.owner;
    }

    public void setOwner(Object owner) {
        this.owner = owner;
    }

    public Class<? extends FuncElementHandler> getFuncElementHandler(String name) {
        Class<? extends FuncElementHandler> type = this.handlers.get(name);
        if (type == null) {
            throw new IllegalArgumentException("Unsupported element: " + name);
        }
        return type;
    }

    public void setElementHandler(String name, Class<? extends FuncElementHandler> Funchandler) {
        this.handlers.put(name, Funchandler);
    }

    public boolean hasVariable(String id) {
        return this.environment.containsKey(id);
    }

    public Object getVariable(String id) {
        if (!this.environment.containsKey(id)) {
            throw new IllegalArgumentException("Unbound variable: " + id);
        }
        return this.environment.get(id);
    }

    public void setVariable(String id, Object value) {
        this.environment.put(id, value);
    }

    public Object[] getObjects() {
        return this.objects.toArray();
    }

    void addObject(Object object) {
        this.objects.add(object);
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) {
        return new InputSource(new StringReader(""));
    }

    @Override
    public void startDocument() {
        this.objects.clear();
        this.Funchandler = null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        FuncElementHandler parent = this.Funchandler;
        try {
            this.Funchandler =
                    getFuncElementHandler(qName).newInstance();
            this.Funchandler.setOwner(Funchandler.getOwner());
            this.Funchandler.setParent(parent);
        }
        catch (Exception exception) {
            throw new SAXException(exception);
        }
        for (int i = 0; i < attributes.getLength(); i++)
            try {
                String name = attributes.getQName(i);
                String value = attributes.getValue(i);
                this.Funchandler.addAttribute(name, value);
            }
            catch (RuntimeException exception) {
                handleException(exception);
            }

        this.Funchandler.startElement();
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        try {
            this.Funchandler.endElement();
        }
        catch (RuntimeException exception) {
            handleException(exception);
        }
        finally {
            this.Funchandler = this.Funchandler.getParent();
        }
    }

    @Override
    public void characters(char[] chars, int start, int length) {
        if (this.Funchandler != null) {
            try {
                while (0 < length--) {
                    this.Funchandler.addCharacter(chars[start++]);
                }
            }
            catch (RuntimeException exception) {
                handleException(exception);
            }
        }
    }

    public void handleException(Exception exception) {
        if (this.listener == null) {
            throw new IllegalStateException(exception);
        }
        this.listener.exceptionThrown(exception);
    }

    public void parse(final InputSource input) {
        if ((this.acc == null) && (null != System.getSecurityManager())) {
            throw new SecurityException("AccessControlContext is not set");
        }
        AccessControlContext stack = AccessController.getContext();
        SharedSecrets.getJavaSecurityAccess().doIntersectionPrivilege(new PrivilegedAction<Void>() {
            public Void run() {
                try {
                    SAXParserFactory.newInstance().newSAXParser().parse(input, FuncDocumentHandler.this);
                }
                catch (ParserConfigurationException exception) {
                    handleException(exception);
                }
                catch (SAXException wrapper) {
                    Exception exception = wrapper.getException();
                    if (exception == null) {
                        exception = wrapper;
                    }
                    handleException(exception);
                }
                catch (IOException exception) {
                    handleException(exception);
                }
                return null;
            }
        }, stack, this.acc);
    }

    public Class<?> findClass(String name) {
        try {
            return ClassFinder.resolveClass(name, getClassLoader());
        }
        catch (ClassNotFoundException exception) {
            handleException(exception);
            return null;
        }
    }

}
