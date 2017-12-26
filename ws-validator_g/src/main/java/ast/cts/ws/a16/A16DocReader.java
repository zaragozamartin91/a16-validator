package ast.cts.ws.a16;

import ast.cts.ws.util.StringStandardizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

public class A16DocReader {
	private InputStream fileStream;
	private int nameCol;
	private int typeCol;
	private int expectedCols;
	private String colDelim;
	private String inputName;
	private String outputName;

	public A16DocReader(int nameCol, int typeCol, String colDelim, String inputName, String outputName) {
		this.nameCol = nameCol;
		this.typeCol = typeCol;
		this.colDelim = colDelim;
		this.inputName = inputName;
		this.outputName = outputName;
	}

	private A16DocReader(InputStream fileStream, int nameCol, int typeCol, String colDelim, String inputName, String outputName) {
		this.fileStream = fileStream;
		this.nameCol = nameCol;
		this.typeCol = typeCol;
		this.colDelim = colDelim;
		this.inputName = inputName;
		this.outputName = outputName;

		expectedCols = Math.max(nameCol, typeCol) + 1;
	}

	public A16Doc readA16Txt(InputStream fileStream) throws IOException {
		return new A16DocReader(fileStream, nameCol, typeCol, colDelim, inputName, outputName).readA16Txt();
	}

	private A16Doc readA16Txt() throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileStream));

		Map<String, A16Table> tables = new LinkedHashMap<>();

		while (true) {
			String line = bufferedReader.readLine();
			if (line == null) { break; }

			line = standarizeLine(line);
			String[] split = line.split(",");

			if (isTitle(split)) {
				String rawTitle = getTitle(split);
				String title = transformTitle(rawTitle);

				System.out.println();
				System.out.println("Creando tabla: " + title);

				A16Table table = new A16Table(title);
				boolean end = addRows(table, bufferedReader);
				tables.put(title, table);
				if (end) { break; }
			}
		}

		bufferedReader.close();

		return new A16Doc(tables);
	}

	private String transformTitle(String title) {
		if ("input".equalsIgnoreCase(title)) { return inputName; }
		if ("output".equalsIgnoreCase(title)) { return outputName; }
		return title;
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
			if (split.length >= expectedCols) {
				String name = split[nameCol];
				String type = split[typeCol];
				table.addRow(name, type);
				System.out.printf("Fila agregada: %s, %s%n", name, type);
			}
		}

		return false;
	}

	private String standarizeLine(String line) {
		return StringStandardizer.INSTANCE
				.replaceSpecialChars(line)
				.replaceAll(colDelim + "+", ",")
				.trim();
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
