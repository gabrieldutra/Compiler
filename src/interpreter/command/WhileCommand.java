
package interpreter.command;

import interpreter.expr.BoolExpr;
import interpreter.util.Arguments;
import interpreter.util.Instance;

/**
 *
 * @author MarceloFCandido, gabrieldutra
 */
public class WhileCommand extends Command {

    private BoolExpr expr;
    private Command cmd;
    
    public WhileCommand(int line, BoolExpr expr, Command cmd) {
        super(line);
        this.expr = expr;
        this.cmd = cmd;
    }

    @Override
    public void execute(Instance instance, Arguments args) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
