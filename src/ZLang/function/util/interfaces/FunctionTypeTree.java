package ZLang.function.util.interfaces;

public interface FunctionTypeTree extends FunctionTree{

    void accept(FunctionTypeTreeVisitor<?> v);

}
