import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private final List<LineSegment> segments = new ArrayList<>();

    // Finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
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
            Point origin = copy[i];
            Point[] sorted = copy.clone();
            Arrays.sort(sorted, origin.slopeOrder());

            int count = 1;
            for (int j = 1; j < sorted.length - 1; j++) {
                if (origin.slopeTo(sorted[j]) == origin.slopeTo(sorted[j + 1])) {
                    count++;
                } else {
                    if (count >= 3) {
                        Point[] collinear = Arrays.copyOfRange(sorted, j - count + 1, j + 1);
                        Arrays.sort(collinear);
                        if (origin.compareTo(collinear[0]) < 0) {
                            segments.add(new LineSegment(origin, collinear[collinear.length - 1]));
                        }
                    }
                    count = 1;
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
