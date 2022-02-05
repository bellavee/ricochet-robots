package board;

import java.util.List;

import model.*;
import solve.AStar;

import java.util.ArrayList;
import java.awt.Color;
import java.awt.Point;

public class Board {

    /* --------------- LOCAL VARIABLES --------------- */

    private int size;
    private Case[][] board;
    private Color red = new Color(189, 37, 34);
    private Color blue = new Color(28, 119, 195);
    private Color yellow = new Color(236, 159, 5);
    private Color green = new Color(142, 166, 4);
    private Color gray = new Color(54, 54, 54);
    private int countMove;
    private List<Target> targetList;
    private List<Target> foundList;
    private List<Robot> robotList;
    private List<Point> startRobotPosition;
    private Target currentTarget;
    private Robot currentRobot;
    private List<Robot> nonActiveRobot;
    private List<Case> path;

    /* --------------- CONSTRUCTOR --------------- */

    /**
     * Constructs a board with a size and create necessary element
     * 
     * @param size Size of board
     */
    public Board(int size) {
        this.size = size;
        this.board = new Case[size][size];
        this.foundList = new ArrayList<>();
        createBoard();
        createTargets();
        createRobots();
        setCurrentTarget(targetList.get(foundList.size()));
        setActiveRobot();
        getStartRobotPoint();
        newTurn();

    }

    /* --------------- GET LOCAL VARIABLE --------------- */

    /**
     * Returns case of board with a point
     * 
     * @param p Point to get
     * @return Case of board
     */
    public Case getCase(Point p) {
        return this.board[p.x][p.y];
    }

    /**
     * Returns size of board
     * 
     * @return Size of board
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Returns current robot
     * 
     * @return Current robot
     */
    public Robot getCurrenRobot() {
        return this.currentRobot;
    }

    /**
     * Returns current target
     * 
     * @return Current target
     */
    public Target getCurrentTarget() {
        return this.currentTarget;
    }

    /**
     * Returns list of robot
     * 
     * @return List of robot
     */
    public List<Robot> getRobotList() {
        return this.robotList;
    }

    /**
     * Returns list of target
     * 
     * @return List of target
     */
    public List<Target> getTargetList() {
        return this.targetList;
    }

    /**
     * Returns number of robot's moves
     * 
     * @return int
     */
    public int getCountMoves() {
        return this.countMove;
    }

    /**
     * Returns number of target is found
     * 
     * @return int
     */
    public int getNumberFoundTarget() {
        return this.foundList.size();
    }

    /**
     * Get initial robot positions
     */
    public void getStartRobotPoint() {
        startRobotPosition = new ArrayList<>();
        for (int i = 0; i < robotList.size(); i++) {
            startRobotPosition.add(robotList.get(i).getPoint());
        }
    }

    /* --------------- SET FONCTION --------------- */

    /**
     * Set current robot
     * 
     * @param r Robot to set
     */
    public void setCurrentRobot(Robot r) {
        this.currentRobot = r;
    }

    /**
     * Set current target
     * 
     * @param t Target to set
     */
    public void setCurrentTarget(Target t) {
        this.currentTarget = t;
    }

    /**
     * Set current robot to robot same color with current target
     */
    public void setActiveRobot() {
        this.nonActiveRobot = new ArrayList<>();
        for (Robot robot : getRobotList()) {
            if (this.currentTarget.getColor() == robot.getColor())
                setCurrentRobot(robot);
            else
                this.nonActiveRobot.add(robot);
        }
    }

    public void countMoves() {
        countMove++;
    }

    /* --------------- INSTALL BOARD --------------- */

    /**
     * Create list of robots
     */
    public void createRobots() {
        robotList = new ArrayList<>();
        robotList.add(new Robot(board[13][7], red));
        robotList.add(new Robot(board[0][14], blue));
        robotList.add(new Robot(board[14][11], yellow));
        robotList.add(new Robot(board[4][4], green));
        robotList.add(new Robot(board[6][7], gray));
    }

