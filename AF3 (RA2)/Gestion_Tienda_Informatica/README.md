# Gestión de Tienda de Informática - AF3 (RA2) AaD & DIN

Aplicación de consola en Java que simula la gestión del inventario de una pequeña tienda de informática. Para el almacenamiento persistente de los datos se ha utilizado SQLite (base de datos embebida) mediante una conexión JDBC.

---

## 1. Análisis del uso de bases de datos en el entorno profesional

### El caso práctico elegido

Se ha simulado el entorno de una tienda de informática donde es esencial gestionar el **inventario de productos**: conocer la disponibilidad de artículos, controlar el nivel de stock, consultar precios de venta y llevar un registro preciso de reposiciones y ventas.

### ¿Cómo se suelen gestionar estos datos actualmente?

En negocios de menor tamaño, es habitual comenzar utilizando hojas de cálculo (Excel o Google Sheets). Cada vez que se recibe un pedido o se realiza una venta, el archivo se actualiza manualmente. Esta solución puede resultar funcional en sus etapas iniciales, pero genera problemas de sincronización cuando varios empleados necesitan acceder simultáneamente, cuando el volumen del catálogo crece o al intentar consultar datos históricos.

### Ventajas de migrar a una base de datos relacional

* **Consistencia**: La información se centraliza en un único lugar, evitando la redundancia y el problema de tener múltiples versiones de un mismo archivo circulando entre los empleados.
* **Integridad**: Mediante las restricciones propias de SQL (claves primarias, tipos de datos, `NOT NULL`), el sistema previene la introducción de datos erróneos o inconsistentes.
* **Concurrencia**: El sistema de transacciones permite que múltiples usuarios consulten y modifiquen información de forma simultánea sin generar conflictos.
* **Consultas potentes**: El lenguaje SQL facilita la realización de búsquedas complejas y filtrado de datos (por ejemplo, localizar rápidamente los productos sin stock).
* **Escalabilidad**: A diferencia de las hojas de cálculo, que pierden rendimiento al manejar miles de filas, una base de datos relacional mantiene su eficiencia al escalar.

---

## 2. Creación de la base de datos de práctica

### Tecnologías utilizadas

* **Lenguaje**: Java 21 (Java SE).
* **Base de datos**: SQLite (idónea como BBDD embebida, ya que no requiere la instalación ni configuración de un servidor local tipo MySQL o XAMPP).
* **Conector**: JDBC con el driver `sqlite-jdbc` (org.xerial).
* **Gestión de dependencias**: Maven.
* **IDE**: IntelliJ IDEA.

### Estructura del proyecto (Patrón DAO)

```text
src/main/java/informatica/
├── Main.java                      --> Clase principal que controla la ejecución y los menús.
├── conexion/
│   ├── ConexionDB.java            --> Establece la conexión JDBC con SQLite.
│   └── InicializadorBD.java       --> Crea las tablas si no existen e inserta datos iniciales.
├── dao/
│   ├── UsuarioDAO.java            --> Gestiona las consultas de validación de usuarios (Login).
│   └── ProductoDAO.java           --> Centraliza el CRUD completo y la gestión de transacciones.
└── modelo/
    ├── Usuario.java               --> Entidad que representa a un usuario del sistema.
    └── Producto.java              --> Entidad que representa un artículo del inventario.

```

### Diseño de la base de datos

Se ha optado por una estructura normalizada y funcional basada en dos tablas:

**Tabla `usuarios`**

| Campo | Tipo | Restricciones |
| :--- | :--- | :--- |
| id | INTEGER | PRIMARY KEY, AUTOINCREMENT |
| username | TEXT | NOT NULL, UNIQUE |
| password | TEXT | NOT NULL |

**Tabla `productos`**

| Campo | Tipo | Restricciones |
| :--- | :--- | :--- |
| id | INTEGER | PRIMARY KEY, AUTOINCREMENT |
| nombre | TEXT | NOT NULL |
| descripcion | TEXT | |
| precio | REAL | NOT NULL |
| stock | INTEGER | NOT NULL, DEFAULT 0 |

### Operaciones implementadas (CRUD)

La aplicación permite realizar las cuatro operaciones básicas comunicándose directamente con la base de datos:

* **Insertar (Create)**: Registro de nuevos productos introduciendo nombre, descripción, precio y stock.
* **Consultar (Read)**: Visualización completa y formateada del catálogo.
* **Modificar (Update)**: Actualización del nivel de stock de un producto mediante su ID.
* **Eliminar (Delete)**: Borrado definitivo de un artículo del sistema.

*Nota: Para aportar mayor realismo a la práctica, se ha implementado un sistema de autenticación (login) inicial que valida las credenciales del usuario antes de permitir el acceso al menú.*

