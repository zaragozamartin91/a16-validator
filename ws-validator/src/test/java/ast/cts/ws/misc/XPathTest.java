package ast.cts.ws.misc;

import ast.cts.ws.util.XpathBuilder;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

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

	@Test
	public void testGetAllComplexTypes() throws XPathExpressionException, ParserConfigurationException, IOException, SAXException {
		XPath xPath = XpathBuilder.INSTANCE.build();
		XPathExpression expr = xPath.compile("/schema/complexType");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(XPathTest.class.getClassLoader().getResourceAsStream("SegundoFiltro_schema1.xsd"));

		NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		System.out.println(nodes.getLength());

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				System.out.println(element.getAttribute("name"));
			}
		}
	}

	@Test
	public void testGetSingleElement() throws XPathExpressionException, ParserConfigurationException, IOException, SAXException {
		XPath xPath = XpathBuilder.INSTANCE.build();
		XPathExpression expr = xPath.compile("/schema/complexType");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(XPathTest.class.getClassLoader().getResourceAsStream("SegundoFiltro_schema1.xsd"));

		NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		System.out.println(nodes.getLength());

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE && node.getAttributes().getNamedItem("name").getTextContent().equalsIgnoreCase("SegundoFiltroFil")) {
				Element element = (Element) node;
				System.out.println(element.getAttribute("name"));

				XPathExpression sexpr = xPath.compile("sequence/element");
				NodeList children = (NodeList) sexpr.evaluate(element, XPathConstants.NODESET);

				for (int j = 0; j < children.getLength(); j++) {
					Node child = children.item(j);
					if (child.getNodeType() == Node.ELEMENT_NODE) {
						System.out.println(child.getAttributes().getNamedItem("name").getTextContent());
					}
				}

			}
		}
	}
}
