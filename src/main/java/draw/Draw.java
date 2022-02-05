package draw;

import java.awt.*;
import javax.swing.*;

import board.*;

import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

import model.Case;
import model.Name;

public class Draw extends JPanel implements ActionListener, KeyListener {

    private Board board;
    private List<Case> pathAStar;
    private static final int PADDING = 35;
    private static final int BOXSIZE = 30;

    // Color
    static Color tabColor = new Color(245, 240, 230);
    static Color backgroundColor = new Color(195, 156, 132);
    static Color wallColor = new Color(159, 84, 65);
    static Color red = new Color(189, 37, 34);
    static Color blue = new Color(28, 119, 195);
    static Color yellow = new Color(236, 159, 5);
    static Color green = new Color(142, 166, 4);

    private Image token;
    private Image triangle;
    private Image square;
    private Image diamond;
    private Image multicolor;
    private Image watermark;

    /**
     * Constructs board of game, keyboard listener, mouse listener and images
     * 
     * @param board Board of game
     */
    public Draw(Board board) {
        this.board = board;
        this.setBackground(backgroundColor);
        this.addKeyListener(this);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
        this.addMouseListener(mouseEvent);
        this.pathAStar = new ArrayList<>();

        try {
            token = ImageIO.read(new File("./assets/TOKEN.png"));
            triangle = ImageIO.read(new File("./assets/TRIANGLE.png"));
            square = ImageIO.read(new File("./assets/SQUARE.png"));
            diamond = ImageIO.read(new File("./assets/DIAMOND.png"));
            multicolor = ImageIO.read(new File("./assets/MULTICOLOR.png"));
            watermark = ImageIO.read(new File("./assets/ICON.png"));

        } catch (IOException e) {
            System.out.println("Not found!");
        }

    }

    /**
     * Draws board of game
     * 
     * @param g Graphics
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        Stroke oldStroke = g2d.getStroke();

        g2d.setColor(tabColor);
        g2d.fillRect(PADDING, PADDING, BOXSIZE * board.getSize(), BOXSIZE * board.getSize());

        /* Tab timer */
        g.setColor(tabColor);
        g.fillRect(550, PADDING, 196, 230);
        g.setColor(wallColor);
        g.setFont(new Font("serif", Font.BOLD, 30));
        g.drawString("target: " + board.getNumberFoundTarget(), PADDING + 550, PADDING + 40);
        g.drawString("moves: " + board.getCountMoves(), PADDING + 550, PADDING + 205);

        /* Tab choose robot */
        g.setColor(tabColor);
        g.fillRect(550, PADDING + 252, 196, 230);
        g.setColor(wallColor);
        g.drawString("current", 600, 325);
        g.drawString("robot", 565, 355);
        g.drawString("target", 655, 355);
        g.drawString("algorithm A*", 565, 495);

        g.setColor(board.getCurrenRobot().getColor());
        g.fillOval(590, 365, BOXSIZE - 10, BOXSIZE - 10);

