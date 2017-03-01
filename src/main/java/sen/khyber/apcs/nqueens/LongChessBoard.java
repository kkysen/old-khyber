package sen.khyber.apcs.nqueens;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class LongChessBoard extends ChessBoard {
    
    private long cols;
    private final long[] rows;
    private final long fullMask;
    
    public LongChessBoard(final int n) {
        super(n);
        cols = 0L;
        rows = new long[n];
        fullMask = -1L >>> Long.SIZE - n;
    }
    
    private LongChessBoard(final LongChessBoard other) {
        super(other.n);
        cols = other.cols;
        rows = new long[n];
        fullMask = other.fullMask;
        System.arraycopy(other.rows, 0, rows, 0, n);
        numClones++;
    }
    
    public LongChessBoard(final long[] rows) {
        super(rows.length);
        this.rows = rows;
        cols = 0L; // doesn't matter
        fullMask = 0L; // doesn't matter
    }
    
    @Override
    public LongChessBoard clone() {
        return new LongChessBoard(this);
    }
    
    @Override
    public void set(final int i, final int j) {
        numSets++;
        rows[i] |= 1L << j;
    }
    
    @Override
    public boolean isRowFull(final int i) {
        return (rows[i] | cols) == fullMask;
    }
    
    @Override
    protected void addQueenColumn(final int j) {
        cols |= 1L << j;
    }
    
    @Override
    public boolean get(final int i, final int j) {
        numGets++;
        final long col = 1L << j;
        return (cols & col) == col || (rows[i] & col) == col;
    }
    
    @Override
    public void flip(final int i, final int j) {
        rows[i] ^= 1L << j;
    }
    
    @Override
    public LongChessBoard addQueen(final int i, final int j) {
        final LongChessBoard newBoard = clone();
        newBoard.addQueenColumn(j);
        for (int y = i, x = j; x >= 0 && y < n; x--, y++) {
            newBoard.set(y, x);
        }
        for (int y = i, x = j; x < n && y < n; x++, y++) {
            newBoard.set(y, x);
        }
        return newBoard;
    }
    
}
