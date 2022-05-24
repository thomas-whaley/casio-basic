import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;

public class Lexer {
    private String fileName;    // Current file name
    private int lineNum;        // Line number
    private int col;            // Current char pointer

    // Data structure to hold the resulting tokens
    private Queue<Token> tokens;

    private final String WORD = "\\[a-zA-Z][a-zA-Z_0-9]*";

    Lexer(File file) { lexFile(file); }

    public Queue<Token> getTokens() { return tokens; }

    private TokenLocation getCurrentLoc() {
        return new TokenLocation(fileName, lineNum, col + 1);
    }

    private void fail(String message) {
        System.out.println("ERROR: " + getCurrentLoc() + "  " + message);
        System.exit(1);
    }

    private char peek(String line) {
        if (col + 1 >= line.length()) return '\0';
        return line.charAt(col + 1);
    }

    private void lexFile(File file) {
        try {
            // Update the class variables
            this.fileName = file.toString();
            this.lineNum = 0;
            tokens = new LinkedList<>();

            // Splits the file's contents by line
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                lexLine(line);
            }
            br.close();
        } catch (IOException e) { System.out.println("Invalid file \"" + fileName + "\""); System.exit(1); }
    }

    private void lexLine(String line) {
        assert TokenType.values().length == 15 : "Exhaustive handling of TokenTypes in lexLine()";

        this.lineNum ++;
        this.col = 0;
        while (this.col < line.length()) {
            char c = line.charAt(col);

            // Ignore whitespace
            if (Character.isWhitespace(c)) {
            }
            // Current char is a digit, parse the digits
            else if (Character.isDigit(c)) {
                tokens.offer(lexNum(line));
            }

            else if (Character.isAlphabetic(c)) {
                // Variable name
                // VAR_NAME ::= [A-Z]
                if ((c >= 'A' && c <= 'Z') && (Character.isWhitespace(peek(line)) || peek(line) == '\0')) {
                    tokens.offer(new Token(TokenType.VAR_NAME, getCurrentLoc(), c));
                }
                else {
                    tokens.offer(lexIdentifier(line));
                }
            }

            else if (c == '+') {
                tokens.offer(new Token(TokenType.PLUS, getCurrentLoc(), '+'));
            }
            else if (c == '-') {
                if (peek(line) == '>') {
                    tokens.offer(new Token(TokenType.VAR_ASSIGN, getCurrentLoc(), "->"));
                    this.col ++;
                }
                else {
                    tokens.offer(new Token(TokenType.MINUS, getCurrentLoc(), '-'));
                }
            }
            else if (c == '*') {
                tokens.offer(new Token(TokenType.MULTIPLY, getCurrentLoc(), '*'));
            }
            else if (c == '÷') {
                tokens.offer(new Token(TokenType.DIVIDE, getCurrentLoc(), '÷'));
            }
            else if (c == '(') {
                tokens.offer(new Token(TokenType.LPAREN, getCurrentLoc(), '('));
            }
            else if (c == ')') {
                tokens.offer(new Token(TokenType.RPAREN, getCurrentLoc(), ')'));
            }
            else if (c == '>') {
                tokens.offer(new Token(TokenType.G_THAN, getCurrentLoc(), '>'));
            }
            else if (c == '<') {
                tokens.offer(new Token(TokenType.L_THAN, getCurrentLoc(), '<'));
            }
            else if (c == '=') {
                tokens.offer(new Token(TokenType.EQ_TO, getCurrentLoc(), '='));
            }


            
            else {
                fail("Unknown character '" + c + "'");
            }
            this.col ++;
        }
    }

    private Token lexNum(String line) {
        // String to hold the value
        String num = "";
        while (this.col < line.length()) {
            char c = line.charAt(this.col);
            if (!Character.isDigit(c)) break;
            num += c;
            this.col ++;
        }
        // We have gone one too far, so back track
        this.col --;
        if (num.isEmpty()) fail("Unreachable in lexNum(): num string is empty. This is a bug in the lexer");
        int val = Integer.valueOf(num);
        return new Token(TokenType.NUM, getCurrentLoc(), val);
    }

    private Token lexIdentifier(String line) {
        String identifier = "";
        while (this.col < line.length()) {
            char c = line.charAt(this.col);
            if (!Character.isAlphabetic(c)) break;
            identifier += c;
            this.col ++;
        }

        this.col --;
        TokenType type = null;
        if (identifier.equals("and"))      type = TokenType.AND;
        else if (identifier.equals("or"))  type = TokenType.OR;
        else if (identifier.equals("not")) type = TokenType.NOT;

        if (type == null) {
            fail("Unknown identifier '" + identifier + "'");
        }
        return new Token(type, getCurrentLoc(), identifier);
    }
}
