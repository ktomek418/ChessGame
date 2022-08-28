package pieces.moves;

import board.Square;
import pieces.*;

public abstract class MoveUtils {


    public static AbstractPiece createPieceCopy(AbstractPiece oldPiece, int newPosition){
        if(oldPiece.getPieceTypes() == PieceTypes.PAWN){
            return new Pawn(newPosition, oldPiece.getPieceColor(), false);
        }else if(oldPiece.getPieceTypes() == PieceTypes.KNIGHT){
            return  new Knight(newPosition, oldPiece.getPieceColor());
        }else if (oldPiece.getPieceTypes() == PieceTypes.BISHOP){
            return new Bishop(newPosition, oldPiece.getPieceColor());
        }else if(oldPiece.getPieceTypes() == PieceTypes.ROOK){
            return new Rook(newPosition, oldPiece.getPieceColor(), false);
        }else if(oldPiece.getPieceTypes() == PieceTypes.QUEEN){
            return new Queen(newPosition, oldPiece.getPieceColor());
        }else return new King(newPosition, oldPiece.getPieceColor(), false);
    }

    public static Square  createSquareCopy(Square squareToCopy){
        return new Square(squareToCopy.getPosition(), null);
    }
    public static Square  createSquareCopy(Square squareToCopy, AbstractPiece piece){
        return new Square(squareToCopy.getPosition(), piece);
    }
}
