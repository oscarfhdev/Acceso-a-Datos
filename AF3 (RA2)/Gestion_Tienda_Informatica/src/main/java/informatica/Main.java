package informatica;

import informatica.conexion.InicializadorBD;
import informatica.dao.ProductoDAO;
import informatica.dao.UsuarioDAO;
import informatica.modelo.Producto;

import java.util.List;
import java.util.Scanner;

// Clase principal de la aplicación de gestión de tienda de informática.
// Aquí se arranca todo: se prepara la base de datos, se pide login
// y luego se muestra un menú con las opciones del CRUD y la transacción.
public class Main {

    public static void main(String[] args) {

        // Primero mostramos el título de la aplicación
        System.out.println("====================================================");
        System.out.println("   GESTIÓN DE TIENDA DE INFORMÁTICA - RA2 DAM");
        System.out.println("====================================================");
        System.out.println();

        // Inicializamos la base de datos: crea las tablas si no existen
        // y mete datos de prueba (usuario admin y productos de ejemplo)
        InicializadorBD.inicializar();

        System.out.println();

        // Creamos los DAOs que usaremos para acceder a los datos
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        ProductoDAO productoDAO = new ProductoDAO();

        // Scanner para leer lo que escriba el usuario por teclado
        Scanner scanner = new Scanner(System.in);

        // --- SECCIÓN DE LOGIN ---
        // El usuario tiene que meter sus credenciales para poder entrar.
        // Si falla, se le pide otra vez (bucle infinito hasta que acierte)
        System.out.println("------------ INICIO DE SESIÓN ------------");
        boolean autenticado = false;

        while (!autenticado) {
            System.out.print("Usuario: ");
            String username = scanner.nextLine().trim();
            System.out.print("Contraseña: ");
            String password = scanner.nextLine().trim();

            // Validamos contra la base de datos con PreparedStatement
            if (usuarioDAO.validarLogin(username, password)) {
                autenticado = true;
                System.out.println("\nBienvenido, " + username + ". Acceso concedido.\n");
            } else {
                System.out.println("[ERROR] Credenciales incorrectas. Inténtelo de nuevo.\n");
            }
        }

        // --- MENÚ PRINCIPAL ---
        // Bucle con while + switch para que el usuario elija qué quiere hacer.
        // Cada opción llama al método correspondiente del DAO.
        boolean ejecutando = true;

        while (ejecutando) {
            System.out.println("--------------------------------------------");
            System.out.println("             MENÚ PRINCIPAL");
            System.out.println("--------------------------------------------");
            System.out.println("  1. Listar todos los productos");
            System.out.println("  2. Insertar nuevo producto");
            System.out.println("  3. Actualizar stock de un producto");
            System.out.println("  4. Eliminar un producto");
            System.out.println("  5. Simular transacción (Rollback)");
            System.out.println("  6. Salir");
            System.out.println("--------------------------------------------");
            System.out.print("Seleccione una opción: ");

            String opcion = scanner.nextLine().trim();

            switch (opcion) {

                // Opción 1: sacar todos los productos de la BD y mostrarlos
                case "1":
                    System.out.println("\n--- LISTADO DE PRODUCTOS ---");
                    List<Producto> productos = productoDAO.obtenerTodos();

                    if (productos.isEmpty()) {
                        System.out.println("No hay productos registrados.");
                    } else {
                        System.out.println("-----------------------------------------------------------------");
                        for (Producto p : productos) {
                            System.out.println(p);
                        }
                        System.out.println("-----------------------------------------------------------------");
                        System.out.println("Total de productos: " + productos.size());
                    }
                    System.out.println();
                    break;

                // Opción 2: pedir datos al usuario y crear un producto nuevo
                case "2":
                    System.out.println("\n--- INSERTAR NUEVO PRODUCTO ---");
                    System.out.print("Nombre: ");
                    String nombre = scanner.nextLine().trim();

                    System.out.print("Descripción: ");
                    String descripcion = scanner.nextLine().trim();

                    System.out.print("Precio: ");
                    double precio = leerDouble(scanner);

                    System.out.print("Stock inicial: ");
                    int stock = leerEntero(scanner);

                    // Creamos el objeto Producto y lo mandamos al DAO para que lo inserte
                    Producto nuevoProducto = new Producto(nombre, descripcion, precio, stock);
                    productoDAO.insertarProducto(nuevoProducto);
                    System.out.println();
                    break;

                // Opción 3: cambiar el stock de un producto existente
                case "3":
                    System.out.println("\n--- ACTUALIZAR STOCK ---");
                    System.out.print("ID del producto: ");
                    int idActualizar = leerEntero(scanner);

                    System.out.print("Nuevo stock: ");
                    int nuevoStock = leerEntero(scanner);

                    productoDAO.actualizarStock(idActualizar, nuevoStock);
                    System.out.println();
                    break;

                // Opción 4: eliminar un producto por su ID
                case "4":
                    System.out.println("\n--- ELIMINAR PRODUCTO ---");
                    System.out.print("ID del producto a eliminar: ");
                    int idEliminar = leerEntero(scanner);

                    productoDAO.eliminarProducto(idEliminar);
                    System.out.println();
                    break;

                // Opción 5: simulación de transacción con rollback
                // Hace un UPDATE, luego fuerza un error y deshace los cambios
                case "5":
                    System.out.println("\n--- SIMULAR TRANSACCIÓN (ROLLBACK) ---");
                    System.out.print("ID del producto para simular la venta: ");
                    int idTransaccion = leerEntero(scanner);

                    System.out.print("Cantidad a vender: ");
                    int cantidadVenta = leerEntero(scanner);

                    productoDAO.simularVentaTransaccional(idTransaccion, cantidadVenta);
                    break;

                // Opción 6: salir del programa
                case "6":
                    ejecutando = false;
                    System.out.println("\nCerrando la aplicación. ¡Hasta pronto!");
                    break;

                default:
                    System.out.println("\nOpción no válida. Seleccione del 1 al 6.\n");
                    break;
            }
        }

        // Cerramos el scanner al terminar
        scanner.close();
    }

    // Método auxiliar para leer un entero por teclado de forma segura.
    // Si el usuario mete algo que no es un número, le vuelve a pedir.
    private static int leerEntero(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Introduzca un número entero válido: ");
            }
        }
    }

    // Igual pero para números decimales (precio, etc.)
    private static double leerDouble(Scanner scanner) {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Introduzca un número decimal válido: ");
            }
        }
    }
}
