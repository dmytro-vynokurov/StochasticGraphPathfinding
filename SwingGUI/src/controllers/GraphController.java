package controllers;

import convertion.CollectionsConverter;

import graph.Edge;
import graph.Graph;
import graph.Vertex;
import stochastic.NormalDistribution;

import java.util.List;

import static convertion.CollectionsConverter.*;

public class GraphController {
    private static GraphController instance = new GraphController();
    private Graph graph;

    private GraphController() {
        graph = new Graph();
    }

    public static synchronized GraphController getInstance() {
        return instance;
    }

    public synchronized Graph getGraph() {
        return graph;
    }

    public synchronized void addVertex(Vertex vertex) {
        graph.addVertex(vertex);
    }

    public synchronized void removeVertex(Vertex vertex) {
        graph.removeVertex(vertex);
    }

    public synchronized List<Vertex> getVertexes() {
        return toJavaList(graph.vertexes());
    }

    public synchronized void addEdge(Vertex begin, Vertex end, Double expectation, Double variance) {
        Edge.apply(begin, end, expectation, variance);
    }

    public synchronized void removeEdge(Edge edge) {
        graph.removeEdge(edge);
    }

    public synchronized java.util.List<Edge> getEdges() {
        return toJavaList(graph.edges());
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

    public synchronized boolean existsConnection(Vertex first,Vertex second){
        System.out.println("Checking connection between");
        System.out.println(first);
        System.out.println(second);
        System.out.println();
        return first.isConnectedTo(second);
    }

    public synchronized List<Vertex> getBestPath(){
        graph.generateCheckpoints();
        return toJavaList(graph.bestPath());
    }



    

}
