package sen.khyber.util;

/**
 *
 *
 * @author Khyber Sen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Time {
    
    public int numIters() default 1;
    
}
