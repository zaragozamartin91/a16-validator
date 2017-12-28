# Validador de A16 con XSDs

## Generar el entregable

Para generar el componente entregable, se debe correr el comando "__gradlew installFull__"

Los componentes entregables se generarán en el directorio __build/install/ws-validator__. Nos referiremos a este directorio como __$wsv__

## Archivos y configuración

El programa requiere de dos archivos para operar:
* Archivo txt con tablas del a16 __CON UTF-8 ENCODING__
* Archivo xsd del servicio web a validar

El programa puede ser configurado desde el archivo __$wsv/configuration.properties__

## Uso

Para usar el componente, correr el script __$wsv/run.bat__:
* Indicar archivo txt con tablas de las entidades del a16
* Indicar archivo XSD a utilizar para validar las entidades
* La información de las validaciones se mostrarán por consola

## Ayuda

Es posible modificar el encoding de un archivo txt desde Notepad++:
* Ir a encoding
* Convert to UTF-8
