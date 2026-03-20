# GenerateInfoFiles

Proyecto en Java para generar archivos de informacion de productos, vendedores y ventas.

## Que genera

Al ejecutar el programa se crean, si no existen, estas rutas:

```text
generated_files/
generated_files/products_info.txt
generated_files/salesmen_info.txt
generated_files/sales/
generated_files/sales/ventas_<documento>.txt
```

## Formatos

Archivo de productos `generated_files/products_info.txt`:

```text
IDProducto;NombreProducto;PrecioPorUnidad
```

Archivo de vendedores `generated_files/salesmen_info.txt`:

```text
TipoDocumento;NumeroDocumento;Nombres;Apellidos
```

Archivo de ventas por vendedor `generated_files/sales/ventas_<documento>.txt`:

```text
TipoDocumento;NumeroDocumento
IDProducto;Cantidad
IDProducto;Cantidad
```

La primera linea identifica al vendedor y las siguientes lineas representan sus ventas.

## Estructura del proyecto

- `src/GenerateInfoFiles.java`: clase principal con el metodo `main`.
- `src/FileUtil.java`: crea la estructura base de carpetas.
- `src/Constants.java`: constantes de rutas, nombres de archivos y cantidades por defecto.
- `src/Product.java`: genera el archivo de productos.
- `src/Salesmen.java`: genera el archivo de vendedores y los archivos de ventas por vendedor.
- `src/TestValidator.java`: validacion basica de formato para los archivos principales.

## Requisitos

- Java JDK 8 o superior.

## Compilacion

Desde la raiz del proyecto:

```bash
javac src/*.java
```

## Ejecucion

Despues de compilar:

```bash
java src.GenerateInfoFiles
```

## Flujo de ejecucion

La ejecucion principal hace este proceso:

1. Crea la carpeta `generated_files` y la subcarpeta `generated_files/sales`.
2. Genera `products_info.txt`.
3. Genera `salesmen_info.txt`.
4. Genera un archivo de ventas por cada vendedor dentro de `generated_files/sales`.
