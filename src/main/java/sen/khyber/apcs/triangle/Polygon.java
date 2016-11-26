package sen.khyber.apcs.triangle;

import java.awt.Graphics;
import java.awt.geom.Path2D;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * immutable except for memoization
 * but all methods are referentially transparent except for random()
 * 
 * @author Khyber Sen
 */
public class Polygon {
    
    private static final double IS_CLOSE_DISTANCE = 0.000000001;
    
    private final Point[] vertices;
    private final int size;
    private final double[] sides; // for memoization
    private String sideType; // for memoization
    private final double[] angles; // for memoization
    private int numRightAngles = 0;
    private int numAcuteAngles = 0;
    private int numObtuseAngles = 0;
    private double area, perimeter; // for memoization
    private String toString; // for memoization
    
    protected Polygon(final int size) {
        if (size < 3) {
            throw new IllegalArgumentException("no polygons exist with less than 3 sides");
        }
        this.size = size;
        vertices = new Point[size];
        sides = new double[size];
        angles = new double[size];
    }
    
    public Polygon(final Point... vertices) {
        this(vertices.length);
        for (int i = 0; i < size; i++) {
            final Point vertex = vertices[i];
            if (vertex == null) {
                throw new NullPointerException();
            }
            this.vertices[i] = vertex.clone(); // unnecessary since Point is immutable
        }
    }
    
    public Polygon(final double... verticesCoords) {
        this(verticesCoords.length / 2);
        if (verticesCoords.length % 2 != 0) {
            throw new IllegalArgumentException("cannot define a point with only one coordinate");
        }
        for (int i = 0; i < size; i++) {
            vertices[i] = new Point(verticesCoords[i * 2], verticesCoords[i * 2 + 1]);
        }
    }
    
    // random polygon
    // might not be a polygon if points not in the right place
    // and it forms a intersecting shape
    public static Polygon random(final int size) {
        final Point[] vertices = new Point[size];
        for (int i = 0; i < size; i++) {
            vertices[i] = Point.random();
        }
        return new Polygon(vertices);
    }
    
    public Point[] getVertices() {
        return vertices.clone();
    }
    
    public int getSize() {
        return size;
    }
    
    /*
     * wraps indices around
     * arr.length becomes 0
     * -1 becomes arr.length - 1
     * as safety in loops when doing i + 1 or i - 1
     */
    private int fixIndex(final int i) {
        if (i == size) {
            return 0;
        } else if (i == -1) {
            return size - 1;
        } else {
            return i;
        }
    }
    
    private double calcSide(final int i) {
        final int next = fixIndex(i + 1);
        return vertices[i].distanceTo(vertices[next]);
    }
    
    // for memoization
    public double getSide(final int i) {
        if (sides[i] == 0) {
            sides[i] = calcSide(i);
        }
        return sides[i];
    }
    
    public double[] getSides() {
        for (int i = 0; i < size; i++) {
            getSide(i);
        }
        return sides.clone();
    }
    
    /*
     * does not work for any reflex angles
     * because the inverse cosine is undefined for reflex angles
     */
    private double calcAngle(final int i) {
        final int prev = fixIndex(i - 1);
        final int next = fixIndex(i + 1);
        final double a = getSide(prev);
        final double b = getSide(i);
        final double c = vertices[prev].distanceTo(vertices[next]);
        final double numerator = a * a + b * b - c * c;
        final double denominator = 2 * a * b;
        return Math.acos(numerator / denominator);
    }
    
    // for memoization
    public double getAngle(int i) {
        i = fixIndex(i);
        if (angles[i] == 0) {
            angles[i] = calcAngle(i);
        }
        return angles[i];
    }
    
    public double[] getAngles() {
        for (int i = 0; i < size; i++) {
            angles[i] = getAngle(i);
        }
        return angles;
    }
    
    private static boolean isClose(final double a, final double b) {
        return Math.abs(a - b) < IS_CLOSE_DISTANCE;
    }
    
    private static boolean isRight(final double angle) {
        return isClose(angle, Math.PI / 2);
    }
    
    private static boolean isAcute(final double angle) {
        return angle < Math.PI / 2;
    }
    
    // not needed because opposite of isAcute();
    /*private static boolean isObtuse(double angle) {
        return ! isAcute(angle);
    }*/
    
    // memoized
    private void calcNumAngleTypes() {
        if (numRightAngles + numAcuteAngles + numObtuseAngles == 0) {
            for (int i = 0; i < size; i++) {
                final double angle = getAngle(i);
                if (isRight(angle)) {
                    numRightAngles++;
                } else if (isAcute(angle)) {
                    numAcuteAngles++;
                } else { // must be obtuse because from arccos
                    numObtuseAngles++;
                }
            }
        }
    }
    
    public int getNumRightAngles() {
        calcNumAngleTypes();
        return numRightAngles;
    }
    
    public int getNumAcuteAngles() {
        calcNumAngleTypes();
        return numAcuteAngles;
    }
    
