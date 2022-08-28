package GUI;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    private final int MENU_BAR_HEIGHT = 25;

    private JPanel chessBoard;
    private JMenuBar menuBar;


    public Window(){
        this.setTitle("Chess");
        ImageIcon imgIcon = new ImageIcon("img/gameIcon.png");
        this.setSize(new Dimension(SquareWindow.SQUARE_SIZE*9 , SquareWindow.SQUARE_SIZE*9 + MENU_BAR_HEIGHT));
        this.setIconImage(imgIcon.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(0,0));
        this.chessBoard = new ChessBoardWindow();
        this.add(this.chessBoard);
        this.addMenuBar();
        this.setResizable(false);
        this.setVisible(true);
        this.pack();
    }

    public void addMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(SquareWindow.SQUARE_SIZE*9, MENU_BAR_HEIGHT));
        JMenu gameMenu = new JMenu("game");
        JMenu settingsMenu = new JMenu("settings");
        menuBar.add(gameMenu);
        menuBar.add(settingsMenu);
        this.setJMenuBar(menuBar);
    }
}
