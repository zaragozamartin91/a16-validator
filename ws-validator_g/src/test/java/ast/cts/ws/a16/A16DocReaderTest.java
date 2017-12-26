package ast.cts.ws.a16;

import org.junit.Test;

import java.io.InputStream;

public class A16DocReaderTest {
	@Test
	public void readA16Txt() throws Exception {
		InputStream fileStream = A16DocReaderTest.class.getClassLoader().getResourceAsStream("SegundoFiltro-input.txt");

		A16DocReader a16DocReader = new A16DocReader(1, 2, "\t", "SegundoFiltroFil", "SegundoFiltroRes", true);
		A16Doc a16Doc = a16DocReader.readA16Txt(fileStream);

	}

}