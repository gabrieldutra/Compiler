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
    private void procCode() throws IOException{
        matchToken(TokenType.OPEN_CUR);
        procStatement();
        matchToken(TokenType.CLOSE_CUR);
    }
    
    // <statement> ::= <if> | <while> | <cmd>
    private void procStatement() throws IOException{
        if (current.type == TokenType.IF) 
            procIf(); // TODO: verificar isso
        else if (current.type == TokenType.WHILE)
            procWhile();
        else
            procCmd();
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
    // TODO: Completar isso
    private void procCmd() throws IOException {
        procAccess();
        if (current.type == TokenType.ATTRIB) 
            procAssign();
        else if (current.type == TokenType.OPEN_PAR)
            procCall();
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
        procRhs();
        while (current.type == TokenType.COMMA) {
            matchToken(TokenType.COMMA);
            procRhs();
        }
        matchToken(TokenType.CLOSE_PAR);
    }
    
    // <boolexpr> ::= [ '!' ] <cmpexpr> [ ('&' | '|') <boolexpr> ]
    private void procBoolExpr() throws IOException{
        if (current.type == TokenType.NOT)
            matchToken(TokenType.NOT);
        procCmpexpr();
        // PAREI AQUI
    }
    
    // <cmpexpr> ::= <expr> <relop> <expr>
    private void procCmpexpr() {
        
    }
    
    // <relop> ::= '==' | '!=' | '<' | '>' | '<=' | '>='
    private void procRelop() {
        
    }
    
    // <rhs> ::= <function> | <expr>
    private void procRhs() {
        
    }
    
    // <function>  ::= function '{' <code> [ return <rhs> ] '}'
    private void procFunction() {
    
    }
    
    // <expr>      ::= <term> { ('+' | '-') <term> }
    private void procExpr() {
        
    }
    
    // <term>      ::= <factor> { ('*' | '/' | '%') <factor> }
    private void procTerm() {
        
    }
    
    // <factor> ::= <number> | <string> | <access> [ <call> ]
    private void procFactor() {
        
    }
    
    // <var>       ::= system | self | args | <name>
    private void procVar() {
        
    }
    
    private void procName() {
        
    }
    
}
