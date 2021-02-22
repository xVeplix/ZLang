package contentTest.function.util.interfaces;

import sun.reflect.generics.visitor.TypeTreeVisitor;

public interface FunctionTypeTree extends FunctionTree{

    void accept(FunctionTypeTreeVisitor<?> v);

}
