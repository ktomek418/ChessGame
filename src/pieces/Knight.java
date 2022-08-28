package pieces;

import board.ChessBoard;
import pieces.moves.IMove;
import game.Player;
import java.util.ArrayList;

public class Knight extends AbstractPiece{

    public Knight(int position, Player color){
        super(position, color);
        this.pieceTypes = PieceTypes.KNIGHT;
    }

    @Override
    protected ArrayList<IMove> getCorrectMoves(ChessBoard chessBoard, boolean onlyAggressiveMoves) {
        return getSingleMovements(chessBoard);
    }

    @Override
    protected boolean isMoveLegal(int currentPosition, int newPosition){
        if(newPosition < 0 || newPosition > 63) return false;
        int column = calculateColumn(currentPosition);
        int newColumn = calculateColumn(newPosition);
        if(column == 0 && (newColumn == 7 || newColumn == 6)){
            return false;
        } else if(column == 7 && (newColumn == 0 || newColumn == 1)){
            return false;
        } else if(column == 1 && newColumn == 7){
            return false;
        }else return column != 6 || newColumn != 0;
    }
}
