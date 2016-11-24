package sen.khyber.apcs.abstractClasses;

import lombok.Getter;
import lombok.Setter;

public class Circle extends Shape {
    
    private @Getter @Setter double radius;
    
    public Circle(final double radius) {
        this.radius = radius;
    }
    
    public Circle() {
        this(1.0);
    }
    
    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }
    
    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }
    
    @Override
    public String toString() {
        return "A " + Circle.class.getSimpleName() + " with radius=" + radius
                + ", which is a subclass of " + super.toString();
    }
    
}
