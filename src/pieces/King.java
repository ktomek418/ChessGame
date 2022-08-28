package pieces;

import board.ChessBoard;
import pieces.moves.Castling;
import pieces.moves.IMove;
import game.Player;
import java.util.ArrayList;

public class King extends AbstractPiece{

    boolean firstMove = true;


    public King(int position, Player color){
        super(position, color);
        this.pieceTypes = PieceTypes.KING;
    }

    public King(int position, Player color, boolean firstMove){
        this(position, color);
        this.firstMove = firstMove;
    }


    public boolean isFirstMove() {
        return this.firstMove;
    }

    private boolean onFirstRow(){
        return getPieceColor() == Player.WHITE ? calculateRow(getPosition()) == 0 : calculateRow(getPosition()) == 7;
    }


    @Override
    protected ArrayList<IMove> getCorrectMoves(ChessBoard chessBoard, boolean onlyAggressiveMoves) {
        ArrayList<IMove> legalMoves = getSingleMovements(chessBoard);

        if(!onlyAggressiveMoves && onFirstRow() && isFirstMove() && !chessBoard.isKingInCheck(getPieceColor())){
            IMove longCastling = getCastlingMove(CastlingTypes.LONG, chessBoard);
            IMove shortCastling = getCastlingMove(CastlingTypes.SHORT, chessBoard);
            if(longCastling != null) legalMoves.add(longCastling);
            if(shortCastling != null) legalMoves.add(shortCastling);
        }
        return legalMoves;
    }

    @Override
    public boolean isMoveLegal(int currentPosition, int newPosition){
        if(newPosition < 0 || newPosition > 63) return false;
        int column = calculateColumn(currentPosition);
        int newColumn = calculateColumn(newPosition);
        return !((column == 0 && newColumn == 7) || (column == 7 && newColumn == 0));
    }

    private IMove getCastlingMove(CastlingTypes castling, ChessBoard chessBoard){
        int rookPosition;
        if(getPieceColor() == Player.WHITE){
            rookPosition = castling == CastlingTypes.SHORT ? 7 : 0;
        } else {
            rookPosition = castling == CastlingTypes.SHORT ? 63 : 56;
        }
        int moveDirection = getPosition() > rookPosition ? -1 : 1;
        if(isCastlePossible(rookPosition, chessBoard)){
            for(int i = 1; i<=Math.abs(getPosition() - rookPosition); i += 1){
                if(i <3){
                    if(chessBoard.isSquareAttackByEnemy(getPosition() + i*moveDirection, getEnemyColor(),
                            true)
                            || chessBoard.getSquare(getPosition() + i * moveDirection).isOccupied()) break;
                }
                if(rookPosition == getPosition() + i * moveDirection){
                    return new Castling(this, chessBoard.getSquare(rookPosition).getPiece(),
                            getPosition() + 2*moveDirection,getPosition() + moveDirection, chessBoard);
                }
            }
        }
        return null;
    }

    private boolean isCastlePossible(int rookPosition, ChessBoard chessBoard){
        return chessBoard.getSquare(rookPosition).isOccupied() &&
                chessBoard.getSquare(rookPosition).getPiece().getPieceColor() == getPieceColor() &&
                chessBoard.getSquare(rookPosition).getPiece().getPieceTypes() == PieceTypes.ROOK &&
                ((Rook) chessBoard.getSquare(rookPosition).getPiece()).isFirstMove();
    }


    private enum CastlingTypes {
        LONG,SHORT
    }
}
