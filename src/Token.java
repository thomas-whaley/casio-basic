public class Token {
    private TokenType tokenType;
    private TokenLocation loc;
    private Object value = null;

    Token(TokenType tokenType, TokenLocation loc) {
        this.tokenType = tokenType;
        this.loc = loc;
    }

    Token(TokenType tokenType, TokenLocation loc, Object value) {
        this(tokenType, loc);
        this.value = value;
    }

    public TokenType getType() { return tokenType; }
    public TokenLocation getLoc() { return loc; }
    public Object getVal() { return value; }

    public String toString() {
        if (this.value != null) return "Token(type=" + tokenType + ", loc=" + loc + ", val=" + value + ")";
        return "Token(type=" + tokenType + ", loc=" + loc + ")";
    }
}


