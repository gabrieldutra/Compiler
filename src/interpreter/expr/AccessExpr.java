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
        if (mem == self) {
            return new InstanceValue(self);
        }
        return path.getValue(self, args);
    }

}
