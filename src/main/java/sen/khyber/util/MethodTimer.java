

public class MethodTimer {
    
    private static void timeSortAlgorithm(Method method, Supplier<int[]> arraySupplier, int iterNum) {
        int[] a = arraySupplier.get();
        Type returnType = method.getGenericReturnType();
        long startTime = System.nanoTime();
        if (returnType.getTypeName().equals("void")) {
            method.invoke(null, a);
        } else {
            a = method.invoke(null, a);
        }
        long time = System.nanoTime() - startTime;
        System.out.println("\ttime #" + iterNum + ": " + time / 1_000_000.0 + " sec");
        if (!Sorts.isSorted(a)) {
            System.out.println("failed: " + Arrays.toString(a));
            Arrays.sort(a);
            System.out.println("sorted: " + Arrays.toString(a));
            throw new AssertionError("not sorted");
        }
    }
    
    private static Annotation getAnnotation(Method method, Class<? extends Annotation> annotation) {
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation anno : annotations) {
            if (anno.annotationType().equals(annotation)) {
                return anno;
            }
        }
        return null;
    }
    
    public static void timeSortAlgorithms(Class<?> type, Supplier<int[]> arraySupplier) {
        Method[] methods = type.getDeclaredMethods();
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers)) {
                Annotation annotation = getAnnotation(method, SortAlgorithm.class);
                if (annotation == null) {
                    continue;
                }
                int numIters = annotation.numIters();
                System.out.println("timing " + method.getName() + " (" + numIters + " times)");
                for (int i = 0; i < numIters; i++) {
                    timeSortAlgorithm(method, arraySupplier, i + 1);
                }
            }
        }
    }
    
}
