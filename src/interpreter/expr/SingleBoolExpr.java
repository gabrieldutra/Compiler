package interpreter.expr;

import interpreter.util.Arguments;
import interpreter.util.Instance;
import interpreter.util.InterpreterError;
import interpreter.value.IntegerValue;
import interpreter.value.Value;

/**
 *
 * @author MarceloFCandido, gabrieldutra
 */
public class SingleBoolExpr extends BoolExpr {

    private Expr left;
    private RelOp op;
    private Expr right;

    public SingleBoolExpr(int line, Expr left, RelOp op, Expr right) {
        super(line);
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public boolean expr(Instance self, Arguments args) {
        Value<?> leftValue = left.rhs(self, args);
        Value<?> rightValue = right.rhs(self, args);
        
        if(leftValue instanceof IntegerValue && rightValue instanceof IntegerValue) {
            int leftIntValue = ((IntegerValue) leftValue).value();
            int rightIntValue = ((IntegerValue) rightValue).value();

            if (op == RelOp.Equal) {
                return leftIntValue == rightIntValue;
            }
            if (op == RelOp.GreaterEqual) {
                return leftIntValue >= rightIntValue;
            }
            if (op == RelOp.GreaterThan) {
                return leftIntValue > rightIntValue;
            }
            if (op == RelOp.LowerEqual) {
                return leftIntValue <= rightIntValue;
            }
            if (op == RelOp.LowerThan) {
                return leftIntValue < rightIntValue;
            }
            if (op == RelOp.NotEqual) {
                return leftIntValue != rightIntValue;
            }
        } else {
            InterpreterError.abort(this.getLine());            
        }
        
        return false;
    }

}
