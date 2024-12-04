package com.daliborstakic.util;

public class Subset {
	private int rank, parent;

	public Subset(int parent, int rank) {
		super();
		this.parent = parent;
		this.rank = rank;
	}

	public static void union(Subset[] subsets, int x, int y) {
		int rootX = findRoot(subsets, x);
		int rootY = findRoot(subsets, y);

		if (subsets[rootY].rank < subsets[rootX].rank)
			subsets[rootY].parent = rootX;
		else if (subsets[rootX].rank < subsets[rootY].rank)
			subsets[rootX].parent = rootY;
		else {
			subsets[rootY].parent = rootX;
			subsets[rootX].rank++;
		}
	}

	public static int findRoot(Subset[] subsets, int i) {
		if (subsets[i].parent == i)
			return subsets[i].parent;

		subsets[i].parent = findRoot(subsets, subsets[i].parent);
		return subsets[i].parent;
	}
}