    public int getNumObtuseAngles() {
        calcNumAngleTypes();
        return numObtuseAngles;
    }
    
    /*
     * double wrapper class that implements equals() and hashCode()
     * allowing for a 4 bit variation
     * for use in HashSets in calcSideType()
     */
    private static class CloseDouble {
        
        private static final int IS_CLOSE_BITSHIFT = 4;
        
        double d;
        
        public CloseDouble(final double d) {
            this.d = d;
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            final long temp = Double.doubleToLongBits(d) >> IS_CLOSE_BITSHIFT << IS_CLOSE_BITSHIFT;
            result = prime * result + (int) (temp ^ temp >>> 32);
            return result;
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            } else if (obj == null) {
                return false;
            } else if (getClass() != obj.getClass()) {
                return false;
            }
            final CloseDouble other = (CloseDouble) obj;
            final long thisAsLong = Double
                    .doubleToLongBits(d) >> IS_CLOSE_BITSHIFT << IS_CLOSE_BITSHIFT;
            final long otherAsLong = Double
                    .doubleToLongBits(other.d) >> IS_CLOSE_BITSHIFT << IS_CLOSE_BITSHIFT;
            return thisAsLong == otherAsLong;
        }
        
    }
    
    /*
     * creates a HashSet and fills it with CloseDoubles for the sides
     * by checking the size of the Set and comparing it to the polygon size
     * it can determine the sideType: equilateral, scalene, or isosceles
     */
    private String calcSideType() {
        final Set<CloseDouble> sideLengths = new HashSet<>();
        for (int i = 0; i < size; i++) {
            sideLengths.add(new CloseDouble(getSide(i)));
        }
        final int uniqueSize = sideLengths.size();
        if (uniqueSize == 1) {
            return "equilateral";
        } else if (uniqueSize == size) {
            return "scalene";
        } else {
            return "isosceles";
        }
    }
    
    // for memoization
    public String getSideType() {
        if (sideType == null) {
            sideType = calcSideType();
        }
        return sideType;
    }
    
    // defined as having exactly one equal side
    public boolean isEquilateral() {
        return getSideType().equals("equilateral");
    }
    
    // defined as having at least two equal sides
    public boolean isIsosceles() {
        return getSideType().equals("isosceles");
    }
    
    // defined as having no equal sides
    public boolean isScalene() {
        return getSideType().equals("scalene");
    }
    
    private double calcPerimeter() {
        double perimeter = 0;
        for (int i = 0; i < size; i++) {
            perimeter += getSide(i);
        }
        return perimeter;
    }
    
    // for memoization
    public double getPerimeter() {
        if (perimeter == 0) {
            perimeter = calcPerimeter();
        }
        return perimeter;
    }
    
    // shoelace formula
    private double calcArea() {
        double area = 0;
        for (int i = 0; i < size - 1; i++) {
            final Point p1 = vertices[i];
            final Point p2 = vertices[i + 1];
            area += p1.getX() * p2.getY();
            area -= p2.getX() * p1.getY();
        }
        final Point first = vertices[0];
        final Point last = vertices[size - 1];
        area += last.getX() * first.getY();
        area -= first.getX() * last.getY();
        return Math.abs(area) / 2;
    }
    
    // for memoization
    public double getArea() {
        if (area == 0) {
            area = calcArea();
        }
        return area;
    }
    
    private String calcString() {
        final StringJoiner sj = new StringJoiner(", ", getClass().getSimpleName() + " @ ", "");
        for (final Point pt : vertices) {
            sj.add(pt.toString());
        }
        return sj.toString();
    }
    
    // for memoization
    @Override
    public String toString() {
        if (toString == null) {
            toString = calcString();
        }
        return toString;
    }
    
    public Path2D.Double getPath2D() {
        final Path2D.Double polygonPath = new Path2D.Double();
        final Point startPt = vertices[0];
        polygonPath.moveTo(startPt.getX(), startPt.getY());
        for (int i = 1; i < size; i++) {
            final Point pt = vertices[i];
            polygonPath.lineTo(pt.getX(), pt.getY());
        }
        return polygonPath;
    }
    
    public java.awt.Polygon getAWTPolygon() {
        final java.awt.Polygon AWTPolygon = new java.awt.Polygon();
        for (final Point pt : vertices) {
            AWTPolygon.addPoint((int) pt.getX() + 100, (int) pt.getY() + 100);
        }
        return AWTPolygon;
    }
    
    private class Canvas extends JComponent {
        
        private static final long serialVersionUID = 1L;
        
        @Override
        public void paint(final Graphics g) {
            g.drawPolygon(getAWTPolygon());
        }
        
    }
    
    public void draw() {
        final JFrame frame = new JFrame(toString());
        frame.setBounds(30, 30, 300, 300);
        frame.getContentPane().add(new Canvas());
        frame.setVisible(true);
    }
    
}