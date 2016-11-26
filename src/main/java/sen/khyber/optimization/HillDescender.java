package sen.khyber.optimization;

import sen.khyber.optimization.sa.State;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class HillDescender {
    
    private final State state;
    private double energy;
    private State minState;
    private double minEnergy;
    
    public HillDescender(final State initState) {
        state = initState;
        energy = initState.energy();
        minState = state.clone();
        minEnergy = energy;
    }
    
    public State search(final int numIters) {
        for (int i = 0; i < numIters; i++) {
            //if (i % 100000 == 0) System.out.println(minEnergy + "\t" + energy);
            state.step();
            final double nextEnergy = state.energy();
            if (nextEnergy <= energy) {
                energy = nextEnergy;
                if (nextEnergy < minEnergy) {
                    minState = state.clone();
                    minEnergy = nextEnergy;
                }
            } else {
                state.undo();
            }
        }
        return minState;
    }
    
}
