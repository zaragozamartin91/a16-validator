package ast.cts.ws.a16;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Representacion de tabla de a16
 */
public class A16Table {
    private String title;
    private Set<A16Row> rows = new LinkedHashSet<>();

    /**
     * Crea una representacion de tabla de a16
     *
     * @param title Titulo de la tabla.
     */
    public A16Table(String title) {
        this.title = title;
    }

    /**
     * Agrega una fila a la tabla.
     *
     * @param name Nombre del campo de la tabla.
     * @param type Tipo de la tabla.
     * @return this.
     */
    public A16Table addRow(String name, String type) {
        rows.add(new A16Row(name, type));
        return this;
    }

    /**
     * Obtiene una copia de las filas.
     *
     * @return copia de las filas de la tabla.
     */
    public Set<A16Row> getRows() { return new LinkedHashSet<>(rows); }

    /**
     * Obtiene el titulo de la tabla.
     *
     * @return titulo de la tabla.
     */
    public String getTitle() { return title; }

    /**
     * Aplica una funcion a cada fila de la tabla.
     *
     * @param consumer Accion a aplicar a cada fila.
     */
    public void forEachRow(Consumer<A16Row> consumer) { getRows().forEach(consumer); }

    @Override
    public String toString() {
        return "A16Table{" +
                "title='" + title + '\'' +
                ", rows=" + rows +
                '}';
    }
}
