package dao;

import conexion.ConexionXML;
import modelo.Producto;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) que gestiona las operaciones CRUD
 * sobre los productos almacenados en el archivo XML.
 * Usamos la API DOM para leer, añadir, modificar y eliminar nodos.
 */
public class ProductoXMLDAO {

    // Ruta por defecto del archivo XML donde guardamos los productos
    private final String rutaXml;

    /**
     * Constructor por defecto.
     * Usamos "productos.xml" como nombre del archivo si no se indica otro.
     */
    public ProductoXMLDAO() {
        // Obtenemos la ruta base del proyecto asegurándonos de que caiga en /datos/
        String dirActual = System.getProperty("user.dir");
        if (dirActual.endsWith("Gestion_XML_Tienda")) {
            this.rutaXml = dirActual + java.io.File.separator + "datos" + java.io.File.separator + "productos.xml";
        } else {
            this.rutaXml = dirActual + java.io.File.separator + "Gestion_XML_Tienda" + java.io.File.separator + "datos"
                    + java.io.File.separator + "productos.xml";
        }
    }

    /**
     * Insertamos un nuevo producto en el archivo XML.
     * Generamos el ID automáticamente buscando el ID numérico más alto que ya
     * exita y le sumamos 1 para que no se repita.
     */
    public void insertarProducto(Producto p) {
        // Cargamos el DOM actual desde el archivo
        Document doc = ConexionXML.cargarDOM(rutaXml);

        // Si el documento es null, algo ha ido mal y no podemos continuar
        if (doc == null) {
            System.err.println("No se pudo cargar el documento XML.");
            return;
        }

        // Buscamos el ID más alto que ya está en el XML para generar el siguiente
        int maxId = 0;
        // Obtenemos todos los nodos <producto> que existan
        NodeList productos = doc.getElementsByTagName("producto");
        // Recorremos la lista para encontrar el ID numérico mayor
        for (int i = 0; i < productos.getLength(); i++) {
            Element elem = (Element) productos.item(i);
            // Leemos el atributo "id" de cada producto
            String idStr = elem.getAttribute("id");
            try {
                // Intentamos convertirlo a número
                int idNum = Integer.parseInt(idStr);
                // Si es mayor que el máximo actual, lo actualizamos
                if (idNum > maxId) {
                    maxId = idNum;
                }
            } catch (NumberFormatException e) {
                // Si el id no es numérico, lo ignoramos y seguimos
            }
        }

        // Generamos el nuevo ID sumando 1 al máximo encontrado
        String nuevoId = String.valueOf(maxId + 1);
        // Asignamos el ID generado al producto
        p.setId(nuevoId);

        // Creamos el elemento <producto> y le añadimos el atributo id
        Element elemProducto = doc.createElement("producto");
        elemProducto.setAttribute("id", nuevoId);

        // Creamos la etiqueta <nombre> con el texto del producto
        Element elemNombre = doc.createElement("nombre");
        elemNombre.setTextContent(p.getNombre());
        elemProducto.appendChild(elemNombre);

        // Creamos la etiqueta <descripcion>
        Element elemDescripcion = doc.createElement("descripcion");
        elemDescripcion.setTextContent(p.getDescripcion());
        elemProducto.appendChild(elemDescripcion);

        // Creamos la etiqueta <precio>
        Element elemPrecio = doc.createElement("precio");
        elemPrecio.setTextContent(String.valueOf(p.getPrecio()));
        elemProducto.appendChild(elemPrecio);

        // Creamos la etiqueta <stock>
        Element elemStock = doc.createElement("stock");
        elemStock.setTextContent(String.valueOf(p.getStock()));
        elemProducto.appendChild(elemStock);

        // Añadimos el nodo <producto> completo al elemento raíz <tienda>
        doc.getDocumentElement().appendChild(elemProducto);

        // Guardamos los cambios en el archivo físico
        ConexionXML.guardarDOM(doc, rutaXml);

        // Informamos al usuario de que todo ha ido bien
        System.out.println("Producto insertado correctamente con ID: " + nuevoId);
    }

