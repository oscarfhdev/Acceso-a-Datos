import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        File xml = new File("data/catalogo.xml");
        List<Libro> libros = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        int opcion;
        boolean disponible = true;

        while (disponible){

            System.out.println("\nMenú: ");
            System.out.println("1 - DOM ");
            System.out.println("2 - SAX ");
            System.out.println("3 - Salir ");
            System.out.print("Escoge una opción: ");
            opcion = sc.nextInt();

            switch (opcion){
                case 1:{
                    System.out.println("[DOM] Libros:\n");
                    try {
                        libros = LectorDOM.read(xml);
                        libros.forEach(System.out::println);
                        consultasPersonalizadas(libros);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 2:{
                    System.out.println("[SAX] Libros:\n");
                    try {
                        libros = LectorSAX.read(xml);
                        libros.forEach(System.out::println);
                        consultasPersonalizadas(libros);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 3: disponible = false; System.out.println("Saliendo del programa...");
                break;
            }
        }
    }

    private static void consultasPersonalizadas(List<Libro> libros) {
        // Ahora hacemos las consultas personalizadas
        System.out.println("\nSólo títulos publicados después de 2010: ");
        for (Libro libro : libros){
            if (libro.getAno() > 2010) System.out.println(libro);
        }

        System.out.println("\nLibros con más de un autor: ");
        for (Libro libro : libros){
            if (libro.getAutores().size() > 1) System.out.println(libro); // Si tiene más de un autor imprimimos el libro
        }

        System.out.println("\n");
        double sumaEuros = 0;
        int contador = 0;
        for (Libro libro : libros){ //En el bucle, si la monedas es 'EUR' incrementamos el contador y lo sumamos
            if (libro.getMoneda().equals("EUR")) {
                sumaEuros+= libro.getPrecio();
                contador++;
            }
        }
        System.out.println("Precio medio de libros con precio en 'EUR': " + sumaEuros / contador);
    }
}
