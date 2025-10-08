import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class GestorAlumnos {
    public static void main(String[] args) {
        System.out.println("--- Gestor de Alumnos ---");
        boolean disponible = true;
        Scanner scanner = new Scanner(System.in);
        String ficheroAlumnos = "src/Alumnos.txt";
        String ficheroNotas = "src/Notas.txt";
        try {

           while (disponible){
                System.out.println("Menú:");
                System.out.println("1 - Añadir alumnos");
                System.out.println("2 - Devolver el ID de un alumno (por nombre y apellido)");
                System.out.println("3 - Insertar notas");
                System.out.println("4 - Calcular la media de notas de un alumno");
                System.out.println("5 - Salir");


                System.out.print("Escoge una opción: ");
                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion){
                    case 1: anadirAlumnos(scanner, ficheroAlumnos); break;
                    case 2: comprobarId(scanner, ficheroAlumnos); break;
                    case 3: anadirNotas(scanner, ficheroNotas, ficheroAlumnos); break;
                    case 4: calucularMediaNotas(scanner, ficheroNotas, ficheroAlumnos); break;
                    case 5: disponible = false; System.out.println("Hasta pronto!"); break;
                    default:
                        System.out.println("Opción no válida");
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void calucularMediaNotas(Scanner scanner, String ficheroNotas, String ficheroAlumnos) {
        try {
            System.out.print("Introduce el nombre del alumno: ");
            String nombre = scanner.nextLine();
            System.out.print("Introduce los apellidos alumno: ");
            String apellidos = scanner.nextLine();
            Integer id;
            if ((id = devolverId(ficheroAlumnos, nombre, apellidos)) != null){
                System.out.println("entra");
                BufferedReader br = new BufferedReader(new FileReader(ficheroNotas));
                String linea;
                double suma = 0;
                while ((linea = br.readLine()) != null){
                    String[] lineaSeparada = linea.split("\\|");
                    System.out.println(id);
                    System.out.println(lineaSeparada[0]);
                    if (id == Integer.parseInt(lineaSeparada[0])){
                        String[] notaSeparadas = lineaSeparada[1].split(";");
                        System.out.println(Arrays.toString(notaSeparadas));
                        for (int i = 0; i < notaSeparadas.length ; i++) {
                            System.out.println(notaSeparadas[i]);
                            suma += Double.parseDouble(notaSeparadas[i]);
                        }
                        double media = suma / notaSeparadas.length;

                        br.close();
                        System.out.println("La media del alumno con id " + id + "es: " + media);
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void anadirNotas(Scanner scanner, String ficheroNotas, String ficheroAlumnos) {
        try {
            System.out.print("Introduce el nombre del alumno: ");
            String nombre = scanner.nextLine();
            System.out.print("Introduce los apellidos alumno: ");
            String apellidos = scanner.nextLine();
            System.out.print("Introduzca las notas para el alumno 2(separados por ';'): ");
            String notas = scanner.nextLine();
            Integer id;
            if ((id = devolverId(ficheroAlumnos, nombre, apellidos)) != null){
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(ficheroNotas, true));

//                BufferedReader br= new BufferedReader(new FileReader(ficheroNotas));
//                String linea;
//                while ((linea = br.readLine()) != null){
//                    String[] lineaSeparada = linea.split("\\|");
//                    if (id == Integer.parseInt(lineaSeparada[0])){
//                        bufferedWriter.write(id + "|" + lineaSeparada[1] + notas + "\n");
//                        bufferedWriter.close();
//                        System.out.println("Notas añadidas correctamente");
//                        return;
//                    }
//                }

                bufferedWriter.write(id+"|"+notas+"\n");
                bufferedWriter.close();
            }
            System.out.println("Notas añadidas correctamente");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void anadirAlumnos(Scanner scanner, String ficheroAlumnos) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(ficheroAlumnos, true));

        int whileDisponible = 1;
        while (whileDisponible == 1) {
            System.out.print("Introduce el nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Introduce los apellidos: ");
            String apellidos = scanner.nextLine();
            System.out.print("Introduzca la fecha de nacimiento (dd-mm-aaaa): ");
            String fechaNacimiento = scanner.nextLine();
            System.out.print("Introduzca la clase del alumno: ");
            String clase = scanner.nextLine();

            Alumno nuevoAlumno = new Alumno(nombre, apellidos, clase, fechaNacimiento);

            bufferedWriter.write(nuevoAlumno.toString() + "\n");

            System.out.println("Alumno añadido correctamente!");
            System.out.println("Pulsa 1 si quieres insertar otro alumno, o 0 para salir.");
            whileDisponible = Integer.parseInt(scanner.nextLine());
        }
        bufferedWriter.close();
    }

    private static void comprobarId(Scanner scanner, String ficheroAlumnos){
        System.out.print("Introduce el nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Introduce los apellidos: ");
        String apellidos = scanner.nextLine();

        try {
            encuentraId(ficheroAlumnos, nombre, apellidos);

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private static Integer devolverId(String ficheroAlumnos, String nombre, String apellidos){
        try {
            BufferedReader br = new BufferedReader(new FileReader(ficheroAlumnos));
            String linea;
            while ((linea = br.readLine()) != null){
                String[] lineaSeparada = linea.split("\\|");
                if (nombre.equalsIgnoreCase(lineaSeparada[1]) && apellidos.equalsIgnoreCase(lineaSeparada[2])){
                    System.out.println("Alumno encontrado, id " + lineaSeparada[0]);
                    return Integer.parseInt(lineaSeparada[0]);
                }
            }
            throw new AlumnoNotFound("El alumno con nombre" + nombre + " y apellido " + apellidos + " no se encuentra en el archivo.");
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private static boolean encuentraId(String ficheroAlumnos, String nombre, String apellidos) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(ficheroAlumnos));
        String linea;
        while ((linea = br.readLine()) != null){
            String[] lineaSeparada = linea.split("\\|");
            if (nombre.equalsIgnoreCase(lineaSeparada[1]) && apellidos.equalsIgnoreCase(lineaSeparada[2])){
                System.out.println("Alumno encontrado, id " + lineaSeparada[0]);
                return true;
            }
        }

        br.read();
        return false;
    }
}
