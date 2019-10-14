package lexical;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;

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

    @Override
    public void close() throws IOException {
        input.close();
    }

    public int getLine() {
        return this.line;
    }

    public Lexeme nextToken() throws IOException {
        int estado = 1;
        Lexeme lex = new Lexeme("", TokenType.END_OF_FILE);

        while (estado != 12 && estado != 13) {
            int c = input.read();

            // handle unexpected EOF
            if (c == -1 && estado != 1) {
                lex.type = TokenType.UNEXPECTED_EOF;
                break;
            }

            switch (estado) {
                case 1:
                    // jump undesired characters
                    if (c == ' ' || c == '\t' || c == '\r') {
                        break;
                    } else if (c == '\n') { // sum line when finds '\n'
                        line++;
                    } else if (c == '\"') {
                        estado = 2;
                    } else if (Character.isLetter(c)) {
                        lex.token += (char) c;
                        estado = 3;
                    } else if (Character.isDigit(c)) {
                        // float const or integer const
                        lex.token += (char) c;
                        estado = 4;
                    } else if (c == '=' || c == '>') {
                        lex.token += (char) c;
                        estado = 6;
                    } else if (c == '<') {
                        lex.token += (char) c;
                        estado = 7;
                    } else if (c == ',' || c == ';' || c == '+' || c == '-' ||
                               c == '*' || c == '(' || c == ')') {
                        lex.token += (char) c;
                        estado = 12;
                    } else if (c == '/') {
                        lex.token += (char) c;
                        estado = 8;
                    } else if (c == -1) {
                        lex.type = TokenType.END_OF_FILE;
                        estado = 13;
                    } else {
                        lex.token += (char) c;
                        lex.type = TokenType.INVALID_TOKEN;
                        estado = 13;
                    }

                    break;

                case 2:
                    if (c == '"') {
                        lex.type = TokenType.LITERAL;
                        estado = 13;
                    } else {
                        lex.token += (char) c;
                        estado = 2;
                    }
                    break;

                case 3:
                    if (Character.isLetter(c) || Character.isDigit((c))) {
                        lex.token += (char) c;
                        estado = 3;
                    } else {
                        estado = 12;
                        input.unread(c);
                    }
                    break;

                case 4:
                    if (Character.isDigit(c)) {
                        lex.token += (char) c;
                        estado = 4;
                    } else if (c == '.') {
                        lex.token += (char) c;
                        estado = 5;
                    } else {
                        lex.type = TokenType.INTEGER_CONST;
                        input.unread(c);
                        estado = 13;
                    }
                    break;

                case 5:
                    if (Character.isDigit(c)) {
                        lex.token += (char) c;
                        estado = 5;
                    } else {
                        lex.type = TokenType.FLOAT_CONST;
                        input.unread(c);
                        estado = 13;
                    }
                    break;

                case 6:
                    if (c == '=') {
                        lex.token += (char) c;
                    } else {
                        input.unread(c);
                    }
                    estado = 12;
                    break;

                case 7:
                    if (c == '>' || c == '=') {
                        lex.token += (char) c;
                    } else {
                        input.unread(c);
                    }
                    estado = 12;
                    break;
                    
                case 8:
                    if (c == '/') {
                        // Remove the '/' from the token
                        lex.token = lex.token.substring(0, lex.token.length() - 1);
                        estado = 9;
                    } else if (c == '*') {
                        // Remove the '/' from the token
                        lex.token = lex.token.substring(0, lex.token.length() - 1);
                        estado = 10;
                    } else {
                        input.unread(c);
                        estado = 12;
                    }
                    break;
    
                case 9:
                    if (c == '\n') {
                        input.unread(c);
                        estado = 1;
                    } else {
                        estado = 9;
                    }
                    break;
                
                case 10:
                    if (c == '*') {
                        estado = 11;
                    } else {
                        estado = 10;
                    }
                    break;
                    
                case 11:
                    if (c == '/') {
                        estado = 1;
                    } else {
                        estado = 10;
                    }
                    break;

                default:
                    break;
            }
        }

        if (estado == 12) {
            if (st.contains(lex.token)) {
                lex.type = st.find(lex.token);
            } else {
                lex.type = TokenType.NAME;
            }
        }

        return lex;
    }
}
