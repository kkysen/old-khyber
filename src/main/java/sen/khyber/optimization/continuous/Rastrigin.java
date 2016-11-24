package sen.khyber.optimization.continuous;

import java.util.Random;

import sen.khyber.optimization.sa.State;

public class Rastrigin implements State {
    
    public static final double STDDEV = .05;
    public static Random random = new Random();
    public double x;
    public double y;
    public double prevX;
    public double prevY;
    
    public Rastrigin(double x, double y) {
        this.x = x;
        this.y = y;
        prevX = x;
        prevY = y;
    }
        
    public Rastrigin() {
        this(10.0, 10.0);
    }
    
    @Override
    public void step() {
        prevX = x;
        prevY = y;
        x += STDDEV * random.nextGaussian();
        y += STDDEV * random.nextGaussian();
    }

    @Override
    public void undo() {
        x = prevX;
        y = prevY;
    }

    @Override
    public double energy() {
        return (x * x) + (y * y) - Math.cos(18 * x) - Math.cos(18 * y) + 2;
    }

    @Override
    public State clone() {
        Rastrigin copy = new Rastrigin(x, y);
        copy.prevX = prevX;
        copy.prevY = prevY;
        return copy;
    }
    
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

}
