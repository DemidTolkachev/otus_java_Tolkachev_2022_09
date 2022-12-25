package ru.otus.tests;

import ru.otus.junit.After;
import ru.otus.junit.Before;
import ru.otus.junit.Test;
import ru.otus.tests.entities.SomeActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class SomeActionsTest {

    private SomeActions someActions;

    @Before
    public void setUp() {
        someActions = new SomeActions(7, 3);
    }

    @Test
    public void additionTestExpectedError() {
        assertThat(someActions.add()).isEqualTo(10);
    }

    @Test
    public void subtractionTest() {
        assertThat(someActions.sub()).isEqualTo(4);
    }

    @Test
    public void multiplicationTest() {
        assertThat(someActions.mul()).isEqualTo(21);
    }

    @Test
    public void divisionTestExpectedError() {
        assertThat(someActions.div()).isEqualTo(3);
    }

    @Test
    public void exponentiationTest() {
        assertThat(someActions.pow()).isEqualTo(49);
    }

    @After
    public void clean() {
        someActions.clean();
    }
}
