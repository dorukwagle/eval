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

    private String division(String[] exp) {
        var value = Arrays.stream(Arrays.stream(exp)
                .map(Double::parseDouble)
                .toArray(Double[]::new)
        ).reduce((a, b) -> a / b);
        return String.valueOf(value.isPresent() ? value.get() : 1);
    }

    private String multiply(String[] exp) {
        var value = Arrays.stream(Arrays.stream(exp)
                .map(Double::parseDouble)
                .toArray(Double[]::new)
        ).reduce((a, b) -> a * b);
        return String.valueOf(value.isPresent() ? value.get() : 0);
    }

    private String add(String[] exp) {
        var value = Arrays.stream(
                Arrays.stream(exp)
                        .map(Double::parseDouble)
                        .toArray(Double[]::new)
        ).reduce(Double::sum);
        return String.valueOf(value.isPresent() ? value.get() : 0);
    }

    private String subtract(String[] exp) {
        var value = Arrays.stream(
                Arrays.stream(exp)
                        .map(Double::parseDouble)
                        .toArray(Double[]::new)
        ).reduce((a, b) -> a - b);
        return String.valueOf(value.isPresent() ? value.get() : 0);
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
//        [
//                [
//                        [[23]]
//                ],
//                [
//                        [[78]],
//                        [[65]]
//                ],
//                [
//                        [[88]],
//                        [
//                                [6],
//                                [45],
//                                [3]
//                        ],
//                        [
//                                [78, 34]
//                        ],
//                        [
//                                [78, 23, 56]
//                        ],
//                        [[23]]
//                ]
//        ]
        var expression = this.splitDivision(
                this.splitMultiplication(
                        this.splitAddition(this.splitSubtraction())));

        var divPerformed = Arrays.stream(expression)
                .map(arr3d -> Arrays.stream(arr3d)
                        .map(arr2d -> Arrays.stream(arr2d)
                                .map(this::division).toArray(String[]::new)
                        ).toArray(String[][]::new)).toArray(String[][][]::new);

        var mulPerformed = Arrays.stream(divPerformed)
                .map(arr2d -> Arrays.stream(arr2d)
                        .map(this::multiply).toArray(String[]::new)
                ).toArray(String[][]::new);

        var addPerformed = Arrays.stream(mulPerformed)
                .map(this::add).toArray(String[]::new);

        return subtract(addPerformed);
    }
}
