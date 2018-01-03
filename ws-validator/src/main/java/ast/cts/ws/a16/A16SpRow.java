package ast.cts.ws.a16;

public class A16SpRow {
    public final String left;
    public final String right;
    public final String type;

    public A16SpRow(String left, String right) {
        this.left = left;
        this.right = right;
        this.type = "";
    }

    public A16SpRow(String left, String right, String type) {
        this.left = left;
        this.right = right;
        this.type = type;
    }

    public boolean hasType() { return type != null && !type.isEmpty(); }

    @Override
    public String toString() {
        return "A16SpRow{" +
                "left='" + left + '\'' +
                ", right='" + right + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
