package ast.cts.ws.xsd;

import ast.cts.ws.util.XpathBuilder;
import org.w3c.dom.Node;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import java.util.regex.Pattern;

/**
 * Elemento regular de xsd.
 */
public class RegularXsdElement implements XsdElement {
	public static final XsdElement VOID = VoidXsdElement.getInstance();
	private static XPath XPATH = XpathBuilder.INSTANCE.build();
	private Node node;

	public RegularXsdElement(Node node) {
		this.node = node;
	}

	@Override public String getType() {
		try {
			XPathExpression expr = XPATH.compile("@type");
			String elemType = (String) expr.evaluate(node, XPathConstants.STRING);

			return elemType;
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}

	@Override public String getType(String typePrefix) {
		if (typePrefix == null || typePrefix.isEmpty()) { return this.getType(); }

		try {
			XPathExpression expr = XPATH.compile("@type");
			String elemType = (String) expr.evaluate(node, XPathConstants.STRING);

			typePrefix = typePrefix.contains(":") ? typePrefix : typePrefix + ":";

			return elemType.replaceAll(Pattern.quote(typePrefix), "");
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}

	@Override public boolean isComplex() throws XPathExpressionException {
		XPathExpression expr = XPATH.compile("complexType/sequence/element");
		Object node = expr.evaluate(this.node, XPathConstants.NODE);
		return node != null;
	}

	@Override public XsdElement getComplexChild() throws XPathExpressionException {
		XPathExpression expr = XPATH.compile("complexType/sequence/element");
		Node node = (Node) expr.evaluate(this.node, XPathConstants.NODE);
		return node == null ? RegularXsdElement.VOID : new RegularXsdElement(node);
	}

	@Override public boolean hasType(String type, String typePrefix) {
		try {
			String elemType = this.getType(typePrefix);
			return type.equalsIgnoreCase(elemType) || this.getComplexChild().hasType(type, typePrefix);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}

	@Override public String toString() {
		return "XsdElement{" +
				"node=" + node +
				'}';
	}
}
