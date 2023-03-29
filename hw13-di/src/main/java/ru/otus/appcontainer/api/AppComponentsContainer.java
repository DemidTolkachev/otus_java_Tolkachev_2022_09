package ru.otus.appcontainer.api;

public interface AppComponentsContainer {
    <C> C getAppComponent(Class<C> componentClass) throws NoSuchMethodException;
    <C> C getAppComponent(String componentName);
}
