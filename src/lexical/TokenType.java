package lexical;

public enum TokenType {
    // special tokens
    INVALID_TOKEN,
    UNEXPECTED_EOF,
    END_OF_FILE,

    // symbols
    DOT_COMMA,   // ';'
    COMMA,       // ','
    OPEN_CUR,    // '{'
    CLOSE_CUR,   // '}'
    OPEN_PAR,    // '('
    CLOSE_PAR,   // ')'
    
    // keywords
    IF,         // if
    WHILE,      // while   
    FUNCTION,    // fuction def
    SYSTEM,     // var system
    SELF,       // var self
    ARGS,       // var args
    
    // operators
    DOT,        // '.'
    ATTRIB,     // '='
    PLUS,       // '+'
    MINUS,      // '-'
    MULT,       // '*'
    DIV,        // '/'
    MOD,        // '%'
    LESS,       // '<'
    GREATER,    // '>'
    EQUAL,      // '=='
    LESS_EQ,    // '<='
    GREATER_EQ, // '>='
    NOT_EQ,     // '!='
    NOT,        // '!'
    AND,        // '&'
    OR,         // '|'
    
    // others
    NAME,
    NUMBER,
    STRING
};
