package AF3.modelo;

/* Esta clase representa a un usuario del sistema.
   Tiene los mismos campos que la tabla "usuarios" de la base de datos:
   id, username y password. */
public class Usuario {

    // Campos que se corresponden con las columnas de la tabla
    private int id;
    private String username;
    private String password;

    // Constructor vacío, lo necesitamos para poder crear objetos vacíos
    // y luego rellenarlos con los setters
    public Usuario() {
    }

    // Constructor con todos los campos, útil cuando leemos de la BD
    public Usuario(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // Getters y setters para acceder y modificar cada campo

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Para imprimir el usuario por consola de forma legible
    @Override
    public String toString() {
        return "Usuario{id=" + id + ", username='" + username + "'}";
    }
}
