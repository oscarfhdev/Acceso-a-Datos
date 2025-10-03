import java.io.File;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        File xml = new File("EJEMPLO CLASE/src/books.xml");

        List<Book> books;
        books = SaxReader.read(xml);
        System.out.println("[SAX] Libros:");

        books.forEach(System.out::println);
    }
}
