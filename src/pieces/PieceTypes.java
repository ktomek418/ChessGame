package pieces;

public enum PieceTypes {
    PAWN(100, "p"),
    KNIGHT(300, "s"),
    BISHOP(300, "b"),
    ROOK(500, "r"),
    QUEEN(900, "q"),
    KING(1000000, "k");

    private final int pieceValue;
    private final String shortcut;

    PieceTypes(int value, String shortcut){
        this.pieceValue = value;
        this.shortcut = shortcut;
    }

    @Override
    public String toString() {
        return this.shortcut;
    }

    public int getPieceValue() {
        return pieceValue;
    }
}
