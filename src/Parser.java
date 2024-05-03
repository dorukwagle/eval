import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {
    private final String rawExp;

    public Parser(String rawExp) {
        this.rawExp = rawExp;
    }

//    Split the expression with - sign
    private String[] splitSubtraction() {
        var splits = this.rawExp.split("-");
        if (this.rawExp.startsWith("-")) splits[0] = "0";
        return splits;
    }

    private String[][] splitAddition(String[] subtractedExp) {
        return Arrays.stream(subtractedExp)
                .map(item -> item.split("\\+"))
                .toArray(String[][]::new);
    }

    private String[][][] splitMultiplication(String[][] additionExp) {
        return Arrays.stream(additionExp)
                .map(arr -> Arrays.stream(arr)
                        .map(item -> item.split("\\*")).toArray(String[][]::new)
                ).toArray(String[][][]::new);
    }

    private String[][][][] splitDivision(String[][][] multiplicationExp) {
        return Arrays.stream(multiplicationExp)
                .map(arr2d -> Arrays.stream(arr2d)
                        .map(arr -> Arrays.stream(arr)
                                .map(exp -> exp.split("/")
                                ).toArray(String[][]::new)
                        ).toArray(String[][][]::new)
                ).toArray(String[][][][]::new);
    }

    private double division(String[] exp) {
        double res = Double.parseDouble(exp[0]);

        for (int i = 1; i < exp.length; ++i)
            res /= Double.parseDouble(exp[i]);

        return res;
    }



    // TODO: will remove it
    private String expressionToString() {
        return Arrays.toString(
                Arrays.stream(
                                this.splitDivision(this.splitMultiplication(this.splitAddition(this.splitSubtraction())))
                        )
                        .map(arr3d -> Arrays.toString(Arrays.stream(arr3d).map(arr2d -> Arrays.toString(Arrays.stream(arr2d).map(Arrays::toString).toArray())).toArray())).toArray()
        );
    }

    public String evaluate() {

        return this.expressionToString();
    }
}
