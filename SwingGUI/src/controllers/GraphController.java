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

    public Graph getGraph() {
        return graph;
    }

    public void addVertex(Vertex vertex) {
        graph.addVertex(vertex);
    }

    public void removeVertex(Vertex vertex) {
        graph.removeVertex(vertex);
    }

    public List<Vertex> getVertexes() {
        return graph.vertexes();
    }

    public void addEdge(Vertex begin,Vertex end, NormalDistribution weight){
        Edge.apply(begin,end,weight);
    }

    public void addEdge(Vertex begin,Vertex end, Double expectation,Double variance){
        Edge.apply(begin,end,expectation,variance);
    }

    public void addEdge(Vertex begin,Vertex end, Double expectation){
        Edge.apply(begin,end,expectation,0);
    }

    public void removeEdge(Edge edge){
        graph.removeEdge(edge);
    }

    public List<Edge> getEdges(){
        return graph.edges();
    }


}
