package ast.cts.ws.a16;

import org.junit.Test;

import java.io.InputStream;

public class A16ServiceDocReaderTest {
	@Test
	public void readA16Txt() throws Exception {
		InputStream fileStream = A16ServiceDocReaderTest.class.getClassLoader().getResourceAsStream("SegundoFiltro-input.txt");

		A16ServiceDocReader a16ServiceDocReader = new A16ServiceDocReader(1, 2, "\t", "SegundoFiltroFil", "SegundoFiltroRes", 1);
		A16ServiceDoc a16ServiceDoc = a16ServiceDocReader.readA16Txt(fileStream);
	}

}