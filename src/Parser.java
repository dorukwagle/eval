import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {

    private static final Character[] legalChars = {'.', '+', '-', '*', '/'};
    private static final String[] tokens = new String[100];

    private static boolean isLegalChar(char ch) {
        var value = ch - '0';

        return (value >= 0 && value <= 9)
                || Arrays.stream(legalChars)
                .anyMatch(character -> ch == character);
    }

    private static void validate(String exp) throws Exception{
        var exps = exp.toCharArray();
        for (char c : exps) {
            if (!isLegalChar(c)) throw new Exception("Invalid character: " + c);
        }
    }

    // TODO: will remove it
    private static String expressionToString() {
        return "";
    }

    public static String evaluate(String exp) throws Exception{
        validate(exp);
        return expressionToString();
    }
}
