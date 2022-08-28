package GUI;

import board.ChessBoard;
import board.Square;
import game.ChessGame;
import game.GameObserver;
import pieces.PieceTypes;
import pieces.moves.IMove;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChessBoardWindow extends JPanel implements GameObserver {


    private final Color WHITE_SQUARE = new Color(249, 228,212);
    private final Color BLACK_SQUARE = new Color(187, 135, 96);
    private ChessGame game;
    private ChessBoard gameBoard;

    private Square selectedSquare;
    private final Map<Integer, SquareWindow> squares = new HashMap<>();
    private ArrayList<IMove> legalMoves;
    private boolean boardReverse = false;

    public ChessBoardWindow(){
        this.setLayout(new GridLayout(8,8));
        this.setPreferredSize(new Dimension(SquareWindow.SQUARE_SIZE * 9, SquareWindow.SQUARE_SIZE * 9));
        newGame();
    }

    private void newGame(){
        this.game = new ChessGame();
        this.gameBoard = this.game.getBoard();
        this.game.addObserver(this);
        this.createChessBoard();
    }


    private void createChessBoard(){
        if(boardReverse){
            for(int i = 0; i<=56 ; i += 8){
                Color squareColor = (i/8) %2 == 0 ? WHITE_SQUARE : BLACK_SQUARE;
                for(int j = 0; j< 8; j ++){
                    squareColor = squareColor == WHITE_SQUARE ? BLACK_SQUARE : WHITE_SQUARE;
                    this.addSquare(new SquareWindow(i+j, this, squareColor));
                }
            }
        }else{
            for(int i = 56; i>= 0; i -= 8){
                Color squareColor = (i/8) %2 == 0 ? WHITE_SQUARE : BLACK_SQUARE;
                for(int j = 0; j< 8; j ++){
                    squareColor = squareColor == WHITE_SQUARE ? BLACK_SQUARE : WHITE_SQUARE;
                    this.addSquare(new SquareWindow(i+j, this, squareColor));
                }
            }
        }
    }

    private void addSquare(SquareWindow square){
        this.add(square);
        this.squares.put(square.getId(), square);
    }

    public void reverseBoard(){
        this.boardReverse = !this.boardReverse;
}

    public Square getSelectedSquare() {
        return this.selectedSquare;
    }

    public ChessBoard getBoard(){
        return this.gameBoard;
    }

    private IMove getLegalMovesForPieceInSelectedSquare(Square square){
        for(IMove move: legalMoves) if(move.getDestination() == square.getPosition()) return move;
        return null;
    }

    public boolean isBoardReverse() {
        return boardReverse;
    }

    public void firstClickAction(Square firstSelectedSquare) {
        if(this.selectedSquare != null){
            this.selectedSquare = null;
            this.highlightLegalMoves(false);
        }
        if(firstSelectedSquare.isOccupied()){
            this.selectedSquare = firstSelectedSquare;
            this.legalMoves = this.game.getPieceMove(this.selectedSquare.getPiece());
            this.highlightLegalMoves(true);
        }
    }

    public void secondClickAction(Square secondSelectedSquare) {
        IMove move = this.getLegalMovesForPieceInSelectedSquare(secondSelectedSquare);
        if(move == null){
            this.firstClickAction(secondSelectedSquare);
        } else{
            this.highlightLegalMoves(false);
            this.legalMoves = null;
            this.selectedSquare = null;
            this.game.executeMove(move);
        }
    }

    private void highlightLegalMoves(boolean on){
        for(IMove move: legalMoves){
            this.squares.get(move.getDestination()).highlightSquare(on);
        }
        repaint();
        revalidate();
    }

    private void endGame(){
        ChessGame.GameResult result = game.getGameResult();
        String message;
        if(result == ChessGame.GameResult.DRAW) message = "draw";
        else if(result == ChessGame.GameResult.BLACK_WON) message = "black won";
        else message = "white won";
        JOptionPane.showMessageDialog(this,message,"Game over", JOptionPane.INFORMATION_MESSAGE);
        removeAll();
        newGame();
        repaint();
        revalidate();

    }

    @Override
    public void updateBoard() {
        this.gameBoard = this.game.getBoard();
        this.squares.forEach((x, y) -> y.rebuild() );
        repaint();
        revalidate();
        if(this.gameBoard.getPawnToPromote() != null) this.game.promotePawn(getPieceTypeFromPlayer());
        if(this.game.isGameOver()) endGame();
    }

    private PieceTypes getPieceTypeFromPlayer() {
        String[] answers = {"Queen", "Rook", "Bishop", "Knight"};
        int answer = JOptionPane.showOptionDialog(this.squares.get(this.gameBoard.getPawnToPromote().getPosition()),"", "Title", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, answers, null );
        if(answer == 1) return PieceTypes.ROOK;
        else if(answer == 2) return PieceTypes.BISHOP;
        else if(answer == 3) return PieceTypes.KNIGHT;
        else return PieceTypes.QUEEN;
    }

}


