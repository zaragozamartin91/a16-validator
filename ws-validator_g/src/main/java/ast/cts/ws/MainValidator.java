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
					errMsgs.add(String.format("Error al leer el campo %s->%s del XSD", tableTitle, a16rowName));
					e.printStackTrace();
					return;
				}

				if (xsdElement.isVoid()) {
					/* Si el campo del a16 no se encuentra en el xsd, entonces agrego un mensaje de error */
					errMsgs.add(String.format("El campo %s->%s del A16 no fue hallado en el XSD", tableTitle, a16rowName));
					return;
				}

				String a16rowRawType = row.type;
				String a16ParsedType = typeComparator.parseType(a16rowRawType);

				/* Si el parseador de tipos determino que es "desconocido" ...*/
				if (typeComparator.isUnknown(a16ParsedType)) {

					if (a16Doc.isNotCustomType(a16rowRawType)) {
						// si el tipo ademas de ser desconocido NO figura en las tablas del a16 -> el tipo es invalido
						errMsgs.add(String.format("El tipo %s del campo %s->%s del A16 es invalido!", a16rowRawType, tableTitle, a16rowName));
						return;
					}

					if (!xsdElement.hasType(a16rowRawType, xsdComplexTypePrefix)) {
						/* si el tipo es desconocido, figura en las tablasd el a16 pero el nombre del mismo no coincide con el tipo del
						 * elemento correspondiente en el a16 entonces el tipo es invalido (probablemente el nombre este mal) */
						errMsgs.add(String.format("El tipo %s del campo %s->%s figura como tipo complejo en el A16 pero no corresponde con el tipo del XSD",
								a16rowRawType,
								tableTitle,
								a16rowName));
						return;
					}
				}

				// si el tipo es basico...
				if (typeComparator.isBasic(a16ParsedType)) {
					if (!xsdElement.hasType(a16ParsedType, xsdBasicTypePrefix)) {
						/* Si el tipo del a16 es detectado como basico pero no corresponde con el tipo del elemento en el XSD entonces
						 * el tipo es incorrecto  */
						errMsgs.add(String.format("El tipo %s del campo %s->%s del A16 no coincide con el tipo %s del XSD!",
								a16rowRawType,
								tableTitle,
								a16rowName,
								xsdElement.getType(xsdBasicTypePrefix)));
						return;
					}
				}

			});
		});

		return errMsgs;
	}
}