        g.setColor(board.getCurrentTarget().getColor());
        g.fillRect(680, 365, BOXSIZE - 10, BOXSIZE - 10);
        switch (board.getCurrentTarget().getName()) {
        case TOKEN:
            g.drawImage(token, 680 + 2, 365 + 2, null);
            break;
        case TRIANGLE:
            g.drawImage(triangle, 680 + 2, 365 + 3, null);
            break;
        case SQUARE:
            g.drawImage(square, 680 + 3, 365 + 3, null);
            break;
        case DIAMOND:
            g.drawImage(diamond, 680 + 1, 365 + 1, null);
            break;
        case MULTICOLOR:
            g.drawImage(multicolor, 680 + 2, 365 + 2, null);
            break;
        default:
            break;
        }

        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (board.getCase(new Point(i, j)) instanceof Case)
                    drawBox(g, wallColor, i, j);
            }
        }

        /* Wall */
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                Point p = new Point(i, j);
                if (board.getCase(p).isWall(Direction.UP))
                    drawWall(Direction.UP, g2d, oldStroke, wallColor, i, j);
                if (board.getCase(p).isWall(Direction.DOWN))
                    drawWall(Direction.DOWN, g2d, oldStroke, wallColor, i, j);
                if (board.getCase(p).isWall(Direction.LEFT))
                    drawWall(Direction.LEFT, g2d, oldStroke, wallColor, i, j);
                if (board.getCase(p).isWall(Direction.RIGHT))
                    drawWall(Direction.RIGHT, g2d, oldStroke, wallColor, i, j);
            }
        }

        /* Target */
        for (int i = 0; i < board.getTargetList().size(); i++) {
            int x = board.getTargetList().get(i).getCase().getX();
            int y = board.getTargetList().get(i).getCase().getY();
            Color c = board.getTargetList().get(i).getColor();
            Name name = board.getTargetList().get(i).getName();
            drawTarget(g, c, x, y);
            switch (name) {
            case TOKEN:
                g.drawImage(token, PADDING + x * BOXSIZE + 7, PADDING + y * BOXSIZE + 7, null);
                break;
            case TRIANGLE:
                g.drawImage(triangle, PADDING + x * BOXSIZE + 7, PADDING + y * BOXSIZE + 8, null);
                break;
            case SQUARE:
                g.drawImage(square, PADDING + x * BOXSIZE + 8, PADDING + y * BOXSIZE + 8, null);
                break;
            case DIAMOND:
                g.drawImage(diamond, PADDING + x * BOXSIZE + 7, PADDING + y * BOXSIZE + 7, null);
                break;
            case MULTICOLOR:
                g.drawImage(multicolor, PADDING + x * BOXSIZE + 7, PADDING + y * BOXSIZE + 7, null);
                break;
            default:
                break;
            }
        }

        /* Robot */
        for (int i = 0; i < board.getRobotList().size(); i++) {
            int x = board.getRobotList().get(i).getCase().getX();
            int y = board.getRobotList().get(i).getCase().getY();
            Color c = board.getRobotList().get(i).getColor();
            drawRobot(g, c, x, y);
        }

        drawPath(g2d, oldStroke, pathAStar);

        /* Watermark */
        g.drawImage(watermark, 680, 246, null);

    }

    /**
     * Draws small box which is grid of board
     * 
     * @param g Graphics
     * @param c Color
     * @param x Point x
     * @param y Point y
     */
    private void drawBox(Graphics g, Color c, int x, int y) {
        g.setColor(c);
        g.drawRect(PADDING + x * BOXSIZE, PADDING + y * BOXSIZE, BOXSIZE, BOXSIZE);
    }

    /**
     * Draws a fill circle which is a robot
     * 
     * @param g Graphics
     * @param c Color
     * @param x Point x
     * @param y Point y
     */
    private void drawRobot(Graphics g, Color c, int x, int y) {
        drawBox(g, wallColor, x, y);
        g.setColor(c);
        g.fillOval(PADDING + x * BOXSIZE + 5, PADDING + y * BOXSIZE + 5, BOXSIZE - 10, BOXSIZE - 10);
    }

    /**
     * Draws a fill rectangle which is background of a target
     * 
     * @param g Graphics
     * @param c Color
     * @param x Point x
     * @param y Point y
     */
    private void drawTarget(Graphics g, Color c, int x, int y) {
        drawBox(g, wallColor, x, y);
        g.setColor(c);
        g.fillRect(PADDING + x * BOXSIZE + 5, PADDING + y * BOXSIZE + 5, BOXSIZE - 10, BOXSIZE - 10);
    }

    /**
     * Draws 4 walls in 4 directions
     * 
     * @param direction 4 directions
     * @param g2d       Graphics2D
     * @param oldStroke Stroke
     * @param c         Color
     * @param x         Point x
     * @param y         Point y
     */
    private void drawWall(Direction direction, Graphics2D g2d, Stroke oldStroke, Color c, int x, int y) {
        drawBox(g2d, c, x, y);
        g2d.setColor(c);
        g2d.setStroke(new BasicStroke(6));

        switch (direction) {
        case UP:
            g2d.drawLine(PADDING + x * BOXSIZE, PADDING + y * BOXSIZE, PADDING + x * BOXSIZE + BOXSIZE,
                    PADDING + y * BOXSIZE);
            break;
        case DOWN:
            g2d.drawLine(PADDING + x * BOXSIZE, PADDING + y * BOXSIZE + BOXSIZE, PADDING + x * BOXSIZE + BOXSIZE,
                    PADDING + y * BOXSIZE + BOXSIZE);
            break;
        case LEFT:
            g2d.drawLine(PADDING + x * BOXSIZE, PADDING + y * BOXSIZE, PADDING + x * BOXSIZE,
                    PADDING + y * BOXSIZE + BOXSIZE);
            break;
        case RIGHT:
            g2d.drawLine(PADDING + x * BOXSIZE + BOXSIZE, PADDING + y * BOXSIZE, PADDING + x * BOXSIZE + BOXSIZE,
                    PADDING + y * BOXSIZE + BOXSIZE);
            break;
        default:
            break;
        }

        g2d.setStroke(oldStroke);

    }

    /**
     * Draws path of algorithm A*
     * 
     * @param g2d       Graphics2D
     * @param oldStroke Stroke
     * @param points    List of case
     */
    private void drawPath(Graphics2D g2d, Stroke oldStroke, List<Case> points) {
        Case prev = null;
        g2d.setColor(board.getCurrenRobot().getColor());
        g2d.setStroke(new BasicStroke(6));
        for (Case p : points) {
            if (prev != null)
                g2d.drawLine(PADDING + prev.getX() * BOXSIZE + 15, PADDING + prev.getY() * BOXSIZE + 15,
                        PADDING + p.getX() * BOXSIZE + 15, PADDING + p.getY() * BOXSIZE + 15);
            prev = p;
        }

        g2d.setStroke(oldStroke);
        repaint();

    }

    public void actionPerformed(ActionEvent ae) {
        // TODO Auto-generated method stub

    }

    /**
     * Uses navigate keyboard to move robot
     * 
     * @param e Keyboard listener
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_UP:
            board.move(board.getMove(Direction.UP, board.getCurrenRobot().getCase()));
            board.newTurn();
            board.countMoves();
            break;
        case KeyEvent.VK_DOWN:
            board.move(board.getMove(Direction.DOWN, board.getCurrenRobot().getCase()));
            board.newTurn();
            board.countMoves();
            break;
        case KeyEvent.VK_LEFT:
            board.move(board.getMove(Direction.LEFT, board.getCurrenRobot().getCase()));
            board.newTurn();
            board.countMoves();
            break;
        case KeyEvent.VK_RIGHT:
            board.move(board.getMove(Direction.RIGHT, board.getCurrenRobot().getCase()));
            board.newTurn();
            board.countMoves();
            break;
        default:
            break;
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    /**
     * Implements mouse clicked
     */
    private MouseListener mouseEvent = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int xPos = e.getX();
            int yPos = e.getY();
            Point point = new Point((xPos - PADDING) / BOXSIZE, (yPos - PADDING) / BOXSIZE);

            if (xPos > PADDING && xPos < board.getSize() * BOXSIZE + PADDING && yPos > PADDING
                    && yPos < board.getSize() * BOXSIZE + PADDING && board.getCase(point).isRobot()) {
                for (int i = 0; i < board.getRobotList().size(); i++) {
                    if (board.getRobotList().get(i).getCase() == board.getCase(point)) {
                        board.setCurrentRobot(board.getRobotList().get(i));
                    }
                }
            }

            if (xPos > 565 && xPos < 725 && yPos > 475 && yPos < 495) {
                System.out.println("Loading algorithm a*");
                pathAStar = board.runAStar();
                if (pathAStar.isEmpty())
                    System.out.println("Not found path");
                System.out.println("Found path");
            }

            repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void mouseExited(MouseEvent e) {
            // TODO Auto-generated method stub

        }
    };

}
