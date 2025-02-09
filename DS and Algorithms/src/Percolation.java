import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private final WeightedQuickUnionUF uf;
    private final boolean[] openSites;
    private int openSiteCount;
    private final int topVirtualSite;
    private final int bottomVirtualSite;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be > 0");
        this.n = n;
        this.uf = new WeightedQuickUnionUF(n * n + 2);
        this.openSites = new boolean[n * n];
        this.openSiteCount = 0;
        this.topVirtualSite = n * n;
        this.bottomVirtualSite = n * n + 1;
    }

    private int xyTo1D(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException("Invalid row or col");
        return (row - 1) * n + (col - 1);
    }

    public void open(int row, int col) {
        int index = xyTo1D(row, col);
        if (openSites[index]) return;
        openSites[index] = true;
        openSiteCount++;

        // Connect to virtual top and bottom
        if (row == 1) uf.union(index, topVirtualSite);
        if (row == n) uf.union(index, bottomVirtualSite);

        // Connect to neighbors
        if (row > 1 && isOpen(row - 1, col)) uf.union(index, xyTo1D(row - 1, col));
        if (row < n && isOpen(row + 1, col)) uf.union(index, xyTo1D(row + 1, col));
        if (col > 1 && isOpen(row, col - 1)) uf.union(index, xyTo1D(row, col - 1));
        if (col < n && isOpen(row, col + 1)) uf.union(index, xyTo1D(row, col + 1));
    }

    public boolean isOpen(int row, int col) {
        return openSites[xyTo1D(row, col)];
    }

    public boolean isFull(int row, int col) {
        return uf.find(xyTo1D(row, col)) == uf.find(topVirtualSite);
    }

    public int numberOfOpenSites() {
        return openSiteCount;
    }

    public boolean percolates() {
        return uf.find(topVirtualSite) == uf.find(bottomVirtualSite);
    }
}
