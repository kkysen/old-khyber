package sen.khyber.math;

import java.util.Arrays;
import java.util.Random;

import lombok.Getter;

public class Vector implements Cloneable, Comparable<Vector> {

    private static final Random random = new Random();
    
    private double[] vector;
    private final @Getter int dimensions;
    
    public Vector(double... vector) {
        this.vector = vector;
        dimensions = vector.length;
    }
    
    public Vector(int dimensions, double max) {
        this.dimensions = dimensions;
        vector = new double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            vector[i] = random.nextDouble() * max;
        }
    }
    
    @Override
    public Vector clone() {
        return new Vector(Arrays.copyOf(vector, dimensions));
    }
    
    public double[] toArray() {
        return vector;
    }
    
    public double get(int i) {
        return vector[i];
    }
    
    protected void set(int i, double val) {
        vector[i] = val;
    }
    
    public double dot(Vector other) {
        if (dimensions != other.getDimensions()) {
            throw new IllegalArgumentException("cannot dot two vectors of different dimensions");
        }
        double product = 0;
        for (int i = 0; i < dimensions; i++) {
            product += get(i) * other.get(i);
        }
        return product;
    }
    
    public double norm() {
        return dot(this);
    }
    
    public double magnitude() {
        return norm();
    }
    
    public Vector negate() {
        Vector negated = clone();
        for (int i = 0; i < dimensions; i++) {
            negated.set(i, - get(i));
        }
        return negated;
    }
    
    public Vector plus(Vector other) {
        Vector sum = clone();
        // TODO
        return null; 
    }

    @Override
    public int compareTo(Vector o) {
        // TODO Auto-generated method stub
        return 0;
    }
    
}
