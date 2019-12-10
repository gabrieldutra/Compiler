package semantic;

import lexical.LexicalAnalysis;
import lexical.TokenType;
import model.Const;
import model.Identifier;
import model.Memory;
import model.Type;

/**
 *
 * @author gabriel
 */
public class SemanticAnalysis {
    
    private LexicalAnalysis lex;
    private Memory memory;

    public SemanticAnalysis(LexicalAnalysis lex) {
        this.lex = lex;
        this.memory = new Memory();
    }
    
    public void showError(String error) {
        System.out.printf("%02d: %s\n", lex.getLine(), error);
        System.exit(1);
        
    }
    

    public void procDecl(Identifier identifier) {
        if (!memory.hasIdentifier(identifier.getName())) {
            memory.addIdentifier(identifier);
        } else {
            showError("Identifier '" + identifier.getName() +  "' already declared.");
        }
    }

    public Const procFactorWithIdentifier(String id) {
        if (memory.hasIdentifier(id)) {
            return memory.getValue(id);
        }
        showError("Identifier '" + id +  "' was not declared.");
        return null;
    }

    public Const procFactorWithNot(Const factorConst) {
        if (factorConst.getType() == Type.BOOL) {
            return new Const(!((boolean) factorConst.getValue()), factorConst.getType());
        }
        showError("Can only use Not with a boolean expression.");
        return null;
    }

    public Const procFactorWithMinus(Const factorConst) {
        if (factorConst.getType() == Type.INT) {
            return new Const(-((int) factorConst.getValue()), factorConst.getType());
        }
        if (factorConst.getType() == Type.FLOAT) {
            return new Const(-((float) factorConst.getValue()), factorConst.getType());
        }
        showError("Can only use unary minus with integer or float.");        
        return null;
    }

    public Const procFactorWithMulop(Const factor, Const mulFactor, TokenType op) {
        if (factor.getType() == mulFactor.getType()) {
            if (factor.getType() == Type.BOOL && op != TokenType.AND) {
                showError("BOOL is not compatible with that operator.");
            }
            if (factor.getType() != Type.BOOL && op == TokenType.AND) {
                showError("AND operator is only compatible with BOOL.");
            }
            if (!isNumber(factor)) {
                showError(op + " operation is only available for number types.");                
            }
            
            return new Const(factor.getValue(), factor.getType());
        }
        showError("Cannot execute " + op + " operation with '" + factor.getType() +
                "' and '" + mulFactor.getType() + "' - types are different.");
        return null;
    }

    public Const procTermWithSimpleExpr(Const term, Const addTerm, TokenType op) {
        if (term.getType() == addTerm.getType()) {
            if (term.getType() == Type.STRING && op != TokenType.PLUS) {
                showError("STRING is not compatible with " + op + " operator.");
            }
            if (term.getType() == Type.BOOL && op != TokenType.OR) {
                showError("BOOL is not compatible with that operator.");
            }
            if (term.getType() != Type.BOOL && op == TokenType.OR) {
                showError("OR operator is only compatible with BOOL.");
            }
            if (!isNumber(term) && op == TokenType.MINUS) {
                showError("MINUS perator is only compatible with number types.");
            }
            return new Const(term.getValue(), term.getType());
        }
        showError("Cannot execute operation with '" + term.getType() +
                "' and '" + addTerm.getType() + "' - types are different.");
        return null;
    }

    public Const procExpressionWithRelop(Const expression, Const operationExp, TokenType op) {
        if (expression.getType() == operationExp.getType()) {
            return new Const(expression.getValue(), expression.getType());
        }
        showError("Cannot execute operation with '" + expression.getType() +
                "' and '" + operationExp.getType() + "' - types are different.");
        return null;
    }
    
    // helper methods
    private static boolean isNumber(Const value) {
        return value.getType() == Type.FLOAT || value.getType() == Type.INT;
    }
}
