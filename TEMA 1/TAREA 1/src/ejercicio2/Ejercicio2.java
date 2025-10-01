package ejercicio2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Ejercicio2 {
    public static void main(String[] args) {
        String rutaArchivo = "TAREA 1/src/ejercicio2/ficheroDatos.txt";
        try {
            var bw = new BufferedWriter(new FileWriter(rutaArchivo));
            bw.write("1\n");
            bw.write("2\n");
            bw.write("3\n");
            bw.write("4\n");
            bw.write("5\n");


            bw.close();

            BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
            String linea;
            int totalSuma = 0;
            List<Integer> listaNumeros = new ArrayList<>();
            while ((linea = br.readLine()) != null){
                int numero = Integer.parseInt(linea);
                listaNumeros.add(numero);
                totalSuma += numero;
            }

            br.close();

            System.out.println(listaNumeros);
            System.out.println("Suma total de los números: " + totalSuma);
            System.out.println("Media de los números: " + totalSuma / listaNumeros.size());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
