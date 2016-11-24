package sen.khyber.optimization.sa;

import java.util.Random;

import sen.khyber.optimization.continuous.Rastrigin;

public class SimulatedAnnealer {
    
    private static Random random = new Random();
    private State state;
    private double energy;
    public State minState;
    public double minEnergy;
    private double nextEnergy;
    private double temperature;
    private double decayRate;
    
    public SimulatedAnnealer(State initState, double initTemp, double decayRate) {
        state = initState;
        energy = initState.energy();
        minState = (State) state.clone();
        minEnergy = energy;
        temperature = initTemp;
        this.decayRate = decayRate;
    }
    
    public SimulatedAnnealer(State initState) {
        this(initState, 1, .99999);
    }
    
    private boolean metropolis() {
        return random.nextDouble() 
            < Math.exp((energy - nextEnergy)
                       / temperature);
    }
    
    public SimulatedAnnealer search(int iterations) {
        for (int i = 0; i < iterations; i++) {
            //if (i % 100000 == 0) {System.out.println(minEnergy + "\t" + energy);
            state.step();
            nextEnergy = state.energy();
            if (nextEnergy <= energy || metropolis()) {
                energy = nextEnergy;
                if (nextEnergy < minEnergy) {
                    minState = (State) state.clone();
                    minEnergy = nextEnergy;
                }
            } 
            else state.undo();
            temperature *= decayRate;
        }
        return this;
    }
    
    public SimulatedAnnealer search() {
        return search(1000000);
    }
    
    public SimulatedAnnealer search(int base, int index) {
        return search((int) Math.pow(base, index));
    }
    
    @Override
    public String toString() {
        return "{state=" + minState + ", energy=" + minEnergy + "}";
    }
    
    public static void main(String[] args) {
        SimulatedAnnealer annealer = new SimulatedAnnealer(new Rastrigin(1000, 1000));
        for (int i = 0; i < 10; i++) {
            System.out.println(annealer.search());
        }
    }
    
}
