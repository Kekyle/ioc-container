package by.ioc.factory;

import by.ioc.test.RootConfiguration;

import java.lang.reflect.*;
import java.util.*;

//read bean def
//create bean
//inject dep

//java base
//annotation conf

public class ApplicationContext {
    private Class<?> configClass;
    private Map<String, Object> container = new HashMap<>();
    private ScannerDef scannerDef = new ScannerDef();
    private CreatorAndInjector createAndInject = new CreatorAndInjector();

    public ApplicationContext(Class<?> configClass) {
        this.configClass = configClass;
        start();
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ApplicationContext(RootConfiguration.class);
        System.out.println(applicationContext.getBeans());
//        Cat cat = (Cat) container.getBean("cat");
//        User user = (User) container.getBean("user");
//        System.out.println(user);
    }

    public Object getBean(String name) {
        return container.get(name);
    }

    public List<Object> getBeans() {
        return new ArrayList<>(container.values());
    }

    private void start() {
        List<Method> methods = scannerDef.readBeanDefinitionForConfigClass(configClass);
        List<Class<?>> classes = scannerDef.readBeanDefinitionForBasePackage(configClass);
        try {
            createAndInject.createBeanOnMethodFactory(methods, configClass, container);
            createAndInject.createBeanOnClassesFactory(classes, container);
            createAndInject.scanObjectsAndInject(container);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }

//    private List<Class<?>> readBeanDefinitionForBasePackage() {
//        String s = configClass.getAnnotation(ComponentScan.class).basePackage();
//        Reflections reflections = new Reflections(s);
//        return new ArrayList<>(reflections.getTypesAnnotatedWith(Component.class));
//    }
//
//    private List<Method> readBeanDefinitionForConfigClass(Class<?> configClass) {
//        List<Method> methodList = new ArrayList<>();
//        Method[] methods = configClass.getMethods();
//        for (Method method : methods) {
//            if (method.isAnnotationPresent(Bean.class)) {
//                methodList.add(method);
//            }
//        }
//        return methodList;
//    }

//    protected List<Object> getAllObjectsWithoutValue(Class<?>[] classes) {
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
//
//    protected List<Object> getAllObjectsWithValue(Parameter[] parameters) {
//        List<Object> objectList = new ArrayList<>();
//        for (Parameter parameter : parameters) {
//            if (parameter.getType().isInterface()) {
//                if (parameter.isAnnotationPresent(Qualifier.class)) {
//                    String value = parameter.getAnnotation(Qualifier.class).value();
//                    Object o = container.get(value);
//                    objectList.add(o);
//                    continue;
//                }
//            }
//            if (parameter.isAnnotationPresent(Value.class)) {
//                String value = parameter.getAnnotation(Value.class).name();
//                objectList.add(value);
//            } else {
//                Class<?> type = parameter.getType();
//                for (Object value : container.values()) {
//                    if (value.getClass().equals(type)) {
//                        objectList.add(value);
//                    }
//                }
//            }
//        }
//        return objectList;
//    }

//    private void createBeanOnClassesFactory(List<Class<?>> classes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
//        classes.sort(new BeanConstructorComparator());
//        for (Class<?> aClass : classes) {
//            char[] chars = aClass.getSimpleName().toCharArray();
//            char c = Character.toLowerCase(chars[0]);
//            chars[0] = c;
//            String name = String.valueOf(chars);
//            if (aClass.getConstructors().length > 1) {
//                throw new IllegalStateException();
//            }
//            Constructor<?>[] constructors = aClass.getConstructors();
//            if (constructors[0].getParameterCount() == 0) {
//                Object o = aClass.getConstructor().newInstance();
//                container.put(name, o);
//            } else {
//                Constructor<?> constructor = constructors[0];
//                Parameter[] parameters = constructor.getParameters();
//                boolean isValue = false;
//                for (Parameter parameter : parameters) {
//                    if (parameter.isAnnotationPresent(Value.class)) {
//                        isValue = true;
//                        break;
//                    }
//                }
//                if (isValue) {
//                    List<Object> allObjectsWithValue = getAllObjectsWithValue(parameters);
//                    Object o = constructor.newInstance(allObjectsWithValue.toArray());
//                    container.put(name, o);
//                } else {
//                    List<Object> allObjectsWithValue = getAllObjectsWithValue(parameters);
////                    Class<?>[] parameterTypes = constructor.getParameterTypes();
////                    List<Object> allObjectsWithoutValue = getAllObjectsWithoutValue(parameterTypes);
////                    Object o = constructor.newInstance(allObjectsWithoutValue.toArray());
//                    Object o = constructor.newInstance(allObjectsWithValue.toArray());
//                    container.put(name, o);
//                }
//            }
//        }
//    }
//
//    private void scanObjectsAndInject() {
//        Collection<Object> values = container.values();
//        for (Object value : values) {
//            Field[] fields = value.getClass().getDeclaredFields();
//            for (Field field : fields) {
//                if (field.isAnnotationPresent(Autowired.class)) {
//                    if (field.getType().isInterface()) {
//                        if (field.isAnnotationPresent(Qualifier.class)){
//                            String value1 = field.getAnnotation(Qualifier.class).value();
//                            field.setAccessible(true);
//                            try {
//                                field.set(value, container.get(value1));
//                            } catch (IllegalAccessException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    } else {
//                        char[] chars = field.getType().getSimpleName().toCharArray();
//                        char c = Character.toLowerCase(chars[0]);
//                        chars[0] = c;
//                        field.setAccessible(true);
//                        String s = String.valueOf(chars);
//                        try {
//                            field.set(value, container.get(s));
//                        } catch (IllegalAccessException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                if (field.isAnnotationPresent(Value.class)) {
//                    String name = field.getAnnotation(Value.class).name();
//                    field.setAccessible(true);
//                    try {
//                        field.set(value, name);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
//
//    private void createBeanOnMethodFactory(List<Method> methodList) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
//        methodList.sort(new MethodComparator());
//        for (Method method : methodList) {
//            Object o = configClass.getConstructor().newInstance();
//            String name = method.getName();
//            if (method.getParameterCount() == 0) {
//                Object invoke = method.invoke(o);
//                container.put(name, invoke);
//            } else {
//                Parameter[] parameters = method.getParameters();
//                boolean isValue = false;
//                for (Parameter parameter : parameters) {
//                    if (parameter.isAnnotationPresent(Value.class)) {
//                        isValue = true;
//                        break;
//                    }
//                }
//                if (isValue) {
//                    List<Object> abc = getAllObjectsWithValue(parameters);
//                    Object invoke = method.invoke(o, abc.toArray());
//                    container.put(name, invoke);
//                } else {
//                    List<Object> abc = getAllObjectsWithValue(parameters);
////                    List<Object> abc = getAllObjectsWithoutValue(method.getParameterTypes());
//                    Object invoke = method.invoke(o, abc.toArray());
//                    container.put(name, invoke);
//                }
//            }
//        }
//    }
}

