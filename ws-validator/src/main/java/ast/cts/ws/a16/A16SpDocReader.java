package ast.cts.ws.a16;

import ast.cts.ws.util.StringStandardizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

public class A16SpDocReader {
    private int leftCol;
    private int rightCol;
    private int typeCol;
    private String colDelim;

    public A16SpDocReader(int leftCol, int rightCol, int typeCol, String colDelim) {
        this.leftCol = leftCol;
        this.rightCol = rightCol;
        this.typeCol = typeCol;
        this.colDelim = colDelim;
    }

    public A16SpDoc readA16Txt(InputStream fileStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileStream, StandardCharsets.UTF_8));

        A16SpDoc a16SpDoc = new A16SpDoc();

        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) { break; }
            line = line.trim();
            if (line.isEmpty()) { continue; }

            A16SpTable table = readTable(line, bufferedReader);
            a16SpDoc.addTable(table);
        }

        return a16SpDoc;
    }

    private A16SpTable readTable(String titleLine, BufferedReader bufferedReader) throws IOException {
        String spName = titleLine;
        String stype = bufferedReader.readLine().trim();
        A16SpTable table = new A16SpTable(spName, stype);
        A16SpTable.Type tableType = table.getType();

        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) { return table; }

            line = line.trim();
            if (line.isEmpty()) { return table; }

            line = line.replaceAll(colDelim + "+", ",");
            String[] split = line.split(Pattern.quote(","));

            A16SpRow row = buildRow(tableType, split);
            table.addRow(row);
        }
    }

    private A16SpRow buildRow(A16SpTable.Type tableType, String[] split) {
        switch (tableType) {
            case RESULTSET:
                return buildResultsetRow(split);
            case OUTPUT:
                return buildOutputRow(split);
            case INPUT:
                return buildInputRow(split);
        }
        return null;
    }

    private A16SpRow buildInputRow(String[] split) {
        return new A16SpRow(split[leftCol].trim(), split[rightCol].trim(), split[typeCol].trim());
    }

    private A16SpRow buildOutputRow(String[] split) {
        return new A16SpRow(split[leftCol].trim(), split[rightCol].trim());
    }

    private A16SpRow buildResultsetRow(String[] split) {
        return new A16SpRow(split[leftCol].trim(), split[rightCol].trim());
    }
}
