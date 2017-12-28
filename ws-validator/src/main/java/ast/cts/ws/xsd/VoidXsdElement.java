package ast.cts.ws.xsd;

import javax.xml.xpath.XPathExpressionException;

/**
 * Representacion de un elemento xsd vacio.
 * <p>
 * Aplicacion del null-pattern.
 */
public class VoidXsdElement implements XsdElement {
	private static VoidXsdElement ourInstance = new VoidXsdElement();

	public static VoidXsdElement getInstance() { return ourInstance; }

	private VoidXsdElement() { }

	@Override public String getType()  { return "";}

	@Override public String getType(String typePrefix)  { return ""; }

	@Override public boolean isVoid() { return true; }

	@Override public boolean isComplex() throws XPathExpressionException { return false; }

	@Override public XsdElement getComplexChild() throws XPathExpressionException { return this; }

	@Override public boolean hasType(String type, String typePrefix)  { return false; }

	@Override public String toString() { return "VoidXsdElement{}"; }
}
