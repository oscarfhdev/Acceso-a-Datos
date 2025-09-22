package ejercicio3;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Scanner;

public class Ejercicio3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String nombre = " ";

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("TAREA 1/src/ejercicio3/salida.txt"));
            while (!nombre.equals("end")){
                System.out.print("Ingresa el nombre a ingresa, end para acabar: ");
                nombre = scanner.nextLine();
                bw.write(nombre + "\n");
            }
            bw.close();
            System.out.println("Información guardad en el archivo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
