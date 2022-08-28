package pieces;

import board.ChessBoard;
import board.Square;
import pieces.moves.AttackMove;
import pieces.moves.IMove;
import pieces.moves.RegularMove;
import game.Player;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class AbstractPiece {

    private final static int[] PAWN_MOVEMENT = {7, 9};
    private final static int[] BISHOP_MOVEMENT = {-7, 7, -9, 9};
    private final static int[] KNIGHT_MOVEMENT = {-6, 6, -10, 10, -15, 15, -17, 17};
    private final static int[] ROOK_MOVEMENT = {1, -1, 8, -8};
    private final static int[] QUEEN_MOVEMENT = {-7, 7, -9, 9, 1, -1, 8, -8};
    private final static int[] KING_MOVEMENT = {-1, 1, 8, -8, -7, 7, -9, 9};

    private final int position;
    private final Player pieceColor;
    protected PieceTypes pieceTypes;


    public AbstractPiece(int position, Player color){
        this.position = position;
        this.pieceColor = color;
    }

    public int getPosition() {
        return this.position;
    }

    public int getPieceValue(){
        return this.pieceTypes.getPieceValue();
    }

    public Player getPieceColor() {
        return this.pieceColor;
    }

    public Player getEnemyColor(){
        if(this.getPieceColor() == Player.BLACK) return Player.WHITE;
        return Player.BLACK;
    }

    public PieceTypes getPieceTypes() {
        return this.pieceTypes;
    }


    public ArrayList<IMove> getAllLegalMoves(ChessBoard chessBoard, boolean onlyMovesThatAreSafeForTheKing,
                                             boolean getOnlyAggressiveMoves){
        ArrayList<IMove> correctMoves = getCorrectMoves(chessBoard, getOnlyAggressiveMoves);
        if(onlyMovesThatAreSafeForTheKing){
            checkIfKingWillNotBeInCheck(correctMoves);
        }
        return correctMoves;
    }

    protected int[] getPieceMovement(){
        if(pieceTypes == PieceTypes.PAWN) return PAWN_MOVEMENT;
        else if(pieceTypes == PieceTypes.BISHOP) return BISHOP_MOVEMENT;
        else if(pieceTypes == PieceTypes.KNIGHT) return KNIGHT_MOVEMENT;
        else if(pieceTypes == PieceTypes.ROOK) return ROOK_MOVEMENT;
        else if(pieceTypes == PieceTypes.QUEEN) return QUEEN_MOVEMENT;
        else if(pieceTypes == PieceTypes.KING) return KING_MOVEMENT;
        else return null;
    }

    protected ArrayList<IMove> getRepetitiveMovements(ChessBoard chessBoard){
        ArrayList<IMove> legalMoves = new ArrayList<>();
        for(int move: getPieceMovement()) {
            int previousPosition = getPosition();
            int newPosition = previousPosition + move;
            while (isMoveLegal(previousPosition, newPosition)) {
                Square nextSquare = chessBoard.getSquare(newPosition);
                if (nextSquare.isOccupied()) {
                    if (nextSquare.getPiece().getPieceColor() != getPieceColor()) {
                        legalMoves.add(new AttackMove(this, nextSquare.getPiece(), newPosition, chessBoard));
                    }
                    break;
                } else {
                    legalMoves.add(new RegularMove(this, newPosition, chessBoard));
                    newPosition += move;
                    previousPosition  = newPosition - move;
                }
            }
        }
        return legalMoves;
    }

    protected ArrayList<IMove> getSingleMovements(ChessBoard chessBoard) {
        ArrayList<IMove> legalMoves = new ArrayList<>();
        for(int move: getPieceMovement()){
            int newPosition = this.getPosition() + move;
            if(isMoveLegal(this.getPosition(), newPosition)){
                Square square = chessBoard.getSquare(newPosition);
                if(!square.isOccupied()){
                    legalMoves.add(new RegularMove(this, newPosition, chessBoard));
                } else if(square.getPiece().getPieceColor() != getPieceColor()){
                    legalMoves.add(new AttackMove(this, square.getPiece(), newPosition, chessBoard));
                }
            }
        }
        return legalMoves;
    }

    protected abstract ArrayList<IMove> getCorrectMoves(ChessBoard chessBoard, boolean onlyAggressiveMoves);

    protected abstract boolean isMoveLegal(int currentPosition, int newPosition);

    private void checkIfKingWillNotBeInCheck(ArrayList<IMove> moves){
        Iterator<IMove> i = moves.iterator();
        while(i.hasNext()){
            ChessBoard chessBoardToCheck = i.next().executeMove(true);
            if(chessBoardToCheck.isKingInCheck(getPieceColor())) i.remove();
        }
    }


    public static int calculateColumn(int position){ return  position % ChessBoard.SQUARES_PER_LINE;}

    public static int calculateRow(int position){
        return  position / ChessBoard.SQUARES_PER_LINE;}

    @Override
    public String toString() {
        return pieceTypes.toString();
    }
}
