import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final int[][] tiles;
    private final int n;
    private final int blankRow, blankCol;
    private final int hamming;
    private final int manhattan;

    public Board(int[][] tiles) {
        if (tiles == null) throw new IllegalArgumentException("Tiles array cannot be null");
        this.n = tiles.length;
        this.tiles = new int[n][n];
        int hammingCount = 0, manhattanDist = 0;
        int blankR = -1, blankC = -1;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.tiles[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    blankR = i;
                    blankC = j;
                } else if (tiles[i][j] != (i * n + j + 1)) {
                    hammingCount++;
                    int goalRow = (tiles[i][j] - 1) / n;
                    int goalCol = (tiles[i][j] - 1) % n;
                    manhattanDist += Math.abs(goalRow - i) + Math.abs(goalCol - j);
                }
            }
        }

        this.blankRow = blankR;
        this.blankCol = blankC;
        this.hamming = hammingCount;
        this.manhattan = manhattanDist;
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        return hamming;
    }

    public int manhattan() {
        return manhattan;
    }

    public boolean isGoal() {
        return manhattan == 0;
    }

    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null || getClass() != y.getClass()) return false;
        Board other = (Board) y;
        return n == other.n && Arrays.deepEquals(tiles, other.tiles);
    }

    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] d : directions) {
            int newRow = blankRow + d[0], newCol = blankCol + d[1];
            if (newRow >= 0 && newRow < n && newCol >= 0 && newCol < n) {
                int[][] newTiles = copy(tiles);
                newTiles[blankRow][blankCol] = newTiles[newRow][newCol];
                newTiles[newRow][newCol] = 0;
                neighbors.add(new Board(newTiles));
            }
        }
        return neighbors;
    }

    public Board twin() {
        int[][] twinTiles = copy(tiles);
        outer:
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (twinTiles[i][j] != 0 && twinTiles[i][j + 1] != 0) {
                    int temp = twinTiles[i][j];
                    twinTiles[i][j] = twinTiles[i][j + 1];
                    twinTiles[i][j + 1] = temp;
                    break outer;
                }
            }
        }
        return new Board(twinTiles);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(n).append("\n");
        for (int[] row : tiles) {
            for (int tile : row) {
                sb.append(String.format("%2d ", tile));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private int[][] copy(int[][] original) {
        int[][] copy = new int[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, original[i].length);
        }
        return copy;
    }
}
