package conexion;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

/**
 * Clase de utilidad para cargar y guardar documentos XML usando la API DOM.
 * Todos los métodos son estáticos porque no necesitamos crear instancias de
 * esta clase.
 */
public class ConexionXML {

    /**
     * Cargamos el archivo XML como un Document DOM.
     * Si el archivo no existe todavía, creamos un Document nuevo con el elemento
     * raíz "tienda"
     * para que la aplicación funcione desde la primera ejecución sin problemas.
     */
    public static Document cargarDOM(String ruta) {
        try {
            // Creamos la fábrica y el constructor de documentos DOM
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Comprobamos si el archivo XML ya existe en disco
            File archivo = new File(ruta);

            if (archivo.exists()) {
                // Si existe, lo parseamos directamente y devolvemos el Document
                Document doc = builder.parse(archivo);
                // Normalizamos la estructura del DOM para evitar nodos de texto vacíos
                // duplicados
                doc.getDocumentElement().normalize();
                return doc;
            } else {
                // Si no existe, creamos un Document vacío con la raíz <tienda>
                Document doc = builder.newDocument();
                // Añadimos el elemento raíz llamado "tienda"
                doc.appendChild(doc.createElement("tienda"));
                // Guardamos ese Document vacío en disco para tenerlo listo
                guardarDOM(doc, ruta);
                return doc;
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            // Si ocurre algún error inesperado, lo mostramos por consola
            System.err.println("Error al cargar el archivo XML: " + e.getMessage());
            return null;
        }
    }

    /**
     * Volcamos el árbol DOM al archivo físico en la ruta indicada.
     * Configuramos el Transformer para que el XML resultante quede bien indentado
     * y con la codificación UTF-8, así es fácil de leer si abrimos el archivo.
     */
    public static void guardarDOM(Document doc, String ruta) {
        try {
            // Creamos el Transformer que se encarga de escribir el DOM a un archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            // Limpiamos los espacios en blanco del DOM antes de guardarlo
            limpiarEspaciosEnBlanco(doc);

            // Configuramos la salida
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "no");

            // Comprobamos si la carpeta padre existe, y si no, la creamos
            File archivo = new File(ruta);
            File carpetaPadre = archivo.getParentFile();
            if (carpetaPadre != null && !carpetaPadre.exists()) {
                carpetaPadre.mkdirs();
            }

            // Definimos el origen (el DOM en memoria) y el destino (el archivo en disco)
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(archivo);

            // Ejecutamos la transformación, es decir, volcamos el DOM al archivo
            transformer.transform(source, result);

        } catch (TransformerException e) {
            // Si hay algún problema al guardar, lo mostramos
            System.err.println("Error al guardar el archivo XML: " + e.getMessage());
        }
    }

    /**
     * Limpia los nodos de texto vacíos que se generan al modificar el DOM.
     * Si no hacemos esto, el Transformer va acumulando saltos de línea y
     * espacios cada vez que leemos y guardamos el archivo.
     */
    private static void limpiarEspaciosEnBlanco(org.w3c.dom.Node nodo) {
        org.w3c.dom.NodeList hijos = nodo.getChildNodes();
        for (int i = hijos.getLength() - 1; i >= 0; i--) {
            org.w3c.dom.Node hijo = hijos.item(i);
            if (hijo.getNodeType() == org.w3c.dom.Node.TEXT_NODE && hijo.getNodeValue().trim().isEmpty()) {
                nodo.removeChild(hijo);
            } else if (hijo.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                limpiarEspaciosEnBlanco(hijo);
            }
        }
    }
}
