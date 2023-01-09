package ru.otus.junit.runner.options.out;

import ru.otus.junit.runner.ResultOfRunning;
import ru.otus.junit.runner.TestClass;

import java.util.List;
import java.util.Objects;

import static ru.otus.junit.runner.options.utils.Mapper.toCountResults;

public class OutputToConsole implements Output {
    private static final String CLASS_TEMPLATE = "\n- %s\n";
    private static final String TEST_TEMPLATE = "\t%d. %s: %s\n";
    private static final String REPORT_TEMPLATE = """
            ----------------------------------------------------
            Тестов прошло успешно: %s
            Тестов упало:          %s
            Тестов было всего:     %s
            ----------------------------------------------------
            """;

    @Override
    public void printTestTrace(List<ResultOfRunning> results) {
        results.forEach(result -> {
            print(CLASS_TEMPLATE, result.getClazz().getName());
            final int[] index = {0};
            result.getResults().forEach(test -> {
                final var type = test.getType();
                print(TEST_TEMPLATE, ++index[0], type, test.getDescription());
            });
        });
    }

    @Override
    public void printReport(List<ResultOfRunning> results) {
        long successCount =
                toCountResults.apply(results, result -> result.getType().equals(TestClass.Result.Type.SUCCESS));
        long failCount = toCountResults.apply(results, result -> result.getType().equals(TestClass.Result.Type.ERROR));
        long totalCount = toCountResults.apply(results, Objects::nonNull);
        print(REPORT_TEMPLATE, successCount, failCount, totalCount);
    }

    private void print(String template, Object... values) {
        System.out.printf(template, values);
    }
}