    /**
     * Create list of targets
     */
    public void createTargets() {
        targetList = new ArrayList<>();

        targetList.add(new Target(board[9][3], Name.TRIANGLE, red));
        targetList.add(new Target(board[1][6], Name.TRIANGLE, yellow));
        targetList.add(new Target(board[1][9], Name.SQUARE, red));
        targetList.add(new Target(board[3][11], Name.TRIANGLE, green));
        targetList.add(new Target(board[12][13], Name.MULTICOLOR, Color.black));
        targetList.add(new Target(board[14][14], Name.TOKEN, yellow));
        targetList.add(new Target(board[2][2], Name.DIAMOND, green));
        targetList.add(new Target(board[12][9], Name.SQUARE, green));
        targetList.add(new Target(board[13][1], Name.DIAMOND, blue));

        targetList.add(new Target(board[4][13], Name.DIAMOND, yellow));
        targetList.add(new Target(board[5][1], Name.TOKEN, red));
        targetList.add(new Target(board[9][12], Name.TRIANGLE, blue));
        targetList.add(new Target(board[13][4], Name.SQUARE, yellow));
        targetList.add(new Target(board[7][5], Name.SQUARE, blue));
        targetList.add(new Target(board[11][8], Name.DIAMOND, red));
        targetList.add(new Target(board[6][10], Name.TOKEN, blue));
        targetList.add(new Target(board[5][1], Name.TOKEN, red));
        targetList.add(new Target(board[10][1], Name.TOKEN, green));

    }

    /**
     * Create wall's stop, a wall's stop has 2 wall which contain a direction and an
     * opposite direction. When the player move robot in left or right, up or down,
     * there is a wall in this direction and robot will stop.
     * 
     * @param direction Direction to create wall
     * @param x         Point x to create
     * @param y         Point y to create
     */
    public void createWallStop(Direction direction, int x, int y) {
        switch (direction) {
        case UP:
            this.board[x][y].setWall(direction);
            this.board[x][y - 1].setWall(direction.getOpposite());
            break;
        case DOWN:
            this.board[x][y].setWall(direction);
            this.board[x][y + 1].setWall(direction.getOpposite());
            break;
        case LEFT:
            this.board[x][y].setWall(direction);
            this.board[x - 1][y].setWall(direction.getOpposite());
            break;
        case RIGHT:
            this.board[x][y].setWall(direction);
            this.board[x + 1][y].setWall(direction.getOpposite());
            break;
        default:
            break;
        }
    }

