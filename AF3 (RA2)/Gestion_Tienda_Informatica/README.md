# Gestion de Tienda de Informatica - AF3 (RA2) Acceso a Datos

Aplicacion de consola en Java que simula la gestion de inventario de una tienda de informatica.
Utiliza SQLite como base de datos embebida y se conecta mediante JDBC.

---

## 1. Analisis del uso de bases de datos en el entorno profesional

### Proceso identificado

En una tienda de informatica es necesario gestionar el **inventario de productos**: saber que articulos hay disponibles, cuantas unidades quedan de cada uno, a que precio se venden, y llevar un control cuando se hacen ventas o se repone stock.

### Como se suelen gestionar estos datos

En tiendas pequenas, normalmente se usan hojas de calculo en Excel o Google Sheets. Cada vez que llega un pedido o se hace una venta, alguien actualiza la hoja manualmente. Esto funciona al principio pero da problemas cuando hay varias personas trabajando a la vez, cuando el catalogo crece, o cuando hay que buscar datos historicos.

### Ventajas de usar una base de datos relacional

- **Consistencia**: los datos se guardan en un unico sitio, no hay copias desincronizadas como pasa con los Excel compartidos.
- **Integridad**: se pueden definir restricciones (claves primarias, tipos de datos, valores no nulos) que impiden meter datos incorrectos.
- **Concurrencia**: varios usuarios pueden acceder a la vez sin pisarse gracias al sistema de transacciones.
- **Consultas potentes**: con SQL se pueden hacer busquedas, filtros, ordenaciones y agrupaciones de forma rapida y flexible.
- **Escalabilidad**: si el negocio crece, la base de datos aguanta sin problemas. Un Excel con 10.000 filas ya empieza a ir lento.

---

## 2. Creacion de la base de datos de practica

### Tecnologias utilizadas

- **Lenguaje**: Java 21 (Java SE)
- **Base de datos**: SQLite (embebida, no necesita servidor)
- **Conexion**: JDBC con el driver `sqlite-jdbc` (org.xerial)
- **Gestion de dependencias**: Maven
- **IDE**: IntelliJ IDEA

### Estructura del proyecto

```
src/main/java/informatica/
├── Main.java                      --> Clase principal con el menu
├── conexion/
│   ├── ConexionDB.java            --> Conexion JDBC a SQLite
│   └── InicializadorBD.java       --> Crea tablas y mete datos de ejemplo
├── dao/
│   ├── UsuarioDAO.java            --> Consultas sobre usuarios (login)
│   └── ProductoDAO.java           --> CRUD de productos + transaccion
└── modelo/
    ├── Usuario.java               --> Clase que representa un usuario
    └── Producto.java              --> Clase que representa un producto
```

### Diseno de la base de datos

La base de datos tiene dos tablas:

**Tabla `usuarios`**

| Campo    | Tipo    | Restricciones              |
|----------|---------|----------------------------|
| id       | INTEGER | PRIMARY KEY, AUTOINCREMENT |
| username | TEXT    | NOT NULL, UNIQUE           |
| password | TEXT    | NOT NULL                   |

**Tabla `productos`**

| Campo       | Tipo    | Restricciones              |
|-------------|---------|----------------------------|
| id          | INTEGER | PRIMARY KEY, AUTOINCREMENT |
| nombre      | TEXT    | NOT NULL                   |
| descripcion | TEXT    |                            |
| precio      | REAL    | NOT NULL                   |
| stock       | INTEGER | NOT NULL, DEFAULT 0        |

### Operaciones implementadas

La aplicacion permite realizar las cuatro operaciones basicas del CRUD:

- **Insercion**: se pueden anadir productos nuevos introduciendo nombre, descripcion, precio y stock.
- **Consulta**: se listan todos los productos almacenados en la base de datos.
- **Modificacion**: se puede actualizar el stock de un producto existente indicando su ID.
- **Eliminacion**: se puede borrar un producto por su ID.

Ademas, la aplicacion tiene un sistema de login que valida las credenciales contra la tabla de usuarios.

### Como ejecutar la aplicacion

1. Abrir el proyecto en IntelliJ IDEA
2. Hacer clic derecho en `Main.java` > Run 'Main.main()'
3. Iniciar sesion con usuario `admin` y contraseña `1234`

Si se tiene Maven instalado, tambien se puede ejecutar desde terminal:

