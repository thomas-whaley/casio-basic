import java.util.List;

//////////////////////////////////////////////////////////////
//                      BASE INTERFACES                     //
//////////////////////////////////////////////////////////////
interface ASTNode {

    public String toString();

    public String debugString(String prefix, String pad);
}

//////////////////////////////////////////////////////////////
//                          PROGRAM                         //
//////////////////////////////////////////////////////////////

/**
 * PROGRAM ::= STATEMENT*
 */
class ProgramNode implements ASTNode {
    private List<ASTNode> statements;

    ProgramNode(List<ASTNode> statements) { this.statements = statements; }

    public String toString() { 
        String out = "";
        for (ASTNode statement : statements) {
            out += statement + "\n";
        }
        return out.toString();
    }

    public String debugString(String prefix, String pad) {
        String out = prefix + "PROGRAM [\n";
        for (ASTNode statement : statements) {
            out += statement.debugString(prefix + pad, pad);
        }
        return out + prefix + "]\n";
    }
}

//////////////////////////////////////////////////////////////
//                         STATEMENT                        //
//////////////////////////////////////////////////////////////

/**
 * STATEMENT ::= WHILE |
 *               DO_WHILE |
 *               FOR |
 *               IF |
 *               VAR_ASSIGN |
 *               EXPR |
 *               TEXT
 */
class StatementNode implements ASTNode {
    private ASTNode node;

    StatementNode(ASTNode node) { this.node = node; }

    public String toString() {
        return node.toString();
    }

