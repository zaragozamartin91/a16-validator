package ast.cts.ws;

import ast.cts.ws.a16.A16Doc;
import ast.cts.ws.util.TypeComparator;
import ast.cts.ws.xsd.XsdElement;
import ast.cts.ws.xsd.XsdReader;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;

public class MainValidator {
	private final A16Doc a16Doc;
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

		a16Doc.forEachTable(table -> {
			final String tableTitle = table.getTitle();

			table.forEachRow(row -> {
				String a16rowName = row.name;

				System.out.printf("Examinando fila %s%n", a16rowName);

				XsdElement xsdElement;
				try {
					xsdElement = xsdReader.getElement(tableTitle, a16rowName);
				} catch (XPathExpressionException e) {
					errMsgs.add(String.format("Error al leer el campo %s#%s del xsd", tableTitle, a16rowName));
					e.printStackTrace();
					return;
				}

				/* Si el campo del a16 no se encuentra en el xsd, entonces agrego un mensaje de error */
				if (xsdElement.isVoid()) {
					errMsgs.add(String.format("El campo %s#%s del A16 no fue hallado en el Xsd", tableTitle, a16rowName));
					return;
				}

				String a16rowRawType = row.type;
				String a16ParsedType = typeComparator.parseType(a16rowRawType);

				if (typeComparator.isUnknown(a16ParsedType)) {

					/* Si el parseador de tipos determino que es "desconocido" y el tipo no representa alguna de las otras tablas del doc a16,
					* entonces se determina que el tipo es invalido */
					if (a16Doc.isNotCustomType(a16rowRawType)) {
						errMsgs.add(String.format("El tipo %s del campo %s#%s del A16 es invalido!", a16rowRawType, tableTitle, a16rowName));
						return;
					}


				}

			});
		});
	}
}
