package ejercicio4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Ejercicio4 {
    public static void main(String[] args) {
        List<Empleado> empleados = new ArrayList<>();
        try {
            String fileName = "TAREA 1/src/ejercicio4/empleado.txt";
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
            bw.write("Juan;25;Programador\n");
            bw.write("Ana;30;Diseñadora");

            bw.close();

            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String linea;
            while ((linea = br.readLine()) != null){
                String lineaSeparada[] = linea.split(";");
                Empleado empladoNuevo = new Empleado(lineaSeparada[0], Integer.parseInt(lineaSeparada[1]), lineaSeparada[2]);
                empleados.add(empladoNuevo);
            }
            br.close();
            System.out.println(empleados);
        } catch (Exception e) {
            e.printStackTrace();
        }
   }
}
