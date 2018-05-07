package interpreter.expr;

import interpreter.util.AccessPath;
import interpreter.util.Arguments;
import interpreter.util.Instance;
import interpreter.util.Memory;
import interpreter.value.InstanceValue;
import interpreter.value.Value;

public class AccessExpr extends Expr {

    private AccessPath path;

    public AccessExpr(AccessPath path, int line) {
        super(line);
        this.path = path;
    }

    @Override
    public Value<?> rhs(Instance self, Arguments args) {
        Memory mem = path.getReference(self, args);
        System.out.println(path.getNames());
        if (mem instanceof Instance) {
            InstanceValue iv = new InstanceValue((Instance) mem);    
            return iv;
        }
        return path.getValue(self, args);
    }

}
