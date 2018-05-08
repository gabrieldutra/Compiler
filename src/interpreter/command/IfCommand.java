package interpreter.command;

import interpreter.expr.BoolExpr;
import interpreter.util.Arguments;
import interpreter.util.Instance;

/**
 *
 * @author marscanhavelife
 */
public class IfCommand extends Command {
    
    private BoolExpr cond;
    private Command thenCommand;
    private Command elseCommand;
    
    public IfCommand(int line, BoolExpr cond, Command then) {
        super(line);
        this.cond = cond;
        this.thenCommand = then;
    }
    
    public IfCommand(int line, BoolExpr cond, Command then,
            Command elseCommand) {
        super(line);
        this.cond = cond;
        this.thenCommand = then;
        this.elseCommand = elseCommand;
    }
    
    @Override
    public void execute(Instance self, Arguments args) {
        if (cond.expr(self, args)) {
            thenCommand.execute(self, args);
        } else {
            if (elseCommand != null) {
                elseCommand.execute(self, args);
            }
        }
    }
    
}
