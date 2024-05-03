import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var reader = new Scanner(System.in);
        while (true) {
            System.out.println("Enter the expression:-");
            var input = reader.nextLine();
            if (input.equalsIgnoreCase("q")) break;

            var result = new Parser(input).evaluate();
            System.out.println(result);
        }
    }
}