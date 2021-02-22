package ZLang.function.util.interfaces;

import ZLang.function.util.FunctionDescriptor;

public interface FunctionTypeTreeVisitor<T>{

    T getResult();

   /* void visitFormalTypeParameter(FormalTypeParameter ftp);

    void visitClassTypeSignature(ClassTypeSignature ct);
    void visitArrayTypeSignature(ArrayTypeSignature a);
    void visitTypeVariableSignature(TypeVariableSignature tv);
    void visitWildcard(Wildcard w);

    void visitSimpleClassTypeSignature(SimpleClassTypeSignature sct);
    void visitBottomSignature(BottomSignature b);

    //  Primitives and Void
    void visitByteSignature(ByteSignature b);
    void visitBooleanSignature(BooleanSignature b);
    void visitShortSignature(ShortSignature s);
    void visitCharSignature(CharSignature c);
    void visitIntSignature(IntSignature i);
    void visitLongSignature(LongSignature l);
    void visitFloatSignature(FloatSignature f);
    void visitDoubleSignature(DoubleSignature d);

    */

    void visitFuncDescriptor(FunctionDescriptor f);

}
