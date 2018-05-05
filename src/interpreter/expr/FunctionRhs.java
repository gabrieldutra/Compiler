package interpreter.expr;

import interpreter.util.Arguments;
import interpreter.util.Instance;
import interpreter.value.FunctionValue;
import interpreter.value.Value;

/**
 *
 * @author gabrieldutra, MarceloFCandido
 */
public class FunctionRhs extends Rhs {
    
    private FunctionValue func;

    public FunctionRhs(FunctionValue func, int line) {
        super(line);
        this.func = func;
    }

    @Override
    public Value<?> rhs(Instance self, Arguments args) {
        return this.func;
    }
    
}
