# Validador de A16 con XSDs

## Generar el entregable

Para generar el componente entregable, se debe correr el comando "__gradlew installFull__"

Los componentes entregables se generarán en el directorio __build/install/ws-validator__. Nos referiremos a este directorio como __$wsv__

## Archivos y configuración

El programa puede ser configurado desde el archivo __$wsv/configuration.properties__

El programa requiere de dos archivos para operar:
* Archivo txt con tablas del a16:
    * El archivo debe contener las tablas del A16
    * Cada tabla debe estar separada por uno o más ENTERs
    * Cada tabla debe comenzar con una línea de título (ej: Input, Output, IArrayEntidades, etc.)
    * Cada linea despues del titulo de tabla es una fila
    * Es posible configurar la cantidad de filas a ser ignoradas inmediatamente despues del titulo de una tabla
    * Las columnas de cada fila deben estar separadas por espacios o TABS
    * El archivo debe tener formato __UTF-8__
* Archivo xsd del servicio web a validar


## Uso

Para usar el componente, correr el script __$wsv/run.bat__:
* Indicar archivo txt con tablas de las entidades del a16
* Indicar archivo XSD a utilizar para validar las entidades
* La información de las validaciones se mostrarán por consola

## Ayuda

Es posible modificar el encoding de un archivo txt desde Notepad++:
* Ir a encoding
* Convert to UTF-8

