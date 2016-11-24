// https://github.com/philiplarsson/Soduko.git

package sen.khyber.games.soduko;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class SudokuGUI extends JFrame {
    
    private final String FILENAME = "SudokuFile.txt";
    private List<JTextField> inputs;
    private JPanel sudoku;
    private JPanel buttons;
    private int field[][];
    private Sudoku s1;
    private List<Sudoku> sudokus;
    private int nbrOfSoduku;
    private int thisSudoku = 0;

    /**
     * Creates Gui.
     */
    public SudokuGUI() {
        initUI();
        colorMiniSudoku();
    }

    private void initUI() {
        sudoku = new JPanel();
        buttons = new JPanel();

        sudoku.setLayout(new GridLayout(9, 9));
        buttons.setLayout(new FlowLayout());

        inputs = new ArrayList<JTextField>();
        for (int i = 0; i < 81; i++) {
            inputs.add(new JTextField());
        }
        for (int i = 0; i < inputs.size(); i++) {
            sudoku.add(inputs.get(i));
        }

        JButton bNew = new JButton("New Soduko");
        JButton bCheck = new JButton("Check Soduko");
        JButton bClear = new JButton("Clear");
        JButton bSolve = new JButton("Solve");

        bClear.setToolTipText("Clears the entire soduko");
        bSolve.setToolTipText("Solves the soduko");

        bClear.setMnemonic(KeyEvent.VK_C);
        bSolve.setMnemonic(KeyEvent.VK_S);

        buttons.add(bClear);
        buttons.add(bSolve);
        buttons.add(bNew);
        buttons.add(bCheck);

        bSolve.addActionListener(new SolveButtonListener());
        bClear.addActionListener(new ClearButtonListener());
        bCheck.addActionListener(new CheckButtonListener());
        bNew.addActionListener(new NewButtonListener());

        add(sudoku, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        setTitle("Sudoku Solver");
        setVisible(true);
        setSize(500, 500);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    private void makeBoxesUneditable() {
        for (int i = 0; i < inputs.size(); i++) {
            JTextField tmp = inputs.get(i);
            tmp.setEditable(false);
        }
    }

    private void makeBoxesEditable() {
        for (int i = 0; i < inputs.size(); i++) {
            JTextField tmp = inputs.get(i);
            tmp.setEditable(true);
        }
    }

    private void colorMiniSudoku() {

        for (int i = 0; i < 81; i++) {
            if (i >= 0 && i < 3 || i >= 6 && i < 9) {
                inputs.get(i).setBackground(Color.LIGHT_GRAY);
            }
            if (i >= 9 && i < 12 || i >= 15 && i < 18) {
                inputs.get(i).setBackground(Color.LIGHT_GRAY);
            }
            if (i >= 18 && i < 21 || i >= 24 && i < 27) {
                inputs.get(i).setBackground(Color.LIGHT_GRAY);
            }

            if (i >= 30 && i < 33) {
                inputs.get(i).setBackground(Color.LIGHT_GRAY);
            }
            if (i >= 39 && i < 42) {
                inputs.get(i).setBackground(Color.LIGHT_GRAY);
            }
            if (i >= 48 && i < 51) {
                inputs.get(i).setBackground(Color.LIGHT_GRAY);
            }

            if (i >= 54 && i < 57 || i >= 60 && i < 63) {
                inputs.get(i).setBackground(Color.LIGHT_GRAY);
            }
            if (i >= 63 && i < 66 || i >= 69 && i < 72) {
                inputs.get(i).setBackground(Color.LIGHT_GRAY);
            }
            if (i >= 72 && i < 75 || i >= 78 && i < 81) {
                inputs.get(i).setBackground(Color.LIGHT_GRAY);
            }
        }
    }

    class SolveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            if (!loadNbrsFromGui()) {
                return;
            }
            s1 = new Sudoku(field);
            if (!checkIfOk()) {
                return;
            }
            solveSudoku();
            showNbrsInGui();

        }
    }

    class NewButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            field = new int[9][9];
            sudokus = new ArrayList<Sudoku>();
            readInNbrOfSudokus();
            readInSodukos();

            s1 = sudokus.get(thisSudoku);
            thisSudoku++;
            if (thisSudoku >= nbrOfSoduku) {
                thisSudoku = 0;
            }

            clearGui();
            makeBoxesUneditable();
            showNbrsInGui();

        }

        private void readInSodukos() {

            Scanner s = null;
            try {
                s = new Scanner(new File(FILENAME));
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(sudoku, "File " + FILENAME
                        + " could not load", "New Soduko",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }

            for (int k = 0; k < nbrOfSoduku; k++) {
                int tmpField[][] = new int[9][9];
                int row = 0;
                int col;
                for (int i = 0; i < 9; i++) {

                    String line = s.nextLine();
                    if (line.length() == 0) {
                        i--;
                    } else {
                        Scanner lineScanner = new Scanner(line);
                        col = 0;
                        while (lineScanner.hasNext()) {
                            String nextToken = lineScanner.next();
                            tmpField[row][col] = Integer.parseInt(nextToken);
                            col++;
                        }
                        lineScanner.close();
                        row++;
                    }
                }
                sudokus.add(new Sudoku(tmpField));

            }
        }

        private void readInNbrOfSudokus() {
            int nbrOfIntLines = 0;
            Scanner s = null;
            try {
                s = new Scanner(new File(FILENAME));
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(sudoku, "File " + FILENAME
                        + " could not load", "New Soduko",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }

            String line = s.nextLine();

            while (s.hasNextLine()) {

                if (line.matches(".*\\d.*")) {
                    nbrOfIntLines++;
                }
                line = s.nextLine();
            }

            nbrOfSoduku = nbrOfIntLines / 9;

        }

    }

    private class CheckButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (!loadNbrsFromGui()) {
                return;
            }
            s1 = new Sudoku(field);
            if (!checkIfOk()) {
                return;
            } else {
                JOptionPane.showMessageDialog(sudoku, "Everything is OK",
                        "Check Soduko", JOptionPane.INFORMATION_MESSAGE);
            }
            showNbrsInGui();

        }
    }

    private boolean checkIfOk() {
        if (!s1.checkIfValuesOk()) {
            JOptionPane.showMessageDialog(sudoku, "Value error", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void solveSudoku() {
        if (!s1.solve()) {
            JOptionPane.showMessageDialog(sudoku, "Sudoku is unsolvable",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            makeBoxesUneditable();
        }
    }

    private void showNbrsInGui() {
        field = s1.getField();
        int j = 0;
        for (int i = 0; i < 9; i++) {
            for (int k = 0; k < 9; k++) {
                JTextField tmp = inputs.get(j);
                if (field[i][k] == 0) {
                    tmp.setEditable(true);
                } else {
                    tmp.setText(Integer.toString(field[i][k]));
                }
                j++;
            }
        }

    }

    private boolean loadNbrsFromGui() {
        field = new int[9][9];
        int j = 0;
        int tmpTal = 0;
        for (int i = 0; i < 9; i++) {
            for (int k = 0; k < 9; k++) {
                JTextField tmp = inputs.get(j);
                if (tmp.getText().trim().isEmpty()) {
                    tmpTal = 0;
                } else {
                    try {
                        tmpTal = Integer.parseInt(tmp.getText());
                    } catch (NumberFormatException error) {
                        Color tmpColor = tmp.getBackground();
                        tmp.setBackground(Color.RED);
                        JOptionPane.showMessageDialog(sudoku,
                                "Not number in row " + (i + 1) + " col "
                                        + (k + 1), "Error",
                                JOptionPane.ERROR_MESSAGE);
                        tmp.setBackground(tmpColor);
                        return false;
                    }

                }
                if (tmpTal < 0 || tmpTal > 9) {
                    Color tmpColor = tmp.getBackground();
                    tmp.setBackground(Color.RED);

                    JOptionPane.showMessageDialog(sudoku, "Wrong number "
                            + (i + 1) + " col " + (k + 1), "Error",
                            JOptionPane.ERROR_MESSAGE);

                    tmp.setBackground(tmpColor);
                    return false;
                }
                field[i][k] = tmpTal;
                j++;
            }
        }
        return true;
    }

    private class ClearButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            clearGui();
            makeBoxesEditable();
        }

    }

    private void clearGui() {
        for (int i = 0; i < inputs.size(); i++) {
            JTextField tmp = inputs.get(i);
            tmp.setText("");
        }

    }

    /**
     * Starts the application.
     */
    public static void main(String[] args) {
        SudokuGUI g1 = new SudokuGUI();
    }
    
}
