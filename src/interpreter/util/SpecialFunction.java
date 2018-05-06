package interpreter.util;

import interpreter.value.IntegerValue;
import interpreter.value.StringValue;
import interpreter.value.Value;

import java.util.Scanner;

import java.util.Random;

public class SpecialFunction extends Function {

    private FunctionType type;
    private Scanner in;

    public SpecialFunction(FunctionType type) {
        this.type = type;
        this.in = new Scanner(System.in);
    }

    @Override
    public Value<?> call(Instance self, Arguments args) {
        Value<?> v;

        switch (type) {
            case Print:
                v = this.print(args);
                break;
            case Println:
                v = this.println(args);
                break;
            case Read:
                v = this.read(args);
                break;
            case RandomV:
                v = this.random(args);
                break;
            case Get:
                v = this.get(args);
                break;                
            case Set:
                v = this.set(args);
                break;                
            case Abort:
                v = this.abort(args);
                break;                
            case Type:
                v = this.type(args);
                break;
            case Length:
                v = this.length(args);
                break;
            case Substring:
                v = this.subString(args);
                break;                
            case Clone:
                v = this.clone(args);
                break;             
            default:
                throw new RuntimeException("Funçao " + type + " nao existente");
        }

        return v;
    }

    private Value<?> print(Arguments args) {
        if (args.contains("arg1")) {
            Value<?> v = args.getValue("arg1");
            if (v instanceof IntegerValue) {
                IntegerValue iv = (IntegerValue) v;
                System.out.print(v.value());
            } else if (v instanceof StringValue) {
                StringValue sv = (StringValue) v;
                System.out.print(sv.value());
            } else {
                throw new RuntimeException("FIXME: Implement me!");
            }
        }

        return IntegerValue.Zero;
    }

    private Value<?> println(Arguments args) {
        Value<?> v = print(args);
        System.out.println();
        return v;
    }

    private Value<?> read(Arguments args) {
        // Print the argument.
        this.print(args);

        String str = in.nextLine();
        try {
            int n = Integer.parseInt(str);
            IntegerValue iv = new IntegerValue(n);
            return iv;
        } catch (Exception e) {
            StringValue sv = new StringValue(str);
            return sv;
        }
    }
    
    private Value<?> random(Arguments args) {
        this.print(args);
        if (args.contains("arg1") && args.contains("arg2")) {
            Value<?> v1 = args.getValue("arg1");
            Value<?> v2 = args.getValue("arg2");
            if (v1 instanceof IntegerValue && v2 instanceof IntegerValue) {
                IntegerValue v1I = (IntegerValue) args.getValue("arg1");
                IntegerValue v2I = (IntegerValue) args.getValue("arg2");
                if (v2I.valueAsInt() > v1I.valueAsInt()) {
                    Random rand = new Random();
                    int randomNum = v1I.valueAsInt() + 
                            rand.nextInt((v2I.valueAsInt() - 
                                    v1I.valueAsInt()) + 1);
                } else {
                    throw new RuntimeException("O segundo argumento deve ser "
                            + "maior que o primeiro.");
                }
            } else {
                throw new RuntimeException("A funçao random "
                                        + "recebe dois argumentos inteiros.");
            }
        } else {
            throw new RuntimeException("A funçao random "
                                        + "recebe dois argumentos.");
        }
    }
    
    private Value<?> get(Arguments args) {
        
    }
    
    private Value<?> set(Arguments args) {
        
    }
    
    private Value<?> abort(Arguments args) {
        
    }
    
    private Value<?> type(Arguments args) {
        
    }
    
    private Value<?> length(Arguments args) {
        
    }
    
    private Value<?> subString(Arguments args) {
        
    }
    
    private Value<?> clone(Arguments args) {
        
    }
    
}
