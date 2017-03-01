package sen.khyber.apcs.nqueens;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Queen {
    
    private final int n;
    
    public final int i;
    public final int j;
    
    public final Queen parent;
    private final List<Queen> children = new ArrayList<>();
    
    public Queen(final int n, final Queen parent, final int i, final int j) {
        this.n = n;
        this.parent = parent;
        this.i = i;
        this.j = j;
    }
    
    public Queen(final int n, final int i, final int j) {
        this.n = n;
        this.i = i;
        this.j = j;
        parent = null;
    }
    
    public static Queen root(final int n) {
        return new Queen(n, null, -1, -1);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + i;
        result = prime * result + j;
        return result;
    }
    
    public boolean equals(final Queen other) {
        return i == other.i && j == other.j;
    }
    
    public void add(final Queen queen) {
        children.add(queen);
    }
    
    public Queen newChild(final int i, final int j) {
        final Queen child = new Queen(n, this, i, j);
        add(child);
        return child;
    }
    
    public boolean isValidMove(final int i, final int j) {
        if (parent == null) { // is root Queen
            return true;
        }
        for (Queen queen = this; queen.parent != null; queen = queen.parent) {
            final int dy = i - queen.i;
            final int dx = Math.abs(j - queen.j);
            if (dx == dy) {
                return false;
            }
        }
        return true;
    }
    
    public void remove(final Queen queen) {
        children.remove(queen);
    }
    
    public void clear() {
        children.clear();
    }
    
    public void delete() {
        parent.remove(this);
        children.clear();
    }
    
    public List<List<Queen>> solutions(final List<List<Queen>> solutions,
            final List<Queen> queens) {
        queens.add(this);
        for (final Queen child : children) {
            child.solutions(solutions, queens);
        }
        if (children.size() == 0) {
            solutions.add(new ArrayList<>(queens));
        }
        queens.remove(queens.size() - 1);
        return solutions;
    }
    
    public int numSolutions() {
        if (children.size() == 0) {
            if (parent == null) { // this is the root
                return 0;
            }
            return 1; // this is the solution
        }
        int numSolutions = 0;
        for (final Queen child : children) {
            numSolutions += child.numSolutions();
        }
        return numSolutions;
    }
    
    private void locations(final StringBuilder sb, final int tabs) {
        for (int i = 0; i < tabs; i++) {
            sb.append(' ');
        }
        sb.append("(" + i + ", " + j + ")\n");
        for (final Queen child : children) {
            child.locations(sb, tabs + 1);
        }
    }
    
    public String locations() {
        final StringBuilder sb = new StringBuilder();
        locations(sb, 0);
        return sb.toString();
    }
    
    public ChessBoard board() {
        final ChessBoard board = new LongChessBoard(n);
        for (Queen queen = this; queen.parent != null; queen = queen.parent) {
            board.set(queen.i, queen.j);
        }
        return board;
    }
    
    @Override
    public String toString() {
        return board().toString();
    }
    
}
