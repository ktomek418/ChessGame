package pieces.moves;

import board.ChessBoard;
import board.Square;
import game.Player;
import pieces.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AttackMove implements IMove{

    private final AbstractPiece pieceToMove;
    private final AbstractPiece attackedPiece;
    private final int newPosition;
    private final ChessBoard chessBoard;

    public AttackMove(AbstractPiece piece, AbstractPiece attackedPiece, int newPosition, ChessBoard chessBoard){
        this.pieceToMove = piece;
        this.attackedPiece = attackedPiece;
        this.newPosition = newPosition;
        this.chessBoard = chessBoard;
    }


    @Override
    public int getDestination() {
        return newPosition;
    }


    @Override
    public ChessBoard executeMove(boolean computerMove) {

        Map<Integer, Square> boardSquares = new HashMap<>(this.chessBoard.getBoardSquares());
        ArrayList<AbstractPiece> whitePieces = new ArrayList<>(this.chessBoard.getWhitePieces());
        ArrayList<AbstractPiece> blackPieces = new ArrayList<>(this.chessBoard.getBlackPieces());
        King whiteKing = this.chessBoard.getWhiteKing();
        King blackKing = this.chessBoard.getBlackKing();
        Square firstOldSquare = this.chessBoard.getSquare(this.newPosition);
        Square secondOldSquare = this.chessBoard.getSquare(this.pieceToMove.getPosition());

        boardSquares.remove(firstOldSquare.getPosition());
        boardSquares.remove(secondOldSquare.getPosition());

        boardSquares.put(secondOldSquare.getPosition(), new Square(secondOldSquare.getPosition(), null));
        boardSquares.put(secondOldSquare.getPosition(), MoveUtils.createSquareCopy(secondOldSquare));

        Pawn pawnToPromote = null;
        AbstractPiece newPiece = MoveUtils.createPieceCopy(this.pieceToMove, this.newPosition);

        if(newPiece.getPieceTypes() == PieceTypes.PAWN){
            if(((Pawn) newPiece).isOnPromotionSquare()){
                if(computerMove) newPiece = new Queen(this.newPosition, this.pieceToMove.getPieceColor());
                else pawnToPromote = (Pawn) newPiece;
            }
        }

        if(this.pieceToMove.getPieceColor() == Player.WHITE){
            blackPieces.remove(this.attackedPiece);
            whitePieces.remove(this.pieceToMove);
            whitePieces.add(newPiece);
            boardSquares.put(this.newPosition, MoveUtils.createSquareCopy(firstOldSquare, newPiece));
            if(whiteKing == this.pieceToMove && newPiece instanceof King){
                whiteKing = (King) newPiece;
            }
        } else {
            blackPieces.remove(this.pieceToMove);
            whitePieces.remove(this.attackedPiece);
            blackPieces.add(newPiece);
            boardSquares.put(this.newPosition, MoveUtils.createSquareCopy(firstOldSquare, newPiece));
            if(blackKing == this.pieceToMove && newPiece instanceof King){
                blackKing = (King) newPiece;
            }
        }
        return new ChessBoard(boardSquares, whitePieces, blackPieces, whiteKing, blackKing,
                null, pawnToPromote);

    }
}
