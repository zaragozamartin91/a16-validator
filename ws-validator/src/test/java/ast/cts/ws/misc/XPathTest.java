package ast.cts.ws.misc;

import ast.cts.ws.util.XpathBuilder;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;

import static junit.framework.TestCase.assertNotNull;

public class XPathTest {
	@Test
	public void testReadIgnoreCase() throws Exception {
		XPath xPath = XpathBuilder.INSTANCE.build();
		XPathExpression expr = xPath
				.compile("/schema/complexType[translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcedfghijklmnopqrstuvwxyz')='segundofiltrofil']");

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(XPathTest.class.getClassLoader().getResourceAsStream("SegundoFiltro_schema1.xsd"));

		Object node = expr.evaluate(doc, XPathConstants.NODE);
		assertNotNull(node);
	}
}
