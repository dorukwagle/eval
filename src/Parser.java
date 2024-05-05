import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {

    private static final Character[] legalChars = {'.', '+', '-', '*', '/'};
    private static final String[] tokens = new String[100];
    private static int tokenCounter = 0;

    private static boolean isLegalChar(char ch) {
        return (ch >= '0' && ch <= '9')
                || Arrays.stream(legalChars)
                .anyMatch(character -> ch == character);
    }

    private static boolean isDigit(char ch) {
        if (ch == '.') return true;
        return ch >= '0' && ch <= '9';
    }

    private static void reset() {
        tokenCounter = 0;
        Arrays.fill(tokens, "");
    }

    private static void validate(String exp) throws Exception{
        var exps = exp.toCharArray();
        var flag = false;
        var pFlag = false;

        for (char c : exps) {
            if (!isLegalChar(c)) throw new Exception("Invalid character: " + c);
            if (!isDigit(c)) {
                pFlag = flag;
                flag = true;
            } else {
                pFlag = flag;
                flag = false;
            }

            if (flag != pFlag)
                ++tokenCounter;

            tokens[tokenCounter] += c;
        }
    }

    // TODO: will remove it
    private static String expressionToString() {
        return Arrays.toString(tokens);
    }

    public static String evaluate(String exp) throws Exception{
        reset();
        validate(exp);
        return expressionToString();
    }
}
