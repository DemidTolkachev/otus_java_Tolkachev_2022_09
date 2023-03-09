package ru.otus.executor;

import ru.otus.proxy.Log;

public class UserExecutor implements Executor {

    @Log
    @Override
    public void execute(int param1) {
        int result = param1 * param1;
        System.out.println("Выполнение UserExecutor.execute(int param1). Результат: " + result);
    }

    @Log
    @Override
    public void execute(int param1, int param2) {
        int result = param1 + param2;
        System.out.println("Выполнение UserExecutor.execute(int param1, int param2). Результат: " + result);
    }

    @Log
    @Override
    public void execute(int param1, int param2, String param3) {
        int result = param1 + param2;
        System.out.println(
                "Выполнение UserExecutor.execute(int param1, int param2, String param3). Результат: " + param3 +
                        result);
    }
}
