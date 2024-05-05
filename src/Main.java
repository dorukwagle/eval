import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var reader = new Scanner(System.in);
        while (true) {
            System.out.println("Enter the expression:-");
            var input = reader.nextLine();
            if (input.equalsIgnoreCase("q")) break;

            String result = null;
            try {
                result = Parser.evaluate(input);
                System.out.println(result);
            } catch (Exception e) {
//                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}