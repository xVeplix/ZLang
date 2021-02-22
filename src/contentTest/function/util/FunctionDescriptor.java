package contentTest.function.util;

import contentTest.function.util.interfaces.FunctionReturnType;
import contentTest.function.util.interfaces.FunctionTypeTreeVisitor;

public class FunctionDescriptor implements FunctionReturnType {

    private static final FunctionDescriptor singleton = new FunctionDescriptor();

    private FunctionDescriptor(){}

    public static FunctionDescriptor make() {return singleton;}



    public void accept(FunctionTypeTreeVisitor<?> v){v.visitFuncDescriptor(this);}
}
