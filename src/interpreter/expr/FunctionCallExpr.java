package interpreter.expr;

import interpreter.util.*;
import interpreter.value.FunctionValue;
import interpreter.value.Value;

import java.util.ArrayList;
import java.util.List;

public class FunctionCallExpr extends Expr {

    private AccessPath path;
    private List<Rhs> params;

    public FunctionCallExpr(AccessPath path, int line) {
        super(line);

        this.path = path;
        this.params = new ArrayList<Rhs>();
    }

    public void addParam(Rhs rhs) {
        params.add(rhs);
    }

    @Override
    public Value<?> rhs(Instance self, Arguments args) {
        Value<?> funct = path.getValue(self, args);
        if (!(funct instanceof FunctionValue)) {
            InterpreterError.abort(this.getLine());
        }

        Function f = ((FunctionValue) funct).value();

        Instance fSelf = (Instance) path.getReference(self, args);

        Arguments fArgs = new Arguments();
        for (int i = 0; i < params.size(); i++) {
            Rhs rhs = params.get(i);
            fArgs.setValue(String.format("arg%d", i + 1), rhs.rhs(self, args));
        }

        Value<?> ret = f.call(fSelf, fArgs);
        return ret;
    }

}
