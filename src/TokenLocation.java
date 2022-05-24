public class TokenLocation {
    String fileName;
    int lineNum;
    int column;

    TokenLocation(String fileName, int lineNum, int column) {
        this.fileName = fileName;
        this.lineNum = lineNum;
        this.column = column;
    }

    public String toString() {
        return "\"" + fileName + "\":" + lineNum + ":" + column;
    }
}
