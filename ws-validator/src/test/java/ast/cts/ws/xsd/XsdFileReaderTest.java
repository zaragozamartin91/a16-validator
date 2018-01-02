package ast.cts.ws.xsd;

import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class XsdFileReaderTest {

	@BeforeClass
	public static void beforeAll() {
	}

	private static InputStream getSimpleXsdStream() {
		return XsdFileReaderTest.class.getClassLoader().getResourceAsStream("cobiscorp.ecobis.consultagruposriesgo.dto.xsd");
	}

	private static InputStream getComplexXsdStream() {
		return XsdFileReaderTest.class.getClassLoader().getResourceAsStream("SegundoFiltro_schema1.xsd");
	}

	@Test
	public void testReadXsdWithValidator() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
		XsdFileReader xsdValidator = XsdFileReader.fromStream(getSimpleXsdStream());

		boolean isvalid = xsdValidator.validateElement("ConsultaGrupoRiesgoFil", "iCtipoIdTributario", "string");
		assertTrue(isvalid);
	}

	@Test
	public void testGetElement() throws Exception {
		XsdReader xsdReader = XsdFileReader.fromStream(getComplexXsdStream());

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

}