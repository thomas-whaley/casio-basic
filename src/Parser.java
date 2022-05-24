import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Parser {
    private ASTNode root;

    Parser(Queue<Token> tokens) {
        this.root = parseProgram(tokens);
    }

    public ASTNode getRoot() { return root; }

    private void fail(String message) {
        System.out.println("ERROR: " + message);
        System.exit(1);
    }

    private boolean eat(TokenType tokenType, Queue<Token> tokens) {
        if (tokens.isEmpty()) return false;
        if (tokens.peek().getType() == tokenType) {
            tokens.poll();
            return true;
        }
        return false;
    }

    private Token require(TokenType tokenType, String message, Queue<Token> tokens) {
        if (tokens.isEmpty()) fail(message);
        Token token = tokens.poll();
        if (token.getType() != tokenType) fail(token.getLoc() + " " + message);
        return token;
    }


    /**
     * PROGRAM ::= STATEMENT*
     */
    private ASTNode parseProgram(Queue<Token> tokens) {
        List<ASTNode> nodes = new LinkedList<ASTNode>();
        while (!tokens.isEmpty()) { nodes.add(parseStatement(tokens)); }
        return new ProgramNode(nodes);
    }

    /**
     * STATEMENT ::= VAR_ASSIGN
     */
    private ASTNode parseStatement(Queue<Token> tokens) {
        ASTNode body = parseVarAssign(tokens);
        return new StatementNode(body);
    }

    /**
     * VAR_ASSIGN ::= EXPR ASSIGN VAR_NAME
     */
    private ASTNode parseVarAssign(Queue<Token> tokens) {
        ASTNode expr = parseExpression(tokens);
        require(TokenType.VAR_ASSIGN, "Variable assigning expects assign token", tokens);
        Token varName = require(TokenType.VAR_NAME, "Invalid variable name", tokens);
        return new VarAssignNode(expr, (char) varName.getVal());
    }

    /**
     * EXPR ::= LOGIC_OP
     */
    private ASTNode parseExpression(Queue<Token> tokens) {
        ASTNode node = parseLogicOp(tokens);
        return new ExpressionNode(node);
    }
    
    /**
     * LOGIC_OP ::= REL_OP |
     *              REL_OP ( AND REL_OP )* |
     *              REL_OP ( OR REL_OP )* |
     *              NOT REL_OP
     */
    private ASTNode parseLogicOp(Queue<Token> tokens) {
        if (eat(TokenType.NOT, tokens)) return new LogicOpNode(new NotNode(parseRelativeOp(tokens)));
        ASTNode node = parseRelativeOp(tokens);
        while (true) {
            if (eat(TokenType.AND, tokens)) {
                node = new AndNode(node, parseRelativeOp(tokens));
            }
            else if (eat(TokenType.OR, tokens)) {
                node = new OrNode(node, parseRelativeOp(tokens));
            }
            else break;
        }
        return new LogicOpNode(node);
    }
    
    /**
     * REL_OP ::= SUM |
     *            SUM ( L_THAN SUM )* |
     *            SUM ( G_THAN SUM )* |
     *            SUM ( EQ_TO SUM )*
     */
    private ASTNode parseRelativeOp(Queue<Token> tokens) {
        ASTNode node = parseSum(tokens);
        while (true) {
            if (eat(TokenType.L_THAN, tokens)) {
                node = new LessThanNode(node, parseSum(tokens));
            }
            else if (eat(TokenType.G_THAN, tokens)) {
                node = new GreaterThanNode(node, parseSum(tokens));
            }
            else break;
        }
        return new RelativeOpNode(node);
    }

    /**
     * SUM ::= TERM |
     *         TERM ( PLUS TERM )* |
     *         TERM ( MINUS TERM )*
     */
    private ASTNode parseSum(Queue<Token> tokens) {
        ASTNode node = parseTerm(tokens);
        while (true) {
            if (eat(TokenType.PLUS, tokens)) {
                node = new PlusOpNode(node, parseTerm(tokens));
            }
            else if (eat(TokenType.MINUS, tokens)) {
                node = new MinusOpNode(node, parseTerm(tokens));
            }
            else break;
        }
        return new SumNode(node);
    }

    /**
     * TERM ::= FACTOR |
     *          FACTOR ( MULTIPLY FACTOR )* |
     *          FACTOR ( DIVIDE FACTOR )* |
     */
    private ASTNode parseTerm(Queue<Token> tokens) {
        ASTNode node = parseFactor(tokens);
        while (true) {
            if (eat(TokenType.MULTIPLY, tokens)) {
                node = new MultiplyOpNode(node, parseFactor(tokens));
            }
            else if (eat(TokenType.DIVIDE, tokens)) {
                node = new DivideOpNode(node, parseFactor(tokens));
            }
            else break;
        }
        return new TermNode(node);
    }

    /**
     * FACTOR ::= PLUS FACTOR |
     *            MINUS FACTOR |
     *            LPAREN EXPR RPAREN |
     *            NUMBER
     */
    private ASTNode parseFactor(Queue<Token> tokens) {
        ASTNode node = null;
        if (eat(TokenType.PLUS, tokens))       node = new PlusUnaryOpNode(parseFactor(tokens));
        else if (eat(TokenType.MINUS, tokens)) node = new MinusUnaryOpNode(parseFactor(tokens));
        else if (eat(TokenType.LPAREN, tokens)) {
            node = parseExpression(tokens);
            require(TokenType.RPAREN, "Missing closing parenthesis ')' after expression", tokens);
        }
        else node = parseNumber(tokens);
        return new FactorNode(node);
    }
    
    /**
     * NUMBER ::= [0-9]+
     */
    private ASTNode parseNumber(Queue<Token> tokens) {
        Token token = require(TokenType.NUM, "Expects a number", tokens);
        return new NumberNode((int) token.getVal());
    }
}
