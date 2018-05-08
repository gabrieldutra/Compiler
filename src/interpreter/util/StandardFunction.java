package interpreter.util;

import interpreter.command.CommandsBlock;
import interpreter.expr.Rhs;
import interpreter.value.IntegerValue;
import interpreter.value.Value;

/**
 *
 * @author gabrieldutra, MarceloFCandido
 */
public class StandardFunction extends Function {
    
    private CommandsBlock cmds;
    private Rhs ret;

    public StandardFunction(CommandsBlock cmds, Rhs ret) {
        this.cmds = cmds;
        this.ret = ret;
    }
    
    public StandardFunction(CommandsBlock cmds) {
        this.cmds = cmds;
    }
    
    @Override
    public Value<?> call(Instance self, Arguments args) {
        cmds.execute(self, args);
        if (ret == null) {
            return new IntegerValue(0);
        }
        return ret.rhs(self, args);
    }
    
}
