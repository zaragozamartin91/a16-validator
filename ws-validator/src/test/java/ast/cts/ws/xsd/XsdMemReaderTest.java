package ast.cts.ws.xsd;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

public class XsdMemReaderTest {

	@BeforeClass
	public static void beforeAll() {
	}

	private static InputStream getSimpleXsdStream() {
		return XsdMemReaderTest.class.getClassLoader().getResourceAsStream("cobiscorp.ecobis.consultagruposriesgo.dto.xsd");
	}

	private static InputStream getComplexXsdStream() {
		return XsdMemReaderTest.class.getClassLoader().getResourceAsStream("SegundoFiltro_schema1.xsd");
	}

	@Test
	public void testGetElement() throws Exception {
		XsdMemReader xsdReader = XsdMemReader.fromStream(getComplexXsdStream());

		XsdElement simpleElement = xsdReader.getElement("SegundoFiltroFil", "iDatosTitular");
		assertFalse(simpleElement.isVoid());
		assertFalse(simpleElement.isComplex());

		XsdElement nonExistentChild = simpleElement.getComplexChild();
		assertEquals(RegularXsdElement.VOID, nonExistentChild);
		assertTrue(nonExistentChild.getType().isEmpty());

		XsdElement complexElement = xsdReader.getElement("SegundoFiltroFil", "iArrayLineas");
		assertFalse(complexElement.isVoid());
		assertTrue(complexElement.isComplex());
		assertTrue(complexElement.getType().isEmpty());
		assertTrue(complexElement.hasType("IarrayLineas", "tns"));

		XsdElement complexChild = complexElement.getComplexChild();
		assertFalse(complexChild.isVoid());
		assertFalse(complexChild.isComplex());

		assertEquals("IarrayLineas", complexChild.getType("tns"));
		assertEquals("IarrayLineas", complexChild.getType("tns:"));
	}

	@Test
	public void testTypeExistsLenient() throws Exception {
		XsdMemReader xsdReader = XsdMemReader.fromStream(getComplexXsdStream(), true);
		assertTrue(xsdReader.typeExists("segundofiltrofil"));
	}

	@Test
	public void testGetElementLenient() throws Exception {
		XsdMemReader xsdReader = XsdMemReader.fromStream(getComplexXsdStream(), true, true);
		{
			XsdElement element = xsdReader.getElement("segundofiltrofil", "idAplicacion".toLowerCase());
			assertFalse(element.isVoid());
		}

		{
			XsdElement element = xsdReader.getElement("segundofiltrofil", "inEvaluacion".toLowerCase());
			assertFalse(element.isVoid());
		}

		{
			XsdElement element = xsdReader.getElement("segundofiltrofil", "ikIntegrantes".toLowerCase());
			assertFalse(element.isVoid());
		}

		{
			XsdElement element = xsdReader.getElement("segundofiltrofil", "iArrayAdicionalTexto".toLowerCase());
			assertFalse(element.isVoid());
		}
	}
}