package ejercicio4;

public class Empleado {
    private String nombre;
    private int edad;
    private String profesion;

    public Empleado(String nombre, int edad,  String profesion) {
        this.nombre = nombre;
        this.profesion = profesion;
        this.edad = edad;
    }

    @Override
    public String toString() {
        return this.nombre + ";" + this.edad + ";" + this.profesion;
    }
}
