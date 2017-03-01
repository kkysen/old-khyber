package sen.khyber.apcs.nqueens;

/**
 * 
 * 
 * @author Khyber Sen
 */
public abstract class ChessBoard implements Cloneable {
    
    protected static long numClones = 0;
    protected static long numSets = 0;
    protected static long numGets = 0;
    protected static long numAdds = 0;
    
    public static String stats() {
        return "numClones = " + numClones
                + "\nnumSets   = " + numSets
                + "\nnumGets   = " + numGets
                + "\nnumAdds   = " + numAdds;
    }
    
    protected final int n;
    
    protected ChessBoard(final int n) {
        this.n = n;
    }
    
    @Override
    public abstract ChessBoard clone();
    
    public abstract void set(final int i, final int j);
    
    public abstract boolean get(final int i, final int j);
    
    public abstract void flip(final int i, final int j);
    
    public boolean isValidMove(final int i, final int j) {
        return !get(i, j);
    }
    
    public abstract boolean isRowFull(int i);
    
    protected abstract void addQueenColumn(int j);
    
    public ChessBoard addQueen(final int i, final int j) {
        numAdds++;
        final ChessBoard newBoard = clone();
        newBoard.addQueenColumn(j);
        for (int y = i, x = j; x >= 0 && y < n; x--, y++) {
            newBoard.set(y, x);
        }
        for (int y = i, x = j; x < n && y < n; x++, y++) {
            newBoard.set(y, x);
        }
        return newBoard;
    }
    
    public ChessBoard addQueen(final Queen queen) {
        return addQueen(queen.i, queen.j);
    }
    
    public void removeQueen(final Queen queen) {
        // default is cloning, so removing not needed
    }
    
    public static interface CharChooser {
        
        public char choose(int i, int j, boolean full);
        
    }
    
    public String toString(final CharChooser chooser) {
        final StringBuilder sb = new StringBuilder(n * (n + 1));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                //System.out.println("\t" + chooser.choose(i, j, get(i, j)));
                sb.append(chooser.choose(i, j, get(i, j)));
                sb.append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
    
    public String toString(final char queen, final char empty) {
        return toString((i, j, full) -> full ? queen : empty);
    }
    
    @Override
    public String toString() {
        return toString('\u25A0', '\u25A1');
    }
    
    public static void main(final String[] args) {
        final int n = 4;
        final Queen root = Queen.root(n);
        final ChessBoard board1 = new LongChessBoard(n);
        final Queen queen1 = root.newChild(0, 0);
        final ChessBoard board2 = board1.addQueen(queen1);
        final Queen queen2 = queen1.newChild(1, 2);
        final ChessBoard board3 = board2.addQueen(queen2);
        System.out.println(board1);
        System.out.println(board2);
        System.out.println(board3);
    }
    
}