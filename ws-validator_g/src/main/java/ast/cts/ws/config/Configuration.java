package ast.cts.ws.config;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Configuration {
    private static Configuration ourInstance;

    static {
        try {
            ourInstance = new Configuration();
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Error al cargar las configuraciones", e);
        }
    }

    private Properties properties;
    private int nameCol;
    private int typeCol;

    private List<String> intAliases = new ArrayList<>();
    private List<String> stringAliases = new ArrayList<>();
    private List<String> decimalAliases = new ArrayList<>();
    private String xsdBasicTypePrefix;
    private String xsdComplexTypePrefix;
    private boolean lenientSwitchOn;
    private ColDelim colDelim;
    private String inputName;
    private String outputName;

    public static Configuration getInstance() { return ourInstance; }

    private Configuration() throws FileNotFoundException {
        /* Si existe un archivo de configuracion en la raiz del JAR entonces se usa ese como recurso. Caso
         * contrario, se usa aquel que se encuentre en el path (probablemente dentro del JAR) */

        File propsFile = new File("configuration.properties");
        InputStream resource = propsFile.exists() ?
                new FileInputStream(propsFile) :
                Configuration.class.getClassLoader().getResourceAsStream("configuration.properties");

        properties = new Properties();
        try {
            properties.load(resource);
            loadProps();
            validateProps();
        } catch (IOException e) {
            throw new IllegalStateException("Error al cargar las configuraciones", e);
        }
    }

    private void loadProps() {
        nameCol = Integer.parseInt(properties.getProperty("col.index.name", "0"));
        typeCol = Integer.parseInt(properties.getProperty("col.index.type", "1"));

        intAliases = Arrays.stream(properties.getProperty("type.int.aliases").split(Pattern.quote(",")))
                .map(s -> s.trim().toLowerCase())
                .collect(Collectors.toList());

        stringAliases = Arrays.stream(properties.getProperty("type.string.aliases").split(Pattern.quote(",")))
                .map(s -> s.trim().toLowerCase())
                .collect(Collectors.toList());

        decimalAliases = Arrays.stream(properties.getProperty("type.decimal.aliases").split(Pattern.quote(",")))
                .map(s -> s.trim().toLowerCase())
                .collect(Collectors.toList());

        xsdBasicTypePrefix = properties.getProperty("type.xsd.prefix.basic", "").trim();
        xsdComplexTypePrefix = properties.getProperty("type.xsd.prefix.custom", "").trim();

        lenientSwitchOn = Boolean.parseBoolean(properties.getProperty("switch.lenient.on", "false").trim());

        colDelim = ColDelim.valueOf(properties.getProperty("delim.col", "TAB").trim());

        inputName = properties.getProperty("type.input.name", "").trim();
        outputName = properties.getProperty("type.output.name", "").trim();
    }

    private void validateProps() {
        if (nameCol == typeCol) { throw new IllegalStateException("Los indices de columnas de nombre y tipo deben ser diferentes"); }
        if (nameCol < 0 || typeCol < 0) throw new IllegalStateException("Los indices de columna son invalidos!");
    }

    public int getNameCol() { return nameCol; }

    public int getTypeCol() { return typeCol; }

    public List<String> getIntAliases() { return Collections.unmodifiableList(intAliases); }

    public List<String> getStringAliases() { return Collections.unmodifiableList(stringAliases); }

    public List<String> getDecimalAliases() { return Collections.unmodifiableList(decimalAliases); }

    public String getXsdBasicTypePrefix() { return xsdBasicTypePrefix; }

    public String getXsdComplexTypePrefix() { return xsdComplexTypePrefix; }

    public boolean isLenientSwitchOn() { return lenientSwitchOn; }

    public ColDelim getColDelim() { return colDelim; }

    public String getInputName() { return inputName; }

    public String getOutputName() { return outputName; }
}
