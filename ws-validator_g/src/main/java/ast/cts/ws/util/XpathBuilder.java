package ast.cts.ws.util;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

public enum XpathBuilder {
	INSTANCE;

	public XPath build() {
		XPathFactory xPathfactory = XPathFactory.newInstance();
		return xPathfactory.newXPath();
	}
}
