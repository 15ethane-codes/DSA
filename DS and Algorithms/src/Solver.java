import edu.princeton.cs.algs4.MinPQ;
import java.util.ArrayList;
import java.util.List;

public class Solver {
    private final boolean solvable;
    private final int moves;
    private final List<Board> solution;

    private static class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final int priority;
        private final SearchNode previous;

        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
            this.priority = board.manhattan() + moves;
        }

        public int compareTo(SearchNode other) {
            return Integer.compare(this.priority, other.priority);
        }
    }

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Initial board cannot be null");

        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> twinPQ = new MinPQ<>();
        pq.insert(new SearchNode(initial, 0, null));
        twinPQ.insert(new SearchNode(initial.twin(), 0, null));

        SearchNode goalNode = null;

        while (!pq.isEmpty() && !twinPQ.isEmpty()) {
            goalNode = process(pq);
            if (goalNode != null || process(twinPQ) != null) break;
        }

        if (goalNode != null) {
            this.solvable = true;
            this.moves = goalNode.moves;
            this.solution = new ArrayList<>();
            for (SearchNode node = goalNode; node != null; node = node.previous) {
                solution.add(0, node.board);
            }
        } else {
            this.solvable = false;
            this.moves = -1;
            this.solution = null;
        }
    }

    private SearchNode process(MinPQ<SearchNode> pq) {
        if (pq.isEmpty()) return null;
        SearchNode current = pq.delMin();
        if (current.board.isGoal()) return current;
        for (Board neighbor : current.board.neighbors()) {
            if (current.previous == null || !neighbor.equals(current.previous.board)) {
                pq.insert(new SearchNode(neighbor, current.moves + 1, current));
            }
        }
        return null;
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        return moves;
    }

    public Iterable<Board> solution() {
        return solution;
    }

    public static void main(String[] args) {
        // Test client implementation here.
    }
}
