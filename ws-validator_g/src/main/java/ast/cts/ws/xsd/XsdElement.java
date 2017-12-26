package ast.cts.ws.xsd;

import ast.cts.ws.util.XpathBuilder;
import org.w3c.dom.Node;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import java.util.regex.Pattern;

public class XsdElement {
	public static final XsdElement VOID = new XsdElement();
	private static XPath XPATH = XpathBuilder.INSTANCE.build();
	private Node node;

	public XsdElement(Node node) {
		this.node = node;
	}

	private XsdElement() { }

	/**
	 * Obtiene el tipo crudo del elemento.
	 *
	 * @return Tipo crudo del elemento
	 * @throws XPathExpressionException Si ocurre un error al obtener el type usando xpath
	 */
	public String getType() throws XPathExpressionException {
		XPathExpression expr = XPATH.compile("@type");
		String elemType = (String) expr.evaluate(node, XPathConstants.STRING);

		return elemType;
	}

	/**
	 * Obtiene el tipo del elemento ignorando el prefijo de namespace.
	 *
	 * @param typePrefix Prefijo del type (ej: tns, xs)
	 * @return Tipo del elemento sin prefijo de namespace.
	 * @throws XPathExpressionException Si ocurre un error al obtener el type usando xpath
	 */
	public String getType(String typePrefix) throws XPathExpressionException {
		XPathExpression expr = XPATH.compile("@type");
		String elemType = (String) expr.evaluate(node, XPathConstants.STRING);

		typePrefix = typePrefix.contains(":") ? typePrefix : typePrefix + ":";

		return elemType.replaceAll(Pattern.quote(typePrefix), "");
	}

	public boolean isVoid() { return this == VOID; }

	public boolean isComplex() throws XPathExpressionException {
		XPathExpression expr = XPATH.compile("complexType/sequence/element");
		Object node = expr.evaluate(this.node, XPathConstants.NODE);
		return node != null;
	}

	public XsdElement getComplexChild() throws XPathExpressionException {
		XPathExpression expr = XPATH.compile("complexType/sequence/element");
		Node node = (Node) expr.evaluate(this.node, XPathConstants.NODE);
		return node == null ? XsdElement.VOID : new XsdElement(node);
	}
}
