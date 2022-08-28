package GUI;

import pieces.AbstractPiece;
import game.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

public class SquareWindow extends JLayeredPane {
    public final static int SQUARE_SIZE = 85;

    private final static String fillCircle = "img/fillCircle.png";
    private final static String circle = "img/circle.png";
    private final static String[] letterMark = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private final static int[] numberPosition = {5,10};
    private final static int[] letterPosition = {78,75};
    private final static Color marksColor = new Color(25,25,25);

    private final int id;
    private final ChessBoardWindow chessBoard;
    private JLabel highlightIcon;
    private JLabel pieceImage;
    private Color squareColor;

    public SquareWindow(int squareId, ChessBoardWindow chessBoard, Color squareColor){
        this.id = squareId;
        this.chessBoard = chessBoard;
        this.squareColor = squareColor;
        this.setBounds(0,0,SQUARE_SIZE,SQUARE_SIZE);
        this.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        this.setBackground(squareColor);
        this.setOpaque(true);
        this.assignPieceImage();
        this.assignMarks();
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                    if(chessBoard.getSelectedSquare() == null){
                        chessBoard.firstClickAction(chessBoard.getBoard().getSquare(id));
                    }
                    else{
                        chessBoard.secondClickAction(chessBoard.getBoard().getSquare(id));
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                    if(chessBoard.getSelectedSquare() == null){
                        chessBoard.firstClickAction(chessBoard.getBoard().getSquare(id));
                    }
                    else{
                        chessBoard.secondClickAction(chessBoard.getBoard().getSquare(id));
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public int getId() {
        return id;
    }

    public void highlightSquare(boolean on){
        if(on) {
            if(this.highlightIcon != null){
                this.remove(this.highlightIcon);
            }
            String imageSrc = this.chessBoard.getBoard().getSquare(id).isOccupied() ? circle : fillCircle;
                try {
                    this.highlightIcon = new JLabel(new ImageIcon(ImageIO.read(new File(imageSrc))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            this.highlightIcon.setBackground(null);
            this.highlightIcon.setBounds(1,2,90,90);
            this.highlightIcon.setOpaque(true);
            this.add(highlightIcon, DEFAULT_LAYER);
            } else{
            this.remove(this.highlightIcon);
            this.repaint();
            this.revalidate();
        }
    }

    private void assignPieceImage(){
        AbstractPiece piece = chessBoard.getBoard().getSquare(id).getPiece();
        if(piece != null){
            String pieceColor = piece.getPieceColor() == Player.BLACK ? "b" : "w";
            try {
                this.pieceImage = new JLabel(new ImageIcon(ImageIO.read(new File("img/" +
                        pieceColor+ piece.getPieceTypes().toString() +".png"))));

            }
            catch (final IOException e) {
                e.printStackTrace();
            }
            this.pieceImage.setBounds(6,4,SQUARE_SIZE,SQUARE_SIZE);
            add(this.pieceImage, JLayeredPane.DRAG_LAYER);
        }
    }

    private void assignMarks(){
        String numberMark = getNumberMark();
        if(numberMark != null){
            JTextField textField = new JTextField();
            textField.setFont(new Font("Consolas", Font.PLAIN, 20));
            textField.setText(numberMark);
            textField.setBounds(numberPosition[0],numberPosition[1], 20, 20);
            textField.setBackground(this.squareColor);
            this.setForeground(marksColor);
            textField.setBorder(BorderFactory.createEmptyBorder());
            textField.setEditable(false);
            textField.setHighlighter(null);
            this.add(textField, JLayeredPane.DEFAULT_LAYER);
        }
        String letterMark = getLetterMark();
        if(letterMark != null){
            JTextField textField = new JTextField();
            textField.setFont(new Font("Consolas", Font.PLAIN, 20));
            textField.setText(letterMark);
            textField.setBounds(letterPosition[0],letterPosition[1], 20, 20);
            textField.setBackground(this.squareColor);
            textField.setForeground(marksColor);
            textField.setBorder(BorderFactory.createEmptyBorder());
            textField.setEditable(false);
            textField.setHighlighter(null);
            this.add(textField, JLayeredPane.DEFAULT_LAYER);
        }
    }

    private String getNumberMark(){
        if(this.id % 8 == 0) return "" + ((AbstractPiece.calculateRow(this.id)+1));;
        return null;
    }

    private String getLetterMark(){
        if(!this.chessBoard.isBoardReverse() && this.id < 8) return letterMark[this.id];
        else if(this.chessBoard.isBoardReverse() && this.id > 55) return letterMark[this.id - 56];
        return null;
    }

    public void rebuild(){
        this.removeAll();
        assignPieceImage();
        assignMarks();
        repaint();
        revalidate();
    }

}
