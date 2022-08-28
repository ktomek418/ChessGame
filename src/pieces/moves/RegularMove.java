package pieces.moves;

import board.ChessBoard;
import board.Square;
import pieces.*;
import game.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegularMove implements IMove {

    private final AbstractPiece pieceToMove;
    private final int newPiecePosition;
    private final ChessBoard chessBoard;

    public RegularMove(AbstractPiece piece, int newPosition, ChessBoard chessBoard){
        this.pieceToMove = piece;
        this.newPiecePosition = newPosition;
        this.chessBoard = chessBoard;
    }


    @Override
    public int getDestination() {
        return newPiecePosition;
    }


    @Override
    public ChessBoard executeMove(boolean computerMove) {
        Map<Integer, Square> boardSquares = new HashMap<>(this.chessBoard.getBoardSquares());
        ArrayList<AbstractPiece> whitePieces = new ArrayList<>(this.chessBoard.getWhitePieces());
        ArrayList<AbstractPiece> blackPieces = new ArrayList<>(this.chessBoard.getBlackPieces());
        King whiteKing = this.chessBoard.getWhiteKing();
        King blackKing = this.chessBoard.getBlackKing();
        Square firstOldSquare = this.chessBoard.getSquare(this.newPiecePosition);
        Square secondOldSquare = this.chessBoard.getSquare(this.pieceToMove.getPosition());

        boardSquares.remove(firstOldSquare.getPosition());
        boardSquares.remove(secondOldSquare.getPosition());

        boardSquares.put(secondOldSquare.getPosition(), MoveUtils.createSquareCopy(secondOldSquare));

        Pawn pawnToPromote = null;
        Pawn pawnAfterDoubleMove = null;
        AbstractPiece newPiece = MoveUtils.createPieceCopy(this.pieceToMove, this.newPiecePosition);

        if(newPiece.getPieceTypes() == PieceTypes.PAWN){
            if(Math.abs(this.pieceToMove.getPosition() - this.newPiecePosition) == 16){
                pawnAfterDoubleMove = (Pawn) newPiece;
            } else if(((Pawn) newPiece).isOnPromotionSquare()){
                if(computerMove) newPiece = new Queen(this.newPiecePosition, this.pieceToMove.getPieceColor());
                else pawnToPromote = (Pawn) newPiece;
            }
        }

        if(this.pieceToMove.getPieceColor() == Player.WHITE){
            whitePieces.remove(this.pieceToMove);
            whitePieces.add(newPiece);
            boardSquares.put(this.newPiecePosition, MoveUtils.createSquareCopy(firstOldSquare, newPiece));
            if(whiteKing == this.pieceToMove && newPiece instanceof King) whiteKing = (King) newPiece;
        } else {
            blackPieces.remove(this.pieceToMove);
            blackPieces.add(newPiece);
            boardSquares.put(this.newPiecePosition, MoveUtils.createSquareCopy(firstOldSquare, newPiece));
            if(blackKing == this.pieceToMove && newPiece instanceof King) blackKing = (King) newPiece;
        }
        return new ChessBoard(boardSquares, whitePieces, blackPieces, whiteKing,
                blackKing, pawnAfterDoubleMove, pawnToPromote);
    }
}
