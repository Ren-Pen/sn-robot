package top.bioelectronic.framework.commons;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;


public class ClassUtils {

    public static List<Field> getAllField(Class<?> clazz) {

        if (Throwable.class.isAssignableFrom(clazz)) throw new RuntimeException("Method not support Throwable Object");
        List<Field> list = new LinkedList<>();

        while (clazz != Object.class) {

            list.addAll(Arrays.asList(clazz.getDeclaredFields()));
            list.addAll(Arrays.asList(clazz.getFields()));
            clazz = clazz.getSuperclass();
        }

        return list;

    }

    public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> annotation) {

        T an = null;

        do {
            an = clazz.getAnnotation(annotation);
            clazz = clazz.getSuperclass();
        } while (an == null && clazz != Object.class);

        return an;

    }

    /**
     * 获取一个类及其父类上所有的注解
     *
     * @param clazz
     *
     * @return
     */
    public static Set<Class<? extends Annotation>> getClassAllAnnotationsType(Class<?> clazz) {
        Set<Class<? extends Annotation>> an = new LinkedHashSet<>();
        do {
            for (Annotation a : clazz.getAnnotations()) {
                an.add(a.annotationType());
            }
            clazz = clazz.getSuperclass();
        } while (clazz != Object.class);

        an.remove(Target.class);
        an.remove(Annotation.class);
        an.remove(Documented.class);
        an.remove(Inherited.class);
        an.remove(Retention.class);


        return an;
    }


    /**
     * 工具型递归函数
     *
     * @return
     */
    private static boolean hasOrgAnnotationE(Annotation[] annotations, Class<?> annotation, Set<Class<? extends Annotation>> lset) {
        if (annotations.length == 0) return false;
        for (Annotation a : annotations) {
            if (lset.contains(a.annotationType())) continue;
            lset.add(a.annotationType());
            if (a.annotationType() == annotation) return true;
            if (hasOrgAnnotationE(a.annotationType().getDeclaredAnnotations(), annotation, lset)) {
                return true;
            }
        }
        return false;

    }

    /**
     * 判断类上的注解是否有元注解
     */
    public static boolean hasOrgAnnotation(Class<?> clazz, Class<?> annotation) {
        Set<Class<? extends Annotation>> set = getClassAllAnnotationsType(clazz);
        Set<Class<? extends Annotation>> lset = new LinkedHashSet<>();

        if (set.contains(annotation)) return true;
        for (Class<? extends Annotation> c : set) {
            if (hasOrgAnnotationE(c.getDeclaredAnnotations(), annotation, lset)) {
                return true;
            }
        }

        return false;
    }

    private static <T extends Annotation> T getOrgAnnotationE(Annotation[] annotations, Class<T> annotation, Set<Class<? extends Annotation>> lset) {
        if (annotations.length == 0) return null;
        for (Annotation a : annotations) {
            if (lset.contains(a.annotationType())) continue;
            lset.add(a.annotationType());
            if (a.annotationType() == annotation) return (T) a;
            Annotation e = getOrgAnnotationE(a.annotationType().getDeclaredAnnotations(), annotation, lset);
            if (e != null) {
                return (T) e;
            }
        }
        return null;

    }

    /**
     * 获取类上的注解的元注解
     */
    public static <T extends Annotation> T getOrgAnnotation(Class<?> clazz, Class<T> annotation) {

        T a = clazz.getAnnotation(annotation);
        if (a != null) return a;

        Set<Class<? extends Annotation>> set = getClassAllAnnotationsType(clazz);
        Set<Class<? extends Annotation>> lset = new LinkedHashSet<>();

        for (Class<? extends Annotation> c : set) {
            T e = getOrgAnnotationE(c.getDeclaredAnnotations(), annotation, lset);
            if (e != null) {
                return e;
            }
        }

        if (set.contains(annotation)) return getAnnotation(clazz, annotation);

        return null;
    }

    public static Field getDeclaredField(Class<?> clazz, String fieldName) throws NoSuchFieldException {

        do {

            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignore) {
            }

            clazz = clazz.getSuperclass();

        } while (clazz != Object.class);

        throw new NoSuchFieldException();

    }

    /**
     * 获取类泛型类型
     *
     * @param clazz
     * @param slot
     *
     * @return
     */
    public static Class<?> getGenerateClass(Class<?> clazz, int slot) {
        return (Class<?>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[slot];
    }

}
