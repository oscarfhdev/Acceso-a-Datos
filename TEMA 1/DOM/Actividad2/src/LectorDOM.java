import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LectorDOM {
    public static List<Libro> read(File xmlFile) throws Exception {

        // Creamos el documento
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(xmlFile);

        // Obtenemos todos los libros
        NodeList nodosLibro = doc.getElementsByTagName("book");
        List<Libro> libros = new ArrayList<>();

        // Recorremos los libros uno por uno
        for (int i = 0; i < nodosLibro.getLength(); i++) {
            Element libroObtenido = (Element) nodosLibro.item(i);
            // Creamos una instancia de la clase libro
            Libro libro = new Libro();

            // Con los setters, introducimos la información
            libro.setId(libroObtenido.getAttribute("id"));
            libro.setIsbn(libroObtenido.getAttribute("isbn"));
            libro.setTitulo(obtenerTexto(libroObtenido, "title"));

            // Ahora tenemos una lista con los autores, la recorremos
            NodeList autores = libroObtenido.getElementsByTagName("author");
            for (int j = 0; j < autores.getLength(); j++) {
                String role = ((Element)autores.item(j)).getAttribute("role"); // Obtenemos el atributo rol
                if (role.isEmpty()){ // Si no hay 'role', no añadimos los ()
                    libro.anadirAutores(autores.item(j).getTextContent()); // Guardamos el contenido de la etiqueta
                }
                else {
                    libro.anadirAutores(autores.item(j).getTextContent() + " (" + role + ")"); // Guardamos el contenido y el rol entre ()
                }
            }

            // Hacemos lo mismo con categorias
            NodeList categorias = libroObtenido.getElementsByTagName("category");
            for (int j = 0; j < categorias.getLength(); j++) {
                    libro.anadirCategoria(categorias.item(j).getTextContent()); // Guardamos en el array la categoría
            }

            // Obtenemos el atributo, para ello necesitamos un nodeList, como solo hay 1 <price> no hacemos bucle
            NodeList price = libroObtenido.getElementsByTagName("price");
            libro.setMoneda(((Element) price.item(0)).getAttribute("currency")); // guardamos la moneda

            // Obtenemos los dígitos y los parseamos con métodos personalizados para evitar excepciones
            libro.setAno(parsearIntSeguro(obtenerTexto(libroObtenido, "year")));
            libro.setPrecio(parsearDoubleSeguro(obtenerTexto(libroObtenido, "price")));

            // Añadimos el libro al array
            libros.add(libro);
        }

        return libros;
    }

    private static String obtenerTexto(Element parent, String tag) {
        NodeList nl = parent.getElementsByTagName(tag);
        return (nl.getLength() > 0) ? nl.item(0).getTextContent().trim() : ""; // Si no hay texto devuelve vacío ""
    }

    private static int parsearIntSeguro(String cadenaAConvertir) {
        return cadenaAConvertir.isEmpty() ? 0 : Integer.parseInt(cadenaAConvertir); // Si la cadena está vacía devuelve 0
    }
    private static double parsearDoubleSeguro(String cadenaAConvertir) {
        return cadenaAConvertir.isEmpty() ? 0 : Double.parseDouble(cadenaAConvertir);
    }

}
