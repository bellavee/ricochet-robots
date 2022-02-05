package model;

import java.awt.Color;

public class Target {

    private Case cas;
    private Name name;
    private Color color;

    /**
     * Constructs target with a case, a name, and a color. Set this case is a target
     * and set color to this case
     * 
     * @param cas   Case in board
     * @param name  Name of target
     * @param color Color of target
     */
    public Target(Case cas, Name name, Color color) {
        this.cas = cas;
        this.name = name;
        this.color = color;
        this.cas.setTarget();
        this.cas.setColor(color);

    }

    /**
     * Returns name of target
     * 
     * @return Name of target
     */
    public Name getName() {
        return this.name;
    }

    /**
     * Returns case of target
     * 
     * @return Case of target
     */
    public Case getCase() {
        return this.cas;
    }

    /**
     * Returns color of target
     * 
     * @return Color of target
     */
    public Color getColor() {
        return this.color;
    }

}
