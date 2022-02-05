package model;

import java.awt.Point;

import board.Direction;

import java.awt.Color;

public class Case {
    private Point point;
    private boolean isRobot;
    private boolean isTarget;
    private boolean[] wall;
    private Color color;
    private int f;
    private int g;
    private int h;
    private boolean hasH;
    private Case parent;

    /**
     * Constructs case with a point and a color. A Case will contain walls, values
     * of algorithm A*. A boolean to determine if case is robot or target or
     * nothing.
     * 
     * @param point Position to place
     * @param color Color of case
     */
    public Case(Point point, Color color) {
        this.point = point;
        this.isRobot = false;
        this.isTarget = false;
        this.wall = new boolean[] { false, false, false, false };
        this.f = 0;
        this.g = 0;
        this.h = 0;
        this.hasH = false;
        this.parent = null;
        this.color = color;
    }

    /* --------------- GET LOCAL VARIABLE --------------- */

    /**
     * Returns point of case
     * 
     * @return Point
     */
    public Point getPoint() {
        return this.point;
    }

    /**
     * Returns if case is a robot
     * 
     * @return boolean
     */
    public boolean isRobot() {
        return this.isRobot;
    }

    /**
     * Checks if case is a target
     * 
     * @return boolean
     */
    public boolean isTarget() {
        return this.isTarget;
    }

    /**
     * Returns f value
     * 
     * @return f value
     */
    public int getF() {
        return this.f;
    }

    /**
     * Returns g value
     * 
     * @return g value
     */
    public int getG() {
        return this.g;
    }

    /**
     * Returns h value
     * 
     * @return h value
     */
    public int getH() {
        return this.h;
    }

    /**
     * Returns if case is identified
     * 
     * @return boolean
     */
    public boolean hasH() {
        return this.hasH;
    }

    /**
     * Returns parent case
     * 
     * @return Case
     */
    public Case getParent() {
        return this.parent;
    }

    /**
     * Returns color of case
     * 
     * @return Color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Returns point x of case
     * 
     * @return int
     */
    public int getX() {
        return this.point.x;
    }

    /**
     * Returns point y of case
     * 
     * @return int
     */
    public int getY() {
        return this.point.y;
    }

    /* --------------- SET LOCAL VARIABLE --------------- */

    /**
     * Sets this case is robot
     */
    public void setRobot() {
        this.isRobot = true;
    }

    /**
     * Sets this case is not robot
     */
    public void setNotRobot() {
        this.isRobot = false;
    }

    /**
     * Sets this case is target
     */
    public void setTarget() {
        this.isTarget = true;
    }

    /**
     * Sets color of case
     * 
     * @param color Color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Sets point of case or replace current point of case
     * 
     * @param point New point
     */
    public void setPoint(Point point) {
        this.point = point;
    }

    /**
     * Sets case has h value
     */
    public void setHasH() {
        this.hasH = true;
    }

    /**
     * Sets f value of case
     * 
     * @param f value
     */
    public void setF(int f) {
        this.f = f;
    }

    /**
     * Sets g value of case
     * 
     * @param g value
     */
    public void setG(int g) {
        this.g = g;
    }

    /**
     * Sets h value of case
     * 
     * @param h value
     */
    public void setH(int h) {
        this.h = h;
    }

    /**
     * Sets parent of case
     * 
     * @param parent Parent case
     */
    public void setParent(Case parent) {
        this.parent = parent;
    }

    /**
     * Checks if direction has a wall
     * 
     * @param direction 4 directions
     * @return boolean
     */
    public boolean isWall(Direction direction) {
        switch (direction) {
        case UP:
            return this.wall[0];
        case DOWN:
            return this.wall[1];
        case LEFT:
            return this.wall[2];
        case RIGHT:
            return this.wall[3];
        default:
            return false;
        }
    }

    /**
     * Creates wall in which direction
     * 
     * @param direction 4 directions
     */
    public void setWall(Direction direction) {
        switch (direction) {
        case UP:
            this.wall[0] = true;
            break;
        case DOWN:
            this.wall[1] = true;
            break;
        case LEFT:
            this.wall[2] = true;
            break;
        case RIGHT:
            this.wall[3] = true;
            break;
        default:
            break;
        }
    }

}
