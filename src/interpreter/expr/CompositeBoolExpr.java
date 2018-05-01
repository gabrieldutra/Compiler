package interpreter.expr;

import interpreter.util.Arguments;
import interpreter.util.Instance;

/**
 *
 * @author MarceloFCandido
 */
public class CompositeBoolExpr extends BoolExpr {

    BoolExpr left;
    BoolOp op;
    BoolExpr right;

    public CompositeBoolExpr(BoolExpr left, BoolOp op, BoolExpr right, int line) {
        super(line);
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public boolean expr(Instance self, Arguments args) {
        boolean leftResult = left.expr(self, args);
        boolean rightResult = right.expr(self, args);
        if (this.op == BoolOp.And) {
            return leftResult && rightResult;
        }
        return leftResult || rightResult;
    }

}
