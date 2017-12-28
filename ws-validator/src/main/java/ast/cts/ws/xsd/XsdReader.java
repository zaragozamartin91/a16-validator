package ast.cts.ws.xsd;

import javax.xml.xpath.XPathExpressionException;

public interface XsdReader {
	boolean typeExists(String typeName);

	XsdElement getElement(String typeName, String elementName) throws XPathExpressionException;
}
