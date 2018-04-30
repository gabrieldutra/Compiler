/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    private Command then;
    private Command Else;
    
    public IfCommand(int line, BoolExpr cond, Command then) {
        super(line);
        this.cond = cond;
        this.then = then;
    }
    
    public IfCommand(int line, BoolExpr cond, Command then, Command Else) {
        super(line);
        this.cond = cond;
        this.then = then;
        this.Else = Else;
    }

    @Override
    public void execute(Instance instance, Arguments args) {
        throw new UnsupportedOperationException("Not supported yet."); 
         //To change body of generated methods, choose Tools | Templates.
    }
    
}
