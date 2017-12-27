package ast.cts.ws.a16;

/**
 * Fila de tabla de a16
 */
public class A16Row {
    public final String name;
    public final String type;

    /**
     * Crea una nueva fila de tabla de a16
     *
     * @param name Nombre del campo.
     * @param type Tipo del campo.
     */
    public A16Row(String name, String type) {
        this.name = name.trim();
//		this.type = type.toLowerCase().trim();
        this.type = type.trim();
    }

    @Override
    public String toString() {
        return "A16Row{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}