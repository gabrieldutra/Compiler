
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
        throw new UnsupportedOperationException("Not supported yet."); 
        //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
