import interpreter.command.Command;
import lexical.LexicalAnalysis;
import lexical.TokenType;
import syntatic.SyntaticAnalysis;

public class sooi {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java compiler [File]");
            return;
        }

        try (LexicalAnalysis l = new LexicalAnalysis(args[0])) {
            // Codigo para testar o funcionamento do analisador lexico
            Lexeme lex;
            while (checkType((lex = l.nextToken()).type)) {
                System.out.printf("(\"%s\", %s)\n", lex.token, lex.type);
            }

            switch (lex.type) {
                case INVALID_TOKEN:
                    System.out.printf("%02d: Lexema inválido [%s]\n", l.getLine(), lex.token);
                    break;
                case UNEXPECTED_EOF:
                    System.out.printf("%02d: Fim de arquivo inesperado\n", l.getLine());
                    break;
                default:
                    System.out.printf("(\"%s\", %s)\n", lex.token, lex.type);
                    break;
            }
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

