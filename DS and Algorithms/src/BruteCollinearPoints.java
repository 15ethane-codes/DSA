import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private final List<LineSegment> segments = new ArrayList<>();

    // Finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Points array cannot be null");
        for (Point point : points) {
            if (point == null) throw new IllegalArgumentException("Point cannot be null");
        }

        Point[] copy = points.clone();
        Arrays.sort(copy);
        for (int i = 0; i < copy.length - 1; i++) {
            if (copy[i].compareTo(copy[i + 1]) == 0) throw new IllegalArgumentException("Duplicate points detected");
        }

        int n = copy.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    for (int l = k + 1; l < n; l++) {
                        Point p = copy[i], q = copy[j], r = copy[k], s = copy[l];
                        if (p.slopeTo(q) == p.slopeTo(r) && p.slopeTo(q) == p.slopeTo(s)) {
                            segments.add(new LineSegment(p, s));
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
    }
}
