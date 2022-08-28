package pieces;

import board.ChessBoard;
import game.Player;
import pieces.moves.IMove;
import java.util.ArrayList;

public class Bishop extends AbstractPiece{

    public Bishop(int position, Player color){
        super(position, color);
        this.pieceTypes = PieceTypes.BISHOP;
    }

    @Override
    protected ArrayList<IMove> getCorrectMoves(ChessBoard chessBoard, boolean onlyAggressiveMoves) {
        return getRepetitiveMovements(chessBoard);
    }

    @Override
    protected boolean isMoveLegal(int previousPosition, int newPosition){
        if(newPosition < 0 || newPosition > 63) return false;

        int column = calculateColumn(previousPosition);
        int newColumn = calculateColumn(newPosition);

        return Math.abs(column - newColumn) == 1;
    }
}
