package ast.cts.ws.a16;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class A16SpTable {
    public static enum Type {
        INPUT, OUTPUT, RESULTSET
    }

    private String spName;
    private Type type;
    private List<A16SpRow> rows = new ArrayList<>();

    public A16SpTable(String spName, Type type) {
        this.spName = spName;
        this.type = type;
    }

    public A16SpTable(String spName, String type) {
        this(spName, Type.valueOf(type.trim().toUpperCase()));
    }

    public boolean addRow(A16SpRow a16SpRow) {return rows.add(a16SpRow);}

    public String getSpName() { return spName; }

    public Type getType() { return type; }

    public List<A16SpRow> getRows() { return new ArrayList<>(rows); }

    public void forEachRow(Consumer<A16SpRow> consumer) { this.getRows().forEach(consumer); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        A16SpTable that = (A16SpTable) o;

        if (spName != null ? !spName.equals(that.spName) : that.spName != null) return false;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        int result = spName != null ? spName.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "A16SpTable{" +
                "spName='" + spName + '\'' +
                ", type=" + type +
                ", rows=" + rows +
                '}';
    }
}
