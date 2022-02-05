package model;

import java.awt.Color;
import java.awt.Point;

public class Robot {

    private Color color;
    private Case cas;

    /**
     * Constructs robot with a case and a color. Set this case is a robot and set
     * color to this case
     * 
     * @param cas   Case in board
     * @param color Color of robot
     */
    public Robot(Case cas, Color color) {
        this.cas = cas;
        this.color = color;
        this.cas.setRobot();
        this.cas.setColor(color);
    }

    /**
     * Returns case of robot
     * 
     * @return Case of robot
     */
    public Case getCase() {
        return this.cas;
    }

    /**
     * Returns point of robot's case
     * 
     * @return Point of case
     */
    public Point getPoint() {
        return this.cas.getPoint();
    }

    /**
     * Returns color of robot
     * 
     * @return Color of robot
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Sets another case to replace current case
     * 
     * @param c Replaced case
     */
    public void setCase(Case c) {
        this.cas = c;
    }

}