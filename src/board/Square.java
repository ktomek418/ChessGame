package board;
import pieces.AbstractPiece;

public class Square {

    private final int position;
    private boolean occupied;
    private AbstractPiece piece;

    public Square(int position, AbstractPiece piece){
        this.position = position;
        this.occupied = piece != null;
        this.piece = piece;
    }

    public int getPosition() {
        return position;
    }

    public AbstractPiece getPiece() {
        return piece;
    }

    public boolean isOccupied(){
        return occupied;
    }

    public void addPiece(AbstractPiece piece){
        this.piece = piece;
        this.occupied = true;
    }

    @Override
    public String toString() {
        return this.piece == null ? "-": this.piece.toString();
    }
}
