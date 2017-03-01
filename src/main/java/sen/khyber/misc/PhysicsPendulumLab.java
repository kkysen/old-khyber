package sen.khyber.misc;

import sen.khyber.math.MathUtils;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class PhysicsPendulumLab {
    
    private static final double GRAVITY = 9.807;
    
    private static double gravity(final double T, final double I, final double m, final double L,
            final double theta) {
        final double agm = MathUtils.agm(1, Math.cos(theta / 2));
        final double sqrtIdivmgL = T * agm / (2 * Math.PI);
        return I / (m * L * Math.pow(sqrtIdivmgL, 2));
    }
    
    private static double round(final double x) {
        return (int) (x * 1000) / 1000.0;
    }
    
    public static void main(final String[] args) {
        final double theta = Math.toRadians(10);
        final double[][] trials = {
            {1.654, 0.033523812, 0.0961, 0.5, theta},
            {1.503, 0.039067182, 0.2218, 0.335649234, theta},
            {1.530, 0.048922062, 0.2218, 0.414990983, theta},
            {1.561, 0.064948812, 0.2218, 0.5, theta},
            {1.901, 0.159223812, 0.2218, 0.78336339, theta},
            {1.766, 0.059223812, 0.1218, 0.605500821, theta},
            {1.186, 0.004312641, 0.0473, 0.25, theta},
            {1.399, 0.035737641, 0.173, 0.431647399, theta},
            {1.045, 0.012168891, 0.173, 0.25, theta},
            {1.118, 0.005918891, 0.073, 0.25, theta},
            {1.334, 0.010737641, 0.073, 0.338013699, theta}
        };
        
        double sumGravity = 0;
        for (final double[] trial : trials) {
            final double gravity = gravity(trial[0], trial[1], trial[2], trial[3], trial[4]);
            sumGravity += gravity;
            final double error = MathUtils.percentError(GRAVITY, gravity);
            System.out.print(round(gravity) + "\t");
            System.out.println(round(error * 100));
        }
        System.out.println();
        final double avgGravity = sumGravity / trials.length;
        final double avgError = MathUtils.percentError(GRAVITY * trials.length, avgGravity);
        System.out.println(round(avgGravity));
        System.out.println(round(avgError * 100));
        
        System.out.println(round(1.234567));
    }
    
}
