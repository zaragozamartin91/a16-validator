package ast.cts.ws.config;

public enum ColDelim {
    TAB("\t"),
    SPACE(" ");

    public final String delim;

    ColDelim(String s) {
        this.delim = s;
    }
}
