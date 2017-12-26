package ast.cts.ws.a16;

public class A16Row {
	public final String name;
	public final String type;

	public A16Row(String name, String type) {
		this.name = name.trim();
		this.type = type.toLowerCase().trim();
	}

	@Override public String toString() {
		return "A16Row{" +
				"name='" + name + '\'' +
				", type='" + type + '\'' +
				'}';
	}
}