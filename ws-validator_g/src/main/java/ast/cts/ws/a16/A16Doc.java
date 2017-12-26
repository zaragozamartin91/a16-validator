package ast.cts.ws.a16;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class A16Doc {
	private Map<String, A16Table> tables;

	A16Doc(Map<String, A16Table> tables) {
		this.tables = tables;
	}

	public Map<String, A16Table> getTables() { return new LinkedHashMap<>(tables); }

	public A16Table getTable(String tableId) { return tables.get(tableId); }

	public boolean hasTable(String tableId) { return tables.keySet().stream().anyMatch(tableId::equalsIgnoreCase); }

	public void forEachTable(Consumer<A16Table> consumer) { getTables().values().forEach(consumer); }

	public boolean isCustomType(String typeName) {
		return tables.keySet().stream().anyMatch(typeName::equalsIgnoreCase);
	}

	public boolean isNotCustomType(String typeName) { return !this.isCustomType(typeName); }
}
