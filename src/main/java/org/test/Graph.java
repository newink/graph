package org.test;

import java.util.*;
import java.util.function.Consumer;

public class Graph<T> {

    private final boolean directed;

    private final Map<Vertex<T>, List<Vertex<T>>> adjacencyList = new HashMap<>();

    private final List<Edge<T>> edges = new ArrayList<>();

    public Graph(boolean directed) {
        this.directed = directed;
    }

    public synchronized void addVertex(T value) {
        adjacencyList.putIfAbsent(new Vertex<>(value), new ArrayList<>());
    }

    public synchronized void addEdge(T value1, T value2) {
        Vertex<T> v1 = new Vertex<>(value1);
        Vertex<T> v2 = new Vertex<>(value2);
        adjacencyList.get(v1).add(v2);
        edges.add(new Edge<>(v1, v2));
        if (!directed) {
            adjacencyList.get(v2).add(v1);
            edges.add(new Edge<>(v2, v1));
        }
    }

    public synchronized List<Edge<T>> getPath(T val1, T val2) {
        Vertex<T> v1 = new Vertex<>(val1);
        Vertex<T> v2 = new Vertex<>(val2);

        if (!adjacencyList.containsKey(v1) || !adjacencyList.containsKey(v2)) {
            throw new IllegalArgumentException("One of the vertices is absent");
        }

        Map<Vertex<T>, Vertex<T>> parents = new HashMap<>();
        boolean found = false;

        Set<Vertex<T>> visited = new LinkedHashSet<>();
        Stack<Vertex<T>> stack = new Stack<>();
        stack.push(v1);
        while (!stack.isEmpty()) {
            Vertex<T> vertex = stack.pop();
            if (vertex.equals(v2)) {
                found = true;
                break;
            }
            if (!visited.contains(vertex)) {
                visited.add(vertex);
                for (Vertex<T> v : adjacencyList.get(vertex)) {
                    stack.push(v);
                    parents.put(v, vertex);
                }
            }
        }
        if (!found) {
            throw new IllegalArgumentException("Path can't be found");
        }
        List<Edge<T>> path = new ArrayList<>();
        Vertex<T> current = v2;
        while (!current.equals(v1)) {
            Vertex<T> parent = parents.get(current);
            path.add(new Edge<>(parent, current));
            current = parent;
        }
        Collections.reverse(path);
        return path;
    }

    public synchronized void apply(Consumer<Vertex<T>> func) {
        if (adjacencyList.isEmpty()) {
            throw new IllegalArgumentException("Graph is empty");
        }

        Vertex<T> root = new ArrayList<>(adjacencyList.keySet()).get(0);
        Set<Vertex<T>> visited = new LinkedHashSet<>();
        Stack<Vertex<T>> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Vertex<T> vertex = stack.pop();
            if (!visited.contains(vertex)) {
                func.accept(vertex);
                visited.add(vertex);
                for (Vertex<T> v : adjacencyList.get(vertex)) {
                    stack.push(v);
                }
            }
        }
    }
}
