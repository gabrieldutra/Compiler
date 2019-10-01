package lexical;

public enum TokenType {
    // special tokens
    INVALID_TOKEN,
    UNEXPECTED_EOF,
    END_OF_FILE,

    // symbols
    DOT_COMMA,   // ';'
    COMMA,       // ','
    OPEN_PAR,    // '('
    CLOSE_PAR,   // ')'

    // keywords
    START,      // start
    EXIT,       // exit
    INT,        // int
    FLOAT,      // float
    STRING,     // string
    IF,         // if
    THEN,       // then
    ELSE,       // else
    END,        // end
    DO,         // do
    WHILE,      // while
    SCAN,       // scan
    PRINT,      // print

    // operators
    NOT,        // not
    OR,         // or
    AND,        // and
    EQUAL,      // ==
    NOT_EQUAL,  // <>
    GREATER,    // >
    GREATER_EQ, // >=
    LESS,       // <
    LESS_EQ,    // <=
    PLUS,       // +
    MINUS,      // -
    ASSIGN,     // =
    MULT,       // *
    DIV,        // /

    // others
    NAME,
    FLOAT_CONST,
    INTEGER_CONST,
    LITERAL
};
