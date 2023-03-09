package ru.otus.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Ioc {

    private Ioc() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T createWithLogging(Class<T> clazz, T target) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz},
                new LogInvocationHandler(target));
    }

    static class LogInvocationHandler implements InvocationHandler {

        private final Object target;
        private final List<Method> logMethods;

        LogInvocationHandler(Object target) {
            this.target = target;
            this.logMethods = getMethodsBy(target, (method) -> method.isAnnotationPresent(Log.class));
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            logMethodRun(method, args);
            return method.invoke(target, args);
        }

        private void logMethodRun(Method method, Object[] args) {
            final String methodName = method.getName();
            final Class<?>[] parameterTypes = method.getParameterTypes();
            for (Method logMethod : logMethods) {
                if (methodName.equals(logMethod.getName()) &&
                        Arrays.equals(parameterTypes, logMethod.getParameterTypes())) {

                    String paramsString = Arrays.stream(args).map(String::valueOf).collect(Collectors.joining(", "));

                    System.out.printf("Вызван метод: %s, параметры: %s%n", logMethod.getName(), paramsString);
                    return;
                }
            }
        }

        private List<Method> getMethodsBy(Object target, Predicate<Method> methodPredicate) {
            return Arrays.stream(target.getClass().getMethods()).filter(methodPredicate).collect(Collectors.toList());
        }
    }
}
