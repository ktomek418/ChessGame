package board;

import pieces.*;
import pieces.moves.IMove;
import game.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChessBoard {

    public static int SQUARES_PER_LINE = 8;
    public static int SQUARES = 64;
    public static int FIRST_SQUARE = 0;

    private final Map<Integer, Square> boardSquares;
    private final ArrayList<AbstractPiece> whitePieces;
    private final ArrayList<AbstractPiece> blackPieces;
    private King whiteKing;
    private King blackKing;
    private Pawn pawnAfterDoubleMove;
    private Pawn pawnToPromote;

    public ChessBoard(){
        this.boardSquares = new HashMap<>();
        this.whitePieces = new ArrayList<>();
        this.blackPieces = new ArrayList<>();
        for(int position=FIRST_SQUARE; position < SQUARES; position++){
            boardSquares.put(position, (new Square(position, null)));
        }
    }

    public ChessBoard(King whiteKing, King blackKing){
        this();
        this.whiteKing = whiteKing;
        this.blackKing = blackKing;
    }

    public ChessBoard(Map<Integer, Square> boardSquares, ArrayList<AbstractPiece> whitePieces,
                      ArrayList<AbstractPiece> blackPieces, King whiteKing, King blackKing,
                      Pawn pawnAfterDoubleMove, Pawn pawnToPromote){
        this.boardSquares = boardSquares;
        this.whitePieces = whitePieces;
        this.blackPieces = blackPieces;
        this.whiteKing = whiteKing;
        this.blackKing = blackKing;
        this.pawnAfterDoubleMove = pawnAfterDoubleMove;
        this.pawnToPromote = pawnToPromote;
    }


    public Square getSquare(int squarePosition) {
        return boardSquares.get(squarePosition);
    }

    public Map<Integer, Square> getBoardSquares() {
        return boardSquares;
    }

    public ArrayList<AbstractPiece> getWhitePieces() {
        return whitePieces;
    }

    public ArrayList<AbstractPiece> getBlackPieces() {
        return blackPieces;
    }

    public ArrayList<AbstractPiece> getPlayerPieces(Player playerColor){
        if(playerColor == Player.BLACK) return this.blackPieces; else return this.whitePieces;
    }

    public Pawn getPawnToPromote() {
        return this.pawnToPromote;
    }

    public Pawn getPawnAfterDoubleMove() {
        return this.pawnAfterDoubleMove;
    }

    public ArrayList<IMove> getAllPlayerMoves(Player playerColor, boolean onlyMovesThatAreSafeForTheKing,
                                              boolean onlyAggressiveMoves){
        ArrayList<IMove> moves = new ArrayList<>();
        for(AbstractPiece piece: getPlayerPieces(playerColor)){
            moves.addAll(piece.getAllLegalMoves(this, onlyMovesThatAreSafeForTheKing, onlyAggressiveMoves));
        }
        return moves;
    }


    public King getWhiteKing() {
        return whiteKing;
    }

    public King getBlackKing() {
        return blackKing;
    }



    public void addPiece(int position, AbstractPiece piece){
        this.boardSquares.get(position).addPiece(piece);
        if(piece.getPieceColor() == Player.BLACK) this.blackPieces.add(piece);
        else this.whitePieces.add(piece);
    }


    public static ChessBoard generateClassicGame(){
        King whiteKing = new King(4, Player.WHITE);
        King blackKing = new King(60, Player.BLACK);
        ChessBoard chessBoard = new ChessBoard(whiteKing, blackKing);
        chessBoard.addPiece(0, new Rook(0, Player.WHITE));
        chessBoard.addPiece(1, new Knight(1, Player.WHITE));
        chessBoard.addPiece(2, new Bishop(2, Player.WHITE));
        chessBoard.addPiece(3, new Queen(3, Player.WHITE));
        chessBoard.addPiece(4, whiteKing);
        chessBoard.addPiece(5, new Bishop(5, Player.WHITE));
        chessBoard.addPiece(6, new Knight(6, Player.WHITE));
        chessBoard.addPiece(7, new Rook(7, Player.WHITE));
        chessBoard.addPiece(8, new Pawn(8, Player.WHITE));
        chessBoard.addPiece(9, new Pawn(9, Player.WHITE));
        chessBoard.addPiece(10, new Pawn(10, Player.WHITE));
        chessBoard.addPiece(11, new Pawn(11, Player.WHITE));
        chessBoard.addPiece(12, new Pawn(12, Player.WHITE));
        chessBoard.addPiece(13, new Pawn(13, Player.WHITE));
        chessBoard.addPiece(14, new Pawn(14, Player.WHITE));
        chessBoard.addPiece(15, new Pawn(15, Player.WHITE));

        chessBoard.addPiece(56, new Rook(56, Player.BLACK));
        chessBoard.addPiece(57, new Knight(57, Player.BLACK));
        chessBoard.addPiece(58, new Bishop(58, Player.BLACK));
        chessBoard.addPiece(59, new Queen(59, Player.BLACK));
        chessBoard.addPiece(60, blackKing);
        chessBoard.addPiece(61, new Bishop(61, Player.BLACK));
        chessBoard.addPiece(62, new Knight(62, Player.BLACK));
        chessBoard.addPiece(63, new Rook(63, Player.BLACK));
        chessBoard.addPiece(55, new Pawn(55, Player.BLACK));
        chessBoard.addPiece(54, new Pawn(54, Player.BLACK));
        chessBoard.addPiece(53, new Pawn(53, Player.BLACK));
        chessBoard.addPiece(52, new Pawn(52, Player.BLACK));
        chessBoard.addPiece(51, new Pawn(51, Player.BLACK));
        chessBoard.addPiece(50, new Pawn(50, Player.BLACK));
        chessBoard.addPiece(49, new Pawn(49, Player.BLACK));
        chessBoard.addPiece(48, new Pawn(48, Player.BLACK));
        return chessBoard;
    }


    public boolean isSquareAttackByEnemy(int position, Player enemy, boolean onlyAggressiveMoves){
        ArrayList<IMove> moves;
        if(enemy == Player.BLACK){
            moves = getAllPlayerMoves(Player.BLACK, false, onlyAggressiveMoves);
        } else{
            moves = getAllPlayerMoves(Player.WHITE, false, onlyAggressiveMoves);
        }
        for(IMove move: moves){
            if(move.getDestination() == position) return true;
        }
        return false;
    }

    public boolean isKingInCheck(Player kingColor){
        if(kingColor == Player.WHITE){
            return isSquareAttackByEnemy(this.whiteKing.getPosition(), Player.BLACK, true);
        }
        return isSquareAttackByEnemy(this.blackKing.getPosition(), Player.WHITE, true);
    }

    public boolean isKingInCheckMate(Player kingColor){
        return isKingInCheck(kingColor) && getAllPlayerMoves(kingColor, true,
                true).size() == 0;
    }

    public boolean isPlayerInStalemate(Player playerColor){
        return !isKingInCheck(playerColor) && getAllPlayerMoves(playerColor, true,
                true).size() == 0;
    }

    public int getValueOfPlayerPieces(Player player){
        int sum = 0;
        if(player == Player.WHITE){
            for(AbstractPiece piece: whitePieces){
                sum += piece.getPieceValue();
            }

        } else{
            for(AbstractPiece piece: blackPieces){
                sum += piece.getPieceValue();
            }
        }
        return sum;
    }


    public boolean isDraw(){
        return !enoughPieceToPlay() || isPlayerInStalemate(Player.BLACK) || isPlayerInStalemate(Player.WHITE);
    }

    public boolean enoughPieceToPlay(){
        int whitePiecesValue = getValueOfPlayerPieces(Player.WHITE);
        int blackPiecesValue = getValueOfPlayerPieces(Player.BLACK);
        return (whitePiecesValue != 1000000 && (whitePiecesValue != 1000300 || whitePieces.size() >2)) ||
                (blackPiecesValue != 1000000 && (blackPiecesValue != 1000300 || blackPieces.size() > 2));
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(int i = 56; i>= FIRST_SQUARE; i -= 8){
            for(int j = 0; j< SQUARES_PER_LINE; j ++){
                builder.append(boardSquares.get(i + j).toString());
                builder.append("  ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
