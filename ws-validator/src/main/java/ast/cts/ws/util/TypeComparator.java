package ast.cts.ws.util;

import java.util.List;
import java.util.Optional;

public class TypeComparator {
	private List<String> intAliases;
	private List<String> stringAliases;
	private List<String> decimalAliases;

	public TypeComparator(List<String> intAliases, List<String> stringAliases, List<String> decimalAliases) {
		this.intAliases = intAliases;
		this.stringAliases = stringAliases;
		this.decimalAliases = decimalAliases;
	}

	/**
	 * Obtiene una version estandarizada de un type.
	 *
	 * @param rawType Valor crudo del type.
	 * @return
	 */
	public String parseType(String rawType) {
		final String frawType = Optional.ofNullable(rawType).orElse("NULL").trim().toLowerCase();

		if (intAliases.stream().anyMatch(frawType::equalsIgnoreCase)) { return "int"; }
		if (stringAliases.stream().anyMatch(s -> frawType.startsWith(s.toLowerCase()))) { return "string"; }
		if (decimalAliases.stream().anyMatch(frawType::equalsIgnoreCase)) { return "decimal"; }

		return "unknown";
	}

	/**
	 * Retorna true si el type es desconocido.
	 *
	 * @param typeName Nombre del type.
	 * @return true si el type es desconocido, false en caso contrario.
	 */
	public boolean isUnknown(String typeName) { return "unknown".equalsIgnoreCase(typeName); }

	public boolean isBasic(String typeName) { return !this.isUnknown(typeName);}
}
