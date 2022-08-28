package pieces.moves;


import board.ChessBoard;
import pieces.*;

public interface IMove {

    ChessBoard executeMove(boolean computerMove);
    int getDestination();
}
