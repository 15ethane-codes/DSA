import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> points;

    // Initializes an empty set of points
    public PointSET() {
        points = new TreeSet<>();
    }

    // Is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // Number of points in the set
    public int size() {
        return points.size();
    }

    // Adds the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null");
        points.add(p);
    }

    // Does the set contain the point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null");
        return points.contains(p);
    }

    // Draw all points to standard draw
    public void draw() {
        for (Point2D p : points) {
            p.draw();
        }
    }

    // All points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Rectangle cannot be null");

        TreeSet<Point2D> inRange = new TreeSet<>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                inRange.add(p);
            }
        }
        return inRange;
    }

    // A nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null");
        if (isEmpty()) return null;

        Point2D nearestPoint = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (Point2D point : points) {
            double dist = point.distanceSquaredTo(p);
            if (dist < minDistance) {
                minDistance = dist;
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }
}
