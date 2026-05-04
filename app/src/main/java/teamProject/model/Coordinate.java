package teamProject.model;
// a simple coordinate structure for tracking position of entities
public class Coordinate {
    private double x;
    private double y;

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    // equality function override for coordinates
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Coordinate)) return false; // object is not a coordinate
        Coordinate other = (Coordinate)obj;
        return (Double.compare(x, other.x) == 0 &&
                Double.compare(y, other.y) == 0); // make sure coordinates match
    }

    // required by equals/hashCode contract
    @Override
    public int hashCode() {
        return java.util.Objects.hash(x, y);
    }
}
