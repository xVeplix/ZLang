package contentTest.function.util.types;

import com.sun.org.apache.bcel.internal.generic.Instruction;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.*;
import contentTest.function.util.FunctionType;
import contentTest.function.util.interfaces.FuncConstants;

public final class FuncType extends FunctionType {

    public FuncType(){}

    public String toString(){
        return "func";
    }

    public boolean identicalTo(FunctionType other) {
        return this == other;
    }

    public String toSignature() {
        return "F";
    }

    public com.sun.org.apache.bcel.internal.generic.Type toJCType() {
        return null;
    }

    public Instruction POP() {
        return FuncConstants.NOP;
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Type type) {
        if (type == Type.String) {
            this.translateTo(classGen, methodGen, (StringType)type);
        } else {
            ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", this.toString(), type.toString());
            classGen.getParser().reportError(2, err);
        }

    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, StringType type) {
        InstructionList il = methodGen.getInstructionList();
        il.append(new PUSH(classGen.getConstantPool(), ""));
    }

    public void translateFrom(ClassGenerator classGen, MethodGenerator methodGen, Class<?> clazz) {
        if (!clazz.getName().equals("func")) {
            ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", this.toString(), clazz.getName());
            classGen.getParser().reportError(2, err);
        }

    }

}
