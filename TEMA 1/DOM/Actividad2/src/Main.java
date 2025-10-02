import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        File xml = new File("Actividad2/data/catalogo.xml");
        try {
            // Imprimimos primeramente la lista que obtenenmos del método .read()
            List<Libro> libros = LectorDOM.read(xml);
            System.out.println("[DOM] Libros:\n");
            libros.forEach(System.out::println);


            // Ahora hacemos las consultas personalizadas
            System.out.println("\nSólo títulos publicados después de 2010: ");
            for (Libro libro : libros){
                if (libro.getAno() > 2010) System.out.println(libro);
            }

            System.out.println("\nLibros con más de un autor: ");
            for (Libro libro : libros){
                if (libro.getAutores().size() > 1) System.out.println(libro); // Si tiene más de un autor imprimimos el libro
            }

            System.out.println("\nMedia de precio en libros con 'EUR': ");
            double sumaEuros = 0;
            int contador = 0;
            for (Libro libro : libros){ //En el bucle, si la monedas es 'EUR' incrementamos el contador y lo sumamos
                if (libro.getMoneda().equals("EUR")) {
                    sumaEuros+= libro.getPrecio();
                    contador++;
                }
            }
            System.out.println("Precio medio de libros con precio en 'EUR': " + sumaEuros / contador);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
