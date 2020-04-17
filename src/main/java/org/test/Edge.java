package org.test;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Edge<VT> {

    private Vertex<VT> vertex1;

    private Vertex<VT> vertex2;

    public Edge(VT val1, VT val2) {
        vertex1 = new Vertex<>(val1);
        vertex2 = new Vertex<>(val2);
    }
}
