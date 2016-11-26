package sen.khyber.math.geom;

import java.util.Random;

import lombok.Getter;
import lombok.NonNull;

/**
 * immutable
 * 
 * @author Khyber Sen
 */
public class Point implements Comparable<Point>, Cloneable {
    
    private static final double IS_CLOSE_DISTANCE = 0.000000001; // billionth
    private static final double RANDOM_RANGE = 100.0;
    
    private static final Random random = new Random();
    
    private final @Getter double x;
    private final @Getter double y;
    
    public Point(final double x, final double y) {
        this.x = x;
        this.y = y;
    }
    
    public Point() {
        this(0, 0);
    }
    
    private static double rand() {
        return random.nextDouble() * 2 * RANDOM_RANGE - RANDOM_RANGE;
    }
    
    // random point
    public static Point random() {
        return new Point(rand(), rand());
    }
    
    public Point move(final double x, final double y) {
        return new Point(this.x + x, this.y + y);
    }
    
    public Point move(@NonNull final Point other) {
        return move(other.x, other.y);
    }
    
    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }
    
    private boolean isClose(final double a, final double b) {
        return Math.abs(a - b) < IS_CLOSE_DISTANCE;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (obj.getClass() != getClass()) {
            return false;
        }
        final Point other = (Point) obj;
        final boolean xEquals = isClose(x, other.x);
        final boolean yEquals = isClose(y, other.y);
        return xEquals && yEquals;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        // fix to allow for floating point error tolerance
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(y);
        result = prime * result + (int) (temp ^ temp >>> 32);
        return result;
    }
    
    @Override
    public int compareTo(@NonNull final Point other) {
        if (other == null) {
            throw new NullPointerException();
        }
        if (equals(other)) {
            return 0;
        } else {
            return Double.compare(magnitude(), other.magnitude());
        }
    }
    
    public double distanceTo(@NonNull final Point other) {
        final double dx = x - other.x;
        final double dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    public double distance(@NonNull final Point other) {
        return distanceTo(other);
    }
    
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
    
    @Override
    public Point clone() {
        return new Point(x, y);
    }
    
}