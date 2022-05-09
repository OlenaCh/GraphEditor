package cz.cuni.mff.java.project.grapheditor.editor;

/**
 * A helper class that stores information about point coordinates.
 */
public class PointD {
    private Double x;
    private Double y;

    public PointD(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return this.x;
    }

    public Double getY() {
        return this.y;
    }
}
