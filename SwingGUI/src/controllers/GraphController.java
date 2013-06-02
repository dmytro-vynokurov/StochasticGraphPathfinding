package controllers;

import convertion.CollectionsConverter;
import graph.Edge;
import graph.Graph;
import graph.Vertex;
import stochastic.NormalDistribution;

public class GraphController {
    private static GraphController instance = new GraphController();
    private Graph graph;

    private GraphController() {
        graph = new Graph();
    }

    public static synchronized GraphController getInstance() {
        return instance;
    }

    synchronized Graph getGraph() {
        return graph;
    }

    public synchronized void addVertex(Vertex vertex) {
        graph.addVertex(vertex);
    }

    public synchronized void removeVertex(Vertex vertex) {
        System.out.println("Received vertex to remove:\t" + vertex);
        graph.removeVertex(vertex);
    }

    public synchronized java.util.List<Vertex> getVertexes() {
        return CollectionsConverter.toJavaList(graph.vertexes());
    }

    public synchronized void addEdge(Vertex begin, Vertex end, NormalDistribution weight) {
        Edge.apply(begin, end, weight);
    }

    public synchronized void addEdge(Vertex begin, Vertex end, Double expectation, Double variance) {
        Edge.apply(begin, end, expectation, variance);
    }

    public synchronized void addEdge(Vertex begin, Vertex end, Double expectation) {
        Edge.apply(begin, end, expectation, 0);
    }

    public synchronized void removeEdge(Edge edge) {
        graph.removeEdge(edge);
    }

    public synchronized java.util.List<Edge> getEdges() {
        return CollectionsConverter.toJavaList(graph.edges());
    }

    public synchronized Vertex getStart() {
        return graph.start();
    }

    public synchronized void setStart(Vertex vertex) {
        if (vertex != null) graph.setStart(vertex);
    }

    public synchronized Vertex getFinish() {
        return graph.finish();
    }

    public synchronized void setFinish(Vertex vertex) {
        if (vertex != null) graph.setFinish(vertex);
    }

}
