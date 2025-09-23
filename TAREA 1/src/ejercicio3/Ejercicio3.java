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
            System.out.print("Ingresa el nombre a ingresa, end para acabar: ");
            nombre = scanner.nextLine();
            while (!nombre.equals("end")){
                bw.write(nombre + "\n");
                System.out.print("Ingresa el nombre a ingresa, end para acabar: ");
                nombre = scanner.nextLine();
            }
            bw.close();
            System.out.println("Se ha finalizado el programa");
            System.out.println("Información guardada en el archivo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
