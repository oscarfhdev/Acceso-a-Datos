import colecciones.GestorColecciones;
import dao.DataLoader;
import dao.ProductoXMLDAO;
import modelo.Producto;

import java.util.List;
import java.util.Scanner;

/**
 * Clase principal de la aplicación.
 * Mostramos un menú interactivo por consola para gestionar el inventario de la
 * tienda.
 * El usuario puede insertar, listar, modificar precio, eliminar productos y
 * probar la gestión de carpetas.
 */
public class Main {

    public static void main(String[] args) {
        // Creamos el Scanner para leer la entrada del usuario por teclado
        Scanner scanner = new Scanner(System.in);

        // Creamos la instancia del DAO que conecta con el archivo XML
        ProductoXMLDAO dao = new ProductoXMLDAO();

        // Variable para controlar cuándo el usuario quiere salir
        boolean salir = false;

        // Mostramos un mensaje de bienvenida
        System.out.println("\n--- Gestión de Inventario - Tienda XML ---");

        // Cargamos datos de ejemplo si el XML está vacío (solo la primera vez)
        DataLoader.cargarDatosIniciales(dao);

        // Bucle principal del menú: seguimos mostrándolo hasta que el usuario elija
        // salir
        while (!salir) {
            // Mostramos las opciones del menú
            System.out.println("\n-------- MENÚ --------");
            System.out.println("1. Listar productos");
            System.out.println("2. Insertar producto");
            System.out.println("3. Modificar precio");
            System.out.println("4. Eliminar producto");
            System.out.println("5. Probar gestión de carpetas");
            System.out.println("6. Salir");
            System.out.println("----------------------");
            System.out.print("Elige una opción: ");

            // Leemos la opción como texto para evitar problemas si el usuario mete letras
            String entrada = scanner.nextLine().trim();

            // Intentamos convertir la entrada a un número entero
            int opcion;
            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                // Si el usuario no introduce un número válido, avisamos y volvemos al menú
                System.out.println("\n Error: debes introducir un número válido (1-6).");
                continue;
            }

            // Evaluamos la opción elegida
            switch (opcion) {
                case 1:
                    // Opción para listar todos los productos
                    listarProductos(dao);
                    break;
                case 2:
                    // Opción para insertar un nuevo producto
                    insertarProducto(scanner, dao);
                    break;
                case 3:
                    // Opción para modificar el precio de un producto existente
                    modificarPrecio(scanner, dao);
                    break;
                case 4:
                    // Opción para eliminar un producto por su ID
                    eliminarProducto(scanner, dao);
                    break;
                case 5:
                    // Opción para probar la gestión de carpetas y archivos
                    GestorColecciones.demostrarColecciones();
                    break;
                case 6:
                    // El usuario quiere salir del programa
                    salir = true;
                    System.out.println("\n¡Hasta luego! La aplicación se ha cerrado.");
                    break;
                default:
                    // Si el número no está entre 1 y 6, avisamos
                    System.out.println("\n Opción no válida. Introduce un número entre 1 y 6.");
                    break;
            }
        }

        // Cerramos el Scanner al terminar
        scanner.close();
    }

    /**
     * Pedimos los datos al usuario para crear un nuevo producto y lo insertamos.
     * Capturamos los errores de formato numérico por si introduce letras en precio
     * o stock.
     */
    private static void insertarProducto(Scanner scanner, ProductoXMLDAO dao) {
        System.out.println("\n-- Insertar nuevo producto --\n");

        // Pedimos el nombre del producto
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();

        // Pedimos la descripción
        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine().trim();

        // Pedimos el precio y controlamos errores de formato
        double precio;
        System.out.print("Precio: ");
        try {
            precio = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Error: el precio debe ser un número válido.");
            return;
        }

        // Pedimos el stock y controlamos errores de formato
        int stock;
        System.out.print("Stock: ");
        try {
            stock = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Error: el stock debe ser un número entero.");
            return;
        }

        // Creamos el objeto Producto con los datos introducidos (el ID se genera
        // automáticamente)
        Producto producto = new Producto(null, nombre, descripcion, precio, stock);

        // Llamamos al DAO para insertar el producto en el XML
        dao.insertarProducto(producto);
    }

    /**
     * Listamos todos los productos del archivo XML y los mostramos por consola.
     * Si no hay productos, avisamos al usuario.
     */
    private static void listarProductos(ProductoXMLDAO dao) {
        System.out.println("\n-- Listado de productos --\n");

        // Obtenemos la lista de productos del DAO
        List<Producto> productos = dao.listarProductos();

        // Comprobamos si la lista está vacía
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados todavía.");
        } else {
            // Recorremos la lista y mostramos cada producto
            for (Producto p : productos) {
                System.out.println(p);
            }
            // Mostramos el total de productos encontrados
            System.out.println("\nTotal de productos: " + productos.size());
        }
    }

    /**
     * Pedimos el ID del producto y el nuevo precio para actualizarlo.
     * Capturamos el error de formato si el precio no es un número válido.
     */
    private static void modificarPrecio(Scanner scanner, ProductoXMLDAO dao) {
        System.out.println("\n-- Modificar precio --\n");

        // Pedimos el ID del producto a modificar
        System.out.print("ID del producto: ");
        String id = scanner.nextLine().trim();

        // Pedimos el nuevo precio y controlamos errores de formato
        double nuevoPrecio;
        System.out.print("Nuevo precio: ");
        try {
            nuevoPrecio = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Error: el precio debe ser un número válido.");
            return;
        }

        // Llamamos al DAO para actualizar el precio en el XML
        dao.actualizarPrecio(id, nuevoPrecio);
    }

    /**
     * Pedimos el ID del producto que queremos eliminar y lo borramos del XML.
     */
    private static void eliminarProducto(Scanner scanner, ProductoXMLDAO dao) {
        System.out.println("\n-- Eliminar producto --\n");

        // Pedimos el ID del producto a eliminar
        System.out.print("ID del producto a eliminar: ");
        String id = scanner.nextLine().trim();

        // Llamamos al DAO para eliminar el producto del XML
        dao.eliminarProducto(id);
    }
}
