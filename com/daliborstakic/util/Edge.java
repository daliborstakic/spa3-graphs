package com.daliborstakic.util;

public class Edge implements Comparable<Edge> {
	private int source;
	private int target;
	private double weight;

	public Edge(int source, int target, double weight) {
		super();
		this.source = source;
		this.target = target;
		this.weight = weight;
	}

	public double getWeight() {
		return weight;
	}

	public int other(int vertex) {
		if (source == vertex)
			return target;

		return source;
	}

	public int either() {
		return target;
	}

	@Override
	public int compareTo(Edge other) {
		return (int) Math.round(this.getWeight() - other.getWeight());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(this.source);
		sb.append(" - ");
		sb.append(this.target);
		sb.append(" = ");
		sb.append(this.getWeight());

		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (!(obj instanceof Edge))
			return false;

		Edge other = (Edge) obj;

		if (other.source == this.source && other.target == this.target && other.weight == this.weight)
			return true;

		return false;
	}
}
