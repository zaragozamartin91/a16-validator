package ast.cts.ws.xsd;

import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class XsdReaderTest {

	@BeforeClass
	public static void beforeAll() {
	}

	private static InputStream getSimpleXsdStream() {
		return XsdReaderTest.class.getClassLoader().getResourceAsStream("cobiscorp.ecobis.consultagruposriesgo.dto.xsd");
	}

	private static InputStream getComplexXsdStream() {
		return XsdReaderTest.class.getClassLoader().getResourceAsStream("SegundoFiltro_schema1.xsd");
	}

	@Test
	public void testReadXsdWithValidator() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
		XsdReader xsdValidator = XsdReader.fromStream(getSimpleXsdStream());

		boolean isvalid = xsdValidator.validateElement("ConsultaGrupoRiesgoFil", "iCtipoIdTributario", "string");
		assertTrue(isvalid);
	}

	@Test
	public void testGetElement() throws Exception {
		XsdReader xsdReader = XsdReader.fromStream(getComplexXsdStream());

		XsdElement element1 = xsdReader.getElement("SegundoFiltroFil", "iDatosTitular");
		assertFalse(element1.isVoid());
		assertFalse(element1.isComplex());

		XsdElement element2 = xsdReader.getElement("SegundoFiltroFil", "iArrayLineas");
		assertFalse(element2.isVoid());
		assertTrue(element2.isComplex());

		XsdElement element2ComplexChild = element2.getComplexChild();
		assertFalse(element2ComplexChild.isVoid());
		assertFalse(element2ComplexChild.isComplex());

		assertEquals("IarrayLineas", element2ComplexChild.getType("tns"));
		assertEquals("IarrayLineas", element2ComplexChild.getType("tns:"));
	}
}