package com.daliborstakic.graphs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class Digraph {
	private int numOfVertices;
	private int numOfEdges;
	private int[] indegree;
	private int[] outdegree;
	private List<Set<Integer>> graph;
	private List<Set<Integer>> reverse;
	private List<Set<Integer>> transitiveClosure = null;
	private Graph undirectedGraph;

	public Digraph(String filename) {
		this.graph = new ArrayList<>();
		this.reverse = new ArrayList<>();
		this.undirectedGraph = new Graph(filename);

		try (Scanner s = new Scanner(new File(filename))) {
			initializeGraph(s);
			loadEdges(s);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public int getNumOfVertices() {
		return numOfVertices;
	}

	public int getNumOfEdges() {
		return numOfEdges;
	}

	private void initializeGraph(Scanner s) {
		this.numOfVertices = s.nextInt();

		for (int i = 0; i < numOfVertices; i++) {
			graph.add(new HashSet<Integer>());
			reverse.add(new HashSet<Integer>());
		}

		this.numOfEdges = s.nextInt();

		this.indegree = new int[numOfVertices];
		this.outdegree = new int[numOfVertices];
	}

	private void loadEdges(Scanner s) {
		while (s.hasNextLine()) {
			String[] tokens = s.nextLine().trim().split(" ");
			if (tokens.length < 2)
				continue;

			int vertex1 = Integer.parseInt(tokens[0]);
			int vertex2 = Integer.parseInt(tokens[1]);

			this.addEdge(vertex1, vertex2);

			this.outdegree[vertex1]++;
			this.indegree[vertex2]++;
		}
	}

	public void addEdge(int vertex1, int vertex2) {
		this.graph.get(vertex1).add(vertex2);
		this.reverse.get(vertex2).add(vertex1);
	}

	public Set<Integer> adjVertices(int vertex) {
		return this.graph.get(vertex);
	}

	public Set<Integer> adjVerticesReverse(int vertex) {
		return this.reverse.get(vertex);
	}

	public List<Set<Integer>> findAllWeakComponents() {
		boolean[] visited = new boolean[numOfVertices];
		List<Set<Integer>> components = new LinkedList<>();

		for (int i = 0; i < numOfVertices; i++)
			if (!visited[i]) {
				Set<Integer> component = new HashSet<>();
				findAllWeakComponents(i, visited, component);
				components.add(component);
			}

		return components;
	}

	private void findAllWeakComponents(int vertex, boolean[] visited, Set<Integer> component) {
		visited[vertex] = true;
		component.add(vertex);

		for (int adj : undirectedGraph.adjVertices(vertex)) {
			if (!visited[adj]) {
				component.add(adj);
				findAllWeakComponents(adj, visited, component);
			}
		}
	}

	public Deque<Integer> findCycle() {
		boolean[] visited = new boolean[numOfVertices];
		boolean[] recStack = new boolean[numOfVertices];

		for (int i = 0; i < numOfVertices; i++) {
			if (!visited[i]) {
				Deque<Integer> cycle = new LinkedList<>();
				if (findCycle(i, visited, recStack, cycle)) {
					while (cycle.peekFirst() != cycle.peekLast()) {
						cycle.pollFirst();
					}

					return cycle;
				}
			}
		}

		return new LinkedList<>();
	}

	public List<Integer> topologicalSort() {
		if (findCycle().size() > 0)
			return new ArrayList<Integer>();

		List<Integer> topologicalSort = new ArrayList<>();
		boolean[] visited = new boolean[numOfVertices];
		Stack<Integer> stack = new Stack<>();

		for (int i = 0; i < numOfVertices; i++) {
			if (!visited[i])
				dfs(i, visited, stack);
		}

		while (!stack.isEmpty())
			topologicalSort.add(stack.pop());

		return topologicalSort;
	}

	private void dfs(int vertex, boolean[] visited, Stack<Integer> stack) {
		visited[vertex] = true;

		for (int adj : adjVertices(vertex)) {
			if (!visited[adj])
				dfs(adj, visited, stack);
		}

		stack.push(vertex);
	}

	private boolean findCycle(int current, boolean[] visited, boolean[] recStack, Deque<Integer> cycle) {
		visited[current] = true;
		recStack[current] = true;
		cycle.add(current);

		for (int adj : adjVertices(current)) {
			if (!visited[adj]) {
				if (findCycle(adj, visited, recStack, cycle)) {
					return true;
				}
			} else if (recStack[adj]) {
				cycle.add(adj);
				return true;
			}
		}

		cycle.removeLast();
		recStack[current] = false;
		return false;
	}

	public List<Integer> findAllSource() {
		List<Integer> sources = new ArrayList<>();

		for (int i = 0; i < numOfVertices; i++) {
			if (indegree[i] == 0 && outdegree[i] > 0)
				sources.add(i);
		}

		return sources;
	}

	public List<Integer> findAllSinks() {
		List<Integer> sinks = new ArrayList<>();

		for (int i = 0; i < numOfVertices; i++) {
			if (indegree[i] > 0 && outdegree[i] == 0)
				sinks.add(i);
		}

		return sinks;
	}

	public boolean canReachAllFromNode(int vertex) {
		boolean visited[] = new boolean[numOfVertices];
		Queue<Integer> queue = new LinkedList<>();

		queue.add(vertex);
		visited[vertex] = true;

		while (!queue.isEmpty()) {
			int current = queue.poll();

			for (int adj : adjVertices(current))
				if (!visited[adj]) {
					visited[adj] = true;
					queue.add(adj);
				}
		}

		for (int i = 0; i < numOfVertices; i++)
			if (!visited[i])
				return false;

		return true;
	}

	public boolean canReachAllAtleastTwice() {
		int count = 0;

		for (int i = 0; i < numOfVertices; i++) {
			if (canReachAllFromNode(i)) {
				count++;

				if (count > 1)
					return true;
			}
		}

		return false;
	}

	public boolean canReachBack(int vertex) {
		boolean[] visited = new boolean[numOfVertices];
		return canReachBack(vertex, vertex, visited);
	}

	private boolean canReachBack(int start, int current, boolean[] visited) {
		if (visited[current])
			return current == start;

		visited[current] = true;

		for (int adj : adjVertices(current)) {
			if (canReachBack(start, adj, visited))
				return true;
		}

		visited[current] = false;
		return false;
	}

	public List<Set<Integer>> transitiveClosure() {
		if (transitiveClosure == null) {
			transitiveClosure = new ArrayList<>(numOfVertices);

			for (int i = 0; i < numOfVertices; i++) {
				transitiveClosure.add(new HashSet<>());
				transitiveUtil(i, i);
			}
		}

		return transitiveClosure;
	}

	private void transitiveUtil(int start, int current) {
		this.transitiveClosure.get(start).add(current);

		for (int adj : adjVertices(current))
			if (!this.transitiveClosure.get(start).contains(adj))
				transitiveUtil(start, adj);
	}

	public int findMotherVertex() {
		boolean[] visited = new boolean[numOfVertices];
		int lastFinished = 0;

		for (int i = 0; i < numOfVertices; i++)
			if (!visited[i]) {
				dfsUtil(i, visited);
				lastFinished = i;
			}

		Arrays.fill(visited, false);

		dfsUtil(lastFinished, visited);
		for (int i = 0; i < numOfVertices; i++)
			if (!visited[i])
				return -1;

		return lastFinished;
	}

	private void dfsUtil(int current, boolean[] visited) {
		visited[current] = true;

		for (int adj : adjVertices(current)) {
			if (!visited[adj])
				dfsUtil(adj, visited);
		}
	}

	public int canBeReachedFromAll() {
		boolean[] visited = new boolean[numOfVertices];
		int candidate = 0;

		for (int i = 0; i < numOfVertices; i++)
			if (!visited[i]) {
				dfsUtil(i, visited);
				candidate = i;
			}

		Arrays.fill(visited, false);
		dfsUtilReverse(candidate, visited);

		for (int i = 0; i < numOfVertices; i++)
			if (!visited[i])
				return -1;

		return candidate;
	}

	private void dfsUtilReverse(int current, boolean[] visited) {
		visited[current] = true;

		for (int adj : adjVerticesReverse(current)) {
			if (!visited[adj])
				dfsUtilReverse(adj, visited);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Digraph with ").append(numOfVertices).append(" vertices and ").append(numOfEdges)
				.append(" edges.\n");

		for (int i = 0; i < numOfVertices; i++) {
			sb.append(i).append(": ");
			for (int neighbor : graph.get(i)) {
				sb.append(neighbor).append(" ");
			}
			sb.append("\n");
		}

		sb.append("\nDegrees:\n");
		for (int i = 0; i < numOfVertices; i++) {
			sb.append("Vertex ").append(i).append(" -> In-degree: ").append(indegree[i]).append(", Out-degree: ")
					.append(outdegree[i]).append("\n");
		}

		return sb.toString();
	}

}