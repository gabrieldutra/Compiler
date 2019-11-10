import lexical.Lexeme;
import lexical.LexicalAnalysis;
import lexical.TokenType;
import syntatic.SyntaticAnalysis;

public class compiler {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java compiler [File]");
            return;
        }

        try (LexicalAnalysis l = new LexicalAnalysis(args[0])) {
            SyntaticAnalysis s = new SyntaticAnalysis(l);
            s.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Internal error: " + e.getMessage());
        }
    }

    private static boolean checkType(TokenType type) {
        return !(type == TokenType.END_OF_FILE ||
                type == TokenType.INVALID_TOKEN ||
                type == TokenType.UNEXPECTED_EOF);
    }
}

