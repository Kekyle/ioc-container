package by.ioc.factory;

import by.ioc.annotation.Autowired;
import by.ioc.annotation.Qualifier;
import by.ioc.annotation.Value;
import by.ioc.factory.comparator.BeanConstructorComparator;
import by.ioc.factory.comparator.MethodComparator;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CreatorAndInjector {

    public CreatorAndInjector() {
    }

//    protected List<Object> getAllObjectsWithoutValue(Class<?>[] classes, Map<String, Object> container) {
//        List<Object> objectList = new ArrayList<>();
//        for (Class<?> aClass : classes) {
//            for (Object value : container.values()) {
//                if (value.getClass().equals(aClass)) {
//                    objectList.add(value);
//                }
//            }
//        }
//        return objectList;
//    }

    protected List<Object> getAllObjectsWithValue(Parameter[] parameters, Map<String, Object> container) {
        List<Object> objectList = new ArrayList<>();
        for (Parameter parameter : parameters) {
            if (parameter.getType().isInterface()) {
                if (parameter.isAnnotationPresent(Qualifier.class)) {
                    String value = parameter.getAnnotation(Qualifier.class).value();
                    Object o = container.get(value);
                    objectList.add(o);
                    continue;
                }
            }
            if (parameter.isAnnotationPresent(Value.class)) {
                String value = parameter.getAnnotation(Value.class).name();
                objectList.add(value);
            } else {
                Class<?> type = parameter.getType();
                for (Object value : container.values()) {
                    if (value.getClass().equals(type)) {
                        objectList.add(value);
                    }
                }
            }
        }
        return objectList;
    }

    protected void createBeanOnClassesFactory(List<Class<?>> classes, Map<String, Object> container) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        classes.sort(new BeanConstructorComparator());
        for (Class<?> aClass : classes) {
            char[] chars = aClass.getSimpleName().toCharArray();
            char c = Character.toLowerCase(chars[0]);
            chars[0] = c;
            String name = String.valueOf(chars);
            if (aClass.getConstructors().length > 1) {
                throw new IllegalStateException();
            }
            Constructor<?>[] constructors = aClass.getConstructors();
            if (constructors[0].getParameterCount() == 0) {
                Object o = aClass.getConstructor().newInstance();
                container.put(name, o);
            } else {
                Constructor<?> constructor = constructors[0];
                Parameter[] parameters = constructor.getParameters();
//                boolean isValue = false;
//                for (Parameter parameter : parameters) {
//                    if (parameter.isAnnotationPresent(Value.class)) {
//                        isValue = true;
//                        break;
//                    }
//                }
//                if (isValue) {
                List<Object> allObjectsWithValue = getAllObjectsWithValue(parameters, container);
                Object o = constructor.newInstance(allObjectsWithValue.toArray());
                container.put(name, o);
//                } else {
//                    Class<?>[] parameterTypes = constructor.getParameterTypes();
//                    List<Object> allObjectsWithoutValue = getAllObjectsWithoutValue(parameterTypes, container);
//                    Object o = constructor.newInstance(allObjectsWithoutValue.toArray());
//                    container.put(name, o);
//                }
            }
        }
    }

    protected void scanObjectsAndInject(Map<String, Object> container) {
        Collection<Object> values = container.values();
        for (Object value : values) {
            Field[] fields = value.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    if (field.getType().isInterface()) {
                        if (field.isAnnotationPresent(Qualifier.class)) {
                            String value1 = field.getAnnotation(Qualifier.class).value();
                            field.setAccessible(true);
                            try {
                                field.set(value, container.get(value1));
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        char[] chars = field.getType().getSimpleName().toCharArray();
                        char c = Character.toLowerCase(chars[0]);
                        chars[0] = c;
                        field.setAccessible(true);
                        String s = String.valueOf(chars);
                        try {
                            field.set(value, container.get(s));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (field.isAnnotationPresent(Value.class)) {
                    String name = field.getAnnotation(Value.class).name();
                    field.setAccessible(true);
                    try {
                        field.set(value, name);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    protected void createBeanOnMethodFactory(List<Method> methodList, Class<?> configClass, Map<String, Object> container) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        methodList.sort(new MethodComparator());
        for (Method method : methodList) {
            Object o = configClass.getConstructor().newInstance();
            String name = method.getName();
            if (method.getParameterCount() == 0) {
                Object invoke = method.invoke(o);
                container.put(name, invoke);
            } else {
                Parameter[] parameters = method.getParameters();
//                boolean isValue = false;
//                for (Parameter parameter : parameters) {
//                    if (parameter.isAnnotationPresent(Value.class)) {
//                        isValue = true;
//                        break;
//                    }
//                }
//                if (isValue) {
                List<Object> abc = getAllObjectsWithValue(parameters, container);
                Object invoke = method.invoke(o, abc.toArray());
                container.put(name, invoke);
//                } else {
//                    List<Object> abc = getAllObjectsWithoutValue(method.getParameterTypes(), container);
//                    Object invoke = method.invoke(o, abc.toArray());
//                    container.put(name, invoke);
//                }
            }
        }
    }
}
