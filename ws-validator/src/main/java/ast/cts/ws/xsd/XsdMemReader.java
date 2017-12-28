package ast.cts.ws.xsd;

import ast.cts.ws.util.XpathBuilder;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class XsdMemReader implements XsdReader {
	private static final XPath XPATH = XpathBuilder.INSTANCE.build();

	private final Document doc;
	private boolean lenientTypeName = false;
	private boolean lenientElementName = false;

	private List<Element> complexTypes = new ArrayList<>();

	private XsdMemReader(InputStream input) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		doc = builder.parse(input);

		XPathExpression expr = XPATH.compile("/schema/complexType");
		NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (isElement(node)) { complexTypes.add((Element) node); }
		}
	}

	public static XsdMemReader fromStream(InputStream input) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
		return new XsdMemReader(input);
	}

	public static XsdMemReader fromStream(InputStream input, boolean lenientTypeName)
			throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
		XsdMemReader xsdReader = fromStream(input);
		xsdReader.lenientTypeName = lenientTypeName;
		return xsdReader;
	}

	public static XsdMemReader fromStream(InputStream input, boolean lenientTypeName, boolean lenientElementName)
			throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
		XsdMemReader xsdReader = fromStream(input, lenientTypeName);
		xsdReader.lenientElementName = lenientElementName;
		return xsdReader;
	}

	public boolean typeExists(String typeName) {
		return lenientTypeName ?
				complexTypes.stream().anyMatch(ct -> ct.getAttribute("name").equalsIgnoreCase(typeName)) :
				complexTypes.stream().anyMatch(ct -> ct.getAttribute("name").equals(typeName));
	}

	public XsdElement getElement(String typeName, String elementName) throws XPathExpressionException {
		List<Element> complexTypes = lenientTypeName ?
				this.complexTypes.stream().filter(element -> element.getAttribute("name").equalsIgnoreCase(typeName)).collect(Collectors.toList()) :
				this.complexTypes.stream().filter(element -> element.getAttribute("name").equals(typeName)).collect(Collectors.toList());

		if (complexTypes.isEmpty()) { return RegularXsdElement.VOID; }

		Element complexType = complexTypes.get(0);

		XPathExpression elemExpr = XPATH.compile("sequence/element");
		NodeList elements = (NodeList) elemExpr.evaluate(complexType, XPathConstants.NODESET);
		for (int i = 0; i < elements.getLength(); i++) {
			Node node = elements.item(i);

			if (isElement(node) && getName(node).equals(elementName)) {
				return new RegularXsdElement(node);
			}

			if (isElement(node) && lenientElementName && getName(node).equalsIgnoreCase(elementName)) {
				return new RegularXsdElement(node);
			}

		}

		return RegularXsdElement.VOID;
	}

	private boolean isElement(Node node) {
		return node.getNodeType() == Node.ELEMENT_NODE;
	}

	private String getName(Node node) {
		return node.getAttributes().getNamedItem("name").getTextContent();
	}
}