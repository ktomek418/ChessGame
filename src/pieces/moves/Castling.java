package pieces.moves;

import board.ChessBoard;
import board.Square;
import game.Player;
import pieces.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Castling implements IMove{

    private final AbstractPiece kingToMove;
    private final AbstractPiece rookToMove;
    private final int newKingPosition;
    private final int newRookPosition;
    private final ChessBoard chessBoard;


    public Castling(AbstractPiece king, AbstractPiece rook, int newKingPosition, int newRookPosition, ChessBoard chessBoard){
        this.kingToMove = king;
        this.rookToMove = rook;
        this.newKingPosition = newKingPosition;
        this.newRookPosition = newRookPosition;
        this.chessBoard = chessBoard;
    }

    @Override
    public int getDestination() {
        return this.newKingPosition;
    }


    @Override
    public ChessBoard executeMove(boolean computerMove) {

        Map<Integer, Square> boardSquares = new HashMap<>(this.chessBoard.getBoardSquares());
        ArrayList<AbstractPiece> whitePieces = new ArrayList<>(this.chessBoard.getWhitePieces());
        ArrayList<AbstractPiece> blackPieces = new ArrayList<>(this.chessBoard.getBlackPieces());
        King whiteKing = this.chessBoard.getWhiteKing();
        King blackKing = this.chessBoard.getBlackKing();

        Square firstOldSquare = this.chessBoard.getSquare(this.kingToMove.getPosition());
        Square secondOldSquare = this.chessBoard.getSquare(this.rookToMove.getPosition());

        boardSquares.remove(firstOldSquare.getPosition());
        boardSquares.remove(secondOldSquare.getPosition());
        boardSquares.put(firstOldSquare.getPosition(), MoveUtils.createSquareCopy(firstOldSquare));
        boardSquares.put(secondOldSquare.getPosition(), MoveUtils.createSquareCopy(secondOldSquare));

        Square firstNewSquare = this.chessBoard.getSquare(this.newKingPosition);
        Square secondNewSquare = this.chessBoard.getSquare(this.newRookPosition);


        if(this.kingToMove.getPieceColor() == Player.WHITE){
            whitePieces.remove(this.rookToMove);
            whitePieces.remove(this.kingToMove);
            AbstractPiece newKing = MoveUtils.createPieceCopy(this.kingToMove, this.newKingPosition);
            AbstractPiece newRook = MoveUtils.createPieceCopy(this.rookToMove, this.newRookPosition);
            whitePieces.add(newKing);
            whitePieces.add(newRook);
            whiteKing = (King) newKing;
            boardSquares.put(firstNewSquare.getPosition(), MoveUtils.createSquareCopy(firstNewSquare, newKing));
            boardSquares.put(secondNewSquare.getPosition(), MoveUtils.createSquareCopy(secondNewSquare, newRook));

        } else {
            blackPieces.remove(this.kingToMove);
            blackPieces.remove(this.rookToMove);
            AbstractPiece newRook = MoveUtils.createPieceCopy(this.rookToMove, this.newRookPosition);
            AbstractPiece newKing = MoveUtils.createPieceCopy(this.kingToMove, this.newKingPosition);
            blackPieces.add(newRook);
            blackPieces.add(newKing);
            blackKing = (King) newKing;
            boardSquares.put(firstNewSquare.getPosition(), MoveUtils.createSquareCopy(firstNewSquare, newKing));
            boardSquares.put(secondNewSquare.getPosition(), MoveUtils.createSquareCopy(secondNewSquare, newRook));
        }
        return new ChessBoard(boardSquares, whitePieces, blackPieces, whiteKing, blackKing,
                null, null);

    }
}