```bash
mvn clean compile exec:java -Dexec.mainClass="informatica.Main"
```

---

## 3. Simulacion de transacciones y reflexion sobre integridad

### Implementacion en la aplicacion

La opcion 5 del menu ("Simular transaccion") ejecuta el metodo `simularVentaTransaccional()` del `ProductoDAO`. Este metodo demuestra como funcionan las transacciones en JDBC:

1. **Se desactiva el auto-commit** con `conn.setAutoCommit(false)`. Esto hace que los cambios no se guarden automaticamente.
2. **Se ejecuta un UPDATE** que resta stock del producto seleccionado.
3. **Se fuerza un error a proposito** lanzando una excepcion (simulando un fallo inesperado).
4. **Se ejecuta `conn.rollback()`** en el bloque catch, deshaciendo el UPDATE anterior.
5. **Se restaura el auto-commit** en el bloque finally.

El resultado es que, aunque el UPDATE se ejecuto, el rollback lo revierte y **el stock del producto no cambia**. Esto se puede comprobar listando los productos antes y despues de la simulacion.

### Guion SQL equivalente

```sql
-- Desactivar auto-commit (inicio de transaccion)
BEGIN TRANSACTION;

-- Operacion 1: restar stock del producto con ID 1
UPDATE productos SET stock = stock - 5 WHERE id = 1;

-- Aqui ocurriria un error inesperado...
-- Como hay un fallo, deshacemos todos los cambios:
ROLLBACK;

-- Si todo hubiera ido bien, hariamos:
-- COMMIT;
```

### Como se mantiene la integridad de los datos

- **Atomicidad**: la transaccion funciona como un bloque. O se ejecutan todas las operaciones, o no se ejecuta ninguna. Si falla una, el rollback deshace las demas.
- **Consistencia**: al hacer rollback, la base de datos vuelve al estado valido anterior. No quedan datos a medias.
- **Aislamiento**: mientras la transaccion esta abierta, otros procesos no ven los cambios parciales.
- **Durabilidad**: solo cuando se hace commit los datos quedan guardados de forma permanente.

### Aplicacion en un entorno real

En una tienda de informatica real, las transacciones serian fundamentales en situaciones como:

- **Ventas**: al procesar una venta hay que restar stock, registrar la venta y generar factura. Si falla algo a mitad, no puede quedar el stock restado pero sin factura. El rollback evita eso.
- **Transferencias entre almacenes**: restar stock en un almacen y sumarlo en otro debe hacerse de forma atomica.
- **Cobros**: si el pago no se confirma, no se debe registrar la venta en la base de datos.

---

## Correspondencia con los criterios de evaluacion

| Criterio | Descripcion | Donde se cumple |
|----------|-------------|-----------------|
| a) | Ventajas e inconvenientes de conectores | Seccion 1 del README (analisis de BBDD vs Excel) |
| b) | Gestor de BBDD embebido | Usamos SQLite, base de datos embebida que no necesita servidor |
| c) | Conector idoneo | Driver JDBC `sqlite-jdbc` de org.xerial en el `pom.xml` |
| d) | Conexion establecida | Clase `ConexionDB.java` con `DriverManager.getConnection()` |
| e) | Estructura de la BBDD definida | Clase `InicializadorBD.java` con sentencias `CREATE TABLE` (DDL) |
| f) | Aplicacion que modifica datos | `ProductoDAO`: metodos `insertarProducto`, `actualizarStock`, `eliminarProducto` |
| g) | Objetos para almacenar resultados | Clases `Producto.java` y `Usuario.java` (mapeo objeto-relacional) |
| h) | Aplicacion que efectua consultas | `ProductoDAO.obtenerTodos()` y `UsuarioDAO.validarLogin()` con `PreparedStatement` |
| i) | Objetos eliminados tras su funcion | Uso de `try-with-resources` en todos los metodos DAO para cerrar Connection, Statement y ResultSet |
| j) | Transacciones gestionadas | Metodo `simularVentaTransaccional()` con `setAutoCommit(false)`, `rollback()` y restauracion en `finally` |

---

## Datos por defecto

Al ejecutar la aplicacion por primera vez, se crean automaticamente:

- **Usuario**: admin / 1234
- **5 productos de ejemplo**: teclado, raton, monitor, memoria RAM y SSD
