import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

//      Program commands
// If               | x
// Then             | x
// Else             | x
// IfEnd            | x
// For              | x
// To               | x
// Step             | x
// Next             | x
// While            | x
// WhileEnd         | x
// Do               | x
// LpWhile          | x

//      Program controls
// Prog             |
// Return           |
// Break            |
// Stop             |

//      Jump commands
// Lbl              |
// Goto             |
// =>               |
// Isz              |
// Dsz              |
// Menu             |


//      Input/Output commands
// ?                |
// ¸                |

//      Clear commands
// ClrText          |
// ClrGraph         |
// ClrList          |
// ClrMat           |
// ClrVct           |

//      Display commands
// DrawStat         |
// DrawGraph        |
// DrawDyna         |
// DrawWeb          |
// PlotPhase        |
// DispF-Tbl        |
// DispR-Tbl        |
// DrawFTG-Con      |
// DrawFTG-Plt      |
// DrawR-Con        |
// DrawR-Plt        |
// DrawR∑-Con       |
// DrawR∑-Plt       |

//      I/O commands
// Locate           | x
// Getkey           | x
// Send(            |
// Receive(         |
// Send38k          |
// Receive38k       |
// OpenComport38k   |
// CloseComport38k  |

//      Relational operators
// =                | x
// ≠                | x  also `!=`
// >                | x
// <                | x
// ≥                | x  also `>=`
// ≤                | x  also `<=`

//      Multi-statement commands
// :                | x
// \n               | x

//      String commands
// Exp(             |
// Exp>Str(         |
// StrCmp(          |
// StrInv(          |
// StrJoin(         |
// StrLeft(         |
// StrLen(          |
// StrLwr(          |
// StrMid(          |
// StrRight(        |
// StrRotate(       |
// StrShift(        |
// StrSrc(          |
// StrUpr(          |

//      Other
// '                | x



public class Lexer {
    private String fileName;    // Current file name
    private int lineNum;        // Line number
    private int col;            // Current char pointer

    // Data structure to hold the resulting tokens
    private Queue<Token> tokens;

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
        assert TokenType.values().length == 35 : "Exhaustive handling of TokenTypes in lexLine()";

        this.lineNum ++;
        this.col = 0;
        while (this.col < line.length()) {
            char c = line.charAt(col);

            if (c == '\'') return;
            else if (c == '-') {
                if (peek(line) == '>') {
                    tokens.offer(new Token(TokenType.VAR_ASSIGN, getCurrentLoc(), "->"));
                    this.col ++;
                }
                else tokens.offer(new Token(TokenType.MINUS, getCurrentLoc(), '-'));
            }
            else if (Character.isWhitespace(c)) {}
            else if (Character.isDigit(c)) tokens.offer(lexNum(line));
            else if (Character.isAlphabetic(c)) tokens.offer(lexIdentifier(line));
            else if (c == '"') tokens.offer(lexText(line));
            else if (c == '+') tokens.offer(new Token(TokenType.PLUS, getCurrentLoc(), '+'));
            else if (c == '*') tokens.offer(new Token(TokenType.MULTIPLY, getCurrentLoc(), '✕'));
            else if (c == '✕') tokens.offer(new Token(TokenType.MULTIPLY, getCurrentLoc(), '✕'));
            else if (c == '/') tokens.offer(new Token(TokenType.DIVIDE, getCurrentLoc(), '÷'));
            else if (c == '÷') tokens.offer(new Token(TokenType.DIVIDE, getCurrentLoc(), '÷'));
            else if (c == '(') tokens.offer(new Token(TokenType.LPAREN, getCurrentLoc(), '('));
            else if (c == ')') tokens.offer(new Token(TokenType.RPAREN, getCurrentLoc(), ')'));
            else if (c == '>') tokens.offer(new Token(TokenType.G_THAN, getCurrentLoc(), '>'));
            else if (c == '<') tokens.offer(new Token(TokenType.L_THAN, getCurrentLoc(), '<'));
            else if (c == '≤') tokens.offer(new Token(TokenType.L_THAN_E, getCurrentLoc(), '≤'));
            else if (c == '≥') tokens.offer(new Token(TokenType.G_THAN_E, getCurrentLoc(), '>'));
            else if (c == '=') tokens.offer(new Token(TokenType.EQ_TO, getCurrentLoc(), '='));
            else if (c == ',') tokens.offer(new Token(TokenType.COMMA, getCurrentLoc(), ','));
            else fail("Unknown character '" + c + "'");
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
        // Special case: VAR_NAME
        if (identifier.length() == 1) {
            char c = identifier.charAt(0);
            if (c >= 'A' && c <= 'Z') {
                return new Token(TokenType.VAR_NAME, getCurrentLoc(), c);
            }
        }

        TokenType type = null;

        if (identifier.equals("!="))            type = TokenType.NOT_EQ_TO;
        else if (identifier.equals("<="))       type = TokenType.L_THAN_E;
        else if (identifier.equals(">="))       type = TokenType.G_THAN_E;

        else if (identifier.equals("While"))    type = TokenType.WHILE;
        else if (identifier.equals("WhileEnd")) type = TokenType.WHILE_END;
        else if (identifier.equals("LpWhile"))  type = TokenType.LP_WHILE;
        else if (identifier.equals("Do"))       type = TokenType.DO;
        else if (identifier.equals("For"))      type = TokenType.FOR;
        else if (identifier.equals("To"))       type = TokenType.TO;
        else if (identifier.equals("Step"))     type = TokenType.STEP;
        else if (identifier.equals("Next"))     type = TokenType.NEXT;
        else if (identifier.equals("If"))       type = TokenType.IF;
        else if (identifier.equals("Then"))     type = TokenType.THEN;
        else if (identifier.equals("Else"))     type = TokenType.ELSE;
        else if (identifier.equals("IfEnd"))    type = TokenType.IF_END;

        else if (identifier.equals("And"))      type = TokenType.AND;
        else if (identifier.equals("Or"))       type = TokenType.OR;
        else if (identifier.equals("Not"))      type = TokenType.NOT;

        else if (identifier.equals("Getkey"))   type = TokenType.GET_KEY;
        else if (identifier.equals("Locate"))   type = TokenType.LOCATE;

        if (type == null) {
            fail("Unknown identifier '" + identifier + "'");
        }
        return new Token(type, new TokenLocation(fileName, lineNum, col - identifier.length() + 1), identifier);
    }

    private Token lexText(String line) {
        String text = "";
        if (line.charAt(this.col) != '"') fail("Text expects opening `\"`");
        // this.col++;
        while (this.col + 1< line.length()) {
            this.col++;
            char c = line.charAt(this.col);
            if (c == '"') break;
            text += line.charAt(this.col);
        }
        if (line.charAt(this.col) != '"') fail("Text expects closing `\"`");
        // Skip trailing `"`
        this.col ++;
        return new Token(TokenType.TEXT ,new TokenLocation(fileName, lineNum, col - text.length()), text);
    }
}
