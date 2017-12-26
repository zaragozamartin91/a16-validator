package ast.cts.ws.util;

import org.junit.Test;

public class ConsoleWriterTest {
	@Test
	public void println() throws Exception {
		ConsoleWriter.RED.println("hello world");

		ConsoleWriter.WHITE.print("Error:");
		ConsoleWriter.PURPLE.print("Problema grave");
	}

}