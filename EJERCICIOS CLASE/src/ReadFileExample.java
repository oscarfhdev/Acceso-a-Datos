import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadFileExample {
    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader("EJERCICIOS CLASE/src/text.txt"))){
            String linea;

            while ((linea = br. readLine()) != null){
                System.out.println("linea = " + linea);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
