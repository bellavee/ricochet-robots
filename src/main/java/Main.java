import javax.swing.*;

import board.Board;
import draw.Draw;

public class Main extends JFrame {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 590;

    JButton button = new JButton("Click");

    public Main(Board board) {
        super("Ricochet Robots");
        Draw draw = new Draw(board);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
        add(draw);
        repaint();
    }

    public static void main(String[] args) {
        Board board = new Board(16);
        new Main(board);
    }
}
