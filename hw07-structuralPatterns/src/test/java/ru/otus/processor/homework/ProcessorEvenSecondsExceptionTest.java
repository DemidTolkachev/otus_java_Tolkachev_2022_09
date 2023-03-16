package ru.otus.processor.homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.processor.Processor;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

public class ProcessorEvenSecondsExceptionTest {

    @Test
    @DisplayName("Testing the processor call ProcessorEvenSecondsException on an odd second")
    public void atOddSecond() {
        Processor processor =
                new ProcessorEvenSecondsException(() -> LocalDateTime.of(2023, Month.MARCH, 10, 21, 06, 47));
        assertDoesNotThrow(() -> processor.process(null));
    }

    @Test
    @DisplayName("Testing the processor call ProcessorEvenSecondsException on an even second")
    public void atEvenSecond() {
        Processor processor =
                new ProcessorEvenSecondsException(() -> LocalDateTime.of(2023, Month.MARCH, 10, 21, 06, 46));
        assertThrows(InvalidSecondException.class, () -> processor.process(null));
    }

}
