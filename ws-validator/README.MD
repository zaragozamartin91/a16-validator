# Validador de A16 con XSDs

## Generar el entregable

Para generar el componente entregable, se debe correr el comando "__gradlew installFull__"

Los componentes entregables se generarán en el directorio __build/install/ws-validator__. Nos referiremos a este directorio como __$wsv__

## Uso

Para usar el componente, correr el script __run.bat__ desde $wsv:
* Indicar archivo txt con tablas de las entidades del a16
* Indicar archivo XSD a utilizar para validar las entidades
* La información de las validaciones se mostrarán por consola