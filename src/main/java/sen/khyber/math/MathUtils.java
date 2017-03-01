package sen.khyber.math;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sqrt;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class MathUtils {
    
    public static int log2(int bits) {
        int log = 0;
        if ((bits & 0xffff0000) != 0) {
            bits >>>= 16;
            log = 16;
        }
        if (bits >= 256) {
            bits >>>= 8;
            log += 8;
        }
        if (bits >= 16) {
            bits >>>= 4;
            log += 4;
        }
        if (bits >= 4) {
            bits >>>= 2;
            log += 2;
        }
        return log + (bits >>> 1);
    }
    
    private static final int AGM_NUM_ITERS = 18;
    
    /**
     * calculates the arithmetic-geometric mean of two numbers
     * 
     * @param a first number
     * @param b second number
     * @return the arithmetic-geometric mean of a and b
     */
    public static double agm(double a, double b) {
        double temp;
        for (int i = 0; i < AGM_NUM_ITERS; i++) {
            temp = a;
            a = (a + b) / 2;
            b = Math.sqrt(temp * b);
        }
        return a; // same as b
    }
    
    private static final double GRAVITY = 9.807;
    
    /**
     * calculates the complete elliptical integral of the first kind
     * 
     * @param k elliptic modulus or eccentricity
     * @return the complete elliptical integral of the first kind
     */
    public static double completeEllipticalIntegral1(final double k) {
        return PI / (2 * agm(1, sqrt(1 - k * k)));
    }
    
    private static double exactPendulumPeriod(final double inertialProperty, final double theta,
            final double g) {
        final double agm = agm(1, cos(theta / 2));
        return 2 * PI / agm * sqrt(inertialProperty / g);
    }
    
    private static double exactPendulumPeriod(final double inertialProperty, final double theta) {
        return exactPendulumPeriod(inertialProperty, theta, GRAVITY);
    }
    
    public static double exactSimplePendulumPeriod(final double L, final double theta) {
        return exactPendulumPeriod(L, theta);
    }
    
    public static double exactPhysicalPendulumPeriod(final double I, final double m, final double L,
            final double theta) {
        return exactPendulumPeriod(I / (m * L), theta);
    }
    
    public static double percentError(final double accepted, final double experimental) {
        return (accepted - experimental) / accepted;
    }
    
}
