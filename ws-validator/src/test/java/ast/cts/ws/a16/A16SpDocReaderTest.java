package ast.cts.ws.a16;

import ast.cts.ws.config.ColDelim;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

public class A16SpDocReaderTest {
    @Test
    public void readA16Txt() throws Exception {
        A16SpDocReader docReader = new A16SpDocReader(0, 1, 2, ColDelim.TAB.delim);
        InputStream inputStream = A16SpDocReaderTest.class.getClassLoader().getResourceAsStream("BVConsultaPreaprobadoCAModo1Service.txt");
        A16SpDoc a16SpDoc = docReader.readA16Txt(inputStream);

        List<A16SpTable> inputTables = a16SpDoc.getInputTables();
        assertEquals(1, inputTables.size());

        A16SpTable inputTable = inputTables.get(0);
        assertEquals(9, inputTable.getRows().size());

        List<A16SpTable> outputTables = a16SpDoc.getOutputTables();
        assertEquals(1, outputTables.size());
    }

}