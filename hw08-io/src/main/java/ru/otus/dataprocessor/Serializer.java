package ru.otus.dataprocessor;

import java.util.Map;
import java.io.IOException;

public interface Serializer {

    void serialize(Map<String, Double> data) throws IOException;
}
