package informatica.modelo;

/* Esta clase representa un producto del inventario de la tienda.
   Cada objeto Producto se corresponde con una fila de la tabla "productos"
   en la base de datos. Los campos son: id, nombre, descripción, precio y stock. */
public class Producto {

    // Campos que mapean las columnas de la tabla "productos"
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;

    // Constructor vacío por si necesitamos crear un producto sin datos todavía
    public Producto() {
    }

    // Constructor sin id, lo usamos para insertar productos nuevos
    // porque el id lo genera la base de datos automáticamente (AUTOINCREMENT)
    public Producto(String nombre, String descripcion, double precio, int stock) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }

    // Constructor con todos los campos, lo usamos cuando leemos
    // un producto de la base de datos y ya tiene id asignado
    public Producto(int id, String nombre, String descripcion, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }

    // Getters y setters para cada campo

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    // Método toString para mostrar el producto en consola de forma clara
    @Override
    public String toString() {
        return "ID: " + id
                + " - Nombre: " + nombre
                + " - Descripción: " + descripcion
                + " - Precio: " + String.format("%.2f", precio) + " EUR"
                + " - Stock: " + stock;
    }
}
