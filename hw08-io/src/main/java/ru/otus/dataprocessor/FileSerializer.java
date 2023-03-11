package ru.otus.dataprocessor;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Map;

public class FileSerializer implements Serializer {
    private Gson gson = new Gson();
    private final File file;

    public FileSerializer(String fileName) {
        file = new File(fileName);
    }

    @Override
    public void serialize(Map<String, Double> data) throws IOException {
        //формирует результирующий json и сохраняет его в файл
        Writer writer = new FileWriter(file);
        Type type = new TypeToken<Map<String, Double>>() {
        }.getType();
        gson.toJson(data, type, writer);
        writer.close();

    }
}
