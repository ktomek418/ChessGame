package pieces;

import board.ChessBoard;
import game.Player;
import pieces.moves.IMove;
import java.util.ArrayList;

public class Rook extends AbstractPiece{

    private boolean firstMove = true;

    public Rook(int position, Player color){
        super(position, color);
        this.pieceTypes = PieceTypes.ROOK;
    }

    @Override
    protected ArrayList<IMove> getCorrectMoves(ChessBoard chessBoard, boolean onlyAggressiveMoves) {
        return getRepetitiveMovements(chessBoard);
    }

    public Rook(int position, Player color, boolean firstMove){
        this(position, color);
        this.firstMove = firstMove;
    }

    public boolean isFirstMove() {
        return firstMove;
    }

    @Override
    protected boolean isMoveLegal(int previousPosition, int newPosition){
        if(newPosition < 0 || newPosition > 63) return false;

        int column = calculateColumn(previousPosition);
        int newColumn = calculateColumn(newPosition);

        return (Math.abs(previousPosition - newPosition) == 1 && Math.abs(column - newColumn) == 1)
                || Math.abs(previousPosition - newPosition) == 8;
    }
}
