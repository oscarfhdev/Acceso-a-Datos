package ejercicio1;

import java.io.*;
import java.util.Scanner;

public class Ejercicio1 {
    public static void main(String[] args) {
        String rutaArchivo = "TAREA 1/src/ejercicio1/ficheroDatos.txt";
        try {
            var bw = new BufferedWriter(new FileWriter(rutaArchivo));
            bw.write("1:Juan:20\n");
            bw.write("2:Alberto:31\n");
            bw.write("3:Javi:234\n");
            bw.write("4:Adrián:20\n");
            bw.write("5:Alba:20\n");


            bw.close();

            BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
            String linea;
            while ((linea = br.readLine()) != null){
                String[] nombre = linea.split(":");
                System.out.println(nombre[1]);
            }

            br.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


}
