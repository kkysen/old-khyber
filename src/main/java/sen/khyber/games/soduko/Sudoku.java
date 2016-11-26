// https://github.com/philiplarsson/Soduko.git

package sen.khyber.games.soduko;

/**
 * 
 * 
 * @author Philip Plarsson
 */
public class Sudoku {
    
    int[][] field;
    
    /**
     * Creates a new Sudoku field.
     */
    public Sudoku() {
        field = new int[9][9];
    }
    
    /**
     * Creates a new Sudoku field with the parameter int[][] field.
     */
    public Sudoku(final int[][] field) {
        this.field = field;
    }
    
    /**
     * Tries to solve the sudoku.
     * 
     * @return Will return if the sudoku could be solved.
     */
    public boolean solve() {
        return solve(0, 0);
    }
    
    /**
     * Will return the field.
     * 
     * @return Will return int[][] field.
     */
    public int[][] getField() {
        return field;
    }
    
    /**
     * This method checks if the given number n is a valid entry into the
     * coordinate row, column.
     * 
     * @param row Represents rows 1 to 9.
     * @param column Represents columns 1 to 9.
     * @param n a given number between 1 to 9.
     * @return Will return true if the number can be entered in the given
     *         location.
     */
    public boolean checkLegal(final int row, final int column, final int n) {
        
        /*
         * StartColumn and StartRow are used to move between the nine larger
         * fields.
         */
        int startColumn = column / 3 * 3;
        int startRow = row / 3 * 3;
        field[row][column] = 0;
        
        /*
         * Checks the 3*3 field if n already exists. Returns false if n already
         * exists within this area.
         */
        for (int i = 0; i < 3; i++) {
            startRow = row / 3 * 3;
            for (int k = 0; k < 3; k++) {
                if (field[startRow][startColumn] == n) {
                    field[row][column] = n;
                    return false;
                }
                startRow++;
            }
            startColumn++;
        }
        
        /*
         * Checks the column if n already exists. Returns false if row already
         * contains n.
         */
        for (int i = 0; i < 9; i++) {
            if (field[row][i] == n) {
                field[row][column] = n;
                return false;
            }
        }
        
        /*
         * Checks the row if n already exists. Returns false if column already
         * contains n.
         */
        for (int i = 0; i < 9; i++) {
            if (field[i][column] == n) {
                field[row][column] = n;
                return false;
            }
        }
        
        /*
         * Insertion of int n in field[row][column] is valid. Changes the values
         * and returns true.
         */
        field[row][column] = n;
        return true;
    }
    
    /**
     * Goes through the entire sudoku field to check if all entered numbers are
     * valid.
     * It will start in the top left corner and work down toward the lower right
     * corner.
     * 
     * @return Will return true if no illegal numbers are encountered.
     */
    public boolean checkIfValuesOk() {
        int nbr;
        for (int i = 0; i < 9; i++) {
            for (int k = 0; k < 9; k++) {
                nbr = field[i][k];
                if (nbr == 0) {
                    // do nothing
                } else if (!checkLegal(i, k, nbr)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private boolean solve(int row, final int column) {
        if (column >= 9) {
            return solve(++row, 0);
        }
        
        if (row >= 9) {
            return true;
        }
        
        if (field[row][column] == 0) {
            
            for (int i = 1; i <= 9; i++) {
                if (checkLegal(row, column, i)) {
                    field[row][column] = i;
                    if (solve(row, column + 1)) {
                        return true;
                    }
                }
            }
            field[row][column] = 0;
            return false;
        }
        
        if (field[row][column] != 0) {
            final int n = field[row][column];
            
            if (checkLegal(row, column, n)) {
                return solve(row, column + 1);
            }
        }
        return false;
    }
    
}
