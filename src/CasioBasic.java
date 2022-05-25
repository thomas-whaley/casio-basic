import java.io.File;

class CasioBasic {
    public static void main(String[] args) {
        File file = new File("tests/if.cb");
        Lexer lexer = new Lexer(file);

        // for (Token tok : lexer.getTokens()) { System.out.println(tok.getVal()); }

        Parser parser = new Parser(lexer.getTokens());
        System.out.println(parser.getRoot().debugString("", "  "));
        // System.out.println(parser.getRoot());
    }
}