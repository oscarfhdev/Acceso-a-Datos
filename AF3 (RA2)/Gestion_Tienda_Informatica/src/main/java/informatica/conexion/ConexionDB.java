package informatica.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/* Clase que se encarga de la conexión con la base de datos SQLite.
   SQLite es una base de datos embebida, es decir, no necesita un servidor
   aparte. Toda la información se guarda en un archivo llamado "inventario.db"
   que se crea automáticamente en la raíz del proyecto. */
public class ConexionDB {

    // Esta es la URL de conexión JDBC. Le decimos que use SQLite
    // y que el archivo de la base de datos se llame "inventario.db"
    private static final String URL = "jdbc:sqlite:inventario.db";

    // Este método devuelve una conexión nueva cada vez que se llama.
    // Quien lo use debe cerrar la conexión cuando termine (con try-with-resources)
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
