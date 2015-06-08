package controllers;

import graph.Edge;
import graph.Graph;
import graph.Vertex;
import scala.collection.immutable.List$;
import scala.collection.immutable.Nil;
import stochastic.AbstractDistribution;
import stochastic.DistributionComparisons;
import stochastic.NormalDistribution;

import java.util.List;

import static convertion.CollectionsConverter.toJavaList;

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

    public synchronized void clearGraph() {
        List<Edge> edges = getEdges();

        for (Edge edge : edges) {
            edge.begin_$eq(null);
            edge.end_$eq(null);
        }
        graph.start_$eq(null);
        graph.finish_$eq(null);
        graph.checkpointLines_$eq(List$.MODULE$.<Graph.CheckpointLine>empty());
        graph.vertexes_$eq(List$.MODULE$.<Vertex>empty());
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

    public synchronized boolean existsConnection(Vertex first, Vertex second) {
        return first.isConnectedTo(second);
    }

    public synchronized List<Vertex> getBestPath() {
        graph.generateCheckpoints();
        return toJavaList(graph.bestPath());
    }

    public synchronized NormalDistribution getBestPathLength() {
        graph.generateCheckpoints();
        return graph.bestPathLength(graph.bestPath());
    }

    public synchronized void setComparisonTypeDetermined() {
        DistributionComparisons.setComparator(DistributionComparisons.determinedComparator());
    }

    public synchronized void setComparisonTypeFixedDelay(int delayPercentage) {
        DistributionComparisons.setComparator(DistributionComparisons.percentComparator(delayPercentage));
    }

    public synchronized void setComparisonTypeProfitFunction(Double changePoint) {
        DistributionComparisons.setComparator(DistributionComparisons.stepProfitComparator(changePoint));
    }

}
