package by.ioc.factory.comparator;

import java.lang.reflect.Constructor;
import java.util.Comparator;

public class BeanConstructorComparator implements Comparator<Class<?>> {

    @Override
    public int compare(Class<?> o1, Class<?> o2) {
        return Integer.compare(o1.getConstructors()[0].getParameterCount(), o2.getConstructors()[0].getParameterCount());
    }
}
