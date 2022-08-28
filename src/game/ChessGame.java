package game;

import board.ChessBoard;
import pieces.AbstractPiece;
import pieces.PieceTypes;
import pieces.moves.IMove;
import pieces.moves.PawnPromotion;

import java.util.ArrayList;


public class ChessGame {

    private ChessBoard board;
    private GameResult gameResult = GameResult.IN_PROGRESS;
    private Player nextMove;
    private GameObserver gameObserver;

    public ChessGame(){
        generateBoard();
        this.nextMove = Player.WHITE;
    }

    public void addObserver(GameObserver observer){
        this.gameObserver = observer;
    }

    public ChessBoard getBoard() {
        return board;
    }

    public ArrayList<IMove> getPieceMove(AbstractPiece piece){
        if(piece.getPieceColor() == this.nextMove){
            return piece.getAllLegalMoves(this.board, true, false);
        } else return new ArrayList<>();
    }

    public void executeMove(IMove move){
        this.board = move.executeMove(false);
        nextRound();
        updateGameStatus();
        notifyObserver();
    }


    public void updateGameStatus(){
        if(this.board.isDraw()) this.gameResult = GameResult.DRAW;
        else if (this.board.isKingInCheckMate(Player.WHITE)) this.gameResult = GameResult.BLACK_WON;
        else if (this.board.isKingInCheckMate(Player.BLACK)) this.gameResult = GameResult.WHITE_WON;
    }

    public boolean isGameOver(){
        return gameResult != GameResult.IN_PROGRESS;
    }

    private void generateBoard(){
        this.board = ChessBoard.generateClassicGame();
    }

    public GameResult getGameResult(){
        return this.gameResult;
    }

    public void notifyObserver(){
        if(this.gameObserver != null){
            this.gameObserver.updateBoard();
        }
    }

    public void nextRound(){
        this.nextMove = this.nextMove == Player.BLACK ? Player.WHITE : Player.BLACK;
    }

    public void promotePawn(PieceTypes promoteTo){
        IMove promoteMove = new PawnPromotion(this.board.getPawnToPromote(), this.board, promoteTo);
        this.board = promoteMove.executeMove(false);
        updateGameStatus();
        notifyObserver();
    }

    public enum GameResult {
        IN_PROGRESS,
        DRAW,
        WHITE_WON,
        BLACK_WON
    }
}
