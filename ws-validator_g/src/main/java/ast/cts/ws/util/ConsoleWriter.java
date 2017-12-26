package ast.cts.ws.util;

public enum ConsoleWriter {
	RED("\u001B[31m"),
	GREEN("\u001B[32m"),
	YELLOW("\u001B[33m"),
	BLUE("\u001B[34m"),
	PURPLE("\u001B[35m"),
	CYAN("\u001B[36m"),
	WHITE("\u001B[37m");

	public static final String ANSI_RESET = "\u001B[0m";

	private String code;

	ConsoleWriter(String code) { this.code = code; }

	public void println(String text) { System.out.printf("%s%s%s%n", code, text, ANSI_RESET); }

	public void print(String text) { System.out.printf("%s%s%s", code, text, ANSI_RESET); }
}
