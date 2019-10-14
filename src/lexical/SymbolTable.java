package lexical;

import java.util.HashMap;
import java.util.Map;

class SymbolTable {

    private Map<String, TokenType> st;

    public SymbolTable() {
        st = new HashMap<String, TokenType>();

        // symbols
        st.put(";", TokenType.DOT_COMMA);
        st.put(",", TokenType.COMMA);
        st.put("(", TokenType.OPEN_PAR);
        st.put(")", TokenType.CLOSE_PAR);

        // keywords
        st.put("start", TokenType.START);
        st.put("exit", TokenType.EXIT);
        st.put("do", TokenType.DO);
        st.put("then", TokenType.THEN);
        st.put("end", TokenType.END);
        st.put("if", TokenType.IF);
        st.put("else", TokenType.ELSE);
        st.put("while", TokenType.WHILE);
        st.put("int", TokenType.INT);
        st.put("float", TokenType.FLOAT);
        st.put("string", TokenType.STRING);
        st.put("scan", TokenType.SCAN);
        st.put("print", TokenType.PRINT);

        // operators
        st.put("+", TokenType.PLUS);
        st.put("-", TokenType.MINUS);
        st.put("*", TokenType.MULT);
        st.put("/", TokenType.DIV);
        st.put("=", TokenType.ASSIGN);
        st.put("<", TokenType.LESS);
        st.put(">", TokenType.GREATER);
        st.put("==", TokenType.EQUAL);
        st.put("<=", TokenType.LESS_EQ);
        st.put(">=", TokenType.GREATER_EQ);
        st.put("<>", TokenType.NOT_EQUAL);
        st.put("not", TokenType.NOT);
        st.put("and", TokenType.AND);
        st.put("or", TokenType.OR);
    }

    public boolean contains(String token) {
        return st.containsKey(token);
    }

    public TokenType find(String token) {
        return this.contains(token) ?
                st.get(token) : TokenType.INVALID_TOKEN;
    }
}
