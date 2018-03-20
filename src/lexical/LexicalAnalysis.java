package lexical;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.PushbackInputStream;

import java.util.*;

public class LexicalAnalysis implements AutoCloseable {

    private int line;
    private SymbolTable st;
    private PushbackInputStream input;

    public LexicalAnalysis(String filename) throws LexicalException {
        try {
            input = new PushbackInputStream(new FileInputStream(filename));
        } catch (Exception e) {
            throw new LexicalException("Unable to open file");
        }

        st = new SymbolTable();
        line = 1;
    }

    public void close() throws IOException {
        input.close();
    }

    public int getLine() {
        return this.line;
    }

    public Lexeme nextToken() throws IOException {
        int estado = 1;
        Lexeme lex = new Lexeme("", TokenType.END_OF_FILE);

        while (estado != 9 && estado != 10) {
            int c = input.read();
            switch (estado) {
                case 1:
                    // jump not desired characters
                    if (c == ' ' || c == '\t' || c == '\r') {
                        break;
                    } else if (c == '\n') { // sum line when finds '\n'
                        line++;
                    } else if (c == '/') {
                        lex.token += (char) c;
                        estado = 2;
                    } else if (Character.isDigit(c)) {
                        lex.token += (char) c;
                        // we already know the TokenType will be a Number 
                        // because it's the only possible way
                        lex.type = TokenType.NUMBER;
                        estado = 5;
                    } else if (c == '<' || c == '>' || c == '!' || c == '=') {
                        lex.token += (char) c;
                        estado = 6;
                    } else if (Character.isLetter(c)) {
                        lex.token += (char) c;
                        estado = 7;
                    } else if (c == '\"') {
                        lex.type = TokenType.STRING;
                        estado = 8;
                    } else if (c == ';' || c == ',' || c == '.' || c == '('
                            || c == ')' || c == '{' || c == '}' || c == '+'
                            || c == '-' || c == '*' || c == '%' || c == '&'
                            || c == '|') {
                        lex.token += (char) c;
                        estado = 9;
                    } else if (c == -1) {
                        lex.type = TokenType.END_OF_FILE;
                        estado = 10;
                    } else {
                        lex.token += (char) c;
                        lex.type = TokenType.INVALID_TOKEN;
                        estado = 10;
                    }

                    break;

                case 2:
                    if (c == '*') {
                        lex.token = lex.token.substring(0, lex.token.length() - 1); // Remove the '/' from the token
                        estado = 3;
                    } else {
                        input.unread(c);
                        estado = 9;
                    }
                    break;

                case 3:
                    if (c == '*') {
                        estado = 4;
                    } else {
                        estado = 3;
                    }
                    break;

                case 4:
                    if (c == '/') {
                        estado = 1;
                    } else if (c == '*') {
                        estado = 4;
                    } else {
                        estado = 3;
                    }
                    break;

                case 5:
                    if (Character.isDigit(c)) {
                        lex.token += (char) c;
                        estado = 5;
                    } else {
                        if (c != -1) {
                            input.unread(c);
                        }
                        estado = 10;
                    }
                    break;

                case 6:
                    if (c == '=') {
                        lex.token += (char) c;
                    } else {
                        if (c != -1) {
                            input.unread(c);
                        }
                    }
                    estado = 9;
                    break;

                case 7:
                    if (!Character.isLetter(c) && !Character.isDigit(c)) {
                        input.unread(c);
                        estado = 9;
                        break;
                    }

                    lex.token += (char) c;
                    estado = 7;
                    break;                
                case 8:
                    if (c != '\"') {
                        lex.token += (char) c;
                        estado = 8;
                    } else {
                        estado = 10;
                    }
                    break;
                default:
                    break;
            }
        }

        if (estado == 9) {
            if (st.contains(lex.token)) {
                lex.type = st.find(lex.token);
            } else {
                lex.type = TokenType.NAME;
            }
        }

        return lex;
    }
}
