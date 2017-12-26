package ast.cts.ws.a16;

import java.util.LinkedHashMap;
import java.util.Map;

public class A16Doc {
	private Map<String, A16Table> tables;

	A16Doc(Map<String, A16Table> tables) {
		this.tables = tables;
	}

	public Map<String, A16Table> getTables() { return new LinkedHashMap<>(tables); }

	public A16Table getTable(String tableId) { return tables.get(tableId); }

	public boolean hasTable(String tableId) {
		return tables.keySet().stream().anyMatch(tableId::equalsIgnoreCase);
	}
}
