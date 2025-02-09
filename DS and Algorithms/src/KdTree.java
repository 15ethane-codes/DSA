import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {

    private static class Node {
        private Point2D point;  // Point at this node
        private RectHV rect;    // Rectangle associated with this node
        private Node left, right;  // Left and right subtrees

        public Node(Point2D point, RectHV rect) {
            this.point = point;
            this.rect = rect;
        }
    }

    private Node root;
    private int size;

    // Initializes an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // Is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // Number of points in the set
    public int size() {
        return size;
    }

    // Adds the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null");
        root = insert(root, p, 0, new RectHV(0, 0, 1, 1));
    }

    private Node insert(Node node, Point2D p, int level, RectHV rect) {
        if (node == null) {
            size++;
            return new Node(p, rect);
        }

        if (p.equals(node.point)) {
            return node;  // Don't insert duplicate points
        }

        int cmp;
        if (level % 2 == 0) {  // Compare by x-coordinate at even levels
            cmp = Double.compare(p.x(), node.point.x());
            if (cmp < 0) {
                node.left = insert(node.left, p, level + 1, new RectHV(rect.xmin(), rect.ymin(), node.point.x(), rect.ymax()));
            } else {
                node.right = insert(node.right, p, level + 1, new RectHV(node.point.x(), rect.ymin(), rect.xmax(), rect.ymax()));
            }
        } else {  // Compare by y-coordinate at odd levels
            cmp = Double.compare(p.y(), node.point.y());
            if (cmp < 0) {
                node.left = insert(node.left, p, level + 1, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.point.y()));
            } else {
                node.right = insert(node.right, p, level + 1, new RectHV(rect.xmin(), node.point.y(), rect.xmax(), rect.ymax()));
            }
        }

        return node;
    }

    // Does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null");
        return contains(root, p, 0);
    }

    private boolean contains(Node node, Point2D p, int level) {
        if (node == null) return false;

        if (p.equals(node.point)) return true;

        int cmp;
        if (level % 2 == 0) {
            cmp = Double.compare(p.x(), node.point.x());
            if (cmp < 0) {
                return contains(node.left, p, level + 1);
            } else {
                return contains(node.right, p, level + 1);
            }
        } else {
            cmp = Double.compare(p.y(), node.point.y());
            if (cmp < 0) {
                return contains(node.left, p, level + 1);
            } else {
                return contains(node.right, p, level + 1);
            }
        }
    }

    // Draw all points to standard draw
    public void draw() {
        draw(root, 0);
    }

    private void draw(Node node, int level) {
        if (node == null) return;

        node.point.draw();

        if (level % 2 == 0) {
            // Vertical line for x-split
            new RectHV(node.point.x(), 0, node.point.x(), 1).draw();
            draw(node.left, level + 1);
            draw(node.right, level + 1);
        } else {
            // Horizontal line for y-split
            new RectHV(0, node.point.y(), 1, node.point.y()).draw();
            draw(node.left, level + 1);
            draw(node.right, level + 1);
        }
    }

    // All points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Rectangle cannot be null");
        Queue<Point2D> pointsInRange = new Queue<Point2D>();
        range(root, rect, pointsInRange);
        return pointsInRange;
    }

    private void range(Node node, RectHV rect, Queue<Point2D> pointsInRange) {
        if (node == null) return;

        if (rect.contains(node.point)) {
            pointsInRange.enqueue(node.point);
        }

        if (node.left != null && rect.intersects(node.left.rect)) {
            range(node.left, rect, pointsInRange);
        }
        if (node.right != null && rect.intersects(node.right.rect)) {
            range(node.right, rect, pointsInRange);
        }
    }

    // A nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point cannot be null");
        if (isEmpty()) return null;
        return nearest(root, p, null, 0);
    }

    private Point2D nearest(Node node, Point2D p, Point2D nearest, int level) {
        if (node == null) return nearest;

        double distToNode = p.distanceSquaredTo(node.point);
        if (nearest == null || distToNode < p.distanceSquaredTo(nearest)) {
            nearest = node.point;
        }

        int cmp;
        if (level % 2 == 0) {
            cmp = Double.compare(p.x(), node.point.x());
            if (cmp < 0) {
                nearest = nearest(node.left, p, nearest, level + 1);
                if (node.right != null && p.distanceSquaredTo(nearest) > node.right.rect.distanceSquaredTo(p)) {
                    nearest = nearest(node.right, p, nearest, level + 1);
                }
            } else {
                nearest = nearest(node.right, p, nearest, level + 1);
                if (node.left != null && p.distanceSquaredTo(nearest) > node.left.rect.distanceSquaredTo(p)) {
                    nearest = nearest(node.left, p, nearest, level + 1);
                }
            }
        } else {
            cmp = Double.compare(p.y(), node.point.y());
            if (cmp < 0) {
                nearest = nearest(node.left, p, nearest, level + 1);
                if (node.right != null && p.distanceSquaredTo(nearest) > node.right.rect.distanceSquaredTo(p)) {
                    nearest = nearest(node.right, p, nearest, level + 1);
                }
            } else {
                nearest = nearest(node.right, p, nearest, level + 1);
                if (node.left != null && p.distanceSquaredTo(nearest) > node.left.rect.distanceSquaredTo(p)) {
                    nearest = nearest(node.left, p, nearest, level + 1);
                }
            }
        }

        return nearest;
    }

    // Unit testing of the methods
    public static void main(String[] args) {
        KdTree tree = new KdTree();
        Point2D p1 = new Point2D(0.1, 0.2);
        Point2D p2 = new Point2D(0.3, 0.4);
        Point2D p3 = new Point2D(0.5, 0.6);
        tree.insert(p1);
        tree.insert(p2);
        tree.insert(p3);
        System.out.println("Tree size: " + tree.size());
        System.out.println("Contains p1: " + tree.contains(p1));
        System.out.println("Nearest to (0.4, 0.5): " + tree.nearest(new Point2D(0.4, 0.5)));
        System.out.println("Points in range (0.0, 0.0) to (0.5, 0.5):");
        for (Point2D p : tree.range(new RectHV(0.0, 0.0, 0.5, 0.5))) {
            System.out.println(p);
        }
    }
}
