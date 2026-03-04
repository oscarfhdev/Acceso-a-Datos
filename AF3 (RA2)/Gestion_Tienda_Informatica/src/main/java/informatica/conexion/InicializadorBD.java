package informatica.conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/* Esta clase se encarga de preparar la base de datos cuando arranca la aplicación.
   Crea las tablas si es la primera vez que se ejecuta, y mete unos datos
   de ejemplo para que la app no arranque vacía (un usuario admin y varios productos).
   Así separamos la lógica de inicialización de la lógica del CRUD en los DAOs.
 */
public class InicializadorBD {

    // Método principal: se llama una vez desde el Main al arrancar
    public static void inicializar() {
        crearTablas();
        insertarUsuarioPorDefecto();
        insertarProductosPorDefecto();
    }

    // Crea las tablas "usuarios" y "productos" en la base de datos.
    // Usamos CREATE TABLE IF NOT EXISTS para que no dé error si ya existen.
    private static void crearTablas() {

        // Sentencia DDL para la tabla de usuarios
        String tablaUsuarios = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL UNIQUE, "
                + "password TEXT NOT NULL)";

        // Sentencia DDL para la tabla de productos
        String tablaProductos = "CREATE TABLE IF NOT EXISTS productos ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nombre TEXT NOT NULL, "
                + "descripcion TEXT, "
                + "precio REAL NOT NULL, "
                + "stock INTEGER NOT NULL DEFAULT 0)";

        // Abrimos conexión y ejecutamos las dos sentencias.
        // Usamos try-with-resources para que se cierre todo automáticamente
        try (Connection conn = ConexionDB.getConnection();
                Statement stmt = conn.createStatement()) {

            stmt.execute(tablaUsuarios);
            stmt.execute(tablaProductos);
            System.out.println("[INFO] Tablas creadas o verificadas correctamente.");

        } catch (SQLException e) {
            System.err.println("[ERROR] No se pudieron crear las tablas: " + e.getMessage());
        }
    }

    /*
     * Inserta el usuario administrador por defecto (admin / 1234).
     * Usamos INSERT OR IGNORE para que si ya existe no dé error ni lo duplique.
     * Usamos PreparedStatement con interrogaciones (?) para evitar inyección SQL.
     */
    private static void insertarUsuarioPorDefecto() {
        String sql = "INSERT OR IGNORE INTO usuarios (username, password) VALUES (?, ?)";

        try (Connection conn = ConexionDB.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asignamos los valores a los interrogantes de la consulta
            pstmt.setString(1, "admin");
            pstmt.setString(2, "1234");
            int filas = pstmt.executeUpdate();

            if (filas > 0) {
                System.out.println("[INFO] Usuario 'admin' creado.");
            } else {
                System.out.println("[INFO] Usuario 'admin' ya existe.");
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] No se pudo insertar el usuario: " + e.getMessage());
        }
    }

    /*
     * Mete productos de ejemplo en la base de datos, pero solo si la tabla
     * está vacía. Así no se duplican cada vez que arrancamos la app.
     * Usamos addBatch para agrupar varios INSERT y ejecutarlos todos de golpe.
     */
    private static void insertarProductosPorDefecto() {
        String sqlCount = "SELECT COUNT(*) FROM productos";
        String sqlInsert = "INSERT INTO productos (nombre, descripcion, precio, stock) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlCount)) {

            // Comprobamos cuántos productos hay en la tabla
            if (rs.next() && rs.getInt(1) == 0) {

                // La tabla está vacía, así que metemos los productos de ejemplo
                try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {

                    // Producto 1: teclado
                    pstmt.setString(1, "Teclado mecánico RGB");
                    pstmt.setString(2, "Teclado gaming con switches Cherry MX");
                    pstmt.setDouble(3, 79.99);
                    pstmt.setInt(4, 25);
                    pstmt.addBatch();

                    // Producto 2: ratón
                    pstmt.setString(1, "Ratón inalámbrico");
                    pstmt.setString(2, "Ratón ergonómico 2.4GHz con receptor USB");
                    pstmt.setDouble(3, 29.95);
                    pstmt.setInt(4, 50);
                    pstmt.addBatch();

                    // Producto 3: monitor
                    pstmt.setString(1, "Monitor 27 pulgadas 4K");
                    pstmt.setString(2, "Panel IPS, 60Hz, HDR10");
                    pstmt.setDouble(3, 349.00);
                    pstmt.setInt(4, 10);
                    pstmt.addBatch();

                    // Producto 4: memoria RAM
                    pstmt.setString(1, "Memoria RAM DDR5 16GB");
                    pstmt.setString(2, "DDR5 5600MHz CL36");
                    pstmt.setDouble(3, 54.50);
                    pstmt.setInt(4, 40);
                    pstmt.addBatch();

                    // Producto 5: disco SSD
                    pstmt.setString(1, "SSD NVMe 1TB");
                    pstmt.setString(2, "PCIe Gen4, lectura 7000MB/s");
                    pstmt.setDouble(3, 89.99);
                    pstmt.setInt(4, 30);
                    pstmt.addBatch();

                    // Ejecutamos todos los INSERT de golpe
                    pstmt.executeBatch();
                    System.out.println("[INFO] 5 productos de ejemplo insertados.");
                }
            } else {
                System.out.println("[INFO] Ya hay productos en la base de datos.");
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] No se pudieron insertar los productos: " + e.getMessage());
        }
    }
}
