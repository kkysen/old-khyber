package sen.khyber.math.geom.polygons;

import sen.khyber.math.geom.Point;

// import org.junit.Test;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Tester {
    
    public static void print(final Object obj, final Object shouldEqual) {
        System.out.println(obj + " should be... " + shouldEqual);
    }
    
    //@Test
    public static void testPoint() {
        
        System.out.println("\ntesting Point...");
        
        final Point p = new Point(6, 8);
        print(p, "(6.0, 8.0)");
        
        final Point randomPoint = Point.random();
        System.out.println(randomPoint); // (random, random);
        
        final Point q = new Point(3, 4);
        print(q.magnitude(), 5.0);
        
        final Point r = p.move(q);
        print(r, "(9.0, 12.0)");
        
        print(q.equals(q.move(0.00001, 0)), true);
        
        print(p.compareTo(q), 1);
        
        print(q.distanceTo(r), 10.0);
        
    }
    
    //@Test
    public static void testTriangle1() {
        
        System.out.println("\ntesting Triangle1...");
        
        final Point a = new Point(0, 0);
        final Point b = new Point(0, 7);
        final Point c = new Point(24, 0);
        
        final Triangle abc = new Triangle(a, b, c);
        
        print(abc.isRight(), true);
        
        print(abc, "Triangle @ (0.0, 0.0), (0.0, 7.0), (24.0, 0.0)");
        
        print(abc.getPerimeter(), 56.0);
        
        print(abc.getArea(), 84.0);
        
        abc.draw();
        
        final Point d = new Point(0, 100);
        
        final Triangle abd = new Triangle(a, b, d);
        abd.draw();
        System.out.println(abd.getArea() + " == " + abd.getHeronsArea());
        
    }
    
    //@Test
    public static void testTriangle2() {
        
        System.out.println("\ntesting Triangle2...");
        
        final Triangle rand = Triangle.random();
        System.out.println(rand);
        
        /*
         * cannot verify at compile time due to randomness,
         * but when I copied the coordinates to 
         * an online triangle area calculator
         * the area was the same
         * 
         * I also verified this by also making a getHeronsArea() method
         * that uses Heron's formula to find the area
         * which also kind of validates the getPerimeter() method
         * since it is used in getHeronsArea()
         */
        print(rand.getArea(), rand.getHeronsArea());
        
        rand.draw();
        
        for (int i = 0; i < 10; i++) {
            //Triangle.random().draw();
        }
        
    }
    
    public static void main(final String[] args) {
        testPoint();
        testTriangle1();
        testTriangle2();
        
        final Polygon polygon = Polygon.random(100);
        polygon.draw();
        System.out.println(polygon.getArea());
        
    }
    
}