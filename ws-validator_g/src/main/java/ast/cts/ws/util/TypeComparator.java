package ast.cts.ws.util;

import java.util.List;

public class TypeComparator {
	private List<String> intAliases;
	private List<String> stringAliases;
	private List<String> decimalAliases;

	public TypeComparator(List<String> intAliases, List<String> stringAliases, List<String> decimalAliases) {
		this.intAliases = intAliases;
		this.stringAliases = stringAliases;
		this.decimalAliases = decimalAliases;
	}

	public String parseType(String rawType) {
		if (intAliases.stream().anyMatch(rawType::equalsIgnoreCase)) { return "int"; }
		if (stringAliases.stream().anyMatch(rawType::equalsIgnoreCase)) { return "string"; }
		if (decimalAliases.stream().anyMatch(rawType::equalsIgnoreCase)) { return "decimal"; }

		return "unknown";
	}
}
