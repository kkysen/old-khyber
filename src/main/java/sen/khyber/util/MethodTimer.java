package sen.khyber.util;

import sen.khyber.apcs.sorts.SortAlgorithm;
import sen.khyber.apcs.sorts.Sorts;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.Supplier;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class MethodTimer {
    
    private static void timeSortAlgorithm(final Method method, final Supplier<int[]> arraySupplier,
            final int iterNum)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        int[] a = arraySupplier.get();
        final Class<?> returnType = method.getReturnType();
        final long startTime = System.nanoTime();
        if (returnType.equals(Void.TYPE)) {
            method.invoke(null, a);
        } else {
            a = (int[]) method.invoke(null, a);
        }
        final long time = System.nanoTime() - startTime;
        System.out.println("\ttime #" + iterNum + ": " + time / 1e9 + " sec");
        if (!Sorts.isSorted(a)) {
            System.out.println("failed: " + Arrays.toString(a));
            Arrays.sort(a);
            System.out.println("sorted: " + Arrays.toString(a));
            throw new AssertionError("not sorted");
        }
    }
    
    private static Annotation getAnnotation(final Method method,
            final Class<? extends Annotation> annotation) {
        final Annotation[] annotations = method.getDeclaredAnnotations();
        for (final Annotation anno : annotations) {
            if (anno.annotationType().equals(annotation)) {
                return anno;
            }
        }
        return null;
    }
    
    public static void timeSortAlgorithms(final Class<?> type, final Supplier<int[]> arraySupplier)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final Method[] methods = type.getDeclaredMethods();
        for (final Method method : methods) {
            if (Modifier.isStatic(method.getModifiers())) {
                final SortAlgorithm annotation = (SortAlgorithm) getAnnotation(method,
                        SortAlgorithm.class);
                if (annotation == null) {
                    continue;
                }
                final int numIters = annotation.numIters();
                System.out.println("timing " + method.getName() + " (" + numIters + " times)");
                for (int i = 0; i < numIters; i++) {
                    timeSortAlgorithm(method, arraySupplier, i + 1);
                }
            }
        }
    }
    
}
