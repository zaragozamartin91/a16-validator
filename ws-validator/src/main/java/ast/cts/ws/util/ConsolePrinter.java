package ast.cts.ws.util;

import java.util.function.Consumer;

public enum ConsolePrinter {
	OK(text -> System.out.println("OK\t\t: " + text)),
	ERROR(text -> System.out.println("ERROR\t: " + text));

	private Consumer<String> printer;

	ConsolePrinter(Consumer<String> printer) { this.printer = printer; }

	/**
	 * Escribe una linea con salto al final.
	 *
	 * @param text Texto a escribir.
	 */
	public void println(String text) {
		printer.accept(text);
	}
}
