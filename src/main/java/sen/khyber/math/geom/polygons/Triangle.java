package sen.khyber.math.geom.polygons;

import lombok.NonNull;

import sen.khyber.math.geom.Point;

/*
 * most of the important methods are in the class Polygon
 * because things like the finding the area and perimeter
 * can be abstracted to any sized polygon
 * 
 * immutable except for Polygon memoization
 * all methods are referentially transparent except for random()
 */
public class Triangle extends Polygon {
    
    //private static final double IS_CLOSE_DISTANCE = 0.000000001;
    
    @Deprecated
    public Triangle() {
        super(3);
    }
    
    public Triangle(@NonNull Point a, @NonNull Point b, @NonNull Point c) {
        super(a, b, c);
    }
    
    public Triangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        super(x1, y1, x2, y2, x3, y3);
    }
    
    public static Triangle random() {
        return new Triangle(Point.random(), Point.random(), Point.random());
    }
    
    // unused, substitutes in Polygon class
    /*
    private static boolean isCloseEnough(double a, double b) {
        return Math.abs(a - b) < IS_CLOSE_DISTANCE;
    }
    
    private static boolean isPythagorean(double leg1, double leg2, double hypot) {
        return isCloseEnough(leg1 * leg1 + leg2 * leg2, hypot * hypot);
    }
    */
    
    public boolean isRight() {
        return getNumRightAngles() == 1;
    }
    
    public boolean isAcute() {
        return getNumAcuteAngles() == 1;
    }
    
    public boolean isObtuse() {
        return getNumObtuseAngles() == 1;
    }
    
    public double getHeronsArea() {
        double s = getPerimeter() / 2;
        double a = getSide(0);
        double b = getSide(1);
        double c = getSide(2);
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }
    
}