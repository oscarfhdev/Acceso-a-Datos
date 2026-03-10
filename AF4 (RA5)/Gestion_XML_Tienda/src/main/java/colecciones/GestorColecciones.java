package colecciones;

import java.io.File;
import java.io.IOException;

/**
 * Clase para demostrar el manejo de archivos y carpetas con java.io.File.
 * Creamos una estructura de directorios, un archivo dentro, y luego lo
 * limpiamos todo.
 */
public class GestorColecciones {

    /**
     * Demostramos cómo usar File para crear y eliminar carpetas y archivos.
     * Creamos una jerarquía de carpetas, añadimos un archivo XML de prueba,
     * y después eliminamos todo para dejarlo limpio.
     */
    public static void demostrarColecciones() {
        System.out.println("\n--- Demostración de gestión de carpetas ---\n");

        // Comprobamos si el directorio de ejecución es el padre (AF4)
        String dirActual = System.getProperty("user.dir");
        String prefijoRuta = dirActual.endsWith("Gestion_XML_Tienda") ? dirActual + File.separator
                : dirActual + File.separator + "Gestion_XML_Tienda" + File.separator;

        // Definimos la ruta de las carpetas que vamos a crear (carpeta
        // padre/subcarpeta)
        File carpeta = new File(prefijoRuta + "colecciones_xml" + File.separator + "2024");

        // Creamos las carpetas de forma recursiva con mkdirs()
        // mkdirs() crea también las carpetas intermedias si no existen
        boolean carpetaCreada = carpeta.mkdirs();

        // Comprobamos si se han creado correctamente
        if (carpetaCreada) {
            System.out.println("Carpeta creada: " + carpeta.getAbsolutePath());
        } else {
            System.out.println("La carpeta ya existía o no se pudo crear: " + carpeta.getAbsolutePath());
        }

        // Definimos el archivo de prueba dentro de la carpeta creada
        File archivoPrueba = new File(carpeta, "prueba.xml");

        try {
            // Creamos el archivo vacío con createNewFile()
            boolean archivoCreado = archivoPrueba.createNewFile();

            if (archivoCreado) {
                System.out.println("Archivo creado: " + archivoPrueba.getAbsolutePath());
            } else {
                System.out.println("El archivo ya existía: " + archivoPrueba.getAbsolutePath());
            }
        } catch (IOException e) {
            // Si ocurre un error al crear el archivo, lo mostramos
            System.err.println("Error al crear el archivo de prueba: " + e.getMessage());
            return;
        }

        // Verificamos que el archivo existe en disco
        System.out.println("Existe el archivo: " + archivoPrueba.exists());

        // Ahora eliminamos todo para dejar limpio el entorno
        System.out.println("\nLimpiando archivos y carpetas...\n");

        // Primero eliminamos el archivo (hay que borrar los contenidos antes de las
        // carpetas)
        if (archivoPrueba.delete()) {
            System.out.println("Archivo eliminado: " + archivoPrueba.getName());
        } else {
            System.out.println("No se pudo eliminar el archivo.");
        }

        // Luego eliminamos la subcarpeta "2024"
        if (carpeta.delete()) {
            System.out.println("Carpeta eliminada: " + carpeta.getName());
        } else {
            System.out.println("No se pudo eliminar la carpeta '2024'.");
        }

        // Finalmente eliminamos la carpeta padre "colecciones_xml"
        File carpetaPadre = new File(prefijoRuta + "colecciones_xml");
        if (carpetaPadre.delete()) {
            System.out.println("Carpeta eliminada: " + carpetaPadre.getName());
        } else {
            System.out.println("No se pudo eliminar la carpeta 'colecciones_xml'.");
        }

        System.out.println("\n--- Demostración completada ---\n");
    }
}
