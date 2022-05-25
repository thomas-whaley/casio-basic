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

    private boolean peek(TokenType tokenType, Queue<Token> tokens) {
        if (tokens.isEmpty()) return false;
        return tokens.peek().getType() == tokenType;
    }

    private boolean eat(TokenType tokenType, Queue<Token> tokens) {
        if (!peek(tokenType, tokens)) return false;
        tokens.poll();
        return true;
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
     * STATEMENT ::= VAR_ASSIGN |
     *               WHILE |
     *               FOR |
     *               IF
     */
    private ASTNode parseStatement(Queue<Token> tokens) {
        ASTNode body = null;
        if (peek(TokenType.WHILE, tokens))    body = parseWhile(tokens);
        else if (peek(TokenType.FOR, tokens)) body = parseFor(tokens);
        else if (peek(TokenType.IF, tokens))  body = parseIf(tokens);
        else                                  body = parseVarAssign(tokens);
        return new StatementNode(body);
    }
    
    /**
     * VAR_ASSIGN ::= EXPR "->" VAR_NAME
     */
    private ASTNode parseVarAssign(Queue<Token> tokens) {
        ASTNode expr = parseExpression(tokens);
        require(TokenType.VAR_ASSIGN, "Variable assigning expects assign token", tokens);
        Token varName = require(TokenType.VAR_NAME, "Invalid variable name", tokens);
        return new VarAssignNode(expr, (char) varName.getVal());
    }
    
    /**
     * WHILE ::= "While" EXPR BODY "WhileEnd"
     */
    private ASTNode parseWhile(Queue<Token> tokens) {
        require(TokenType.WHILE, "Invalid identifier in while block, expects `While`", tokens);
        ASTNode expr = parseExpression(tokens);
        ASTNode body = parseBody(TokenType.WHILE_END, "WhileEnd", tokens);
        return new WhileNode(expr, body);
    }
    
    /**
     * FOR ::= "For" VAR_ASSIGN "To" EXPR ( "Step" EXPR ) BODY "Next"
     */
    private ASTNode parseFor(Queue<Token> tokens) {
        require(TokenType.FOR, "Invalid identifier in for block, expects `For`", tokens);
        ASTNode var = parseVarAssign(tokens);
        require(TokenType.TO, "Invalid identifier in for block, expects `To`", tokens);
        ASTNode toExpr = parseExpression(tokens);
        ASTNode stepExpr = null;
        if (eat(TokenType.STEP, tokens)) stepExpr = parseExpression(tokens);
        ASTNode body = parseBody(TokenType.NEXT, "Next", tokens);
        return new ForNode(var, toExpr, stepExpr, body);
    }
    
    /**
     * IF ::= "If" EXPR "Then" BODY ( "Else" BODY ) "EndIf"
     */
    private ASTNode parseIf(Queue<Token> tokens) {
        require(TokenType.IF, "Invalid identifier in if block, expects `If`", tokens);
        ASTNode condition = parseExpression(tokens);
        require(TokenType.THEN, "Invalid identifier in if block, expects `Then`", tokens);
        
        List<ASTNode> ifStatements = new LinkedList<ASTNode>();
        // Parse body until `Else` or `EndIf`
        while (!peek(TokenType.ELSE, tokens) && !eat(TokenType.IF_END, tokens)) {
            // If we are at the end, require a `EndIf`
            if (tokens.size() == 1) { require(TokenType.IF_END, "Body expects `IfEnd` at the end, but received nothing", tokens); break; }
            ifStatements.add(parseStatement(tokens));
        }
        ASTNode ifBody = new BodyNode(ifStatements);
        ASTNode elseBody = null;
        if (eat(TokenType.ELSE, tokens)) {
            elseBody = parseBody(TokenType.IF_END, "IfEnd", tokens);
        }
        return new IfNode(condition, ifBody, elseBody);
    }
    
    
    /**
     * BODY ::= STATEMENT* BODY_END
     */
    private ASTNode parseBody(TokenType end, String endString, Queue<Token> tokens) {
        List<ASTNode> body = new LinkedList<ASTNode>();
        while (!eat(end, tokens)) {
            if (tokens.size() == 1) {
                require(end, "Body expects `" + endString + "` at the end, but received nothing", tokens);
                break;
            }
            body.add(parseStatement(tokens));
        }
        return new BodyNode(body);
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
     *              REL_OP ( "And" REL_OP )* |
     *              REL_OP ( "Or" REL_OP )* |
     *              "Not" REL_OP
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
     *            SUM ( "<" SUM )* |
     *            SUM ( ">" SUM )* |
     *            SUM ( "=" SUM )*
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
     *         TERM ( "+" TERM )* |
     *         TERM ( "-" TERM )*
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
     *          FACTOR ( "*" FACTOR )* |
     *          FACTOR ( "/" FACTOR )* |
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
     * FACTOR ::= "+" FACTOR |
     *            "-" FACTOR |
     *            "(" EXPR ")" |
     *            NUMBER |
     *            VAR_NAME
     */
    private ASTNode parseFactor(Queue<Token> tokens) {
        ASTNode node = null;
        if (eat(TokenType.PLUS, tokens))           node = new PlusUnaryOpNode(parseFactor(tokens));
        else if (eat(TokenType.MINUS, tokens))     node = new MinusUnaryOpNode(parseFactor(tokens));
        else if (peek(TokenType.VAR_NAME, tokens)) node = new VarEvaluateNode((char) tokens.poll().getVal());
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
