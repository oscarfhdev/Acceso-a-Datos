package AF3.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import AF3.conexion.ConexionDB;

/* DAO (Data Access Object) para la tabla de usuarios.
   Esta clase solo se encarga de las consultas relacionadas con usuarios.
   En este caso, solo necesitamos validar el login. */
public class UsuarioDAO {

    /*
     * Comprueba si las credenciales que ha metido el usuario son correctas.
     * Hace un SELECT a la tabla "usuarios" buscando coincidencia
     * de username y password. Devuelve true si encuentra al menos un resultado.
     */
    public boolean validarLogin(String username, String password) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE username = ? AND password = ?";

        /*
         * Abrimos conexión y preparamos la consulta con PreparedStatement.
         * Los interrogantes (?) se rellenan con setString para evitar
         * inyección SQL (no concatenamos los valores directamente en la query)
         */
        try (Connection conn = ConexionDB.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asignamos los valores a cada interrogante
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            // Ejecutamos la consulta y leemos el resultado
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Si el COUNT devuelve más de 0, es que las credenciales son válidas
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] Fallo en la validación de login: " + e.getMessage());
        }

        // Si hay algún error o no se encontró el usuario, devolvemos false
        return false;
    }
}
