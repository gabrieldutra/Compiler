package syntatic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lexical.Lexeme;
import lexical.LexicalAnalysis;
import lexical.TokenType;
import model.Const;
import model.Identifier;
import model.Type;
import semantic.SemanticAnalysis;

/**
 *
 * @author gabrieldutra, MarceloFCandido, rubiotorres
 */
public class SyntaticAnalysis {
    private LexicalAnalysis lex;
    private SemanticAnalysis sem;
    private Lexeme current;
    
    /**
     * @param lex
     * @throws IOException
     */
    public SyntaticAnalysis(LexicalAnalysis lex) throws IOException {
        this.lex = lex;
        this.sem = new SemanticAnalysis(lex);
        this.current = lex.nextToken();
    }
    
    public void start() {
        procProgram();
        matchToken(TokenType.END_OF_FILE);
    }    
    
    /*
     * Marca o lexema corrente com o tipo passado por parametro, caso se trate
     * do tipo correto
     */
    private Lexeme matchToken(TokenType type) {
//         System.out.println(lex.getLine() + " - Match token: " + current.type + " == " + type + "?");
        if (type == current.type) {
            try {
                Lexeme previous = current;
                current = lex.nextToken();
                return previous;
            } catch (IOException ex) {
                System.out.printf("Erro: Exceçao de IO na leitura do token\n");
            }
        } else {
            showError();
        }
        return null;
    }
    
     /*
     * Exibe uma determinada mensagem de acordo com o tipo de erro passado por
     * parametro
     */
    private void showError() {
        System.out.printf("%02d: ", lex.getLine());

        switch (current.type) {
            case INVALID_TOKEN:
                System.out.printf("Lexema inválido [%s]\n", current.token);
                break;
            case UNEXPECTED_EOF:
            case END_OF_FILE:
                System.out.printf("Fim de arquivo inesperado\n");
                break;
            default:
                System.out.printf("Lexema não esperado [%s]\n", current.token);
                break;
        }

        System.exit(1);
    }
    
    // program ::= start [decl-list] stmt { stmt } exit
    private void procProgram() {
        matchToken(TokenType.START);
        
        if (current.type == TokenType.INT || current.type == TokenType.FLOAT || current.type == TokenType.STRING) {
            procDeclList();
        }

        do {
            procStmt();
        } while (current.type != TokenType.EXIT);
        
        
        matchToken(TokenType.EXIT);
    }
    
    private void procDeclList() {
        do {
            Type type = procType();
            Lexeme lexeme = matchToken(TokenType.IDENTIFIER);
            sem.procDecl(new Identifier(lexeme.token, type));
            while (current.type == TokenType.COMMA) {
                matchToken(TokenType.COMMA);
                lexeme = matchToken(TokenType.IDENTIFIER);
                sem.procDecl(new Identifier(lexeme.token, type));
            }
            matchToken(TokenType.DOT_COMMA);
            
        } while (current.type == TokenType.INT || current.type == TokenType.FLOAT || current.type == TokenType.STRING);
    }

    private Type procType() {
        if (current.type == TokenType.INT) {
            matchToken(TokenType.INT);
            return Type.INT;
        } else if (current.type == TokenType.FLOAT) {
            matchToken(TokenType.FLOAT);
            return Type.FLOAT;
        }
        matchToken(TokenType.STRING);
        return Type.STRING;
    }
    
    private void procAssignStmt() {
        matchToken(TokenType.IDENTIFIER);
        matchToken(TokenType.ASSIGN);
        procSimpleExpr();
    }
    
    private void procStmt() {
        if (current.type == TokenType.IF) {
            procIfStmt();
        } else if (current.type == TokenType.DO) {
            procWhileStmt();
        } else if (current.type == TokenType.SCAN) {
            procReadStmt();
            matchToken(TokenType.DOT_COMMA);
        } else if (current.type == TokenType.PRINT) {
            procWriteStmt();
            matchToken(TokenType.DOT_COMMA);
        } else {
            procAssignStmt();
            matchToken(TokenType.DOT_COMMA);
        }
    }
    
    // if-stmt ::= if expression then stmt {stmt} [else stmt {stmt}] end
    private void procIfStmt() {
        matchToken(TokenType.IF);
        procExpression();
        matchToken(TokenType.THEN);
        procStmt();
        while (current.type != TokenType.ELSE && current.type != TokenType.END) {
            procStmt();
        }
        
        if (current.type == TokenType.ELSE) {
            matchToken(TokenType.ELSE);
            procStmt();
            while (current.type != TokenType.END) {
                procStmt();
            }
        }
        
        matchToken(TokenType.END);        
    }
    
