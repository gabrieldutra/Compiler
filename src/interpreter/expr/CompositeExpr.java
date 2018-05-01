package interpreter.expr;

import interpreter.util.Arguments;
import interpreter.util.Instance;
import interpreter.util.InterpreterError;
import interpreter.value.IntegerValue;
import interpreter.value.StringValue;
import interpreter.value.Value;

/**
 *
 * @author gabrieldutra
 */
public class CompositeExpr extends Expr {

    private Expr left;
    private CompOp op;
    private Expr right;

    public CompositeExpr(Expr left, CompOp op, Expr right, int line) {
        super(line);
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public Value<?> rhs(Instance self, Arguments args) {
        Value<?> leftValue = left.rhs(self, args);
        Value<?> rightValue = right.rhs(self, args);

        if (this.op == CompOp.Add) { // Unica funçao que funciona pra int e String
            if (leftValue instanceof IntegerValue && rightValue instanceof IntegerValue) {

                int leftIntValue = ((IntegerValue) leftValue).value();
                int rightIntValue = ((IntegerValue) rightValue).value();
                
                return new IntegerValue(leftIntValue+rightIntValue);

            } else {
                String leftStringValue = leftValue.value().toString();
                String rightStringValue = rightValue.value().toString();
                return new StringValue(leftStringValue+rightStringValue);
            }
        }

        if (leftValue instanceof IntegerValue && rightValue instanceof IntegerValue) {

            int leftIntValue = ((IntegerValue) leftValue).value();
            int rightIntValue = ((IntegerValue) rightValue).value();

            if (this.op == CompOp.Div) {                
                return new IntegerValue(leftIntValue+rightIntValue);
            }

            if (this.op == CompOp.Mod) {                
                return new IntegerValue(leftIntValue%rightIntValue);

            }

            if (this.op == CompOp.Mul) {
                return new IntegerValue(leftIntValue*rightIntValue);

            }

            if (this.op == CompOp.Sub) {
                return new IntegerValue(leftIntValue-rightIntValue);

            }
        } else {
            InterpreterError.abort(this.getLine());
        }
        return null;
    }

}
