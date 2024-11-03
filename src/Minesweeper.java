import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Minesweeper {
    private class MineTile extends JButton {
        int r;
        int c;

        public MineTile(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    int titleSize = 70;
    int numRows = 10;
    int numColums = numRows;
    int boardWidch = numColums * titleSize;
    int boardHeight = numRows * titleSize;

    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    int mineCount = 10;
    MineTile[][] board = new MineTile[numRows][numColums];
    ArrayList<MineTile> mineList;
    Random random = new Random();

    int tilesClicked = 0;
    boolean gameOver = false;

    JFrame frame = new JFrame("Minesweeper");

    Minesweeper(){
       
        frame.setSize(boardWidch, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.BOLD, 25));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Minesweeper");
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(numRows, numColums));
        boardPanel.setBackground(Color.BLUE);
        frame.add(boardPanel);

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numColums; c++) {
                MineTile tile = new MineTile(r, c);
                board[r][c] = tile;

                tile.setFocusable(false);
                tile.setMargin(new Insets(0, 0, 0, 0));
                tile.setFont(new Font("Arial Unicode MS", Font.PLAIN, 45));
                //tile.setText("💣");
                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (gameOver) {
                            return;
                        }
                        MineTile tile = (MineTile) e.getSource();

                        //left click
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (tile.getText() == "") {
                                if (mineList.contains(tile)) {
                                    revealMines();
                                }
                                else {
                                    checkMine(tile.r, tile.c);
                                }
                            }
                        }
                        else if (e.getButton() == MouseEvent.BUTTON3) {
                            if (tile.getText() == "" && tile.isEnabled()) {
                                tile.setText("🔖");
                            }
                            else if (tile.getText() == "🔖") {
                                tile.setText("");
                            }
                        }
                    }
                });
                boardPanel.add(tile);
            }
        }
        frame.setVisible(true);

        setMines();
    }
    void setMines() {
        mineList = new ArrayList<MineTile>();

       // mineList.add(board[2][2]);
       // mineList.add(board[2][3]);
       // mineList.add(board[5][6]);
       // mineList.add(board[3][4]);
       // mineList.add(board[1][1]);

       int mineLeft = mineCount;
       while (mineLeft > 0) {
        int r = random.nextInt(numRows);
        int c = random.nextInt(numColums);

        MineTile tile = board[r][c];
        if (!mineList.contains(tile)) {
            mineList.add(tile);
            mineLeft -= 1;
        }
       }
    }
    void revealMines() {
        for (int i = 0; i < mineList.size(); i++) {
            MineTile tile = mineList.get(i);
            tile.setText("💣");
        }

        gameOver = true;
        textLabel.setText("Game Over!");
    }

    void checkMine(int r, int c) {
        if (r < 0 || r >= numRows || c < 0 || c >= numColums) {
            return;
        }

        MineTile tile = board[r][c];
        if (!tile.isEnabled()){
            return;
        }
        tile.setEnabled(false);
        tilesClicked +=1;

        int minesFound = 0;

        minesFound += countMine(r-1, c-1);
        minesFound += countMine(r-1, c);
        minesFound += countMine(r-1, c+1);

        // left and right
        minesFound += countMine(r, c-1);
        minesFound += countMine(r, c+1);

        //botton 3
        minesFound += countMine(r+1, c-1);
        minesFound += countMine(r+1, c);
        minesFound += countMine(r+1, c+1);

        if (minesFound > 0) {
            tile.setText(Integer.toString(minesFound));
        }
        else {
            tile.setText("");

            //top 3
            checkMine(r-1, c-1);
            checkMine(r-1, c);
            checkMine(r-1, c+1);

            // left and right
            checkMine(r, c-1);
            checkMine(r, c+1);

            // bottom 3
            checkMine(r+1, c-1);
            checkMine(r+1, c);
            checkMine(r+1, c+1);
        }
        if (tilesClicked == numRows * numColums - mineList.size()) {
            gameOver = true;
            textLabel.setText("Winner!!!");
        }
    }

    int countMine(int r, int c) {
        if (r < 0 || r >= numRows || c < 0 || c >= numColums) {
            return 0;
        }
        if (mineList.contains(board[r][c])) {
            return 1;
        }
        return 0;
    }
}
