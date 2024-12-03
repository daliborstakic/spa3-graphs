package com.daliborstakic;

import java.util.Arrays;
import java.util.Map;
import java.util.Stack;
import com.daliborstakic.graphs.WeightedGraph;
import com.daliborstakic.util.Edge;

public class ApplicationWeighted {
	public static void main(String[] args) {
		WeightedGraph graph = new WeightedGraph("usm-tez1.txt");
		System.out.println("Graph:\n" + graph);

		displayMostImportantNode(graph);
		displayLeastImportantNodes(graph);
		displayShortestPathBetweenNodes(graph, 5, 9);
		displayShortestPathsFromNode(graph, 0);
		displayShortestPathWithWeightLimit(graph, 5, 2, 100.0);
		displayShortestPathWithEvenNodes(graph, 9, 0, 10.0);
	}

	private static void displayMostImportantNode(WeightedGraph graph) {
		System.out.println("(1) Node with the heaviest weight: " + graph.getMostImportantNode());
	}

	private static void displayLeastImportantNodes(WeightedGraph graph) {
		System.out.println("(2) Node with the lightest weight: " + graph.getLeastImportantNodes());
	}

	private static void displayShortestPathBetweenNodes(WeightedGraph graph, int source, int target) {
		System.out.println("(3) Shortest path between nodes " + source + " and " + target + ": "
				+ Arrays.asList(graph.shortestPathToTarget(source, target)));
	}

	private static void displayShortestPathsFromNode(WeightedGraph graph, int source) {
		Map<Integer, Stack<Edge>> paths = graph.shortestPaths(source);
		System.out.println("\n(4) Shortest paths from node " + source + " to all nodes:");
		for (Map.Entry<Integer, Stack<Edge>> entry : paths.entrySet()) {
			int vertex = entry.getKey();
			Stack<Edge> path = entry.getValue();

			System.out.print("Path to node " + vertex + ": ");
			if (path.isEmpty()) {
				System.out.print("Source node");
			} else {
				while (!path.isEmpty()) {
					System.out.print(path.pop() + " ");
				}
			}
			System.out.println();
		}
	}

	private static void displayShortestPathWithWeightLimit(WeightedGraph graph, int source, int target,
			double weightLimit) {
		System.out.println("\n(5) Path from node " + source + " to node " + target + " with weight less than "
				+ weightLimit + ": " + Arrays.asList(graph.shortestPathN(source, target, weightLimit)));
	}

	private static void displayShortestPathWithEvenNodes(WeightedGraph graph, int source, int target,
			double weightLimit) {
		System.out.println("(6) Path from node " + source + " to node " + target + " with weight less than "
				+ weightLimit + " and only even nodes: "
				+ Arrays.asList(graph.shortestPathEven(source, target, weightLimit)));
	}
}
