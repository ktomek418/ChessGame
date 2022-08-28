package pieces;

import board.ChessBoard;
import board.Square;
import pieces.moves.AttackMove;
import pieces.moves.EnPassantMove;
import pieces.moves.IMove;
import pieces.moves.RegularMove;
import game.Player;
import java.util.ArrayList;

public class Pawn extends AbstractPiece{

    private boolean firstMove = true;
    private int direction = 1;
    private boolean onPromotionSquare;

    public Pawn(int position, Player color){
        super(position, color);
        this.pieceTypes = PieceTypes.PAWN;
        if(color == Player.BLACK) direction *= -1;
    }

    public Pawn(int position, Player color, boolean firstMove){
        this(position, color);
        if(color == Player.WHITE && calculateRow(position) == 7) onPromotionSquare = true;
        else this.onPromotionSquare = color == Player.BLACK && calculateRow(position) == 0;
        this.firstMove = firstMove;
    }



    public boolean isOnPromotionSquare() {
        return onPromotionSquare;
    }

    @Override
    protected ArrayList<IMove> getCorrectMoves(ChessBoard chessBoard, boolean onlyAggressiveMoves){
        if(onPromotionSquare) return new ArrayList<>();

        ArrayList<IMove> legalMoves = new ArrayList<>();
        addPushPawnMoves(chessBoard, legalMoves);

        for(int move: getPieceMovement()){
            int newPosition = getPosition() + move * this.direction;
            if(isMoveLegal(getPosition(), newPosition)) {
                Square squareToAttack = chessBoard.getSquare(newPosition);
                if(isAttackMovePossible(squareToAttack)){
                    legalMoves.add(new AttackMove(this, squareToAttack.getPiece(), newPosition, chessBoard));
                } else if(chessBoard.getPawnAfterDoubleMove() != null){
                    IMove enPassantMove = checkEnPassantMove(getPosition(), newPosition, chessBoard);
                    if(enPassantMove != null){
                        legalMoves.add(enPassantMove);
                    }
                }
            }
        }
        return legalMoves;
    }

    @Override
    protected boolean isMoveLegal(int currentPosition, int newPosition) {
        if(newPosition < 0 || newPosition > 63 ) return false;
        int column = AbstractPiece.calculateColumn(currentPosition);
        int newColumn = AbstractPiece.calculateColumn(newPosition);
        if(column == 0 && newColumn == 7) return false;
        else return column != 7 || newColumn != 0;
    }

    private boolean isAttackMovePossible(Square squareToAttack){
        return squareToAttack.isOccupied() && squareToAttack.getPiece().getPieceColor() != this.getPieceColor();
    }

    private void addPushPawnMoves(ChessBoard chessBoard, ArrayList<IMove> legalMoves){
        IMove forwardMovement = checkForwardMove(getPosition(), chessBoard);
        if(forwardMovement != null){
            legalMoves.add(forwardMovement);
            if(firstMove){
                IMove doubleMove = checkForwardMove(getPosition() +
                        ChessBoard.SQUARES_PER_LINE * this.direction, chessBoard);
                if(doubleMove != null) legalMoves.add(doubleMove);
            }
        }

    }
    private IMove checkForwardMove(int currentPosition, ChessBoard chessBoard){
        int newPosition = currentPosition + ChessBoard.SQUARES_PER_LINE* direction;
        Square squareToCheck = chessBoard.getSquare(newPosition);
        if(!squareToCheck.isOccupied()){
            return new RegularMove(this, newPosition, chessBoard);
        }
        else return null;
    }
    private IMove checkEnPassantMove(int currentPosition,int newPosition, ChessBoard chessBoard){

        Pawn pawnAfterDoubleMove = chessBoard.getPawnAfterDoubleMove();
        if(pawnAfterDoubleMove.getPieceColor() != getPieceColor() &&
                Math.abs(pawnAfterDoubleMove.getPosition() - currentPosition) == 1
                && Math.abs(pawnAfterDoubleMove.getPosition() - newPosition) == 8)
            {
            return new EnPassantMove(this, pawnAfterDoubleMove, newPosition, chessBoard);
        }
        return null;
    }
}
