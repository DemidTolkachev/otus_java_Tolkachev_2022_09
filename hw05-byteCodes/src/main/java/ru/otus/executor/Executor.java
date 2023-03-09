package ru.otus.executor;

public interface Executor {

    void execute(int number);

    void execute(int numberOne, int numberTwo);

    void execute(int numberOne, int numberTwo, String message);
}
