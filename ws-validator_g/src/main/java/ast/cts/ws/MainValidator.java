package ast.cts.ws;

import ast.cts.ws.a16.A16Doc;
import ast.cts.ws.util.StringStandardizer;
import ast.cts.ws.util.TypeComparator;
import ast.cts.ws.xsd.XsdElement;
import ast.cts.ws.xsd.XsdReader;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;

public class MainValidator {
	public static final StringStandardizer STRING_STANDARDIZER = StringStandardizer.INSTANCE;

	private final A16Doc a16Doc;
	private XsdReader xsdReader;
	private String xsdBasicTypePrefix;
	private String xsdComplexTypePrefix;
	private TypeComparator typeComparator;

	public MainValidator(A16Doc a16Doc, XsdReader xsdReader, String xsdBasicTypePrefix, String xsdComplexTypePrefix, TypeComparator typeComparator) {
		this.a16Doc = a16Doc;
		this.xsdReader = xsdReader;
		this.xsdBasicTypePrefix = xsdBasicTypePrefix;
		this.xsdComplexTypePrefix = xsdComplexTypePrefix;
		this.typeComparator = typeComparator;
	}

	/**
	 * Valida el documento a16 escaneado contra el xsd del servicio web.
	 *
	 * @param lenientCheck True si el chequeo debe ser "permisivo", false en caso contrario.
	 * @return Lista con errores.
	 */
	public List<String> validate(boolean lenientCheck) {
		if (lenientCheck) { System.out.println("Chequeo permisivo ACTIVO"); }

		final List<String> errMsgs = new ArrayList<>();

		a16Doc.forEachTable(table -> {
			final String tableTitle = lenientCheck ? STRING_STANDARDIZER.capitalize(table.getTitle()) : table.getTitle();
			System.out.println();
			System.out.println("Examinando tabla " + tableTitle);

			table.forEachRow(row -> {
				String a16rowName = lenientCheck ? STRING_STANDARDIZER.deCapitalize(row.name) : row.name;
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

				/* Si el parseador de tipos determino que es "desconocido" y el tipo no representa alguna de las otras tablas del doc a16,
				* entonces se determina que el tipo es invalido */
				if (typeComparator.isUnknown(a16ParsedType)) {
					// si el tipo parseado figura como unknown...

					if (a16Doc.isNotCustomType(a16rowRawType)) {
						errMsgs.add(String.format("El tipo %s del campo %s#%s del A16 es invalido!", a16rowRawType, tableTitle, a16rowName));
						return;
					}

					// si el tipo es custom...

					if (!xsdElement.hasType(a16rowRawType, xsdComplexTypePrefix)) {
						errMsgs.add(String.format("El tipo %s del campo %s#%s figura como tipo complejo en el A16 pero no corresponde con el tipo del xsd",
								a16rowRawType,
								tableTitle,
								a16rowName));
						return;
					}
				}

				// si el tipo es basico...

				if (!xsdElement.hasType(a16ParsedType, xsdBasicTypePrefix)) {
					errMsgs.add(String.format("El tipo %s del campo %s#%s del A16 no corresponde con el tipo %s del XSD!",
							a16rowRawType,
							tableTitle,
							a16rowName,
							xsdElement.getType(xsdBasicTypePrefix)));
					return;
				}
			});
		});

		return errMsgs;
	}
}
