package ast.cts.ws;

import ast.cts.ws.a16.A16Table;
import ast.cts.ws.config.Configuration;
import ast.cts.ws.util.StringStandardizer;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for simple App.
 */
public class AppTest {
	private static Configuration configuration;

	@BeforeClass
	public static void beforeAll() throws IOException {
		configuration = Configuration.getInstance();
	}


	@Test
	public void testReadXsd() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(AppTest.class.getClassLoader().getResourceAsStream("cobiscorp.ecobis.consultagruposriesgo.dto.xsd"));

		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();

		XPathExpression expr = xpath.compile("//complexType[@name='ConsultaGrupoRiesgoFil']/sequence/element[@name='iCtipoIdTributario']");

		NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		System.out.println(nodes);
		System.out.println(nodes.item(0).getTextContent());
		assertEquals(1, nodes.getLength());

		expr = xpath.compile("@name");
		Object name = expr.evaluate(nodes.item(0), XPathConstants.STRING);
		System.out.println(name);
	}

	@Test
	public void testReadSegundoFiltroInput() throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(AppTest.class.getClassLoader().getResourceAsStream("SegundoFiltro-input.txt")));

		Map<String, A16Table> tables = new LinkedHashMap<>();
		while (true) {
			String line = bufferedReader.readLine();
			if (line == null) { break; }

			line = standarizeLine(line);
			String[] split = line.split(",");

			if (isTitle(split)) {
				// TODO : AGREGAR UNA CONFIGURACION O ALGO PARA TRANSFORMAR LOS TITULOS Input Y Output EN NOMBRES DE FILS
				String title = getTitle(split);
				System.out.println();
				System.out.println("Creando tabla: " + title);

				A16Table table = new A16Table(title);
				boolean end = addRows(table, bufferedReader);
				tables.put(title, table);
				if (end) { break; }
			}
		}

		bufferedReader.close();
	}

	private boolean addRows(A16Table table, BufferedReader bufferedReader) throws IOException {
		/* Se descarta la primera linea que son los titulos de la tabla */
		bufferedReader.readLine();

		while (true) {
			String line = bufferedReader.readLine();

			if (line == null) {return true;}
			if (line.isEmpty()) { break; }

			line = standarizeLine(line);
			String[] split = line.split(",");


			/* Si la cantidad de columnas de la fila es 2 entonces agrego una nueva fila a la tabla de a16
			 * Caso contrario, es una "fila fallida" que debe ser ignorada */
			if (split.length >= 3) {
				// TODO : AGREGAR UN PARAMETRO DE CONFIGURACION O ALGO PARA INDICAR LAS COLUMNAS
				String name = split[1];
				String type = split[2];
				table.addRow(name, type);
				System.out.printf("Fila agregada: %s, %s%n", name, type);
			}
		}

		return false;
	}

	private String standarizeLine(String line) {
		// TODO : AGREGAR UNA CONFIGURACION PARA DETERMINAR EL SEPARADOR (POR DEFECTO ES TAB)
		return StringStandardizer.INSTANCE.replaceSpecialChars(line).replaceAll("\t+", ",").trim();
	}

	private String getTitle(String[] split) {
		for (String s : split) {
			if (!s.trim().isEmpty()) { return s.replaceAll(" +.*", ""); }
		}
		return "";
	}

	private boolean isTitle(String[] split) {
		int notEmptyCount = 0;
		for (String s : split) {
			s = s.trim();
			notEmptyCount = s.isEmpty() ? notEmptyCount : notEmptyCount + 1;
		}
		return notEmptyCount == 1;
	}
}