    /**
     * Listamos todos los productos que hay en el archivo XML.
     * Recorremos los nodos y creamos objetos Producto para devolver una lista
     * cómoda de usar.
     */
    public List<Producto> listarProductos() {
        // Creamos la lista vacía donde iremos añadiendo los productos
        List<Producto> lista = new ArrayList<>();

        // Cargamos el DOM desde el archivo
        Document doc = ConexionXML.cargarDOM(rutaXml);

        // Si no se pudo cargar, retornamos la lista vacía
        if (doc == null) {
            System.err.println("No se pudo cargar el documento XML.");
            return lista;
        }

        // Obtenemos todos los nodos con la etiqueta "producto"
        NodeList productos = doc.getElementsByTagName("producto");

        // Recorremos la lista de nodos producto
        for (int i = 0; i < productos.getLength(); i++) {
            // Convertimos cada nodo a Element para poder acceder a sus atributos y
            // subelementos
            Element elem = (Element) productos.item(i);

            // Leemos el atributo id
            String id = elem.getAttribute("id");

            // Leemos el contenido de texto de cada subelemento
            String nombre = obtenerTextoEtiqueta(elem, "nombre");
            String descripcion = obtenerTextoEtiqueta(elem, "descripcion");

            // Convertimos precio y stock a sus tipos numéricos correspondientes
            double precio = Double.parseDouble(obtenerTextoEtiqueta(elem, "precio"));
            int stock = Integer.parseInt(obtenerTextoEtiqueta(elem, "stock"));

            // Creamos el objeto Producto con los datos leídos y lo añadimos a la lista
            Producto producto = new Producto(id, nombre, descripcion, precio, stock);
            lista.add(producto);
        }

        // Devolvemos la lista completa de productos
        return lista;
    }

    /**
     * Actualizamos el precio de un producto buscándolo por su atributo ID.
     * Si lo encontramos, modificamos el texto de la etiqueta <precio> y guardamos.
     */
    public void actualizarPrecio(String id, double nuevoPrecio) {
        // Cargamos el DOM
        Document doc = ConexionXML.cargarDOM(rutaXml);

        if (doc == null) {
            System.err.println("No se pudo cargar el documento XML.");
            return;
        }

        // Obtenemos todos los nodos <producto>
        NodeList productos = doc.getElementsByTagName("producto");

        // Variable para saber si encontramos el producto
        boolean encontrado = false;

        // Recorremos los nodos buscando el que tenga el ID indicado
        for (int i = 0; i < productos.getLength(); i++) {
            Element elem = (Element) productos.item(i);

            // Comparamos el atributo id con el que buscamos
            if (elem.getAttribute("id").equals(id)) {
                // Accedemos a la etiqueta <precio> dentro de este producto
                NodeList precioNodes = elem.getElementsByTagName("precio");
                // Comprobamos que la etiqueta <precio> existe
                if (precioNodes.getLength() > 0) {
                    // Modificamos el contenido de texto con el nuevo precio
                    precioNodes.item(0).setTextContent(String.valueOf(nuevoPrecio));
                }
                // Marcamos que lo hemos encontrado
                encontrado = true;
                // No necesitamos seguir buscando
                break;
            }
        }

        if (encontrado) {
            // Guardamos los cambios en el archivo físico
            ConexionXML.guardarDOM(doc, rutaXml);
            System.out.println("Precio actualizado correctamente para el producto con ID: " + id);
        } else {
            // Si no lo encontramos, avisamos al usuario
            System.out.println("No se encontró ningún producto con ID: " + id);
        }
    }

    /**
     * Eliminamos un producto del archivo XML buscándolo por su atributo ID.
     * Usamos removeChild para borrarlo de su nodo padre.
     */
    public void eliminarProducto(String id) {
        // Cargamos el DOM
        Document doc = ConexionXML.cargarDOM(rutaXml);

        if (doc == null) {
            System.err.println("No se pudo cargar el documento XML.");
            return;
        }

        // Obtenemos todos los nodos <producto>
        NodeList productos = doc.getElementsByTagName("producto");

        // Variable para controlar si hemos encontrado el producto
        boolean encontrado = false;

        // Recorremos los nodos buscando el ID del producto a eliminar
        for (int i = 0; i < productos.getLength(); i++) {
            Node nodo = productos.item(i);

            // Comprobamos que es un nodo de tipo Element
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nodo;

                // Si el ID coincide, procedemos a eliminarlo
                if (elem.getAttribute("id").equals(id)) {
                    // Usamos removeChild para borrar el nodo de su padre
                    elem.getParentNode().removeChild(elem);
                    encontrado = true;
                    // Salimos del bucle porque ya lo hemos eliminado
                    break;
                }
            }
        }

        if (encontrado) {
            // Guardamos los cambios en el archivo físico
            ConexionXML.guardarDOM(doc, rutaXml);
            System.out.println("Producto con ID " + id + " eliminado correctamente.");
        } else {
            // Si no encontramos el producto, avisamos
            System.out.println("No se encontró ningún producto con ID: " + id);
        }
    }

    /**
     * Método auxiliar privado para obtener el texto de una subelemento concreto.
     * Nos ahorramos repetir la misma lógica cada vez que leemos un dato del XML.
     */
    private String obtenerTextoEtiqueta(Element padre, String nombreEtiqueta) {
        // Buscamos la primera etiqueta con el nombre indicado dentro del elemento padre
        NodeList lista = padre.getElementsByTagName(nombreEtiqueta);
        // Si existe, devolvemos su contenido de texto
        if (lista.getLength() > 0) {
            return lista.item(0).getTextContent();
        }
        // Si no existe, devolvemos una cadena vacía por seguridad
        return "";
    }
}
