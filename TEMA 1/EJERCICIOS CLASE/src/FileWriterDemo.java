import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileWriterDemo {
    public static void main(String[] args) throws IOException {
        try (FileWriter fw = new FileWriter("EJERCICIOS CLASE/src/prueba.txt", true)){
            fw.write("Holaaa \n");
        }
    }
}
