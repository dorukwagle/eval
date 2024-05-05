import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {

    private static final Character[] legalChars = {'.', '+', '-', '*', '/', '^'};
    private static String[] tokens = new String[100];
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
                .replaceAll(" +", "")
                .replaceAll("/+", "/")
                .replaceAll("\\*+", "\\*")
                .replaceAll("--", "+")
                .replaceAll("\\++", "\\+")
                .replaceAll("\\+-", "-")
                .replaceAll("-\\+", "-")
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

    private static int powerOperation(int start) {
        int i = start;
        boolean found = false;
        for (; i <= tokenCounter; ++i)
            if (tokens[i].equals("^")) {
                found = true;
                break;
            }

        if (!found) return -1;

        var res = Math.pow(
                Double.parseDouble(tokens[i - 1]),
                Double.parseDouble(tokens[i + 1]));
        replaceInArray(i - 1, i + 1, String.valueOf(res));

        return i - 1;
    }

    private static int divisionOperation(int start) {
        int i = start;
        boolean found = false;
        for (; i <= tokenCounter; ++i)
            if (tokens[i].equals("/")) {
                found = true;
                break;
            }

        if (!found) return -1;

        var res =
                Double.parseDouble(tokens[i - 1]) /
                Double.parseDouble(tokens[i + 1]);
        replaceInArray(i - 1, i + 1, String.valueOf(res));

        return i - 1;
    }

    private static int multiplicationOperation(int start) {
        int i = start;
        boolean found = false;

        for (; i <= tokenCounter; ++i)
            if (tokens[i].equals("*")) {
                found = true;
                break;
            }

        if (!found) return -1;

        var res =
                Double.parseDouble(tokens[i - 1]) *
                        Double.parseDouble(tokens[i + 1]);
        replaceInArray(i - 1, i + 1, String.valueOf(res));

        return i - 1;
    }

    private static int additionOperation(int start) {
        int i = start;
        boolean found = false;
        for (; i <= tokenCounter; ++i)
            if (tokens[i].equals("+")) {
                found = true;
                break;
            }

        if (!found) return -1;

        var res =
                Double.parseDouble(tokens[i - 1]) +
                        Double.parseDouble(tokens[i + 1]);
        replaceInArray(i - 1, i + 1, String.valueOf(res));

        return i - 1;
    }

    private static int subtractionOperation(int start) {
        int i = start;
        boolean found = false;

        for (; i <= tokenCounter; ++i)
            if (tokens[i].equals("-")) {
                found = true;
                break;
            }

        if (!found) return -1;

        var res =
                Double.parseDouble(tokens[i - 1]) -
                        Double.parseDouble(tokens[i + 1]);
        replaceInArray(i - 1, i + 1, String.valueOf(res));

        return i - 1;
    }

    // TODO: will remove it
    private static String expressionToString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i <= tokenCounter; ++i)
            str.append(tokens[i]);

        var fin = str.toString();
        return fin.startsWith("-") ? "0" + fin : fin;
    }

    public static String evaluate(String exp) throws Exception{
        reset();
        tokenize(preprocess(exp));

        int pow = 0;
        while(pow != -1)
            pow = powerOperation(pow);

        int div = 0;
        while(div != -1)
            div = divisionOperation(div);

        int mul = 0;
        while (mul != -1)
            mul = multiplicationOperation(mul);

        int sub = 0;
        while (sub != -1)
            sub = subtractionOperation(sub);

        sub = 0;
        while (sub != -1)
            sub = subtractionOperation(sub);

        var newExp = expressionToString();
        reset();
        tokenize(preprocess(newExp));

        sub = 0;
        while (sub != -1)
            sub = subtractionOperation(sub);

        int add = 0;
        while (add != -1)
            add = additionOperation(add);

        return tokens[0];
    }
}