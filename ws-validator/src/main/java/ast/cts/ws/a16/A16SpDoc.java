package ast.cts.ws.a16;

import java.util.*;

public class A16SpDoc {
    private List<A16SpTable> inputTables = new ArrayList<>();
    private List<A16SpTable> outputTables = new ArrayList<>();
    private List<A16SpTable> resultsetTables = new ArrayList<>();

    public boolean addTable(A16SpTable table) {
        A16SpTable.Type tableType = table.getType();
        switch (tableType) {
            case INPUT:
                return inputTables.add(table);
            case OUTPUT:
                return outputTables.add(table);
            case RESULTSET:
                return resultsetTables.add(table);
        }
        return false;
    }

    public List<A16SpTable> getInputTables() {
        return inputTables;
    }

    public List<A16SpTable> getOutputTables() {
        return outputTables;
    }

    public List<A16SpTable> getResultsetTables() {
        return resultsetTables;
    }
}
