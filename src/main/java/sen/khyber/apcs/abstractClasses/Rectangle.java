package sen.khyber.apcs.abstractClasses;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Rectangle extends Shape {
    
    private @Getter @Setter double length;
    private @Getter @Setter double width;
    
    public Rectangle(final double height, final double width) {
        this.length = height;
        this.width = width;
    }
    
    public Rectangle() {
        this(1.0, 1.0);
    }
    
    @Override
    public double getArea() {
        return length * width;
    }
    
    @Override
    public double getPerimeter() {
        return 2 * (length + width);
    }
    
    @Override
    public String toString() {
        return "A " + Rectangle.class.getSimpleName() + " with width=" + width + " and length="
                + length + ", which is a subclass of " + super.toString();
    }
    
}
