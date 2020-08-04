package by.ioc.factory;

import by.ioc.annotation.Bean;
import by.ioc.annotation.Component;
import by.ioc.test.ComponentScan;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ScannerDef {

    protected List<Class<?>> readBeanDefinitionForBasePackage(Class<?> configClass) {
        String s = configClass.getAnnotation(ComponentScan.class).basePackage();
        Reflections reflections = new Reflections(s);
        return new ArrayList<>(reflections.getTypesAnnotatedWith(Component.class));
    }

    protected List<Method> readBeanDefinitionForConfigClass(Class<?> configClass) {
        List<Method> methodList = new ArrayList<>();
        Method[] methods = configClass.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Bean.class)) {
                methodList.add(method);
            }
        }
        return methodList;
    }
}
