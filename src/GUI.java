import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {

    private JPanel board;
    private final JButton[][] squares = new JButton[8][8];

    private JTextArea jPrintArea;
    JPanel jpNavigationRight = new JPanel(new GridLayout(0, 1));

    private Icon homePiece = new ImageIcon();
    private Icon awayPiece = new ImageIcon();
    private final Icon empty = new ImageIcon("empty");

    public void createBoard() {
        board = new JPanel(new GridLayout(8, 8));
        board.setSize(400, 400);
        for (int x = 0; x < squares.length; x++)
            for (int y = 0; y < squares[x].length; y++) {
                squares[x][y] = new JButton(x + "," + y);
                squares[x][y].setPreferredSize(new Dimension(50, 50));
                squares[x][y].setForeground(Color.BLACK);
                squares[x][y].setOpaque(true);
                board.add(squares[x][y]);
            }
    }

    public void createTextBoxArea() {
        JPanel jpText = new JPanel(new GridLayout(0, 1));
        jPrintArea = new JTextArea(5, 10);
        jPrintArea.requestFocusInWindow();
        jpText.add(jPrintArea);
        JScrollPane scroll = new JScrollPane(jPrintArea);
        jpText.add(scroll);
        jpNavigationRight.add(jpText);
        add(jpNavigationRight, BorderLayout.SOUTH);
    }

    public void setCampColors() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                squares[x][y].setBackground(new Color(255, 255, 255));
                if ((x + y) <= 3) {
                    squares[x][y].setBackground(new Color(199, 230, 255));
                }
                if ((x + y) >= 11) {
                    squares[x][y].setBackground(new Color(255, 199, 199));
                }
            }
        }
    }

    public void addMarbles() {
        homePiece = new ImageIcon("red.png"); // these are the default icons
        awayPiece = new ImageIcon("blue.png"); // these are the default icons
        for (int x = 0; x < 8; x++)
            for (int y = 0; y < 8; y++)
                if ((x + y) <= 3) {
                    squares[x][y].setIcon(awayPiece);
                }
                 else if ((x + y) >= 11) {
                    squares[x][y].setIcon(homePiece);
                }
                 else {
                    squares[x][y].setIcon(empty);
                }
    }

    public void updateGUI(Tile[][] tiles) {
        for (int x = 0; x < 8; x++)
            for (int y = 0; y < 8; y++)
                if(tiles[x][y].color == 1) {
                    squares[x][y].setIcon(awayPiece);
                }
                else if(tiles[x][y].color == 2) {
                    squares[x][y].setIcon(homePiece);
                }
                else{
                    squares[x][y].setIcon(empty);
                }

    }

    public void addFrame() {
        add(board, BorderLayout.CENTER);
    }

    public void printText(String text) {
        jPrintArea.append(text);
    }

    public void printText(String text, int x, int y) {
        jPrintArea.append(String.format(text, x, y));
    }


}