    public String debugString(String prefix, String pad) {
        String out = prefix + "STATEMENT [\n";
        out += node.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

//////////////////////////////////////////////////////////////
//                            WHILE                         //
//////////////////////////////////////////////////////////////

/**
 * WHILE ::= `While` EXPR BODY `WhileEnd`
 */
class WhileNode implements ASTNode {
    private ASTNode expr;
    private ASTNode body;

    WhileNode(ASTNode expr, ASTNode body) { this.expr = expr; this.body = body; }

    public String toString() {
        String out = "While " + expr + "\n" + body + "\n";
        return out + "WhileEnd";
    }

    public String debugString(String prefix, String pad) {
        String out = prefix + "WHILE [\n";
        out += expr.debugString(prefix + pad, pad);
        out += body.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

/**
 * DO_WHILE ::= `Do` BODY `LpWhile` EXPR
 */
class DoWhileNode implements ASTNode {
    private ASTNode expr;
    private ASTNode body;

    DoWhileNode(ASTNode expr, ASTNode body) { this.expr = expr; this.body = body; }

    public String toString() { return "Do " + body + "\nLpWhile" + expr; }

    public String debugString(String prefix, String pad) {
        String out = prefix + "DO_WHILE [\n";
        out += body.debugString(prefix + pad, pad);
        out += expr.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

//////////////////////////////////////////////////////////////
//                            FOR                           //
//////////////////////////////////////////////////////////////

/**
 * FOR ::= `For` VAR_ASSIGN `To` EXPR ( `Step` EXPR ) BODY `Next`
 */
class ForNode implements ASTNode {
    private ASTNode varAssign;
    private ASTNode toExpr;
    private ASTNode stepExpr;
    private ASTNode body;

    ForNode(ASTNode varAssign, ASTNode toExpr, ASTNode stepExpr, ASTNode body) { 
        this.varAssign = varAssign; 
        this.toExpr = toExpr; 
        this.stepExpr = stepExpr;
        this.body = body; 
    }

    public String toString() {
        String out = "For " + varAssign + " To " + toExpr;
        if (stepExpr != null) out += " Step " + stepExpr;
        out += "\n" + body + "\n";
        return out + "Next";
    }

    public String debugString(String prefix, String pad) {
        String out = prefix + "FOR [\n";
        out += varAssign.debugString(prefix + pad, pad);
        out += toExpr.debugString(prefix + pad, pad);
        if (stepExpr != null) out += stepExpr.debugString(prefix + pad, pad);
        out += body.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

/**
 * IF ::= `If` EXPR `Then` BODY ( `Else` BODY ) `IfEnd`
 */
class IfNode implements ASTNode {
    private ASTNode condition;
    private ASTNode ifBody;
    private ASTNode elseBody;

    IfNode(ASTNode condition, ASTNode ifBody, ASTNode elseBody) { 
        this.condition = condition;
        this.ifBody = ifBody;
        this.elseBody = elseBody;
    }

    public String toString() {
        String out = "If " + condition + "\nThen" + ifBody;
        if (elseBody != null) out += "\nElse " + elseBody;
        return out + "\nIfEnd";
    }

    public String debugString(String prefix, String pad) {
        String out = prefix + "IF [\n";
        out += condition.debugString(prefix + pad, pad);
        out += ifBody.debugString(prefix + pad, pad);
        if (elseBody != null) out += elseBody.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

//////////////////////////////////////////////////////////////
//                            BODY                          //
//////////////////////////////////////////////////////////////

/**
 * BODY ::= STATEMENT* BODY_END
 */
class BodyNode implements ASTNode {
    private List<ASTNode> statements;

    BodyNode(List<ASTNode> statements) { this.statements = statements; }

    public String toString() { 
        String out = "";
        for (ASTNode statement : statements) {
            out += statement + "\n";
        }
        return out.toString();
    }

    public String debugString(String prefix, String pad) {
        String out = prefix + "BODY [\n";
        for (ASTNode statement : statements) {
            out += statement.debugString(prefix + pad, pad);
        }
        return out + prefix + "]\n";
    }
}


//////////////////////////////////////////////////////////////
//                        EXPRESSION                        //
//////////////////////////////////////////////////////////////

/**
 * EXPR ::= COND
 */
class ExpressionNode implements ASTNode {
    private ASTNode node;

    ExpressionNode(ASTNode node) { this.node = node; }

    public String toString() {
        return node.toString();
    }

    public String debugString(String prefix, String pad) {
        String out = prefix + "EXPR [\n";
        out += node.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

//////////////////////////////////////////////////////////////
//                      LOGICAL OPERATOR                    //
//////////////////////////////////////////////////////////////

/**
 * LOGIC_OP ::= REL_OP |
 *              REL_OP ( `And` REL_OP )* |
 *              REL_OP ( `Or` REL_OP )* |
 *              `Not` REL_OP
 */
class LogicOpNode implements ASTNode {
    private ASTNode node;

    LogicOpNode(ASTNode node) { this.node = node; }

    public String toString() {
        return node.toString();
    }

    public String debugString(String prefix, String pad) {
        String out = prefix + "LOGIC_OP [\n";
        out += node.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

class AndNode implements ASTNode {
    private ASTNode l;
    private ASTNode r;
    
    AndNode(ASTNode l, ASTNode r) { this.l = l; this.r = r; }
    
    public String toString() {
        return "(" + l + ") and (" + r + ")";
    }
    
    public String debugString(String prefix, String pad) {
        String out = prefix + "AND [\n";
        out += l.debugString(prefix + pad, pad);
        out += r.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

class OrNode implements ASTNode {
    private ASTNode l;
    private ASTNode r;
    
    OrNode(ASTNode l, ASTNode r) { this.l = l; this.r = r; }
    
    public String toString() {
        return "(" + l + ") or (" + r + ")";
    }
    
    public String debugString(String prefix, String pad) {
        String out = prefix + "OR [\n";
        out += l.debugString(prefix + pad, pad);
        out += r.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

class NotNode implements ASTNode {
    private ASTNode node;
    
    NotNode(ASTNode node) { this.node = node; }
    
    public String toString() {
        return "not (" + node + ")";
    }
    
    public String debugString(String prefix, String pad) {
        String out = prefix + "NOT [\n";
        out += node.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

//////////////////////////////////////////////////////////////
//                      RELATIVE OPERATOR                   //
//////////////////////////////////////////////////////////////

/**
 * REL_OP ::= SUM |
 *            SUM ( `<` SUM )* |
 *            SUM ( `>` SUM )* |
 *            SUM ( `≤` SUM )* |
 *            SUM ( `≥` SUM )* |
 *            SUM ( `=` SUM )*
 *            SUM ( `≠` SUM )*
 */
class RelativeOpNode implements ASTNode {
    private ASTNode node;

    RelativeOpNode(ASTNode node) { this.node = node; }

    public String toString() {
        return node.toString();
    }

    public String debugString(String prefix, String pad) {
        String out = prefix + "RELATIVE_OP [\n";
        out += node.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

class LessThanNode implements ASTNode {
    private ASTNode l;
    private ASTNode r;
    
    LessThanNode(ASTNode l, ASTNode r) { this.l = l; this.r = r; }
    
    public String toString() {
        return "(" + l + ") < (" + r + ")";
    }
    
    public String debugString(String prefix, String pad) {
        String out = prefix + "LESS_THAN [\n";
        out += l.debugString(prefix + pad, pad);
        out += r.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

class GreaterThanNode implements ASTNode {
    private ASTNode l;
    private ASTNode r;
    
    GreaterThanNode(ASTNode l, ASTNode r) { this.l = l; this.r = r; }
    
    public String toString() {
        return "(" + l + ") > (" + r + ")";
    }
    
    public String debugString(String prefix, String pad) {
        String out = prefix + "GREATER_THAN [\n";
        out += l.debugString(prefix + pad, pad);
        out += r.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

class LessThanEqNode implements ASTNode {
    private ASTNode l;
    private ASTNode r;
    
    LessThanEqNode(ASTNode l, ASTNode r) { this.l = l; this.r = r; }
    
    public String toString() {
        return "(" + l + ") ≤ (" + r + ")";
    }
    
    public String debugString(String prefix, String pad) {
        String out = prefix + "LESS_THAN_EQ [\n";
        out += l.debugString(prefix + pad, pad);
        out += r.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

class GreaterThanEqNode implements ASTNode {
    private ASTNode l;
    private ASTNode r;
    
    GreaterThanEqNode(ASTNode l, ASTNode r) { this.l = l; this.r = r; }
    
    public String toString() {
        return "(" + l + ") ≥ (" + r + ")";
    }
    
    public String debugString(String prefix, String pad) {
        String out = prefix + "GREATER_THAN_EQ [\n";
        out += l.debugString(prefix + pad, pad);
        out += r.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

class EqualToNode implements ASTNode {
    private ASTNode l;
    private ASTNode r;
    
    EqualToNode(ASTNode l, ASTNode r) { this.l = l; this.r = r; }
    
    public String toString() {
        return "(" + l + ") = (" + r + ")";
    }
    
    public String debugString(String prefix, String pad) {
        String out = prefix + "EQUAL_TO [\n";
        out += l.debugString(prefix + pad, pad);
        out += r.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

class NotEqualToNode implements ASTNode {
    private ASTNode l;
    private ASTNode r;
    
    NotEqualToNode(ASTNode l, ASTNode r) { this.l = l; this.r = r; }
    
    public String toString() {
        return "(" + l + ") ≠ (" + r + ")";
    }
    
    public String debugString(String prefix, String pad) {
        String out = prefix + "NOT_EQUAL_TO [\n";
        out += l.debugString(prefix + pad, pad);
        out += r.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

//////////////////////////////////////////////////////////////
//                            SUM                           //
//////////////////////////////////////////////////////////////

/**
 * SUM ::= TERM |
 *         TERM ( `+` TERM )* |
 *         TERM ( `-` TERM )*
 */
class SumNode implements ASTNode {
    private ASTNode node;

    SumNode(ASTNode node) { this.node = node; }

    public String toString() {
        return node.toString();
    }

    public String debugString(String prefix, String pad) {
        String out = prefix + "SUM [\n";
        out += node.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

class PlusOpNode implements ASTNode {
    private ASTNode l;
    private ASTNode r;

    PlusOpNode(ASTNode l, ASTNode r) { this.l = l; this.r = r; }

    public String toString() {
        return "(" + l + ") + (" + r + ")";
    }

    public String debugString(String prefix, String pad) {
        String out = prefix + "PLUS_OP [\n";
        out += l.debugString(prefix + pad, pad);
        out += r.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

class MinusOpNode implements ASTNode {
    private ASTNode l;
    private ASTNode r;

    MinusOpNode(ASTNode l, ASTNode r) { this.l = l; this.r = r; }

    public String toString() {
        return "(" + l + ") - (" + r + ")";
    }

    public String debugString(String prefix, String pad) {
        String out = prefix + "MINUS_OP [\n";
        out += l.debugString(prefix + pad, pad);
        out += r.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

//////////////////////////////////////////////////////////////
//                            TERM                          //
//////////////////////////////////////////////////////////////

/**
 * TERM ::= FACTOR |
 *          FACTOR ( `✕` FACTOR )* |
 *          FACTOR ( `÷` FACTOR )* |
 */
class TermNode implements ASTNode {
    private ASTNode node;

    TermNode(ASTNode node) { this.node = node; }

    public String toString() {
        return node.toString();
    }

    public String debugString(String prefix, String pad) {
        String out = prefix + "TERM [\n";
        out += node.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

class MultiplyOpNode implements ASTNode {
    private ASTNode l;
    private ASTNode r;

    MultiplyOpNode(ASTNode l, ASTNode r) { this.l = l; this.r = r; }

    public String toString() {
        return "(" + l + ") ✕ (" + r + ")";
    }

    public String debugString(String prefix, String pad) {
        String out = prefix + "MULTIPLY_OP [\n";
        out += l.debugString(prefix + pad, pad) + "\n";
        out += r.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

class DivideOpNode implements ASTNode {
    private ASTNode l;
    private ASTNode r;

    DivideOpNode(ASTNode l, ASTNode r) { this.l = l; this.r = r; }

    public String toString() {
        return "(" + l + ") ÷ (" + r + ")";
    }

    public String debugString(String prefix, String pad) {
        String out = prefix + "DIVIDE_OP [\n";
        out += l.debugString(prefix + pad, pad) + "\n";
        out += r.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

//////////////////////////////////////////////////////////////
//                          FACTOR                          //
//////////////////////////////////////////////////////////////

/**
 * FACTOR ::= `+` FACTOR |
 *            `-` FACTOR |
 *            `(` EXPR ")" |
 *            NUMBER |
 *            VAR_NAME |
 *            `Getkey`
 */
class FactorNode implements ASTNode {
    private ASTNode node;

    FactorNode(ASTNode node) { this.node = node; }

    public String toString() {
        return node.toString();
    }

    public String debugString(String prefix, String pad) {
        String out = prefix + "FACTOR [\n";
        out += node.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

class PlusUnaryOpNode implements ASTNode {
    private ASTNode body;

    PlusUnaryOpNode(ASTNode body) { this.body = body; }

    public String toString() {
        return "+" + body;
    }

    public String debugString(String prefix, String pad) {
        String out = prefix + "PLUS_UNARY_OP [\n";
        out += body.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

class MinusUnaryOpNode implements ASTNode {
    private ASTNode body;

    MinusUnaryOpNode(ASTNode body) { this.body = body; }

    public String toString() {
        return "-" + body;
    }

    public String debugString(String prefix, String pad) {
        String out = prefix + "MINUS_UNARY_OP [\n";
        out += body.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

//////////////////////////////////////////////////////////////
//                          VARIABLES                       //
//////////////////////////////////////////////////////////////

/**
 * VAR_ASSIGN ::= EXPR `->` VAR_NAME
 * VAR_NAME ::= [A-Z]
 */
class VarAssignNode implements ASTNode {
    private ASTNode expr;
    private char varName;

    VarAssignNode(ASTNode expr, char varName) { this.expr = expr; this.varName = varName; }

    public String toString() {
        return expr + " -> " + varName;
    }

    public String debugString(String prefix, String pad) {
        String out = prefix + "VAR_ASSIGN [\n";
        out += expr.debugString(prefix + pad, pad);
        out += prefix + pad + varName + "\n";
        return out + prefix + "]\n";
    }
}

/**
 * VAR_EVALUATE ::= VAR_NAME
 */
class VarEvaluateNode implements ASTNode {
    private char varName;

    VarEvaluateNode(char varName) { this.varName = varName; }

    public String toString() {
        return varName + "";
    }

    public String debugString(String prefix, String pad) {
        String out = prefix + "VAR_EVAL [\n";
        out += prefix + pad + varName + "\n";
        return out + prefix + "]\n";
    }
}

//////////////////////////////////////////////////////////////
//                            IO                            //
//////////////////////////////////////////////////////////////

/**
 * GET_KEY ::= `GetKey`
 */
class GetKeyNode implements ASTNode {
    public String toString() {
        return "GetKey";
    }

    public String debugString(String prefix, String pad) {
        return prefix + "GET_KEY\n";
    }
}

/**
 * LOCATE ::= `Locate` EXPR `,` EXPR `,` TEXT
 *            `Locate` EXPR `,` EXPR `,` EXPR
 */
class LocateNode implements ASTNode {
    private ASTNode x;
    private ASTNode y;
    private ASTNode value;

    LocateNode(ASTNode x, ASTNode y, ASTNode value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public String toString() { return "Locate " + x + ", " + y + ", " + value; }

    public String debugString(String prefix, String pad) {
        String out = prefix + "LOCATE [\n";
        out += x.debugString(prefix + pad, pad);
        out += y.debugString(prefix + pad, pad);
        out += value.debugString(prefix + pad, pad);
        return out + prefix + "]\n";
    }
}

//////////////////////////////////////////////////////////////
//                          NUMBER                          //
//////////////////////////////////////////////////////////////

/**
 * NUMBER ::= [0-9]+
 */
class NumberNode implements ASTNode {
    private int value;

    NumberNode(int value) { this.value = value; }

    public String toString() {
        return value + "";
    }

    public String debugString(String prefix, String pad) {
        String out = prefix + "NUMBER [\n";
        out += prefix + pad + toString() + "\n";
        return out + prefix + "]\n";
    }
}

//////////////////////////////////////////////////////////////
//                            TEXT                          //
//////////////////////////////////////////////////////////////

/**
 * TEXT ::= `"` .* `"`
 */
class TextNode implements ASTNode {
    private String value;

    TextNode(String value) { this.value = value; }

    public String toString() {
        return "\"" + value + "\"";
    }

    public String debugString(String prefix, String pad) {
        String out = prefix + "TEXT [\n";
        out += prefix + pad + toString() + "\n";
        return out + prefix + "]\n";
    }
}
