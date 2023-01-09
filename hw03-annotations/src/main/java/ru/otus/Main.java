package ru.otus;

import ru.otus.junit.runner.TestRunner;
import ru.otus.tests.SomeActionsTest;

public class Main {

    public static void main(String[] args) {
        TestRunner.run(SomeActionsTest.class);
    }
}
