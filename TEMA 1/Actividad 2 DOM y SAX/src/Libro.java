import java.util.ArrayList;
import java.util.List;

public class Libro {
    private String id;
    private String isbn;
    private String titulo;
    private List<String> autores;
    private List<String> categorias;
    private int ano;
    private double precio;
    private String moneda;

    public Libro() {
        this.autores = new ArrayList<>();
        this.categorias = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<String> getAutores() {
        return autores;
    }

    public void anadirAutores(String autores) {
        this.autores.add(autores);
    }

    public List<String> getCategorias() {
        return categorias;
    }

    public void anadirCategoria(String categoria) {
        this.categorias.add(categoria);
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    @Override
    public String toString() {
        // Ponemos la salida idéntica a la esperada
        return "[" + this.id + "] " + this.titulo + " (" + this.ano + ")" + "\n" +
                "ISBN: " + this.isbn + "\n" +
                "Autores: " + this.autores + "\n" +
                "Categorias: " + this.categorias + "\n" +
                "Precio: " + this.precio + " " + this.moneda + "\n";
    }
}
