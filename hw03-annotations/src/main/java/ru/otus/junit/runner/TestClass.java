package ru.otus.junit.runner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestClass {
    private final Class<?> clazz;
    private Object instance;

    public Object instance(Object... args)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (instance == null) {
            instance = getConstructor().newInstance(args);
        }
        return instance;
    }

    public List<Result> invokeMethods(Class<? extends Annotation> annotation, Method[] methods) {
        return Arrays.stream(methods).filter(method -> method.isAnnotationPresent(annotation)).map(method -> {
            try {
                method.invoke(instance());
                return new Result(Result.Type.SUCCESS, method.getName());
            } catch (InvocationTargetException ex) {
                return new Result(Result.Type.ERROR,
                        String.format("%s, error: %s", method.getName(), ex.getTargetException().getMessage()));
            } catch (Exception ex) {
                return new Result(Result.Type.ERROR, ex.getMessage());
            }
        }).collect(Collectors.toList());
    }

    public List<Result> invokeMethod(Method[] methods) {
        return Arrays.stream(methods).map(method -> {
            try {
                method.invoke(instance());
                return new Result(Result.Type.SUCCESS, method.getName());
            } catch (InvocationTargetException ex) {
                return new Result(Result.Type.ERROR,
                        String.format("%s, error: %s", method.getName(), ex.getTargetException().getMessage()));
            } catch (Exception ex) {
                return new Result(Result.Type.ERROR, ex.getMessage());
            }
        }).collect(Collectors.toList());
    }


    public static class Result {
        final Type type;
        final String description;


        public enum Type {
            SUCCESS("passed"), ERROR("failed");
            private final String state;

            @Override
            public String toString() {
                return String.format("[ %s ]", state.toUpperCase());
            }

            public String getState() {
                return this.state;
            }

            private Type(final String state) {
                this.state = state;
            }
        }

        @Override
        public String toString() {
            return type + ": " + description;
        }

        public Type getType() {
            return this.type;
        }

        public String getDescription() {
            return this.description;
        }

        public Result(final Type type, final String description) {
            this.type = type;
            this.description = description;
        }
    }

    private Constructor<?> getConstructor() throws NoSuchMethodException {
        return clazz.getConstructor();
    }

    private TestClass(final Class<?> clazz) {
        if (clazz == null) {
            throw new java.lang.NullPointerException("clazz is marked non-null but is null");
        }
        this.clazz = clazz;
    }

    public static TestClass of(final Class<?> clazz) {
        return new TestClass(clazz);
    }
}
