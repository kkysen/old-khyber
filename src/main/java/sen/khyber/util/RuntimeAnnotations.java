public class RuntimeAnnotations {
    
    public static void processMethods(Class<?> type, Consumer<Method> methodProcessor) {
        for (Method method : type.getDeclaredMethods()) {
            methodProcessor.accept(method);
        }
    }
    
    public static void processMethodsWithAnnotation(Class<?> type, Class<? extends Annotation> annotation, Consumer<Method> methodProcessor) {
        processMethods(type, method -> {if (method.isAnnotationPresent(annotation)) methodProcessor.accept(method);});
    }
    
}
