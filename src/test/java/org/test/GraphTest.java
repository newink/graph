package org.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GraphTest {


    public Graph<String> directedGraph() {
        Graph<String> graph = new Graph<>(true);
        addVertices(graph);
        return graph;
    }

    public Graph<String> undirectedGraph() {
        Graph<String> graph = new Graph<>(false);
        addVertices(graph);
        return graph;
    }

    private void addVertices(Graph<String> graph) {
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.addVertex("v4");
        graph.addVertex("v5");
        graph.addVertex("v6");

        graph.addEdge("v1", "v2");
        graph.addEdge("v2", "v3");
        graph.addEdge("v3", "v4");
        graph.addEdge("v4", "v5");
        graph.addEdge("v5", "v6");
        graph.addEdge("v4", "v1");
        graph.addEdge("v1", "v5");
    }

    @Test
    public void testPathFound() {
        Graph<String> graph = directedGraph();
        List<Edge<String>> path = graph.getPath("v1", "v6");

        assertEquals(path, new ArrayList<>() {{
            add(new Edge<>("v1", "v5"));
            add(new Edge<>("v5", "v6"));
        }});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPathToUnreachableNotFound() {
        Graph<String> graph = directedGraph();
        String nodeName = "unreachable";

        graph.addVertex(nodeName);
        graph.getPath("v1", nodeName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReversePathIsNotFoundInDirected() {
        Graph<String> graph = directedGraph();
        graph.getPath("v6", "v1");
    }

    @Test
    public void testReversePathIsFoundInUndirected() {
        Graph<String> graph = undirectedGraph();
        List<Edge<String>> path = graph.getPath("v6", "v1");
        assertEquals(new ArrayList<>() {{
            add(new Edge<>("v6", "v5"));
            add(new Edge<>("v5", "v1"));
        }}, path);
    }

    @Test
    public void testFunctionIsApplied() {
        Graph<String> graph = directedGraph();
        List<String> names = new ArrayList<>();
        graph.apply(stringVertex -> names.add(stringVertex.getValue()));

        assertEquals(6, names.size());
    }
}
