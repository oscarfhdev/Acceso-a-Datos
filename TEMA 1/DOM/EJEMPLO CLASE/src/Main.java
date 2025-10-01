import java.io.File;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
//        if (args.length == 0) {
//            System.out.println("Uso: java Main [dom|sax|stax] [ruta_xml_opcional]");
//            return;
//        }
        String mode = "dom";
        File xml = new File("EJEMPLO CLASE/src/books.xml");

        List<Book> books;
        books = DomReader.read(xml);
        System.out.println("[DOM] Libros:");

        books.forEach(System.out::println);
    }
}
