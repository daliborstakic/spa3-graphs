package com.daliborstakic.graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Graph {
	private final int numOfVertices;
	private int numOfEdges;
	private final List<Set<Integer>> graph;

	public Graph(String filename) {
		graph = new ArrayList<>();

		try (Scanner scanner = new Scanner(new File(filename))) {
			numOfVertices = scanner.nextInt();

			for (int i = 0; i < numOfVertices; i++)
				graph.add(new HashSet<>());

			numOfEdges = scanner.nextInt();
			loadEdges(scanner);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File not found: " + filename, e);
		}
	}

	public int getNumOfVertices() {
		return numOfVertices;
	}

	public int getNumOfEdges() {
		return numOfEdges;
	}

	private void loadEdges(Scanner scanner) {
		while (scanner.hasNextInt()) {
			int v1 = scanner.nextInt();
			int v2 = scanner.nextInt();
			addEdge(v1, v2);
		}
	}

	public void addEdge(int v1, int v2) {
		if (!graph.get(v1).contains(v2)) {
			graph.get(v1).add(v2);
			graph.get(v2).add(v1);
			numOfEdges++;
		}
	}

	public Set<Integer> adjVertices(int v) {
		return graph.get(v);
	}

	private void dfs(int v, boolean[] visited, Set<Integer> component) {
		visited[v] = true;
		component.add(v);

		for (int adj : adjVertices(v)) {
			if (!visited[adj])
				dfs(adj, visited, component);
		}
	}

	public Set<Integer> findComponent(int v) {
		Set<Integer> component = new HashSet<>();
		dfs(v, new boolean[numOfVertices], component);
		return component;
	}

	public List<Set<Integer>> findAllComponents() {
		List<Set<Integer>> components = new ArrayList<>();
		boolean[] visited = new boolean[numOfVertices];

		for (int i = 0; i < numOfVertices; i++) {
			if (!visited[i]) {
				Set<Integer> component = new TreeSet<>();
				dfs(i, visited, component);
				components.add(component);
			}
		}

		return components;
	}

	public boolean existsPath(int source, int target) {
		return dfsPath(source, target, new boolean[numOfVertices]);
	}

	private boolean dfsPath(int current, int target, boolean[] visited) {
		if (current == target)
			return true;

		visited[current] = true;

		for (int adj : adjVertices(current)) {
			if (!visited[adj] && dfsPath(adj, target, visited))
				return true;
		}

		return false;
	}

	public Deque<Integer> findPath(int source, int target) {
		Deque<Integer> path = new LinkedList<>();

		if (findPathDFS(source, target, new boolean[numOfVertices], path))
			path.addFirst(source);

		return path;
	}

	private boolean findPathDFS(int current, int target, boolean[] visited, Deque<Integer> path) {
		if (current == target)
			return true;

		visited[current] = true;

		for (int adj : adjVertices(current)) {
			if (!visited[adj] && findPathDFS(adj, target, visited, path)) {
				path.addFirst(adj);
				return true;
			}
		}

		return false;
	}

	public double[] distanceFromNode(int vertex) {
		double[] distTo = new double[numOfVertices];
		Arrays.fill(distTo, Double.POSITIVE_INFINITY);
		bfs(vertex, distTo);
		return distTo;
	}

	private void bfs(int vertex, double[] distTo) {
		Queue<Integer> queue = new LinkedList<>();
		distTo[vertex] = 0;
		queue.add(vertex);

		while (!queue.isEmpty()) {
			int current = queue.poll();

			for (int adj : adjVertices(current)) {
				if (distTo[adj] == Double.POSITIVE_INFINITY) {
					distTo[adj] = distTo[current] + 1;
					queue.add(adj);
				}
			}
		}
	}

	public boolean hasCycle() {
		boolean[] visited = new boolean[numOfVertices];

		for (int i = 0; i < numOfVertices; i++) {
			if (!visited[i] && hasCycleDFS(-1, i, visited))
				return true;
		}

		return false;
	}

	private boolean hasCycleDFS(int parent, int v, boolean[] visited) {
		visited[v] = true;

		for (int adj : adjVertices(v)) {
			if (!visited[adj]) {
				if (hasCycleDFS(v, adj, visited))
					return true;
			} else if (adj != parent)
				return true;
		}

		return false;
	}

	public boolean isBipartite() {
		int[] colors = new int[numOfVertices];
		Arrays.fill(colors, -1);

		for (int i = 0; i < numOfVertices; i++) {
			if (colors[i] == -1 && !isBipartiteBFS(i, colors))
				return false;
		}

		return true;
	}

	private boolean isBipartiteBFS(int start, int[] colors) {
		Queue<Integer> queue = new LinkedList<>();
		colors[start] = 0;
		queue.add(start);

		while (!queue.isEmpty()) {
			int v = queue.poll();

			for (int adj : adjVertices(v)) {
				if (colors[adj] == colors[v])
					return false;
				if (colors[adj] == -1) {
					colors[adj] = 1 - colors[v];
					queue.add(adj);
				}
			}
		}

		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Graph with ").append(numOfVertices).append(" vertices and ").append(numOfEdges).append(" edges.\n");

		for (int i = 0; i < graph.size(); i++)
			sb.append(i).append(": ").append(graph.get(i)).append("\n");

		return sb.toString();
	}
}