    /**
     * Create walls of board
     */
    public void createBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++)
                this.board[i][j] = new Case(new Point(i, j), null);
        }

        for (int i = 0; i < size; i++)
            this.board[i][0].setWall(Direction.UP);

        for (int i = 0; i < size; i++)
            this.board[i][size - 1].setWall(Direction.DOWN);

        for (int i = 0; i < size; i++)
            this.board[0][i].setWall(Direction.LEFT);

        for (int i = 0; i < size; i++)
            this.board[size - 1][i].setWall(Direction.RIGHT);

        createWallStop(Direction.LEFT, 3, 0);
        createWallStop(Direction.UP, 0, 4);

        createWallStop(Direction.RIGHT, 12, 0);
        createWallStop(Direction.UP, 15, 6);

        createWallStop(Direction.LEFT, 5, 15);
        createWallStop(Direction.DOWN, 0, 11);

        createWallStop(Direction.RIGHT, 10, 15);
        createWallStop(Direction.DOWN, 15, 13);

        createWallStop(Direction.DOWN, 5, 1);
        createWallStop(Direction.LEFT, 5, 1);
        createWallStop(Direction.DOWN, 2, 2);
        createWallStop(Direction.LEFT, 2, 2);
        createWallStop(Direction.DOWN, 1, 6);
        createWallStop(Direction.LEFT, 1, 6);
        createWallStop(Direction.UP, 7, 5);
        createWallStop(Direction.RIGHT, 7, 5);

        createWallStop(Direction.UP, 9, 3);
        createWallStop(Direction.LEFT, 9, 3);
        createWallStop(Direction.UP, 10, 1);
        createWallStop(Direction.RIGHT, 10, 1);
        createWallStop(Direction.DOWN, 13, 1);
        createWallStop(Direction.LEFT, 13, 1);
        createWallStop(Direction.DOWN, 13, 4);
        createWallStop(Direction.RIGHT, 13, 4);

        createWallStop(Direction.UP, 1, 9);
        createWallStop(Direction.LEFT, 1, 9);
        createWallStop(Direction.DOWN, 3, 11);
        createWallStop(Direction.RIGHT, 3, 11);
        createWallStop(Direction.DOWN, 6, 10);
        createWallStop(Direction.RIGHT, 6, 10);
        createWallStop(Direction.UP, 4, 13);
        createWallStop(Direction.LEFT, 4, 13);

        createWallStop(Direction.DOWN, 11, 8);
        createWallStop(Direction.LEFT, 11, 8);
        createWallStop(Direction.UP, 12, 9);
        createWallStop(Direction.RIGHT, 12, 9);
        createWallStop(Direction.DOWN, 9, 12);
        createWallStop(Direction.LEFT, 9, 12);
        createWallStop(Direction.UP, 14, 14);
        createWallStop(Direction.LEFT, 14, 14);

        createWallStop(Direction.UP, 12, 13);
        createWallStop(Direction.RIGHT, 12, 13);

        /**
         * Create center wall
         */
        createWallStop(Direction.UP, 7, 7);
        createWallStop(Direction.UP, 8, 7);
        createWallStop(Direction.DOWN, 7, 8);
        createWallStop(Direction.DOWN, 8, 8);
        createWallStop(Direction.LEFT, 7, 8);
        createWallStop(Direction.LEFT, 7, 7);
        createWallStop(Direction.RIGHT, 8, 8);
        createWallStop(Direction.RIGHT, 8, 7);
    }

    /* --------------- CONDITION --------------- */

    /**
     * Check if a point is valid on board
     * 
     * @param p Point to check
     * @return boolean
     */
    public boolean isValidOnBoard(Point p) {
        return p.x >= 0 && p.y >= 0 && p.x < size && p.y < size;
    }

    /* --------------- FUNCTION --------------- */

    /**
     * Calculate next point in 4 directions
     * 
     * @param start     Point start
     * @param direction 4 directions
     * @return Next point
     */
    public Point calculNextPoint(Point start, Direction direction) {
        Point end = null;

        switch (direction) {
        case UP:
            end = new Point(start.x, start.y - 1);
            break;
        case DOWN:
            end = new Point(start.x, start.y + 1);
            break;
        case LEFT:
            end = new Point(start.x - 1, start.y);
            break;
        case RIGHT:
            end = new Point(start.x + 1, start.y);
            break;
        default:
            break;
        }

        return end;
    }

    /**
     * Get the last position robot can move, it means the robot will move and stop
     * when hits a wall or a robot
     * 
     * @param direction 4 directions
     * @param robot     Robot to move
     * @return Point to move
     */
    public Point getMove(Direction direction, Case robot) {
        Point start = robot.getPoint();
        Point end = calculNextPoint(start, direction);
        ArrayList<Point> positions = new ArrayList<>();

        while (!(board[start.x][start.y].isWall(direction) || board[end.x][end.y].isRobot())) {
            if (isValidOnBoard(end)) {
                positions.add(end);
                start = end;
                end = calculNextPoint(start, direction);
            }
        }
        positions.add(start);
        return positions.get(positions.size() - 1);
    }

    /**
     * Move current robot to a point
     * 
     * @param end Point to move
     */
    public void move(Point end) {
        currentRobot.getCase().setNotRobot();
        currentRobot.setCase(getCase(end));
        currentRobot.getCase().setRobot();
    }

    /**
     * Check if robots can find to target, if found, then add found target to the
     * list of found target and return true
     * 
     * @return boolean
     */
    public boolean foundTarget() {
        if (this.currentTarget.getCase().getPoint() == this.currentRobot.getPoint()
                && (this.currentTarget.getColor() == this.currentRobot.getColor()
                        || this.currentTarget.getName() == Name.MULTICOLOR)) {
            this.currentTarget = targetList.get(foundList.size() + 1);
            foundList.add(this.currentTarget);
            setActiveRobot();

            return true;
        }
        return false;
    }

    /**
     * Reset robot positions back to initial position
     */
    public void resetBoard() {
        for (int i = 0; i < startRobotPosition.size(); i++) {
            robotList.get(i).getCase().setNotRobot();
            robotList.get(i).setCase(getCase(startRobotPosition.get(i)));
            robotList.get(i).getCase().setRobot();
        }

    }

    /**
     * When robot finds target, reset board to initial board
     */
    public void newTurn() {
        if (foundTarget()) {
            resetBoard();
            this.countMove = -1;
            if (this.path != null)
                this.path.clear();
            if (this.foundList.size() == 16) {
                setCurrentTarget(targetList.get(0));
                this.foundList.clear();
            }
        }
    }

    /**
     * Execute algorithm A*
     * 
     * @return List of path
     */
    public List<Case> runAStar() {
        AStar aStar = new AStar(this);
        aStar.aStar();
        this.path = aStar.getPath();
        this.path.add(this.path.size(), aStar.getStart());
        return this.path;
    }

}
