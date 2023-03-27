package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.appcontainer.api.AppComponent;

import java.util.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    record MarkedMethod(int order, Method method) {
    }

    public AppComponentsContainerImpl(Class<?> initialConfigClass)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        processConfig(initialConfigClass);
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) throws NoSuchMethodException {
        Object obj = null;
        for (Object o : appComponents) {
            if (componentClass.isAssignableFrom(o.getClass())) {
                // нашли
                if (obj != null) {
                    // нашли повторно
                    throw new NoSuchMethodException();
                }
                obj = o;
            }
        }
        // если нашли, то возвращаем, что нашли
        if (obj != null) {
            return (C) obj;
        }
        // не нашли
        throw new NoSuchMethodException();
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }

    private void processConfig(Class<?> configClass)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<MarkedMethod> markedMethodList = new ArrayList<>();
        checkConfigClass(configClass);
        for (Method m : configClass.getMethods()) {
            if (m.isAnnotationPresent(AppComponent.class)) {
                var annotation = m.getAnnotation(AppComponent.class);
                markedMethodList.add(new MarkedMethod(((AppComponent) annotation).order(), m));
            }
        }
        // отсортируем список аннотированных методов
        markedMethodList.sort((o1, o2) -> Integer.compare(o1.order, o2.order));

        // создаем объекты, размещаем их в List в порядке очередности и в Map по имени
        Object configClassInst = configClass.getConstructor().newInstance();

        for (MarkedMethod mm : markedMethodList) {
            var parameterTypes = mm.method.getParameterTypes();
            Object[] objects = new Object[parameterTypes.length];
            // заполняем аргументы
            for (int i = 0; i < parameterTypes.length; i++) {
                for (Object o : appComponents) {
                    if (parameterTypes[i].isAssignableFrom(o.getClass())) {
                        objects[i] = o;
                        break;
                    }
                }
                if (objects[i] == null) {
                    throw new NoSuchMethodException();
                }
            }
            Object obj = mm.method.invoke(configClassInst, objects);
            if (appComponentsByName.get(mm.method.getAnnotation(AppComponent.class).name()) != null) {
                // объект уже есть в Map
                throw new NoSuchMethodException();
            }
            // размещаем объект в List
            appComponents.add(obj);
            // размещаем объект в Map
            appComponentsByName.put(mm.method.getAnnotation(AppComponent.class).name(), obj);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }
}
