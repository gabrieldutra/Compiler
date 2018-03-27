package syntatic;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import lexical.Lexeme;
import lexical.TokenType;
import lexical.LexicalAnalysis;

import interpreter.command.Command;

public class SyntaticAnalysis {

    private LexicalAnalysis lex;
    private Lexeme current;

    public SyntaticAnalysis(LexicalAnalysis lex) throws IOException {
        this.lex = lex;
        this.current = lex.nextToken();
    }

    public Command start() throws IOException {
        return null;
    }

    private void matchToken(TokenType type) throws IOException {
        // System.out.println("Match token: " + current.type + " == " + type + "?");
        if (type == current.type) {
            current = lex.nextToken();
        } else {
            showError();
        }
    }

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

    // <code> ::= { <statement> }
    private void procCode() throws IOException {
        matchToken(TokenType.OPEN_CUR);
        procStatement();
        matchToken(TokenType.CLOSE_CUR);
    }

    // <statement> ::= <if> | <while> | <cmd>
    private void procStatement() throws IOException {
        if (current.type == TokenType.IF) {
            procIf(); // TODO: verificar isso
        } else if (current.type == TokenType.WHILE) {
            procWhile();
        } else {
            procCmd();
        }
    }

    // <if> ::= if '(' <boolexpr> ')' '{' <code> '}' [else '{' <code> '}' ]
    private void procIf() throws IOException {
        matchToken(TokenType.IF);
        matchToken(TokenType.OPEN_PAR);
        procBoolExpr();
        matchToken(TokenType.CLOSE_PAR);
        matchToken(TokenType.OPEN_CUR);
        procCode();
        matchToken(TokenType.CLOSE_CUR);
        if (current.type == TokenType.ELSE) {
            matchToken(TokenType.ELSE);
            matchToken(TokenType.OPEN_CUR);
            procCode();
            matchToken(TokenType.CLOSE_CUR);
        }
    }

    // <while> ::= while '(' <boolexpr> ')' '{' <code> '}'
    private void procWhile() throws IOException {
        matchToken(TokenType.WHILE);
        matchToken(TokenType.OPEN_PAR);
        procBoolExpr();
        matchToken(TokenType.CLOSE_PAR);
        matchToken(TokenType.OPEN_CUR);
        procCode();
        matchToken(TokenType.CLOSE_CUR);
    }

    // <cmd> ::= <access> ( <assign> | <call> ) ';'
    private void procCmd() throws IOException {
        procAccess();
        if (current.type == TokenType.ATTRIB) {
            procAssign();
        } else if (current.type == TokenType.OPEN_PAR) {
            procCall();
        }
        matchToken(TokenType.DOT_COMMA);
    }

    // <access> ::= <var> { '.' <name> }
    private void procAccess() throws IOException {
        procVar();
        while (current.type == TokenType.DOT) {
            matchToken(TokenType.DOT);
            procName();
        }
    }

    // <assign> ::= '=' <rhs>
    private void procAssign() throws IOException {
        matchToken(TokenType.ATTRIB);
        procRhs();
    }

    // <call> ::= '(' [ <rhs> { ',' <rhs> } ] ')'
    private void procCall() throws IOException {
        matchToken(TokenType.OPEN_PAR);
        if (current.type == TokenType.FUNCTION
         || current.type == TokenType.NUMBER
         || current.type == TokenType.STRING
         || current.type == TokenType.OPEN_PAR
         || current.type == TokenType.SYSTEM
         || current.type == TokenType.SELF
         || current.type == TokenType.ARGS
         || current.type == TokenType.NAME) {
            procRhs();
            while (current.type == TokenType.COMMA) {
                matchToken(TokenType.COMMA);
                procRhs();
            }
        } 
        
        matchToken(TokenType.CLOSE_PAR);
    }

    // <boolexpr> ::= [ '!' ] <cmpexpr> [ ('&' | '|') <boolexpr> ]
    private void procBoolExpr() throws IOException {
        if (current.type == TokenType.NOT) {
            matchToken(TokenType.NOT);
        }
        procCmpExpr();

        if (current.type == TokenType.AND || current.type == TokenType.OR) {
            matchToken(current.type);
            procBoolExpr();
        }
    }

    // <cmpexpr> ::= <expr> <relop> <expr>
    private void procCmpExpr() throws IOException {
        procExpr();
        procRelop();
        procExpr();
    }

    // <relop> ::= '==' | '!=' | '<' | '>' | '<=' | '>='
    private void procRelop() throws IOException {
        if (current.type == TokenType.EQUAL || current.type == TokenType.NOT_EQ
                || current.type == TokenType.LESS
                || current.type == TokenType.GREATER
                || current.type == TokenType.LESS_EQ
                || current.type == TokenType.GREATER_EQ) {
            matchToken(current.type);
        }
    }

    // <rhs> ::= <function> | <expr>
    private void procRhs() throws IOException {
        if (current.type == TokenType.FUNCTION) {
            procFunction();
        } else {
            procExpr();
        }
    }

    // <function>  ::= function '{' <code> [ return <rhs> ; ] '}'
    private void procFunction() throws IOException {
        matchToken(TokenType.FUNCTION);
        matchToken(TokenType.OPEN_CUR);
        procCode();
        if (current.type == TokenType.RETURN) {
            matchToken(TokenType.RETURN);
            procRhs();
        }
        matchToken(TokenType.CLOSE_CUR);
    }

    // <expr>      ::= <term> { ('+' | '-') <term> }
    private void procExpr() throws IOException {
        procTerm();
        while (current.type == TokenType.PLUS || current.type == TokenType.MINUS) {
            matchToken(current.type);
            procTerm();
        }
    }

    // <term>      ::= <factor> { ('*' | '/' | '%') <factor> }
    private void procTerm() throws IOException {
        procFactor();
        while (current.type == TokenType.MULT || current.type == TokenType.DIV
                || current.type == TokenType.MOD) {
            matchToken(current.type);
            procFactor();
        }
    }

    // <factor> ::= <number> | <string> | <access> [ <call> ]
    private void procFactor() throws IOException {
        if (current.type == TokenType.NUMBER) {
            procNumber();
        } else if (current.type == TokenType.STRING) {
            matchToken(TokenType.STRING);
        } else {
            procAccess();
            if (current.type == TokenType.OPEN_CUR) {
                procCall();
            }
        }
        
    }

    private void procNumber() throws IOException {
        matchToken(TokenType.NUMBER);
    }

    // <var>       ::= system | self | args | <name>
    private void procVar() throws IOException {
        if (current.type == TokenType.SYSTEM || current.type == TokenType.SELF || current.type == TokenType.ARGS) {
            matchToken(current.type);
        } else {
            procName();
        }
    }

    private void procName() throws IOException {
        matchToken(TokenType.NAME);
    }

}
