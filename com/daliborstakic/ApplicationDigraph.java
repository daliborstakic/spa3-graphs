package com.daliborstakic;

import java.util.Deque;
import java.util.List;
import java.util.Set;
import com.daliborstakic.graphs.Digraph;

public class ApplicationDigraph {
	public static void main(String[] args) {
		Digraph graph = new Digraph("digraf-konture.txt");
		System.out.println(graph);

		displayWeakComponents(graph);
		displayCycle(graph);
		displayTopologicalSort(graph);
		displaySources(graph);
		displaySinks(graph);
		checkReachability(graph);
		displayTransitiveClosure(graph);
		displayMotherVertex(graph);
		displayReachableCandidate(graph);
	}

	private static void displayWeakComponents(Digraph graph) {
		List<Set<Integer>> components = graph.findAllWeakComponents();
		System.out.println("Number of components: " + components.size());
		System.out.println("Weakly Connected Components:");
		for (int i = 0; i < components.size(); i++) {
			System.out.println("Component " + (i + 1) + ": " + components.get(i));
		}
	}

	private static void displayCycle(Digraph graph) {
		Deque<Integer> cycle = graph.findCycle();
		System.out.println();
		if (cycle.isEmpty()) {
			System.out.println("Graph has no cycle!");
		} else {
			System.out.print("Cycle found: ");
			while (!cycle.isEmpty()) {
				System.out.print(cycle.pop() + " ");
			}
			System.out.println();
		}
	}

	private static void displayTopologicalSort(Digraph graph) {
		System.out.println("Topological sort: " + graph.topologicalSort());
	}

	private static void displaySources(Digraph graph) {
		List<Integer> sources = graph.findAllSource();
		System.out.println("\nSources with in-degree 0 and out-degree > 0:");
		sources.forEach(source -> System.out.println("Vertex: " + source));
	}

	private static void displaySinks(Digraph graph) {
		List<Integer> sinks = graph.findAllSinks();
		System.out.println("\nSinks with in-degree > 0 and out-degree 0:");
		sinks.forEach(sink -> System.out.println("Vertex: " + sink));
	}

	private static void checkReachability(Digraph graph) {
		System.out.println("\nMore than one node can reach all others: " + graph.canReachAllAtleastTwice());
		System.out.println("Can reach all from node 0: " + graph.canReachAllFromNode(0));
		for (int i = 0; i < 5; i++) {
			System.out.println("Can reach back to node " + i + ": " + graph.canReachBack(i));
		}
	}

	private static void displayTransitiveClosure(Digraph graph) {
		System.out.println("\nTransitive closure:");
		List<Set<Integer>> closure = graph.transitiveClosure();
		for (int i = 0; i < closure.size(); i++) {
			System.out.println("Vertex " + i + " can reach: " + closure.get(i));
		}
	}

	private static void displayMotherVertex(Digraph graph) {
		int motherVertex = graph.findMotherVertex();
		if (motherVertex == -1) {
			System.out.println("\nThere is no dominant node.\n");
		} else {
			System.out.println("\nDominant node (Mother Vertex): " + motherVertex);
		}
	}

	private static void displayReachableCandidate(Digraph graph) {
		int candidate = graph.canBeReachedFromAll();
		if (candidate != -1) {
			System.out.printf("Node that can be reached from all: %d%n", candidate);
		} else {
			System.out.println("There is no node that can be reached from all.");
		}
	}
}
