import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Alumno {
    public static int contadorAlumnos = 0;
    private int id;
    private String nombre;
    private String apellidos;
    private String clase;
    private String fecha;

    public Alumno(String nombre, String apellidos, String clase, String fecha) {
        this.id = comprobarId();
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.clase = clase;
        this.fecha = fecha;
    }

    private static int comprobarId() {
        File archivoAlumnos = new File("src/Alumnos.txt");
        if (archivoAlumnos.exists()){
            try {
                int id = 0;
                BufferedReader br = new BufferedReader(new FileReader(archivoAlumnos));
                String linea;
                while ((linea = br.readLine()) != null){
                    String[] lineaSeparada = linea.split("\\|");
                    id = Integer.parseInt(lineaSeparada[0]);
                }
                return ++id;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ++contadorAlumnos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return id + "|" + nombre + "|" + apellidos + "|" + fecha + "|" + clase + ";";
    }
}
