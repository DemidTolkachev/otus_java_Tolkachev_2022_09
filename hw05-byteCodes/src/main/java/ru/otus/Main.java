package ru.otus;

import ru.otus.executor.Executor;
import ru.otus.executor.UserExecutor;
import ru.otus.proxy.Ioc;

public class Main {

    public static void main(String[] args) {

        final Class<Executor> executorClass = Executor.class;
        final Executor executorExample = Ioc.createWithLogging(executorClass, new UserExecutor());
        executorExample.execute(3);
        executorExample.execute(4, 7);
        executorExample.execute(5, 12, "Сумма параметров = ");

    }

}
