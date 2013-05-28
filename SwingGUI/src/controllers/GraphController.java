package controllers;

import graph.Edge;
import graph.Graph;
import graph.Vertex;
import scala.collection.immutable.List;
import stochastic.NormalDistribution;

/**
 * Created with IntelliJ IDEA.
 * User: Vinokurov
 * Date: 27.05.13
 * Time: 11:05
 * To change this template use File | Settings | File Templates.
 */
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
        graph.removeVertex(vertex);
    }

    public synchronized List<Vertex> getVertexes() {
        return graph.vertexes();
    }

    public synchronized void addEdge(Vertex begin,Vertex end, NormalDistribution weight){
        Edge.apply(begin,end,weight);
    }

    public synchronized void addEdge(Vertex begin,Vertex end, Double expectation,Double variance){
        Edge.apply(begin,end,expectation,variance);
    }

    public synchronized void addEdge(Vertex begin,Vertex end, Double expectation){
        Edge.apply(begin,end,expectation,0);
    }

    public synchronized void removeEdge(Edge edge){
        graph.removeEdge(edge);
    }

    public synchronized List<Edge> getEdges(){
        return graph.edges();
    }

    public synchronized void setStart(Vertex vertex){
        graph.setStart(vertex);
    }

    public synchronized Vertex getStart(){
        return graph.start();
    }

    public synchronized void setFinish(Vertex vertex){
        graph.setFinish(vertex);
    }

    public synchronized Vertex getFinish(){
        return graph.finish();
    }
}
