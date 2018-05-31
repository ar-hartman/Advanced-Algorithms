package week8;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;


public class Dijkstras {
	private static final char SOURCE = 'A';
	
	public static void main(String[] args) throws IOException {
		Graph dg = readFile();								// read input file and return Graph object
		Stack<Integer> path = shortestPath(dg);				// use Graph object to find shortest path
		printPath(path);									// print the solution
	}
	
	private static Stack<Integer> shortestPath(Graph dg) {
		int V = dg.V();																		// identify the size of he graph (V)
		int[] dist = new int[V];															// create a 'distance' array with size V
		int[] parent = new int[V];															// create a 'parent' array with size V
		
		for (int i = 0; i < dg.V(); i++) {
			dist[i] = Integer.MAX_VALUE;													// initialize distance array to INFINITY
			parent[i] = Integer.MIN_VALUE;													// initialize parent array to negative INFINITY
		}
		
		dist[((int)SOURCE)-65] = 0;															// initialize vertex 'A' distance array to 0
		
		IndexMinPQ<Integer> pq = new IndexMinPQ<Integer>(V);								// Create a priority queue (heap)
		for (int i = 0; i < V; i++) {
			pq.insert(i, dist[i]);															// insert all vertices into priority queue
		}
		
		for (int i = 0; i < V - 1; i++) {													// iterate through the vertices and...
			int p = pq.minIndex();															// find the smallest distance in the priority queue
			pq.delMin();																	// delete the smallest
	
			for (DirectedEdge x: dg.adj(p)) {												// for each adjacent vertex to smallest distanced vertex
				if (dist[x.to()] > (dist[p] + x.weight())) {								// compare distance in distance array to weight of edge
					dist[x.to()] = (int) (dist[p] + x.weight());							// if distance of edge is smaller than value in array, update
					parent[x.to()] = p;														// dependent on above, update parent
					pq.decreaseKey(x.to(), dist[x.to()]);									// decrease key 
				}
			}
		}
		System.out.println("The single-source, shortest path distance: " + dist[1]);		// console print the weight of the shortest path
		
		Stack<Integer> path = new Stack<Integer>();											// create a stack to identify the path
		path.push(1);																		// push out ending vertex onto the stack
		int index = parent[1];																// find the preceding vertex index 
		while (index != 0) {
			path.push(index);																// continue to through the path and add the index to the stack
			index = parent[index];
		}
		path.push(index);																	// add the final vertex to the stack
		
		return path;
	}
	
	/*
	 * utility function to iterate through our shortest path stack 
	 * and print the path to console
	 */
	public static void printPath(Stack<Integer> path) {
		int size = path.size();
		for (int i = 0; i < size; i++) {
			System.out.print((char)(path.peek()+65) + " ");
			path.pop();
		}
	}

	/*
	 * Utility function to read user input from the commany like and to ingest the points from the test files.
	 */
	@SuppressWarnings("resource")
	private static Graph readFile() throws FileNotFoundException {
		System.out.println("Please enter an integer representing the case you wish to test. (1, 2, or 3)");
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		int caseNumber = scanner.nextInt();
		String CASE = "";
		
		switch(caseNumber) {
			case 1: CASE = "1";
					break;
			case 2: CASE = "2";
					break;
			case 3: CASE = "3";
					break;
			default: System.out.println("Invalid Selection"); 
					System.exit(0);;
					break;
		}
		System.out.println("Test File Selected: " + CASE);

		// relative file path (file located in same package location as class files
		String fileName = System.getProperty("user.dir") + "\\week8\\Case" + CASE + ".txt";
		
		
		// counter to check the number of files within the case study
		int counter = 0;
		int edges = 0;
		int vertices = 0;
		
		// open file and scanner
		File file = new File(fileName);
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(file);
		
		//use scanner to count the number of points in the case file
		while (sc.hasNextLine()) {
			if (counter == 0) {
				vertices = sc.nextInt();
				System.out.println("The number of vertices: " + vertices);
			}
			if (counter != 0) {
				edges++;
			}
			sc.nextLine();
			counter++;
		}
		
		String[] sourceArray = new String[edges];
		String[] destArray = new String[edges];
		int[] weightArray = new int[edges];
		
		sc = new Scanner(file);
		counter = 0;
		while (sc.hasNextLine()) {
			if (counter != 0) {
				sourceArray[counter-1] = sc.next();
				destArray[counter-1] = sc.next();
				weightArray[counter-1] = sc.nextInt();
			}
			if (counter < edges) sc.nextLine();
			counter++;
		}		
		Graph dg = new Graph(vertices, sourceArray, destArray, weightArray);
		return dg;
	}	
}
