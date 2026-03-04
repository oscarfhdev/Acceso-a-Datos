package AF3.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import AF3.conexion.ConexionDB;
import AF3.modelo.Producto;

/* DAO (Data Access Object) para la tabla de productos.
   Aquí están todas las operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
   y el método de simulación de transacción con rollback.
   Todos los métodos usan try-with-resources para cerrar las conexiones
   automáticamente cuando terminan. */
public class ProductoDAO {

    /*
     * INSERT: inserta un producto nuevo en la base de datos.
     * Recibe un objeto Producto con los datos que ha metido el usuario
     * y los inserta en la tabla usando PreparedStatement.
     */
    public void insertarProducto(Producto producto) {
        String sql = "INSERT INTO productos (nombre, descripcion, precio, stock) VALUES (?, ?, ?, ?)";

        // Abrimos conexión y preparamos el INSERT con los datos del producto
        try (Connection conn = ConexionDB.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Rellenamos cada interrogante con los valores del producto
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getDescripcion());
            pstmt.setDouble(3, producto.getPrecio());
            pstmt.setInt(4, producto.getStock());

            // Ejecutamos la inserción
            pstmt.executeUpdate();

            System.out.println("Producto '" + producto.getNombre() + "' insertado correctamente.");

        } catch (SQLException e) {
            System.err.println("[ERROR] No se pudo insertar el producto: " + e.getMessage());
        }
    }

    /*
     * SELECT: obtiene todos los productos de la base de datos.
     * Devuelve una lista de objetos Producto. Cada fila del ResultSet
     * se convierte en un objeto Producto (esto es el mapeo objeto-relacional).
     */
    public List<Producto> obtenerTodos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion, precio, stock FROM productos";

        // Abrimos conexión, creamos el Statement y ejecutamos la consulta.
        // Los tres recursos se cierran automáticamente con try-with-resources.
        try (Connection conn = ConexionDB.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // Recorremos todas las filas del resultado
            while (rs.next()) {
                // Por cada fila creamos un objeto Producto con los datos de la BD
                Producto p = new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"));
                // Lo añadimos a la lista
                productos.add(p);
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] No se pudieron obtener los productos: " + e.getMessage());
        }

        return productos;
    }

    // UPDATE: actualiza el stock de un producto existente.
    // Recibe el ID del producto y el nuevo valor de stock.
    public void actualizarStock(int id, int nuevoStock) {
        String sql = "UPDATE productos SET stock = ? WHERE id = ?";

        try (Connection conn = ConexionDB.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Primer interrogante: el nuevo stock. Segundo: el ID del producto
            pstmt.setInt(1, nuevoStock);
            pstmt.setInt(2, id);

            // executeUpdate devuelve cuántas filas se han modificado
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Stock del producto con ID " + id + " actualizado a " + nuevoStock + ".");
            } else {
                // Si no se modificó ninguna fila, es que no existe ese ID
                System.out.println("No se encontró ningún producto con ID " + id + ".");
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] No se pudo actualizar el stock: " + e.getMessage());
        }
    }

    // DELETE: elimina un producto de la base de datos por su ID.
    public void eliminarProducto(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";

        try (Connection conn = ConexionDB.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            // Comprobamos si realmente se eliminó algo
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Producto con ID " + id + " eliminado correctamente.");
            } else {
                System.out.println("No se encontró ningún producto con ID " + id + ".");
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] No se pudo eliminar el producto: " + e.getMessage());
        }
    }

    /*
     * TRANSACCIÓN: este método simula una venta para demostrar cómo funciona
     * el control de transacciones con commit y rollback.
     * 
     * Lo que hace es:
     * 1. Desactiva el auto-commit (para controlar la transacción a mano)
     * 2. Ejecuta un UPDATE restando stock del producto
     * 3. Fuerza un error a propósito (lanza una excepción)
     * 4. En el catch hace rollback, deshaciendo el UPDATE
     * 5. En el finally restaura el auto-commit y cierra la conexión
     * 
     * El resultado es que el stock NO cambia, porque el rollback lo revierte.
     */
    public void simularVentaTransaccional(int idProducto, int cantidadVendida) {
        // Necesitamos la conexión fuera del try para poder usarla en catch y finally
        Connection conn = null;

        try {
            // Obtenemos la conexión
            conn = ConexionDB.getConnection();

            // Desactivamos el auto-commit: ahora los cambios no se guardan
            // automáticamente, sino que tenemos que hacer commit manualmente
            conn.setAutoCommit(false);

            System.out.println("\n--- SIMULACIÓN DE TRANSACCIÓN ---");
            System.out.println("Auto-commit desactivado. Transacción iniciada.");

            // Preparamos el UPDATE para restar la cantidad vendida al stock
            String sql = "UPDATE productos SET stock = stock - ? WHERE id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, cantidadVendida);
                pstmt.setInt(2, idProducto);

                int filasAfectadas = pstmt.executeUpdate();

                if (filasAfectadas > 0) {
                    System.out.println("UPDATE ejecutado: se restaron " + cantidadVendida
                            + " unidades del producto ID " + idProducto + ".");
                } else {
                    System.out.println("No se encontró el producto con ID " + idProducto + ".");
                }
            }

            // Aquí forzamos un error a propósito lanzando una excepción.
            // En la vida real esto sería un fallo inesperado (conexión caída, etc.)
            // Como no hemos hecho commit, el UPDATE de arriba no se ha guardado todavía
            System.out.println("Simulando un error inesperado...");
            throw new SQLException("ERROR SIMULADO: Fallo forzado para demostrar el rollback.");

        } catch (SQLException e) {
            // Entramos aquí porque se lanzó la excepción (simulada o real)
            System.err.println("Error en la transacción: " + e.getMessage());

            // Hacemos rollback para deshacer todos los cambios de esta transacción
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Se ha ejecutado ROLLBACK correctamente.");
                    System.out.println("La integridad de los datos se ha mantenido.");
                    System.out.println("El stock del producto NO ha sido modificado.");
                } catch (SQLException rollbackEx) {
                    System.err.println("Fallo al ejecutar rollback: " + rollbackEx.getMessage());
                }
            }

        } finally {
            // El bloque finally se ejecuta siempre, haya error o no.
            // Aquí restauramos el auto-commit y cerramos la conexión.
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                    System.out.println("Auto-commit restaurado. Conexión cerrada.");
                    System.out.println("--- FIN DE SIMULACIÓN ---\n");
                } catch (SQLException closeEx) {
                    System.err.println("Fallo al cerrar la conexión: " + closeEx.getMessage());
                }
            }
        }
    }
}
