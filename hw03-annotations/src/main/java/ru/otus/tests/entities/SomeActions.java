package ru.otus.tests.entities;

public class SomeActions {
    private int num1;
    private int num2;

    public SomeActions(final int num1, final int num2) {
        this.num1 = num1;
        this.num2 = num2;
    }

    public int add() {
        return num1 + num2;
    }

    public int sub() {
        return num1 - num2;
    }

    public int mul() {
        return num1 * num2;
    }

    public int div() {
        return num1 / num2;
    }

    public int pow() {
        return (int) Math.pow(num1, num2);
    }

    public void clean() {
        this.num1 = 0;
        this.num2 = 0;
    }
}
