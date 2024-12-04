package com.daliborstakic.graphs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

import com.daliborstakic.util.Edge;
import com.daliborstakic.util.Subset;

import edu.princeton.cs.algs4.IndexMinPQ;

public class WeightedGraph {
	private final static Random random = new Random();
	private final Map<Integer, Set<Edge>> graph = new HashMap<>();
	private final TreeMap<Double, Set<Integer>> importance = new TreeMap<>(Collections.reverseOrder());
	private List<Edge> edges;
	private int numOfVertices;
	private int numOfEdges;

	public WeightedGraph(String filename) {
		this.edges = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(new File(filename)))) {
			numOfVertices = Integer.parseInt(reader.readLine());
			numOfEdges = Integer.parseInt(reader.readLine());

			for (int i = 0; i < numOfVertices; i++) {
				graph.put(i, new HashSet<>());
			}

			Map<Integer, Double> tempImportance = new HashMap<>();
			String line;
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(" ");
				int v1 = Integer.parseInt(tokens[0].trim());
				int v2 = Integer.parseInt(tokens[1].trim());
				double weight = Double.parseDouble(tokens[2].trim());

				tempImportance.put(v1, tempImportance.getOrDefault(v1, 0.0) + weight);
				tempImportance.put(v2, tempImportance.getOrDefault(v2, 0.0) + weight);

				this.edges.add(new Edge(v1, v2, weight));
				addEdge(v1, v2, weight);
			}

			for (Entry<Integer, Double> entry : tempImportance.entrySet()) {
				importance.computeIfAbsent(entry.getValue(), k -> new HashSet<>()).add(entry.getKey());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getNumOfVertices() {
		return numOfVertices;
	}

	public int getNumOfEdges() {
		return numOfEdges;
	}

	public Edge getRandomEdge() {
		return this.edges.get(random.nextInt(0, numOfEdges));
	}

	public void addEdge(int source, int target, double weight) {
		graph.get(source).add(new Edge(source, target, weight));
	}

	public Set<Edge> adj(int vertex) {
		return graph.get(vertex);
	}

	public Entry<Double, Set<Integer>> getMostImportantNode() {
		return importance.firstEntry();
	}

	public Entry<Double, Set<Integer>> getLeastImportantNodes() {
		return importance.lastEntry();
	}

	public Stack<Edge> shortestPathToTarget(int source, int target) {
		double[] distTo = new double[numOfVertices];
		Edge[] edgeTo = new Edge[numOfVertices];
		Arrays.fill(distTo, Double.POSITIVE_INFINITY);
		distTo[source] = 0.0;

		IndexMinPQ<Double> pq = new IndexMinPQ<>(numOfVertices);
		pq.insert(source, 0.0);

		while (!pq.isEmpty()) {
			int v = pq.delMin();

			for (Edge e : adj(v))
				relax(e, v, distTo, edgeTo, pq);
		}

		if (distTo[target] == Double.POSITIVE_INFINITY)
			return null;

		return buildPath(edgeTo, target);
	}

	public Map<Integer, Stack<Edge>> shortestPaths(int source) {
		double[] distTo = new double[numOfVertices];
		Edge[] edgeTo = new Edge[numOfVertices];
		Arrays.fill(distTo, Double.POSITIVE_INFINITY);
		distTo[source] = 0.0;

		IndexMinPQ<Double> pq = new IndexMinPQ<>(numOfVertices);
		pq.insert(source, 0.0);

		while (!pq.isEmpty()) {
			int v = pq.delMin();

			for (Edge e : adj(v))
				relax(e, v, distTo, edgeTo, pq);
		}

		Map<Integer, Stack<Edge>> paths = new TreeMap<>();
		for (int i = 0; i < numOfVertices; i++) {
			paths.put(i, buildPath(edgeTo, i));
		}

		return paths;
	}

	public Stack<Edge> shortestPathN(int source, int target, double maxWeight) {
		double[] distTo = new double[numOfVertices];
		Edge[] edgeTo = new Edge[numOfVertices];
		Arrays.fill(distTo, Double.POSITIVE_INFINITY);
		distTo[source] = 0.0;

		IndexMinPQ<Double> pq = new IndexMinPQ<>(numOfVertices);
		pq.insert(source, 0.0);

		while (!pq.isEmpty()) {
			int v = pq.delMin();

			for (Edge e : adj(v))
				relaxN(e, v, distTo, edgeTo, pq, maxWeight);
		}

		return buildPath(edgeTo, target);
	}

	public Stack<Edge> shortestPathEven(int source, int target, double maxWeight) {
		double[] distTo = new double[numOfVertices];
		Edge[] edgeTo = new Edge[numOfVertices];
		Arrays.fill(distTo, Double.POSITIVE_INFINITY);
		distTo[source] = 0.0;

		IndexMinPQ<Double> pq = new IndexMinPQ<>(numOfVertices);
		pq.insert(source, 0.0);

		while (!pq.isEmpty()) {
			int v = pq.delMin();

			for (Edge e : adj(v))
				relaxEven(e, v, distTo, edgeTo, pq, maxWeight, source, target);
		}

		return buildPath(edgeTo, target);
	}

	private void relax(Edge e, int v, double[] distTo, Edge[] edgeTo, IndexMinPQ<Double> pq) {
		int w = e.other(v);

		if (distTo[w] > distTo[v] + e.getWeight()) {
			distTo[w] = distTo[v] + e.getWeight();
			edgeTo[w] = e;

			if (pq.contains(w))
				pq.decreaseKey(w, distTo[w]);
			else
				pq.insert(w, distTo[w]);
		}
	}

	private void relaxN(Edge e, int v, double[] distTo, Edge[] edgeTo, IndexMinPQ<Double> pq, double maxWeight) {
		int w = e.other(v);

		if (distTo[w] > distTo[v] + e.getWeight() && distTo[v] + e.getWeight() <= maxWeight) {
			distTo[w] = distTo[v] + e.getWeight();
			edgeTo[w] = e;

			if (pq.contains(w))
				pq.decreaseKey(w, distTo[w]);
			else
				pq.insert(w, distTo[w]);
		}
	}

	private void relaxEven(Edge e, int v, double[] distTo, Edge[] edgeTo, IndexMinPQ<Double> pq, double maxWeight,
			int source, int target) {
		int w = e.other(v);
		double newWeight = distTo[v] + e.getWeight();

		if ((w % 2 == 0 || w == source || w == target) && newWeight <= maxWeight && newWeight < distTo[w]) {
			distTo[w] = newWeight;
			edgeTo[w] = e;

			if (pq.contains(w)) {
				pq.decreaseKey(w, newWeight);
			} else {
				pq.insert(w, newWeight);
			}
		}
	}

	private Stack<Edge> buildPath(Edge[] edgeTo, int target) {
		Stack<Edge> path = new Stack<>();
		for (Edge e = edgeTo[target]; e != null; e = edgeTo[e.other(e.either())]) {
			path.push(e);
		}

		return path;
	}

	public List<Edge> kruskals() {
		List<Edge> tree = new ArrayList<>(numOfVertices - 1);
		PriorityQueue<Edge> queue = new PriorityQueue<>(this.edges);

		Subset[] subsets = new Subset[numOfVertices];

		for (int i = 0; i < numOfVertices; i++)
			subsets[i] = new Subset(i, 0);

		addEdgesToMST(tree, queue, subsets);

		return tree;
	}

	public List<Edge> kruskalsWithEdge(Edge edge) {
		List<Edge> tree = new ArrayList<>(numOfVertices - 1);
		PriorityQueue<Edge> queue = new PriorityQueue<>(this.edges);

		Subset[] subsets = new Subset[numOfVertices];

		for (int i = 0; i < numOfVertices; i++)
			subsets[i] = new Subset(i, 0);

		addEdgeToMST(tree, subsets, edge);
		addEdgesToMST(tree, queue, subsets);

		return tree;

	}

	private void addEdgesToMST(List<Edge> tree, PriorityQueue<Edge> queue, Subset[] subsets) {
		while (!queue.isEmpty()) {
			Edge currentEdge = queue.poll();

			addEdgeToMST(tree, subsets, currentEdge);
		}
	}

	private void addEdgeToMST(List<Edge> tree, Subset[] subsets, Edge currentEdge) {
		int x = Subset.findRoot(subsets, currentEdge.getSource());
		int y = Subset.findRoot(subsets, currentEdge.getTarget());

		if (x != y) {
			Subset.union(subsets, x, y);
			tree.add(currentEdge);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		graph.forEach((v, edges) -> sb.append(v).append(": ").append(edges).append("\n"));
		return sb.toString();
	}
}
