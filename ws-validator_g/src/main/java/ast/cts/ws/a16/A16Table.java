package ast.cts.ws.a16;

import java.util.HashSet;
import java.util.Set;

public class A16Table {
	private String title;
	private Set<A16Row> rows = new HashSet<>();

	public A16Table(String title) {
		this.title = title;
	}

	public A16Table addRow(String name, String type) {
		rows.add(new A16Row(name, type));
		return this;
	}

	public Set<A16Row> getRows() { return new HashSet<>(rows); }

	public String getTitle() { return title; }

	@Override public String toString() {
		return "A16Table{" +
				"title='" + title + '\'' +
				", rows=" + rows +
				'}';
	}
}
