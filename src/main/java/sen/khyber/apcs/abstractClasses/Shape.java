package sen.khyber.apcs.abstractClasses;

public abstract class Shape {
    
    public Shape() {}
    
    public abstract double getArea();
    
    public abstract double getPerimeter();
    
    public double getSemiPerimeter() {
        return getPerimeter() / 2;
    }
    
    @Override
    public String toString() {
        return Shape.class.getSimpleName();
    }
    
}
