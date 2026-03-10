package dao;

import modelo.Producto;

/**
 * Clase que carga unos productos de ejemplo en el XML.
 * La usamos para que al arrancar la aplicación ya haya datos disponibles
 * y no tengamos que insertar todo a mano cada vez.
 */
public class DataLoader {

    /**
     * Comprobamos si el XML está vacío y, si es así, insertamos unos productos de
     * ejemplo.
     * Así la primera vez que ejecutamos la app ya tenemos datos para probar las
     * demás opciones.
     */
    public static void cargarDatosIniciales(ProductoXMLDAO dao) {
        // Miramos si ya hay productos en el XML
        if (!dao.listarProductos().isEmpty()) {
            // Si ya hay productos, no hacemos nada para no duplicar datos
            System.out.println("Ya hay productos cargados en el XML, no insertamos datos de ejemplo.");
            return;
        }

        System.out.println("El XML está vacío, insertamos productos de ejemplo...\n");

        // Creamos algunos productos variados de una tienda de informática
        Producto p1 = new Producto(null, "Ratón inalámbrico Logitech",
                "Ratón ergonómico con conexión Bluetooth y batería recargable", 29.99, 50);

        Producto p2 = new Producto(null, "Teclado mecánico RGB",
                "Teclado mecánico con switches Cherry MX Red e iluminación RGB", 89.95, 30);

        Producto p3 = new Producto(null, "Monitor 27 pulgadas 4K",
                "Monitor IPS de 27 pulgadas con resolución 4K y 60Hz", 349.00, 15);

        Producto p4 = new Producto(null, "Auriculares con micrófono",
                "Auriculares gaming con cancelación de ruido y micrófono integrado", 59.50, 40);

        Producto p5 = new Producto(null, "Cable HDMI 2.1",
                "Cable HDMI de alta velocidad compatible con 4K a 120Hz, 2 metros", 12.99, 100);

        // Insertamos cada producto usando el DAO
        dao.insertarProducto(p1);
        dao.insertarProducto(p2);
        dao.insertarProducto(p3);
        dao.insertarProducto(p4);
        dao.insertarProducto(p5);

        System.out.println("\nDatos de ejemplo cargados correctamente.");
    }
}
