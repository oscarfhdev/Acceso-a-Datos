package modelo;

/**
 * Clase que representa un producto de nuestra tienda.
 * Almacenamos los datos básicos: id, nombre, descripción, precio y stock.
 */
public class Producto {

    // Identificador único del producto (lo guardamos como String porque en el XML
    // es un atributo de texto)
    private String id;

    // Nombre del producto
    private String nombre;

    // Breve descripción del producto
    private String descripcion;

    // Precio del producto en euros
    private double precio;

    // Cantidad disponible en el almacén
    private int stock;

    /**
     * Constructor vacío.
     * Lo necesitamos por si queremos crear un Producto y rellenar sus datos después
     * con los setters.
     */
    public Producto() {
    }

    /**
     * Constructor completo con todos los campos.
     * Lo usamos cuando ya tenemos todos los datos disponibles, por ejemplo al leer
     * del XML.
     */
    public Producto(String id, String nombre, String descripcion, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }

    // --- Getters y Setters ---
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    /**
     * Mostramos los datos del producto de forma legible por consola.
     */
    @Override
    public String toString() {
        return "----------------------------------\n" +
                "  ID:          " + id + "\n" +
                "  Nombre:      " + nombre + "\n" +
                "  Descripción: " + descripcion + "\n" +
                // Formateamos el precio con dos decimales para que quede más bonito
                "  Precio:      " + String.format("%.2f", precio) + " €\n" +
                "  Stock:       " + stock + " uds.\n" +
                "----------------------------------";
    }
}
