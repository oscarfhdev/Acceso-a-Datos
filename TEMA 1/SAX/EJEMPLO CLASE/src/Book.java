import java.util.ArrayList;

public class Book {
    public String id;
    public String title;
    public ArrayList<String> authors;
    public int year;
    public double price;

    {
        this.authors = new ArrayList<>();
    }

    @Override
    public String toString() {
        return String.format(
            "Book{id=%s, title=%s, authors=%s, year=%d, price=%.2f}",
            id, title, authors, year, price
        );
    }
}