    // while ::= do stmt {stmt} while expression end
    private void procWhileStmt() {
        matchToken(TokenType.DO);
         procStmt();
        while (current.type != TokenType.WHILE) {
            procStmt();
        }
        matchToken(TokenType.WHILE);
        procExpression();
        matchToken(TokenType.END);
    }
    
    // write-stmt ::= print "(" simple-expr ")"
    private void procReadStmt() {
        matchToken(TokenType.SCAN);
        matchToken(TokenType.OPEN_PAR);
        matchToken(TokenType.IDENTIFIER);
        matchToken(TokenType.CLOSE_PAR);
    }
    
    // write-stmt ::= print "(" simple-expr ")"
    private void procWriteStmt() {
        matchToken(TokenType.PRINT);
        matchToken(TokenType.OPEN_PAR);
        procSimpleExpr();
        matchToken(TokenType.CLOSE_PAR);
    }
    
    // expression ::= simple-expr { relop simple-expr }
    private Const procExpression() {
        Const expression = procSimpleExpr();

        while (current.type == TokenType.EQUAL || current.type == TokenType.GREATER ||
            current.type == TokenType.GREATER_EQ || current.type == TokenType.LESS ||
            current.type == TokenType.LESS_EQ || current.type == TokenType.NOT_EQUAL) {
            TokenType op = current.type;
            procRelop();
            Const operationExp = procSimpleExpr();
            expression = sem.procExpressionWithRelop(expression, operationExp, op);
        }
        return expression;
    }
    
    // simple-expr ::= term { addop term }
    private Const procSimpleExpr() {
        Const term = procTerm();

        while (current.type == TokenType.PLUS || current.type == TokenType.MINUS ||
            current.type == TokenType.OR) {
            TokenType op = current.type;
            procAddop();
            Const addTerm = procTerm();
            term = sem.procTermWithSimpleExpr(term, addTerm, op);
        }
        return term;
    }
    
    // term ::= factor-a { mulop factor-a }
    private Const procTerm() {
        Const factor = procFactorA();

        while (current.type == TokenType.MULT || current.type == TokenType.DIV ||
            current.type == TokenType.AND) {
            TokenType op = current.type;
            procMulop();
            Const mulFactor = procFactorA();
            factor = sem.procFactorWithMulop(factor, mulFactor, op);
        }
        return factor;
    }
    
    private Const procFactorA() {
        TokenType extraToken = null;
        if (current.type == TokenType.NOT || current.type == TokenType.MINUS) {
            extraToken = current.type;
            matchToken(current.type);
        }
        Const factorConst = procFactor();
        if (extraToken == TokenType.NOT) {
            return sem.procFactorWithNot(factorConst);
        }
        if (extraToken == TokenType.MINUS) {
            return sem.procFactorWithMinus(factorConst);
        }
        return factorConst;
    }
    
    private Const procFactor() {
        if (current.type == TokenType.IDENTIFIER) {
            Lexeme lexeme = matchToken(TokenType.IDENTIFIER);
            return sem.procFactorWithIdentifier(lexeme.token);
        } else if (current.type == TokenType.OPEN_PAR) {
            matchToken(TokenType.OPEN_PAR);
            Const expValue = procExpression();
            matchToken(TokenType.CLOSE_PAR);
            return expValue;
        }
        return procConstant();
    }
    
    private void procRelop() {
        if (current.type == TokenType.EQUAL || current.type == TokenType.GREATER ||
            current.type == TokenType.GREATER_EQ || current.type == TokenType.LESS ||
            current.type == TokenType.LESS_EQ || current.type == TokenType.NOT_EQUAL) {
            matchToken(current.type);
        } else {
            showError();
        }
    }
    
    private void procAddop() {
        if (current.type == TokenType.PLUS) {
            matchToken(TokenType.PLUS);
        } else if (current.type == TokenType.MINUS) {
            matchToken(TokenType.MINUS);
        } else {
            matchToken(TokenType.OR);
        }
    }
    
    private void procMulop() {
        if (current.type == TokenType.MULT) {
            matchToken(TokenType.MULT);
        } else if (current.type == TokenType.DIV) {
            matchToken(TokenType.DIV);
        } else {
            matchToken(TokenType.AND);
        }
    }
    
    private Const procConstant() {
        if (current.type == TokenType.INTEGER_CONST) {
            return procIntegerConst();
        } else if (current.type == TokenType.FLOAT_CONST) {
            return procFloatConst();
        }
        return procLiteral();
    }
    
    private Const procIntegerConst() {
        return new Const(Integer.parseInt(matchToken(TokenType.INTEGER_CONST).token), Type.INT);
    }
    
    private Const procFloatConst() {
        return new Const(Float.parseFloat(matchToken(TokenType.FLOAT_CONST).token), Type.FLOAT);
    }
    
    private Const procLiteral() {
        return new Const(matchToken(TokenType.LITERAL).token, Type.STRING);
    }
}
