package ejercicio5;

import ejercicio4.Empleado;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class Ejercicio5 {
    public static void main(String[] args) {
        try{
            double aumentoPrecio = 10;
            String archivoProductos = "TAREA 1/src/ejercicio5/productos.txt";
            String archivoProductosActualizado = "TAREA 1/src/ejercicio5/productos_actualizados.txt";
            BufferedWriter bw = new BufferedWriter(new FileWriter(archivoProductos));
            bw.write("1;Teclado;25.5\n");
            bw.write("2;Ratón;15.0\n");
            bw.write("3;Monitor;15.0\n");

            bw.close();

            BufferedReader br = new BufferedReader(new FileReader(archivoProductos));
            BufferedWriter bw2 = new BufferedWriter(new FileWriter(archivoProductosActualizado));

            String linea;
            while ((linea = br.readLine()) != null){
                String lineaSeparada[] = linea.split(";");

                Producto productoActualizado = new Producto(Integer.parseInt(lineaSeparada[0]), lineaSeparada[1], Double.parseDouble(lineaSeparada[2]));
                double precioActualizado = productoActualizado.getPrecio() + (productoActualizado.getPrecio() * aumentoPrecio/100);
                bw2.write(productoActualizado.getId() + ";" + productoActualizado.getNombre() + ";" + precioActualizado + "\n");
            }
            br.close();
            bw2.close();

            System.out.println("Se ha guardado la información en un nuevo archivo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
