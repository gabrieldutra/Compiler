
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
    public void execute(Instance self, Arguments args) {
        while(expr.expr(self, args)) {
            cmd.execute(self, args);
        }
    }
    
}
