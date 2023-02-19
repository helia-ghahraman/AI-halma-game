import javax.swing.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Halma {

    private final static byte maxDepth = 3;
    private final Tile[][] tiles;
    private final short totalMoves = 0;
    GUI gameUI = new GUI();
    private byte playerTurn;
    private Agent agent;
    private byte firstX, firstY, secondX, secondY;

    public Halma() {
        tiles = new Tile[8][8];
        playerTurn = 1;
        assignCoordinates();
    }

    private void assignCoordinates() {
        for (byte i = 0; i < 8; i++)
            for (byte j = 0; j < 8; j++) {
                tiles[i][j] = new Tile(i, j);
                if ((i + j) <= 3)
                    tiles[i][j].color = tiles[i][j].zone = 1;
                else if ((i + j) >= 11)
                    tiles[i][j].color = tiles[i][j].zone = 2;
            }
    }

    private void startGame() {

        checkWinner();

        if (playerTurn == 1)
            doRandomAction(playerTurn);
        else {
            var move = agent.doMinMax(tiles, playerTurn);
            if (move != null)
                movePiece(move);
            else
                doRandomAction(playerTurn);
        }
        
        startGame();
    }

    private void checkWinner() {
        if (agent.checkTerminal(tiles)) {
            gameUI.printText("\n Game has ended! \n"+"winner: "+playerTurn);

            try {
                TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
            } catch (Exception ignored) {
            }
        }
    }

    private void doRandomAction(int playerTurn) {
        var possibleMoves = agent.createPossibleMoves(tiles, playerTurn);
        var random = new Random().nextInt(possibleMoves.size() - 1);
        firstX = possibleMoves.get(random).startPos.x;
        firstY = possibleMoves.get(random).startPos.y;
        secondX = possibleMoves.get(random).finalPos.x;
        secondY = possibleMoves.get(random).finalPos.y;
        movePiece(possibleMoves.get(random));
    }


    public void changeTurn(short player) {
        gameUI.printText("Player %d has ended their turn.\n", player, player);
        playerTurn = (byte) (3 - player);
    }

    public void movePiece(Move move) {
        firstX = move.startPos.x;
        firstY = move.startPos.y;
        secondX = move.finalPos.x;
        secondY = move.finalPos.y;
        tiles[secondX][secondY].color = tiles[firstX][firstY].color;
        tiles[firstX][firstY].color = 0;
        changeTurn(playerTurn);
        gameUI.updateGUI(tiles);
    }

    public void runGame() {
        Board board = new Board();
        agent = new Agent(board);
        GUI jk = new GUI();
        jk.createBoard();
        jk.createTextBoxArea();
        gameUI = jk;
        setUpGame();
        createLayout(jk);
    }

    private void createLayout(GUI jk) {
        jk.setTitle("Halma");
        jk.setVisible(true);
        jk.pack();
        jk.setSize(648, 800);
        jk.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // closes frame
        jk.setLocationRelativeTo(null);
        jk.setVisible(true); // makes HalmaBoard visible
        startGame();
    }

    public void setUpGame() {
        gameUI.setCampColors();
        gameUI.addMarbles();
        gameUI.addFrame();
    }
}
