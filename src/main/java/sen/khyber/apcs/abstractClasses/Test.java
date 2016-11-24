package sen.khyber.apcs.abstractClasses;


public class Test {
    
    public static void main(final String[] args) {
        final Square square = new Square();
        System.out.println(square);
        square.setWidth(10);
        System.out.println(square);
        System.out.println(square.getArea());
        
        final Circle circle = new Circle(5);
        System.out.println(circle);
        System.out.println(circle.getArea());
        System.out.println(circle.getSemiPerimeter());
    }
    
}
