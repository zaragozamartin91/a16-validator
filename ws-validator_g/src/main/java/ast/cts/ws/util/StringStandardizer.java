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
}
