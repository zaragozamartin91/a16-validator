package ast.cts.ws.util;

public enum StringStandardizer {
	INSTANCE;

	public String replaceSpecialChars(String input) {
		return input.replaceAll("Á", "A")
				.replaceAll("É", "E")
				.replaceAll("Í", "I")
				.replaceAll("Ó", "O")
				.replaceAll("Ú", "U")
				.replaceAll("Ñ", "N")

				.replaceAll("á", "a")
				.replaceAll("é", "e")
				.replaceAll("í", "i")
				.replaceAll("ó", "o")
				.replaceAll("ú", "u")
				.replaceAll("ñ", "n");
	}

	public String capitalize(String line) {
		if (line.isEmpty()) { return ""; }
		if (line.length() == 1) { return line.toUpperCase(); }
		return Character.toUpperCase(line.charAt(0)) + line.substring(1);
	}

	public String deCapitalize(String line) {
		if (line.isEmpty()) { return ""; }
		if (line.length() == 1) { return line.toLowerCase(); }
		return Character.toLowerCase(line.charAt(0)) + line.substring(1);
	}
}
