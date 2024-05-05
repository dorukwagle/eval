import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {

    private static final Character[] legalChars = {'.', '+', '-', '*', '/', '^'};
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

    private static String preprocess(String exp) {
        return exp
                .replaceAll("/+", "/")
                .replaceAll("\\*+", "\\*")
                .replaceAll("--", "+")
                .replaceAll("\\++", "\\+")
                .replaceAll("\\+-", "-")
                .replaceAll("\\^+", "\\^")
                .replaceAll("%+", "%")
                .replaceAll("%", "/100");
    }

    private static void tokenize(String exp) throws Exception{
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

    private static void replaceInArray(int start, int end, String value) {
        var diff = end - start;
        tokens[start++] = value;
        for (int i = start; i <= tokenCounter - diff; ++i)
            tokens[i] = tokens[i + diff];
        tokenCounter -= diff;
    }

    private static void powerOperation() {
        for (int i = 0; i <= tokenCounter; ++i) {
            if (!tokens[i].equals("^")) continue;

            var res = Math.pow(
                    Double.parseDouble(tokens[i - 1]),
                    Double.parseDouble(tokens[i + 1]));
            replaceInArray(i - 1, i + 1, String.valueOf(res));
        }
    }

    private static void divisionOperation() {
        for (int i = 0; i <= tokenCounter; ++i) {
            if (!tokens[i].equals("/")) continue;

            var res =
                    Double.parseDouble(tokens[i - 1]) /
                    Double.parseDouble(tokens[i + 1]);
            replaceInArray(i - 1, i + 1, String.valueOf(res));
        }
    }

    // TODO: will remove it
    private static String expressionToString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i <= tokenCounter; ++i)
            str.append(tokens[i]);
        return str.toString();
    }

    public static String evaluate(String exp) throws Exception{
        reset();
        tokenize(preprocess(exp));

        powerOperation();
        divisionOperation();

        return expressionToString();
    }
}
