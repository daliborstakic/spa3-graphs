package com.daliborstakic;

import java.util.Deque;
import java.util.List;
import java.util.Set;
import com.daliborstakic.graphs.Graph;

public class ApplicationGraph {
	public static void main(String[] args) {
		Graph graph = new Graph("graf-nepovezan.txt");
		System.out.println("Graph Representation:\n" + graph);

		displayAdjacencyList(graph);
		displayAdjacentVertices(graph, 0);
		displayComponentContainingVertex(graph, 0);
		displayAllComponents(graph);
		checkPathExistence(graph, 1, 3);
		displayPathBetweenVertices(graph, 8, 9);
		displayDistancesFromNode(graph, 7);
		checkCycleAndBipartite(graph);
	}

	private static void displayAdjacencyList(Graph graph) {
		System.out.println("Adjacency List:");
		System.out.printf("%-8s | %-24s%n", "Vertex", "Adjacent Vertices");
		System.out.println("---------+--------------------------");

		for (int i = 0; i < graph.getNumOfVertices(); i++) {
			System.out.printf("%-8d | %-24s%n", i, graph.adjVertices(i));
		}

		System.out.println();
	}

	private static void displayAdjacentVertices(Graph graph, int vertex) {
		System.out.println("Adjacent vertices to vertex " + vertex + ": " + graph.adjVertices(vertex));
	}

	private static void displayComponentContainingVertex(Graph graph, int vertex) {
		Set<Integer> component = graph.findComponent(vertex);
		System.out.println("Connected component containing vertex " + vertex + ": " + component);
	}

	private static void displayAllComponents(Graph graph) {
		System.out.println("\nAll connected components:");
		List<Set<Integer>> allComponents = graph.findAllComponents();

		for (int i = 0; i < allComponents.size(); i++) {
			System.out.println("Component " + (i + 1) + ": " + allComponents.get(i));
		}
	}

	private static void checkPathExistence(Graph graph, int source, int target) {
		System.out.println("\nPath existence check:");
		System.out.println("Path from " + source + " to " + target + ": "
				+ (graph.existsPath(source, target) ? "Exists" : "Does not exist"));
	}

	private static void displayPathBetweenVertices(Graph graph, int source, int target) {
		Deque<Integer> path = graph.findPath(source, target);

		if (!path.isEmpty()) {
			System.out.println("Path from " + source + " to " + target + ": "
					+ String.join(" -> ", path.stream().map(String::valueOf).toArray(String[]::new)));
		} else {
			System.out.println("No path exists from " + source + " to " + target + ".");
		}
	}

	private static void displayDistancesFromNode(Graph graph, int node) {
		System.out.println("\nDistances from node " + node + ":");
		double[] distances = graph.distanceFromNode(node);

		for (int i = 0; i < distances.length; i++) {
			System.out.printf("Distance to vertex %d: %.2f%n", i, distances[i]);
		}
	}

	private static void checkCycleAndBipartite(Graph graph) {
		System.out.println("\nCycle detection: " + (graph.hasCycle() ? "Cycle detected" : "No cycle detected"));
		System.out
				.println("Bipartite check: " + (graph.isBipartite() ? "Graph is bipartite" : "Graph is not bipartite"));
	}
}
