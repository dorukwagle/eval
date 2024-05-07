import java.util.Arrays;

public class Parser {
    private final static int EXP_LENGTH = 100;
    private static final Character[] legalChars = {'.', '+', '-', '*', '/', '^'};
    private final static String[] tokens = new String[EXP_LENGTH];
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
                .replaceAll("\\*+", "*")
                .replaceAll("--", "+")
                .replaceAll("\\++", "+")
                .replaceAll("\\+-", "-")
                .replaceAll("-\\+", "-")
                .replaceAll("\\^+", "^")
                .replaceAll("%+", "%")
                .replaceAll("%", "/100");
    }

    private static void tokenize(String exp) throws Exception{
        if (exp.length() > EXP_LENGTH) throw new Exception("Expression too long");

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

    private static int operate(int start, String operator) {
        int i = start;
        boolean found = false;
        for (; i <= tokenCounter; ++i)
            if (tokens[i].equals(operator)) {
                found = true;
                break;
            }

        if (!found) return -1;

        var a = Double.parseDouble(tokens[i - 1]);
        var b = Double.parseDouble(tokens[i + 1]);

        double res = switch (operator) {
            case "^" -> Math.pow(a, b);
            case "/" -> a / b;
            case "*" -> a * b;
            case "+" -> a + b;
            default -> a - b;
        };

        replaceInArray(i - 1, i + 1, String.valueOf(res));

        return i - 1;
    }

    private static int powerOperation(int start) {
        return operate(start, "^");
    }

    private static int divisionOperation(int start) {
        return operate(start, "/");
    }

    private static int multiplicationOperation(int start) {
        return operate(start, "*");
    }

    private static int additionOperation(int start) {
        return operate(start, "+");
    }

    private static int subtractionOperation(int start) {
        return operate(start, "-");
    }

    private static String expressionToString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i <= tokenCounter; ++i)
            str.append(tokens[i]);

        var fin = str.toString();
        return fin.startsWith("-") ? "0" + fin : fin;
    }

    private static String evaluator(String exp) throws Exception{
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

    private static boolean checkBraces(String exp) throws Exception {
        var count = 0;
        if (!exp.contains("(")) return false;

        for (char c : exp.toCharArray()) {
            if (c == '(') count++;
            else if (c == ')') count--;
            if (count < 0) throw new Exception("Unexpected braces ')' at the beginning");
        }
        if (count > 0) throw new Exception("Expected ')' at the end");
        return true;
    }

    private static String bracesEvaluator(String exp) {
        return "";
    }

    public static String evaluate(String exp) throws Exception {
        return checkBraces(exp) ? bracesEvaluator(exp) : evaluator(exp);
    }
}