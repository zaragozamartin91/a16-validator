package ast.cts.ws.xsd;

import ast.cts.ws.util.XpathBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class XsdReader {
	private static final XPath XPATH = XpathBuilder.INSTANCE.build();
	private final Document doc;

	public static XsdReader fromFile(File xsdFile) throws IOException, SAXException, ParserConfigurationException {
		return new XsdReader(xsdFile);
	}

	public static XsdReader fromStream(InputStream input) throws IOException, SAXException, ParserConfigurationException {
		return new XsdReader(input);
	}

	private XsdReader(File xsdFile) throws ParserConfigurationException, IOException, SAXException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		doc = builder.parse(xsdFile);
	}

	private XsdReader(InputStream input) throws ParserConfigurationException, IOException, SAXException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		doc = builder.parse(input);
	}

	public boolean validateElement(String typeName, String elementName, String elementType) throws XPathExpressionException {
		XPathExpression expr = XPATH
				.compile(String.format("//complexType[@name='%s']/sequence/element[@name='%s'][@type='%s']", typeName, elementName, elementType));
		NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

		return nodes.getLength() == 1;
	}

	public boolean typeExists(String typeName) {
		try {
			XPathExpression expr = XPATH.compile(String.format("/schema/complexType[@name='%s']", typeName));
			NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

			return nodes.getLength() >= 1;
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}

	public XsdElement getElement(String typeName, String elementName) throws XPathExpressionException {
		XPathExpression expr = XPATH
				.compile(String.format("//complexType[@name='%s']/sequence/element[@name='%s']", typeName, elementName));
		NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

		return nodes.getLength() == 0 ? RegularXsdElement.VOID : new RegularXsdElement(nodes.item(0));
	}
}
