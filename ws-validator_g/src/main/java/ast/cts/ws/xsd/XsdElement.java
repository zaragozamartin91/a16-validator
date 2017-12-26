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
	 * @return Tipo crudo del elemento. String vacio si el elemento no tiene tipo.
	 * @throws XPathExpressionException Si ocurre un error al obtener el type usando xpath
	 */
	public String getType() throws XPathExpressionException {
		if (this.isVoid()) { return ""; }

		XPathExpression expr = XPATH.compile("@type");
		String elemType = (String) expr.evaluate(node, XPathConstants.STRING);

		return elemType;
	}

	/**
	 * Obtiene el tipo del elemento ignorando el prefijo de namespace.
	 *
	 * @param typePrefix Prefijo del type (ej: tns, xs)
	 * @return Tipo del elemento sin prefijo de namespace. String vacio si el elemento no tiene tipo.
	 * @throws XPathExpressionException Si ocurre un error al obtener el type usando xpath
	 */
	public String getType(String typePrefix) throws XPathExpressionException {
		if (this.isVoid()) { return ""; }
		if (typePrefix == null || typePrefix.isEmpty()) { return this.getType(); }

		XPathExpression expr = XPATH.compile("@type");
		String elemType = (String) expr.evaluate(node, XPathConstants.STRING);

		typePrefix = typePrefix.contains(":") ? typePrefix : typePrefix + ":";

		return elemType.replaceAll(Pattern.quote(typePrefix), "");
	}

	/**
	 * Retorna true si este elemento es vacio
	 *
	 * @return true si este elemento es vacio, false en caso contrario
	 */
	public boolean isVoid() { return this == VOID; }

	/**
	 * Retorna true si este elemento es complejo. Se entiende por elemento complejo a aquel que tiene jerarquia de hijos de tipo
	 * complexType -> sequence -> element.
	 *
	 * @return true si este elemento es complejo, false en caso contrario.
	 * @throws XPathExpressionException En caso que la expresion de xpath no aplique al elemento
	 */
	public boolean isComplex() throws XPathExpressionException {
		if (this.isVoid()) { return false; }

		XPathExpression expr = XPATH.compile("complexType/sequence/element");
		Object node = expr.evaluate(this.node, XPathConstants.NODE);
		return node != null;
	}

	/**
	 * Obtiene un {@link XsdElement} hijo del elemento this siguiendo la jerarquia complexType -> sequence -> element.
	 *
	 * @return Elemento hijo en caso de existir, {@link XsdElement#VOID} en caso de no existir.
	 * @throws XPathExpressionException
	 */
	public XsdElement getComplexChild() throws XPathExpressionException {
		if (this.isVoid()) { return this; }

		XPathExpression expr = XPATH.compile("complexType/sequence/element");
		Node node = (Node) expr.evaluate(this.node, XPathConstants.NODE);
		return node == null ? XsdElement.VOID : new XsdElement(node);
	}

	public boolean hasType(String type, String typePrefix) throws XPathExpressionException {
		if (this.isVoid()) { return false; }

		String elemType = this.getType(typePrefix);
		return type.equalsIgnoreCase(elemType) || this.getComplexChild().hasType(type, typePrefix);
	}

	@Override public String toString() {
		if (this.isVoid()) { return "VOID"; }
		return "XsdElement{" +
				"node=" + node +
				'}';
	}
}
