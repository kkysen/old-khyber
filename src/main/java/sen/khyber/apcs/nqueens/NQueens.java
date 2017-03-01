package sen.khyber.apcs.nqueens;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class NQueens {
    
    private final int n;
    
    private long solutionCount = -1;
    
    private final long all; // mask for n 1's
    
    private final long[] moves; // stored as bits
    private final List<long[]> solutions = new ArrayList<>(); // keeps track of what columns queens are placed in
    private List<ChessBoard> boards;
    private ChessBoard board;
    
    public NQueens(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException("size n must be non-negative");
        }
        this.n = n;
        all = (1L << n) - 1; // all 1's
        moves = new long[n];
    }
    
    private void checkSize() {
        if (n > 64) {
            throw new IllegalStateException(
                    "Use of 64-bit longs does not allow boards larger than 64 to be solved. "
                            + "Boards of that size will take millions of years anyways.");
        }
    }
    
    /**
     * counts all solutions bitwise, is extremely fast
     * 
     * @param cols tracks invalid places by columns
     * @param ld tracks invalid places by the left diagonal
     * @param rd tracks invalid places by the right diagonal
     */
    private void countSolutions(final long cols, final long ld, final long rd) {
        if (cols == all) {
            solutionCount++;
            return;
        }
        long row = ~(cols | ld | rd) & all; // 1's are empty
        // iterate through 1 bits by finding next least significant bit and toggling it off
        while (row != 0) {
            final long col = row & -row; // next open column (least significant bit)
            // row ^= col; // XOR seems to be a bit slower than subtraction for some reason
            row -= col; // place queen by toggling off that bit
            // add col bit to cols, ld, and rd
            // shift ld to the right and rd to the left
            countSolutions(cols | col, (ld | col) >>> 1, (rd | col) << 1);
        }
    }
    
    /**
     * wraps the bitwise countSolutions(long cols, long ld, long rd)
     * takes advantage of basic reflection symmetry to only find half of the
     * boards and then multiply by 2
     */
    private void countSolutions() {
        // mid is the symmetric version of n
        int mid = n >>> 1;
        // parallel doesn't work always b/c concurrent access to solutionCount (I think)
        //        IntStream.range(0, mid)
        //                .parallel()
        //                .forEach(j -> {
        //                    final long col = 1L << j;
        //                    countSolutions(col, col >>> 1, col << 1);
        //                });
        for (int j = 0; j < mid; j++) {
            final long col = 1L << j;
            countSolutions(col, col >>> 1, col << 1);
        }
        // if n is odd you need to account for the middle column
        if ((n & 1) == 1) {
            final long midCol = 1L << mid;
            mid--; // exclude position right below and to the left of the top middle
            for (int j = 0; j < mid; j++) {
                final long col = 1L << j;
                final long cols = midCol | col;
                final long ld = (midCol >>> 1 | col) >>> 1;
                final long rd = (midCol << 1 | col) << 1;
                countSolutions(cols, ld, rd);
            }
        }
        // double because of symmetry
        solutionCount *= 2;
    }
    
    public long numSolutions() {
        if (solutionCount != -1) {
            return solutionCount;
        }
        checkSize();
        solutionCount++; // set to 0
        countSolutions();
        return solutionCount;
    }
    
    private void findAllBoards(final int i, final long cols, final long ld, final long rd) {
        if (cols == all) {
            solutionCount++;
            solutions.add(Arrays.copyOf(moves, n)); // save solution
            return;
        }
        long row = ~(cols | ld | rd) & all; // 1's are empty
        while (row != 0) {
            final long col = row & -row; // next open column (least significant bit)
            row -= col; // place queen
            moves[i] = col;
            // add col bit to cols, ld, and rd
            // shift ld to the right and rd to the left
            findAllBoards(i + 1, cols | col, (ld | col) >>> 1, (rd | col) << 1);
        }
    }
    
    private void findAllBoards() {
        // mid is the symmetric version of n
        int mid = n >>> 1;
        for (int j = 0; j < mid; j++) {
            final long col = 1L << j;
            moves[0] = col;
            findAllBoards(1, col, col >>> 1, col << 1);
        }
        
        // if n is odd you need to account for the middle column
        if ((n & 1) == 1) {
            final long midCol = 1L << mid;
            moves[0] = midCol;
            mid--; // exclude position right below and to the left of the top middle
            for (int j = 0; j < mid; j++) {
                final long col = 1L << j;
                moves[1] = col;
                final long cols = midCol | col;
                final long ld = (midCol >>> 1 | col) >>> 1;
                final long rd = (midCol << 1 | col) << 1;
                findAllBoards(2, cols, ld, rd);
            }
        }
        
        // double because of reflective symmetry
        solutionCount <<= 1;
    }
    
    public List<ChessBoard> allSolutions() {
        if (boards != null) {
            return boards;
        }
        checkSize();
        solutionCount++; // set to 0
        findAllBoards();
        
        // copy a reverse of all solutions because of reflective symmetry
        // will fail if solutionCount > Integer.MAX_VALUE (if n >= 19)
        boards = new ArrayList<>((int) solutionCount);
        final int len = (int) (solutionCount >>> 1);
        for (int i = 0; i < len; i++) {
            final long[] solution = solutions.get(i);
            final long[] reverse = new long[solution.length]; // better JIT optimizations
            for (int j = 0; j < reverse.length; j++) {
                reverse[j] = Long.reverse(solution[j]) >> 64 - n;
            }
            boards.add(new LongChessBoard(solution));
            boards.add(new LongChessBoard(reverse));
        }
        return boards;
    }
    
    private boolean findFirstBoard(final int i, final long cols, final long ld, final long rd) {
        if (cols == all) {
            return true;
        }
        long row = ~(cols | ld | rd) & all; // 1's are empty
        while (row != 0) {
            final long col = row & -row; // next open column (least significant bit)
            row -= col; // place queen
            moves[i] = col;
            // add col bit to cols, ld, and rd
            // shift ld to the right and rd to the left
            if (findFirstBoard(i + 1, cols | col, (ld | col) >>> 1, (rd | col) << 1)) {
                return true;
            }
        }
        return false;
    }
    
    public ChessBoard firstSolution() {
        if (board != null) {
            return board;
        }
        checkSize();
        findFirstBoard(0, 0L, 0L, 0L);
        board = new LongChessBoard(moves);
        return board;
    }
    
    public static void numSolutionsTest(final int n) {
        final long start = System.nanoTime();
        final NQueens nQueens = new NQueens(n);
        final DecimalFormat formatter = new DecimalFormat();
        final long numSolutions = nQueens.numSolutions();
        System.out.println(formatter.format(numSolutions) + " solutions");
        final double seconds = (System.nanoTime() - start) / 1e9;
        System.out.println(seconds + " sec");
        final long solutionsPerSec = (long) (numSolutions / seconds);
        System.out.println(formatter.format(solutionsPerSec) + " solutions per sec");
    }
    
    public static void allSolutionsTest(final int n, final boolean print) {
        final long start = System.nanoTime();
        final NQueens nQueens = new NQueens(n);
        nQueens.allSolutions();
        final double seconds = (System.nanoTime() - start) / 1e9;
        if (print) {
            for (final ChessBoard solution : nQueens.allSolutions()) {
                System.out.println(solution);
            }
        }
        System.out.println(nQueens.numSolutions());
        System.out.println(seconds + " sec");
    }
    
    public static void firstSolutionTest(final int n) {
        final long start = System.nanoTime();
        final NQueens nQueens = new NQueens(n);
        nQueens.firstSolution();
        final double seconds = (System.nanoTime() - start) / 1e9;
        System.out.println(nQueens.firstSolution());
        System.out.println(seconds + " sec");
    }
    
    public static void main(final String[] args) {
        //firstSolutionTest(35);
        //allSolutionsTest(16, false);
        //allSolutionsTest(8, true);
        //        for (int n = 0; n <= 18; n++) {
        //            System.out.println("n = " + n);
        //            numSolutionsTest(n);
        //            System.out.println();
        //        }
        numSolutionsTest(12);
    }
    
}
