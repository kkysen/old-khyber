package sen.khyber.optimization.sa;

import sen.khyber.optimization.continuous.Rastrigin;

import java.util.Random;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class SimulatedAnnealer {
    
    private static Random random = new Random();
    private final State state;
    private double energy;
    public State minState;
    public double minEnergy;
    private double nextEnergy;
    private double temperature;
    private final double decayRate;
    
    public SimulatedAnnealer(final State initState, final double initTemp, final double decayRate) {
        state = initState;
        energy = initState.energy();
        minState = state.clone();
        minEnergy = energy;
        temperature = initTemp;
        this.decayRate = decayRate;
    }
    
    public SimulatedAnnealer(final State initState) {
        this(initState, 1, .99999);
    }
    
    private boolean metropolis() {
        return random.nextDouble() < Math.exp((energy - nextEnergy) / temperature);
    }
    
    public SimulatedAnnealer search(final int iterations) {
        for (int i = 0; i < iterations; i++) {
            //if (i % 100000 == 0) {System.out.println(minEnergy + "\t" + energy);
            state.step();
            nextEnergy = state.energy();
            if (nextEnergy <= energy || metropolis()) {
                energy = nextEnergy;
                if (nextEnergy < minEnergy) {
                    minState = state.clone();
                    minEnergy = nextEnergy;
                }
            } else {
                state.undo();
            }
            temperature *= decayRate;
        }
        return this;
    }
    
    public SimulatedAnnealer search() {
        return search(1000000);
    }
    
    public SimulatedAnnealer search(final int base, final int index) {
        return search((int) Math.pow(base, index));
    }
    
    @Override
    public String toString() {
        return "{state=" + minState + ", energy=" + minEnergy + "}";
    }
    
    public static void main(final String[] args) {
        final SimulatedAnnealer annealer = new SimulatedAnnealer(new Rastrigin(1000, 1000));
        for (int i = 0; i < 10; i++) {
            System.out.println(annealer.search());
        }
    }
    
}
