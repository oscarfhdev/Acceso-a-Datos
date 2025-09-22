import java.io.*;
import java.util.Scanner;

public class Ejemplo {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        //ejercicio1();
        ejercicio2();
    }

    private static void ejercicio1() {
        System.out.print("Ingresa el nombre del archivo: ");
        File file = new File("EJERCICIOS CLASE/src/" + scanner.nextLine());

        try {
            if (file.createNewFile()) {
                System.out.println("Archivo creado correctamente");
            } else {
                System.out.println("El archivo ya existía, se sobrescribirá");
            }

            // Escribir en el archivo
            try (FileWriter fileWriter = new FileWriter(file)) {
                for (int i = 0; i < 3; i++) {
                    System.out.print("Ingresa el contenido de la línea " + (i + 1) + ": ");
                    String linea = scanner.nextLine();
                    fileWriter.write(linea + "\n");
                }
            }

            // Leer el archivo
            System.out.println("\nContenido del archivo:");
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    System.out.println(linea);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void ejercicio2() {
        try {
            FileOutputStream fos = new FileOutputStream("EJERCICIOS CLASE/src/prueba.txt");
            DataOutputStream dos = new DataOutputStream(fos);

            dos.writeInt(25);
            dos.writeDouble(6.4);
            dos.writeUTF("Holaa clase de DAM");

            dos.close();
            fos.close();
            System.out.println("Datos escritos correctamente");



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileInputStream fis = new FileInputStream("EJERCICIOS CLASE/src/prueba.txt");
             DataInputStream dis = new DataInputStream(fis)) {

            int numero = dis.readInt();
            double decimal = dis.readDouble();
            String texto = dis.readUTF();

            System.out.println("Entero leído: " + numero);
            System.out.println("Double leído: " + decimal);
            System.out.println("String leído: " + texto);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