### Cómo probar la aplicación

1. Abrir el proyecto en IntelliJ IDEA.
2. Ejecutar la clase principal (`Main.java`).
3. Iniciar sesión con el usuario `admin` y la contraseña `1234`.

*(Alternativamente, se puede iniciar mediante consola utilizando Maven: `mvn clean compile exec:java -Dexec.mainClass="informatica.Main"`).*

---

## 3. Simulación de transacciones y reflexión sobre integridad

### Implementación en la aplicación

Para dar cumplimiento al **criterio J**, la opción 5 del menú principal ejecuta una simulación (`simularVentaTransaccional()` en `ProductoDAO`) que ilustra cómo se gestiona un fallo crítico durante un proceso:

1. **Desactivación del auto-commit**: Se establece `conn.setAutoCommit(false)` para evitar que los cambios se apliquen de forma automática.
2. **Ejecución del UPDATE**: Se simula una venta restando la cantidad indicada al stock del producto.
3. **Fallo forzado**: Se lanza una excepción (`SQLException`) intencionadamente para emular un error técnico (ej. interrupción de conexión o fallo en la pasarela de pago).
4. **Reversión de cambios**: En el bloque `catch`, se captura dicha excepción y se ejecuta `conn.rollback()`, deshaciendo el `UPDATE` previo.
5. **Restauración (Finally)**: Se vuelve a habilitar el auto-commit (`true`) y se cierra la conexión de forma segura.

*Resultado:* Al producirse la interrupción, la base de datos revierte la operación, por lo que el nivel de stock inicial se mantiene intacto, garantizando la consistencia del inventario.

### Guion SQL equivalente

La simulación realizada en Java equivale al siguiente flujo de operaciones en SQL puro:

```sql
-- Inicio de la transacción (desactiva el auto-commit):
BEGIN TRANSACTION;

-- Operación de venta (ej. restar 5 unidades al producto con ID 1):
UPDATE productos SET stock = stock - 5 WHERE id = 1;

-- Se produce un error en el sistema. Se revierten los cambios:
ROLLBACK;

-- (Si la operación hubiera sido exitosa, se confirmaría con COMMIT;)

```

### ¿Por qué esto es fundamental en un entorno real?

En un entorno de producción, este mecanismo es imprescindible. Al realizar una transacción comercial, el sistema debe descontar el stock e intentar procesar el cobro. Si la pasarela de pago falla, la operación debe cancelarse por completo. Las transacciones garantizan la atomicidad ("todo o nada"): si algún eslabón del proceso falla, se aplica un `rollback` y los datos regresan a su estado original, previniendo descuadres contables y errores graves en el inventario.

---

## Correspondencia con los criterios de evaluación (RA2)

A continuación, se detalla cómo se ha cubierto cada punto de la rúbrica de evaluación:

| Criterio | Descripción | Implementación en el proyecto |
| --- | --- | --- |
| **a)** | Ventajas e inconvenientes de conectores | Sección 1 de este documento (análisis frente a hojas de cálculo). |
| **b)** | Gestor de BBDD embebido | Implementación de SQLite (archivo `.db` local independiente de servidores externos). |
| **c)** | Conector idóneo | Importación y configuración del Driver JDBC `sqlite-jdbc` en `pom.xml`. |
| **d)** | Conexión establecida | Gestionada en la clase `ConexionDB.java` mediante la API `DriverManager.getConnection()`. |
| **e)** | Estructura de la BBDD definida | La clase `InicializadorBD.java` emplea sentencias DDL (`CREATE TABLE IF NOT EXISTS`) en el primer arranque. |
| **f)** | Aplicación que modifica datos | Desarrollo del CRUD operativo en la clase `ProductoDAO` (inserción, actualización y borrado). |
| **g)** | Objetos para almacenar resultados | Creación de las entidades POJO `Producto.java` y `Usuario.java` para el mapeo objeto-relacional. |
| **h)** | Aplicación que efectúa consultas | Todas las operaciones SQL se ejecutan de forma segura mediante `PreparedStatement`. |
| **i)** | Objetos eliminados tras su función | Uso estricto y sistemático de bloques `try-with-resources` en los DAOs, garantizando el cierre automático de conexiones (`Connection`, `Statement`, `ResultSet`). |
| **j)** | Transacciones gestionadas | Implementado en el método `simularVentaTransaccional()` con gestión manual de `auto-commit`, `rollback()` y `commit()`. |

---

*Nota adicional: Al compilar y ejecutar la aplicación por primera vez, el sistema autogenera el usuario administrador (`admin` / `1234`) e inserta 5 productos variados de demostración, permitiendo evaluar el funcionamiento del CRUD inmediatamente sin necesidad de introducir registros manuales.*