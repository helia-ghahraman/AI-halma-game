
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.abs;

public class Agent {
    private final Board board;
    private byte playerTurn;
    private byte MAX_DEPTH = 3;
    public Agent(Board board) {
        this.board = board;
    }

    public Move doMinMax(Tile[][] tiles, byte playerTurn) {
        this.playerTurn = playerTurn;
        Pair temp = ab_max(tiles, playerTurn, (byte) (0),Integer.MAX_VALUE);
        return temp.move;
    }

    private Pair ab_max(Tile[][] currentBoard, byte currentColor, byte depth ,int parentValue) {
        int maxValue = Integer.MIN_VALUE, value;
        Move bestMove = null;
        boolean reachedCutOff = (depth + 1) >= MAX_DEPTH;
        List<Move> possibleMoves = createPossibleMoves(currentBoard, currentColor);

        if (checkTerminal(currentBoard)) {
            return new Pair(null, evaluate(currentBoard,currentBoard.clone(), currentColor));
        }

        for (Move possibleMove : possibleMoves) {
            if (reachedCutOff){
                value= evaluate(board.doMove(possibleMove, currentBoard),currentBoard.clone(), currentColor);
            }else{
                value= ab_min(board.doMove(possibleMove, currentBoard), (byte) (currentColor == 0 ? 1 : 0), (byte) (depth + 1),maxValue).value;
            }

            if (value > maxValue) {
                maxValue = value;
                bestMove = possibleMove;
            }
            if(value >= parentValue) return new Pair(bestMove, maxValue);
        }
        return new Pair(bestMove, maxValue);
    }

    private Pair ab_min(Tile[][] currentBoard, byte currentColor, byte depth , int parentValue) {
        int minValue = Integer.MAX_VALUE, value;
        Move bestMove = null;
        boolean reachedCutOff = (depth + 1) >= MAX_DEPTH;
        List<Move> possibleMoves = createPossibleMoves(currentBoard, currentColor);

        if (checkTerminal(currentBoard)) {
            return new Pair(null, evaluate(currentBoard,currentBoard.clone(), currentColor));
        }

        for (Move possibleMove : possibleMoves) {
            if (reachedCutOff){
                value= evaluate(board.doMove(possibleMove, currentBoard),currentBoard.clone(),currentColor);
            }else{
                value= ab_max(board.doMove(possibleMove, currentBoard), (byte) (currentColor == 0 ? 1 : 0), (byte) (depth + 1),minValue).value;
            }

            if (value < minValue) {
                minValue = value;
                bestMove = possibleMove;
            }
            if(value <= parentValue) return new Pair(bestMove, minValue);
        }
        return new Pair(bestMove, minValue);
    }


    private int evaluate(Tile[][] currentBoard,Tile[][] parentBoard ,byte currentColor) {
        if (win(currentBoard)) return Integer.MAX_VALUE;
        short score = 0;
        int difference=capturedCells(currentBoard, currentColor) -capturedCells(parentBoard, currentColor);
        if (difference>0)score+= (100*difference);

        List<Tile> emptyZones =empty(currentBoard);
        int distance,x;
        for (byte i = 0; i < currentBoard.length; i++) {
            for (byte j = 0; j < currentBoard.length; j++) {
                if (currentBoard[i][j].color == playerTurn) {
                    score += (7 - i);
                    score += (7 - j);
                    distance=100;
                    for (Tile e : emptyZones) {
                        if (e.zone == (3 - playerTurn)) {
                            x = abs(e.x - i);
                            x += abs(e.y - j);
                            if (x < distance) distance = x;
                        }
                    }
                    score += distance;
                } else if (currentBoard[i][j].color == (3 - playerTurn)) {
                    score -= i;
                    score -= j;
                    distance=100;
                    if(currentBoard[i][j].zone!=playerTurn) {
                        for (Tile e : emptyZones) {
                            if (e.zone == (playerTurn)) {
                                x = abs(e.x - i);
                                x += abs(e.y - j);
                                if (x < distance) distance = x;
                            }
                        }
                        score -= distance;
                    }

                }
            }
        }
        return score;
    }


    private List<Tile> empty(Tile[][] currentBoard) {

        List<Tile> emptyZones = new LinkedList<>();

        for (byte i = 0; i < currentBoard.length; i++) {
            for (byte j = 0; j < currentBoard.length; j++) {
                if (currentBoard[i][j].zone == 1 && currentBoard[i][j].color == 0) {
                    emptyZones.add(currentBoard[i][j]);
                } else if (currentBoard[i][j].zone == 2 && currentBoard[i][j].color == 0) {
                    emptyZones.add(currentBoard[i][j]);
                }
            }
        }
        return emptyZones;
    }


    private int capturedCells(Tile[][] currentBoard, byte currentColor) {
        int startX=0,startY=0,finishX=3,finishY=3;
        if (currentColor!=1){
            startY=startX=4;
            finishX=finishY=7;
        }
        int capturedCells = 0, enemyColor=2 ;
        if (currentColor != 1)enemyColor=1 ;
        for (int x = startX; x <= finishX; x++) {
            for (int y = startY; y <= finishY; y++) {
                if (currentBoard[y][x].zone == enemyColor && currentBoard[y][x].color == currentColor)
                    capturedCells++;
                else if (currentBoard[y][x].zone == enemyColor && currentBoard[y][x].color == currentColor)
                    capturedCells++;
            }
        }
        return capturedCells;
    }


    public boolean win(Tile[][] currentTiles) {
        byte redCount=0, blueCount=0;
        for (byte x = 0; x < 8; x++) {
            for (byte y = 0; y < 8; y++) {
                if (currentTiles[x][y].zone == 1 && currentTiles[x][y].color == 2) {
                    if ((++redCount >= 10 && playerTurn == 2) || (++blueCount >= 10 && playerTurn == 1)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public List<Move> createPossibleMoves(Tile[][] newBoard, int currentColor) {
        List<Move> possibleMoves = new LinkedList<>();
        for (byte i = 0; i < 8; i++)
            for (byte j = 0; j < 8; j++)
                if (newBoard[i][j].color == currentColor) {
                    List<Tile> legalTiles = new LinkedList<>();
                    board.findPossibleMoves(newBoard, newBoard[i][j], legalTiles, newBoard[i][j], true);
                    for (Tile tile : legalTiles)
                        possibleMoves.add(new Move(newBoard[i][j], tile));
                }
        return possibleMoves;
    }

    public boolean checkTerminal(Tile[][] currentTiles) {
        byte redCount, blueCount;
        redCount = blueCount = 0;

        for (byte x = 0; x < 8; x++)
            for (byte y = 0; y < 8; y++)
                if (currentTiles[x][y].zone == 1 && currentTiles[x][y].color == 2)
                    if (++redCount >= 10)
                        return true;
                    else if (currentTiles[x][y].zone == 2 && currentTiles[x][y].color == 1)
                        if (++blueCount >= 10)
                            return true;
        return false;
    }

}
