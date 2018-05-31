package week8;



public class Graph {
	private final int V;
	private int E;
	private Bag<DirectedEdge>[] adj;
	
	@SuppressWarnings("unchecked")
	public Graph(int V, String[] sourceArray, String[] destArray, int[] weightArray) {
		this.V = V;
		this.E = 0;
		adj = (Bag<DirectedEdge>[]) new Bag[V];
		for (int v = 0; v < V; v++)
			adj[v] = new Bag<>();
		for (int v = 0; v < sourceArray.length; v++) {
			int source = sourceArray[v].charAt(0)-65;
			int dest = destArray[v].charAt(0)-65;
			int weight = weightArray[v];
			DirectedEdge e = new DirectedEdge(source, dest, weight);
			addEdge(e);
		}
	}
	

	public int V() { return V; }
	
	public int E() { return E; }
	
	/**
	 * Add the edge e to this digraph.
	 */
	public void addEdge(DirectedEdge e) {
		int v = e.from();
		this.adj[v].add(e);
		E++;
	}
	
	public Bag<DirectedEdge> adj(int v) { 
		return adj[v]; 
	}
}