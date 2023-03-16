package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import java.util.List;
import java.io.IOException;

public interface Loader {

    List<Measurement> load() throws IOException;
}
