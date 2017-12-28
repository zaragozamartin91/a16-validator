package ast.cts.ws.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringStandardizerTest {
	@Test
	public void capitalize() throws Exception {
		assertEquals("Hola como estas", StringStandardizer.INSTANCE.capitalize("hola como estas"));
		assertEquals("", StringStandardizer.INSTANCE.capitalize(""));
		assertEquals("P", StringStandardizer.INSTANCE.capitalize("p"));
	}

	@Test
	public void deCapitalize() throws Exception {
		assertEquals("hola como estas", StringStandardizer.INSTANCE.deCapitalize("Hola como estas"));
		assertEquals("", StringStandardizer.INSTANCE.deCapitalize(""));
		assertEquals("p", StringStandardizer.INSTANCE.deCapitalize("P"));
	}

}