package pieces.moves;

import board.ChessBoard;
import board.Square;
import game.Player;
import pieces.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PawnPromotion implements IMove{

    private final AbstractPiece pawnToPromote;
    private final ChessBoard chessBoard;
    private final PieceTypes promoteTo;

    public PawnPromotion(Pawn pawnToPromote, ChessBoard chessBoard, PieceTypes promoteTo){
        this.pawnToPromote = pawnToPromote;
        this.chessBoard = chessBoard;
        this.promoteTo = promoteTo;
    }

    @Override
    public ChessBoard executeMove(boolean computerMove) {
        Map<Integer, Square> boardSquares = new HashMap<>(this.chessBoard.getBoardSquares());
        ArrayList<AbstractPiece> whitePieces = new ArrayList<>(this.chessBoard.getWhitePieces());
        ArrayList<AbstractPiece> blackPieces = new ArrayList<>(this.chessBoard.getBlackPieces());
        King whiteKing = this.chessBoard.getWhiteKing();
        King blackKing = this.chessBoard.getBlackKing();

        Square oldSquare = this.chessBoard.getSquare(this.pawnToPromote.getPosition());
        boardSquares.remove(oldSquare.getPosition());

        AbstractPiece newPiece = promotePawn();
        boardSquares.put(oldSquare.getPosition(), new Square(oldSquare.getPosition(), newPiece));

        if(this.pawnToPromote.getPieceColor() == Player.WHITE){
            whitePieces.remove(this.pawnToPromote);
            whitePieces.add(newPiece);
        }else{
            blackPieces.remove(this.pawnToPromote);
            blackPieces.add(newPiece);
        }
        return new ChessBoard(boardSquares, whitePieces, blackPieces, whiteKing, blackKing,
                null, null);
    }

    @Override
    public int getDestination() {
        return pawnToPromote.getPosition();
    }

    private AbstractPiece promotePawn(){
        if(this.promoteTo == PieceTypes.KNIGHT) return new Knight(this.pawnToPromote.getPosition(),
                this.pawnToPromote.getPieceColor());
        else if(this.promoteTo == PieceTypes.BISHOP) return new Bishop(this.pawnToPromote.getPosition(),
                this.pawnToPromote.getPieceColor());
        else if(this.promoteTo == PieceTypes.ROOK) return new Rook(this.pawnToPromote.getPosition(),
                this.pawnToPromote.getPieceColor());
        else return new Queen(this.pawnToPromote.getPosition(),
                    this.pawnToPromote.getPieceColor());
    }
}
