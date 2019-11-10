package syntatic;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lexical.Lexeme;
import lexical.LexicalAnalysis;
import lexical.TokenType;

/**
 *
 * @author gabrieldutra, MarceloFCandido, rubiotorres
 */
public class SyntaticAnalysis {
    private LexicalAnalysis lex;
    private Lexeme current;
    
    /**
     * @param lex
     * @throws IOException
     */
    public SyntaticAnalysis(LexicalAnalysis lex) throws IOException {
        this.lex = lex;
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
    private void matchToken(TokenType type) {
        // System.out.println("Match token: " + current.type + " == " + type + "?");
        if (type == current.type) {
            try {
                current = lex.nextToken();
            } catch (IOException ex) {
                System.out.printf("Erro: Exceçao de IO na leitura do token\n");
            }
        } else {
            showError();
        }
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
            procType();
            matchToken(TokenType.IDENTIFIER);
            while (current.type == TokenType.COMMA) {
                matchToken(TokenType.COMMA);
                matchToken(TokenType.IDENTIFIER);
            }
            matchToken(TokenType.DOT_COMMA);
        } while (current.type == TokenType.INT || current.type == TokenType.FLOAT || current.type == TokenType.STRING);
    }

    private void procType() {
        if (current.type == TokenType.INT) {
            matchToken(TokenType.INT);
        } else if (current.type == TokenType.FLOAT) {
            matchToken(TokenType.FLOAT);
        } else {
            matchToken(TokenType.STRING);
        }
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
    private void procExpression() {
        procSimpleExpr();

        while (current.type == TokenType.EQUAL || current.type == TokenType.GREATER ||
            current.type == TokenType.GREATER_EQ || current.type == TokenType.LESS ||
            current.type == TokenType.LESS_EQ || current.type == TokenType.NOT_EQUAL) {
            procRelop();
            procSimpleExpr();
        }
    }
    
    // simple-expr ::= term { addop term }
    private void procSimpleExpr() {
        procTerm();

        while (current.type == TokenType.PLUS || current.type == TokenType.MINUS ||
            current.type == TokenType.OR) {
            procAddop();
            procTerm();
        }
    }
    
    // term ::= factor-a { mulop factor-a }
    private void procTerm() {
        procFactorA();

        while (current.type == TokenType.MULT || current.type == TokenType.DIV ||
            current.type == TokenType.AND) {
            procMulop();
            procFactorA();
        }
    }
    
    private void procFactorA() {
        if (current.type == TokenType.NOT || current.type == TokenType.MINUS) {
            matchToken(current.type);
        }
        procFactor();
    }
    
    private void procFactor() {
        if (current.type == TokenType.IDENTIFIER) {
            matchToken(TokenType.IDENTIFIER);
        } else if (current.type == TokenType.OPEN_PAR) {
            matchToken(TokenType.OPEN_PAR);
            // procExpression
            matchToken(TokenType.CLOSE_PAR);
        } else {
            procConstant();
        }
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
    
    private void procConstant() {
        if (current.type == TokenType.INTEGER_CONST) {
            procIntegerConst();
        } else if (current.type == TokenType.FLOAT_CONST) {
            procFloatConst();
        } else {
            procLiteral();
        }
    }
    
    private void procIntegerConst() {
        matchToken(TokenType.INTEGER_CONST);
    }
    
    private void procFloatConst() {
        matchToken(TokenType.FLOAT_CONST);
    }
    
    private void procLiteral() {
        matchToken(TokenType.LITERAL);
    }
}
