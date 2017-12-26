package ast.cts.ws;

import ast.cts.ws.a16.A16Doc;
import ast.cts.ws.a16.A16Table;
import ast.cts.ws.util.TypeComparator;
import ast.cts.ws.xsd.XsdElement;
import ast.cts.ws.xsd.XsdReader;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainValidator {
	private A16Doc a16Doc;
	private XsdReader xsdReader;
	private String xsdTypePrefix;
	private TypeComparator typeComparator;

	public MainValidator(A16Doc a16Doc, XsdReader xsdReader, String xsdTypePrefix, TypeComparator typeComparator) {
		this.a16Doc = a16Doc;
		this.xsdReader = xsdReader;
		this.xsdTypePrefix = xsdTypePrefix;
		this.typeComparator = typeComparator;
	}

	public void validate() {
		final List<String> errMsgs = new ArrayList<>();
		Map<String, A16Table> tables = a16Doc.getTables();

		tables.values().forEach(table -> {
			final String tableTitle = table.getTitle();

			table.getRows().forEach(row -> {
				String a16rowName = row.name;

				XsdElement xsdElement;
				try {
					xsdElement = xsdReader.getElement(tableTitle, a16rowName);
				} catch (XPathExpressionException e) {
					errMsgs.add(String.format("Error al leer el campo %s#%s del xsd", tableTitle, a16rowName));
					e.printStackTrace();
					return;
				}

				if (xsdElement.isVoid()) {return;}

				String a16rowRawType = row.type;
				String a16type = typeComparator.parseType(a16rowRawType);


			});
		});
	}
}
