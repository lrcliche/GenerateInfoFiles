# GenerateInfoFiles

Proyecto en Java para generar la estructura base de carpetas de archivos de informacion.

Actualmente el programa crea una carpeta principal y una subcarpeta para ventas. El proyecto tambien incluye entidades simples como `Product` y `Salesman`, junto con una clase de constantes y una utilidad para manejo de directorios.

## Objetivo

Este proyecto sirve como base para un generador de archivos de informacion. Por ahora, la funcionalidad implementada se enfoca en crear la estructura inicial de carpetas donde se podrian almacenar productos, vendedores y ventas.

## Estructura del proyecto

- `src/GenerateInfoFile.java`: clase principal con el metodo `main`.
- `src/FileUtil.java`: utilidades para crear directorios base.
- `src/Constants.java`: constantes generales del proyecto.
- `src/Product.java`: entidad que representa un producto.
- `src/Salesman.java`: entidad que representa un vendedor.

## Requisitos

- Java JDK 8 o superior.
- Consola o terminal para compilar y ejecutar el proyecto.

## Compilacion

Desde la raiz del proyecto, ejecuta:

```bash
javac src/*.java
```

## Ejecucion

Despues de compilar, ejecuta:

```bash
java src.GenerateInfoFile
```

## Resultado esperado

Al ejecutar el programa se crea la siguiente estructura si no existe:

```text
generated_files/
generated_files/sales/
```

En consola deberia mostrarse un mensaje indicando que el proceso fue ejecutado correctamente o, en caso de error, el detalle de la excepcion.

## Notas

- Las constantes de nombres de archivos y cantidades por defecto ya estan definidas en `Constants`, aunque en este momento no todas se usan en la ejecucion principal.
- El proyecto puede extenderse para generar archivos de productos, vendedores y ventas con informacion de prueba.
