package sen.khyber.optimization.combinations;

import java.util.Random;

import sen.khyber.optimization.sa.State;

public class BinPackingProblem implements State {
    
    public static Random random = new Random();
    public int[] sizes;
    public int[] placements;
    public int[] binLoads;
    public int lastItem;
    public int lastPlacement;
    
    public BinPackingProblem(int min, int max, int items, int bins) {
        sizes = new int[items];
        placements = new int[items];
        binLoads = new int[bins];
        for (int i = 0; i < items; i++) {
            sizes[i] = random.nextInt(max - min + 1) + min;
            placements[i] = random.nextInt(bins);
            binLoads[placements[i]] += sizes[i];
        }
    }
    
    public BinPackingProblem() {
        this(1, 100, 100, 10);
    }
    
    @Override
    public void step() {
        lastItem = random.nextInt(sizes.length);
        lastPlacement = placements[lastItem];
        while (placements[lastItem] == lastPlacement) {
            placements[lastItem] = random.nextInt(binLoads.length);
        }
        binLoads[placements[lastItem]] += sizes[lastItem];
    }

    @Override
    public void undo() {
        binLoads[placements[lastItem]] -= sizes[lastItem];
        binLoads[lastPlacement] += sizes[lastItem];
        placements[lastItem] = lastPlacement;
    }

    @Override
    public double energy() {
        int min = binLoads[0];
        int max = binLoads[0];
        for (int i = 1; i < binLoads.length; i++) {
            int next = binLoads[i];
            if (next < min) min = next;
            if (next > max) max = next;
        }
        return max - min;
    }

    @Override
    public State clone() {
        BinPackingProblem copy = new BinPackingProblem();
        copy.sizes = sizes.clone();
        copy.placements = placements.clone();
        copy.binLoads = binLoads.clone();
        return copy;
    }

}
