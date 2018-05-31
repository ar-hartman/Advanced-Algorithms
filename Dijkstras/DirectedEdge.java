package week8;



public class DirectedEdge implements Comparable<DirectedEdge> {
	private final int v;
	private final int w;
	private final double weight;


	public DirectedEdge(int v, int w, double weight) {
		this.v = v;
		this.w = w;
		this.weight = weight;
	}

	/**
	 * Return the vertex where this edge begins.
	 */
	public int from() {
		return v;
	}

	/**
	 * Return the vertex where this edge ends.
	 */
	public int to() {
		return w;
	}

	/**
	 * Return the weight of this edge.
	 */
	public double weight() { return weight; }

	/**
	 * Return a string representation of this edge.
	 */
	public String toString() {
		return String.format("%d->%d %.2f", v, w, weight);
	}

	/**
	 * Compare by weights.
	 */
	public int compareTo (DirectedEdge o) {
		if (weight < o.weight) return -1;
		if (weight > o.weight) return 1;
		return 0;
	}
}
