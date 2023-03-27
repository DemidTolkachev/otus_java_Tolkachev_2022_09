package ru.otus.jdbc.mapper;

import ru.otus.annotations.Id;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData {

    private final Class<?> clazz;
    private final Field[] fields;
    private final String name;

    public EntityClassMetaDataImpl(T t) {
        clazz = t.getClass();
        fields = clazz.getDeclaredFields();
        name = clazz.getSimpleName().toLowerCase();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor getConstructor() {
        try {
            return clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    @Override
    public Field getIdField() {
        return getFieldsWithAnnotation(Id.class).size() > 0 ? getFieldsWithAnnotation(Id.class).get(0) : null;
    }

    @Override
    public List<Field> getAllFields() {
        return List.of(fields);
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return Arrays.stream(fields).filter(field -> !field.isAnnotationPresent(Id.class)).toList();
    }

    private List<Field> getFieldsWithAnnotation(Class<? extends Annotation> annotation) {
        return Arrays.stream(fields).filter(field -> field.isAnnotationPresent(annotation)).toList();
    }
}
