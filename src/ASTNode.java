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
        return out + "\n" + prefix + "]\n";
    }
}

//////////////////////////////////////////////////////////////
//                         STATEMENT                        //
//////////////////////////////////////////////////////////////

/**
 * STATEMENT ::= EXPR
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
        return out + "\n" + prefix + "]\n";
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
        return out + "\n" + prefix + "]\n";
    }
}

//////////////////////////////////////////////////////////////
//                      LOGICAL OPERATOR                    //
//////////////////////////////////////////////////////////////

/**
 * LOGIC_OP ::= REL_OP |
 *              REL_OP ( AND REL_OP )* |
 *              REL_OP ( OR REL_OP )* |
 *              NOT REL_OP
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
        return out + "\n" + prefix + "]\n";
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
        out += l.debugString(prefix + pad, pad) + "\n";
        out += r.debugString(prefix + pad, pad);
        return out + "\n" + prefix + "]\n";
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
        out += l.debugString(prefix + pad, pad) + "\n";
        out += r.debugString(prefix + pad, pad);
        return out + "\n" + prefix + "]\n";
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
        return out + "\n" + prefix + "]\n";
    }
}

//////////////////////////////////////////////////////////////
//                      RELATIVE OPERATOR                   //
//////////////////////////////////////////////////////////////

/**
 * REL_OP ::= SUM |
 *            SUM ( L_THAN SUM )* |
 *            SUM ( G_THAN SUM )* |
 *            SUM ( EQ_TO SUM )*
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
        return out + "\n" + prefix + "]\n";
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
        out += l.debugString(prefix + pad, pad) + "\n";
        out += r.debugString(prefix + pad, pad);
        return out + "\n" + prefix + "]\n";
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
        out += l.debugString(prefix + pad, pad) + "\n";
        out += r.debugString(prefix + pad, pad);
        return out + "\n" + prefix + "]\n";
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
        out += l.debugString(prefix + pad, pad) + "\n";
        out += r.debugString(prefix + pad, pad);
        return out + "\n" + prefix + "]\n";
    }
}

//////////////////////////////////////////////////////////////
//                            SUM                           //
//////////////////////////////////////////////////////////////

/**
 * SUM ::= TERM |
 *         TERM ( PLUS TERM )* |
 *         TERM ( MINUS TERM )*
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
        return out + "\n" + prefix + "]\n";
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
        out += l.debugString(prefix + pad, pad) + "\n";
        out += r.debugString(prefix + pad, pad);
        return out + "\n" + prefix + "]\n";
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
        out += l.debugString(prefix + pad, pad) + "\n";
        out += r.debugString(prefix + pad, pad);
        return out + "\n" + prefix + "]\n";
    }
}

//////////////////////////////////////////////////////////////
//                            TERM                          //
//////////////////////////////////////////////////////////////

/**
 * TERM ::= FACTOR |
 *          FACTOR ( MULTIPLY FACTOR )* |
 *          FACTOR ( DIVIDE FACTOR )* |
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
        return out + "\n" + prefix + "]\n";
    }
}

class MultiplyOpNode implements ASTNode {
    private ASTNode l;
    private ASTNode r;

    MultiplyOpNode(ASTNode l, ASTNode r) { this.l = l; this.r = r; }

    public String toString() {
        return "(" + l + ") * (" + r + ")";
    }

    public String debugString(String prefix, String pad) {
        String out = prefix + "MULTIPLY_OP [\n";
        out += l.debugString(prefix + pad, pad) + "\n";
        out += r.debugString(prefix + pad, pad);
        return out + "\n" + prefix + "]\n";
    }
}

class DivideOpNode implements ASTNode {
    private ASTNode l;
    private ASTNode r;

    DivideOpNode(ASTNode l, ASTNode r) { this.l = l; this.r = r; }

    public String toString() {
        return "(" + l + ") / (" + r + ")";
    }

    public String debugString(String prefix, String pad) {
        String out = prefix + "DIVIDE_OP [\n";
        out += l.debugString(prefix + pad, pad) + "\n";
        out += r.debugString(prefix + pad, pad);
        return out + "\n" + prefix + "]\n";
    }
}

//////////////////////////////////////////////////////////////
//                          FACTOR                          //
//////////////////////////////////////////////////////////////

/**
 * FACTOR ::= PLUS FACTOR |
 *            MINUS FACTOR |
 *            LPAREN EXPR RPAREN |
 *            NUMBER
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
        return out + "\n" + prefix + "]\n";
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
        return out + "\n" + prefix + "]\n";
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
        return out + "\n" + prefix + "]\n";
    }
}

//////////////////////////////////////////////////////////////
//                          VARIABLES                       //
//////////////////////////////////////////////////////////////

/**
 * VAR_ASSIGN ::= EXPR ASSIGN VAR_NAME
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
        out += expr.debugString(prefix + pad, pad) + "\n";
        out += prefix + pad + varName;
        return out + "\n" + prefix + "]\n";
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
        out += prefix + pad + toString();
        return out + "\n" + prefix + "]\n";
    }
}
