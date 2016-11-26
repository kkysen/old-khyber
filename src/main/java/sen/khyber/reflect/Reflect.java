package sen.khyber.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Reflect {
    
    private static <T> Stream<Field> getInstanceVarsStream(final Class<T> type) {
        final Stream<Field> fields = Arrays.asList(type.getDeclaredFields()).parallelStream();
        return fields.filter(field -> !Modifier.isStatic(field.getModifiers()))
                .map(field -> {
                    field.setAccessible(true);
                    return field;
                });
    }
    
    public static <T> List<Field> getInstanceVars(final Class<T> type) {
        return getInstanceVarsStream(type).collect(Collectors.toList());
    }
    
    public static <T> Stream<AbstractMap.SimpleImmutableEntry<String, Object>> getInstanceVarsEntries(
            final Class<T> type, final T t) {
        return getInstanceVarsStream(type).map(field -> {
            Object val;
            try {
                val = field.get(t);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                val = e;
            }
            return new AbstractMap.SimpleImmutableEntry<String, Object>(field.getName(), val);
        });
    }
    
    public static <T> Stream<AbstractMap.SimpleImmutableEntry<String, String>> getInstanceVarsStringEntries(
            final Class<T> type, final T t) {
        return getInstanceVarsStream(type).map(field -> {
            Object val;
            try {
                val = field.get(t);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                val = e;
            }
            return new AbstractMap.SimpleImmutableEntry<String, String>(field.getName(),
                    val.toString());
        });
    }
    
    public static <T> String toString(final Class<T> type, final T t, final CharSequence sep) {
        return getInstanceVarsStringEntries(type, t)
                .map(entry -> entry.getKey() + sep + entry.getValue())
                .collect(Collectors.joining(", ", "{", "}"));
    }
    
    public static <T> String toString(final Class<T> type, final T t) {
        return toString(type, t, ": ");
    }
    
}
