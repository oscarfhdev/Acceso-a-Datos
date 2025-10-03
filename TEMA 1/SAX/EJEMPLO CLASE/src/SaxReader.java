import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SaxReader {

    public static List<Book> read(File xmlFile) throws Exception {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser parser = spf.newSAXParser();

        List<Book> books = new ArrayList<>();

        DefaultHandler handler = new DefaultHandler() {
            Book current;
            StringBuilder content = new StringBuilder();

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) {
                if ("book".equals(qName)) {
                    current = new Book();
                    current.id = attributes.getValue("id");
                }
                content.setLength(0);
            }

            @Override
            public void characters(char[] ch, int start, int length) {
                content.append(ch, start, length);
            }

            @Override
            public void endElement(String uri, String localName, String qName) {
                String text = content.toString().trim();
                if (current != null) {
                    switch (qName) {
                        case "title":  current.title  = text; break;
                        case "author": current.authors.add(text); break;
                        case "year":   if (!text.isEmpty()) current.year  = Integer.parseInt(text); break;
                        case "price":  if (!text.isEmpty()) current.price = Double.parseDouble(text); break;
                        case "book":   books.add(current); current = null; break;
                    }
                }
            }
        };

        parser.parse(xmlFile, handler);
        return books;
    }
}