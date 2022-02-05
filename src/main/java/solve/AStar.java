package solve;

import java.awt.Point;
import java.util.List;

import board.*;

import java.util.ArrayList;
import java.util.Collections;

import model.Case;
import model.Robot;

public class AStar {
    private Board board;

    private Case start;
    private Case end;
    private Robot activeRobot;

    private List<Case> path;

    private List<Case> openList;
    private List<Case> closedList;

    /**
     * Constructs algorithm A*
     * 
     * @param board Board of game
     */
    public AStar(Board board) {
        this.board = board;
        this.path = new ArrayList<>();
        this.activeRobot = board.getCurrenRobot();
        this.start = this.activeRobot.getCase();
        this.end = board.getCurrentTarget().getCase();
        this.openList = new ArrayList<>();
        this.closedList = new ArrayList<>();
    }

    /**
     * Get start case
     * 
     * @return Start case
     */
    public Case getStart() {
        return this.start;
    }

    /**
     * Get list of path
     * 
     * @return List of path
     */
    public List<Case> getPath() {
        return this.path;
    }

    /**
     * Generate list of point where a robot can move
     * 
     * @param robot Case of robot
     * @return List of point
     */
    public List<Case> getMoves(Case robot) {
        List<Case> positions = new ArrayList<>();
        positions.add(board.getCase(board.getMove(Direction.UP, robot)));
        positions.add(board.getCase(board.getMove(Direction.DOWN, robot)));
        positions.add(board.getCase(board.getMove(Direction.LEFT, robot)));
        positions.add(board.getCase(board.getMove(Direction.RIGHT, robot)));
        return positions;
    }

    /**
     * Calcul next point and set heuristic value to board's case
     * 
     * @param start     Point to start
     * @param direction 4 directions
     * @param h         heuristic value
     */
    private void setHeuristic(Point start, Direction direction, int h) {
        Point next = board.calculNextPoint(start, direction);
        while (!board.getCase(start).isWall(direction) && !board.getCase(next).hasH()) {
            if (board.isValidOnBoard(next)) {
                start = next;
                board.getCase(next).setH(h);
                next = board.calculNextPoint(start, direction);
            }
        }
    }

    /**
     * Add heuristic value is 1 to case of board
     */
    private void addNumberOne() {
        board.getCase(this.activeRobot.getPoint()).setH(1);
        board.getCase(this.activeRobot.getPoint()).setHasH();
        setHeuristic(this.activeRobot.getPoint(), Direction.UP, 1);
        setHeuristic(this.activeRobot.getPoint(), Direction.DOWN, 1);
        setHeuristic(this.activeRobot.getPoint(), Direction.LEFT, 1);
        setHeuristic(this.activeRobot.getPoint(), Direction.RIGHT, 1);
    }

    /**
     * Set case has heuristic value if this case had this value
     * 
     * @param h heuristic value
     */
    private void setCaseHasH(int h) {
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                Point p = new Point(i, j);
                if (board.getCase(p).getH() == h)
                    board.getCase(p).setHasH();
            }
        }
    }

    /**
     * Add another heuristic value to board's case
     * 
     * @param h heuristic value
     */
    private void addAnotherNumber(int h) {
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                Point p = new Point(i, j);
                if (board.getCase(p).getH() == h - 1) {
                    setHeuristic(p, Direction.UP, h);
                    setHeuristic(p, Direction.DOWN, h);
                    setHeuristic(p, Direction.LEFT, h);
                    setHeuristic(p, Direction.RIGHT, h);
                }
            }
        }
    }

    /**
     * Precompute board with heuristic value
     */
    private void precomputeBoard() {
        addNumberOne();
        setCaseHasH(1);
        addAnotherNumber(2);
        setCaseHasH(2);
        addAnotherNumber(3);
        setCaseHasH(3);
        addAnotherNumber(4);
        setCaseHasH(4);
        addAnotherNumber(5);
        setCaseHasH(5);
        addAnotherNumber(6);
    }

    /**
     * Implement algorithm A*
     * 
     * @return List of found path
     */
    public List<Case> aStar() {
        precomputeBoard();
        int node = 0;

        this.openList.clear();
        this.closedList.clear();

        this.start.setF(this.start.getG() + this.start.getH());
        this.openList.add(this.start);

        while (!this.openList.isEmpty()) {
            node++;
            Case currentCase = this.openList.get(0);
            for (Case case_i : openList) {
                if (case_i.getF() < currentCase.getF())
                    currentCase = case_i;
            }

            this.openList.remove(currentCase);
            this.closedList.add(currentCase);

            if (currentCase.getPoint() == this.end.getPoint() && currentCase.getColor() == this.end.getColor()) {
                this.openList.clear();
                this.closedList.clear();
                System.out.println("Node count " + node);
                return reconstructPath(this.end);
            }

            else {

                for (Case caseIndex : getMoves(currentCase)) {
                    Case tmpCase = caseIndex;
                    if (this.closedList.contains(tmpCase)) {
                        continue;
                    }

                    int tmpG = currentCase.getG() + currentCase.getH();

                    if (!this.openList.contains(tmpCase) || tmpG < tmpCase.getG()) {
                        tmpCase.setParent(currentCase);
                        tmpCase.setG(tmpG);
                        tmpCase.setF(tmpCase.getG() + tmpCase.getH());
                        if (!this.openList.contains(tmpCase))
                            this.openList.add(tmpCase);
                    }

                }

            }
        }

        return Collections.emptyList();

    }

    /**
     * Reconstruct path implement in algorithm A*, this will find the path from
     * parent of case
     * 
     * @param c End case or goal case
     * @return Final list of path
     */
    public List<Case> reconstructPath(Case c) {
        this.path = new ArrayList<>();
        this.path.clear();
        Case tmp = c;
        while (tmp != null) {
            if (tmp.getParent() == null)
                return this.path;

            this.path.add(tmp);
            tmp = tmp.getParent();
        }

        return this.path;
    }

}
