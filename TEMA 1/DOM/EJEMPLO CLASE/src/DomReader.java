import org.w3c.dom.*; // Element
import javax.xml.parsers.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DomReader {

    public static List<Book> read(File xmlFile) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(xmlFile);

        NodeList nodes = doc.getElementsByTagName("book");
        List<Book> books = new ArrayList<>();

        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);
            Book b = new Book();
            b.id = e.getAttribute("id");
            b.title  = textOf(e, "title");

            NodeList authors = e.getElementsByTagName("author");
            for (int j = 0; j < authors.getLength(); j++) {
                b.authors.add(((Text) authors.item(j)).getTextContent());
            }
            b.year   = parseIntSafe(textOf(e, "year"));
            b.price  = parseDoubleSafe(textOf(e, "price"));
            books.add(b);
        }
        return books;
    }

    private static String textOf(Element parent, String tag) {
        NodeList nl = parent.getElementsByTagName(tag);
        return (nl.getLength() > 0) ? nl.item(0).getTextContent().trim() : "";
    }

    private static int parseIntSafe(String s) {
        return s.isEmpty() ? 0 : Integer.parseInt(s);
    }

    private static double parseDoubleSafe(String s) {
        return s.isEmpty() ? 0.0 : Double.parseDouble(s);
    }
}
