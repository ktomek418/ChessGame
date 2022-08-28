package pieces.moves;

import board.ChessBoard;
import board.Square;
import game.Player;
import pieces.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EnPassantMove implements IMove{

    private final Pawn pawnToMove;
    private final Pawn attackedPawn;
    private final int newPawnPosition;
    private final ChessBoard chessBoard;

    public EnPassantMove(Pawn piece, Pawn attackedPiece, int newPosition, ChessBoard chessBoard){
        this.pawnToMove = piece;
        this.attackedPawn = attackedPiece;
        this.newPawnPosition = newPosition;
        this.chessBoard = chessBoard;
    }


    @Override
    public int getDestination() {
        return newPawnPosition;
    }


    @Override
    public ChessBoard executeMove(boolean compuerMove) {
        Map<Integer, Square> boardSquares = new HashMap<>(this.chessBoard.getBoardSquares());
        ArrayList<AbstractPiece> whitePieces = new ArrayList<>(this.chessBoard.getWhitePieces());
        ArrayList<AbstractPiece> blackPieces = new ArrayList<>(chessBoard.getBlackPieces());
        King whiteKing = this.chessBoard.getWhiteKing();
        King blackKing = this.chessBoard.getBlackKing();
        Square firstOldSquare = this.chessBoard.getSquare(this.newPawnPosition);
        Square secondOldSquare = this.chessBoard.getSquare(this.pawnToMove.getPosition());
        Square thirdOldSquare = this.chessBoard.getSquare(this.attackedPawn.getPosition());

        boardSquares.remove(firstOldSquare.getPosition());
        boardSquares.remove(secondOldSquare.getPosition());
        boardSquares.remove(thirdOldSquare.getPosition());

        boardSquares.put(secondOldSquare.getPosition(), MoveUtils.createSquareCopy(secondOldSquare));
        boardSquares.put(thirdOldSquare.getPosition(), MoveUtils.createSquareCopy(thirdOldSquare));

        if(this.pawnToMove.getPieceColor() == Player.WHITE){
            whitePieces.remove(this.pawnToMove);
            blackPieces.remove(this.attackedPawn);
            AbstractPiece pawnCopy = MoveUtils.createPieceCopy(this.pawnToMove, this.newPawnPosition);
            whitePieces.add(pawnCopy);
            boardSquares.put(this.newPawnPosition, MoveUtils.createSquareCopy(firstOldSquare, pawnCopy));
        } else {
            blackPieces.remove(this.pawnToMove);
            whitePieces.remove(this.attackedPawn);
            AbstractPiece pawnCopy = MoveUtils.createPieceCopy(this.pawnToMove, this.newPawnPosition);
            blackPieces.add(pawnCopy);
            boardSquares.put(this.newPawnPosition, MoveUtils.createSquareCopy(firstOldSquare, pawnCopy));
        }
        return new ChessBoard(boardSquares, whitePieces, blackPieces, whiteKing, blackKing,
                null, null);

    }
}
