package controllers;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static convertion.CollectionsConverter.toJavaList;


public class GraphLayoutManager {
    private static GraphLayoutManager ourInstance = new GraphLayoutManager();
    private Graph graph;
    private Map<Vertex, GraphLayoutElement> vertexRelation;

    private GraphLayoutManager() {
        graph = GraphController.getInstance().getGraph();
        vertexRelation = new HashMap<>(totalElements());
    }

    public static synchronized GraphLayoutManager getInstance() {
        return ourInstance;
    }

    public final synchronized int totalElements() {
        return graph.vertexes().length();
    }

    public synchronized int getXCellsCount() {
        graph.generateCheckpoints();
        return graph.checkpointLines().length();
    }

    public synchronized int getYCellsCount() {
        graph.generateCheckpoints();
        return maxLength(toJavaList(graph.checkpointLines()));
    }

    public synchronized GraphLayoutElement[] getLayoutElements() {
        prepareLayoutElements();
        return vertexRelation.values().toArray(new GraphLayoutElement[0]);
    }

    private int maxLength(List<Graph.CheckpointLine> lines) {
        int max = -1;
        for (Graph.CheckpointLine line : lines) {
            if (line.checkpoints().length() > max) max = line.checkpoints().length();
        }
        return max;
    }

    private void prepareLayoutElements(){
        mapVertexesToLayoutElements();
        setElementsPositions();
    }

    private void mapVertexesToLayoutElements() {
        Map<Vertex,GraphLayoutElement> temp=new HashMap<Vertex, GraphLayoutElement>();

        GraphLayoutElement element;
        List<Vertex> vertexes = toJavaList(graph.vertexes());

        for (Vertex vertex : vertexes) {
            element = new GraphLayoutElement(vertex);
            temp.put(vertex, element);
        }

        if(!temp.equals(vertexRelation))vertexRelation=temp;
    }

    private void setElementsPositions() {
        int maxElementsInLine = maxLength(toJavaList(graph.checkpointLines()));
        int elementsInLine;
        int indexInLine;
        int lineIndex;
        Graph.CheckpointLine currentLine;
        Vertex vertex;
        for (GraphLayoutElement element : vertexRelation.values()) {
            vertex = element.vertex;
            currentLine = lineContainsVertex(vertex);
            elementsInLine = currentLine.checkpoints().length();
            indexInLine = indexInLine(vertex, currentLine);
            lineIndex = lineIndex(currentLine);
            element.setX(getXCellsCount()-lineIndex-1);
            element.setY(maxElementsInLine * indexInLine / elementsInLine);
        }
    }

    private Graph.CheckpointLine lineContainsVertex(Vertex vertex) {
        List<Graph.CheckpointLine> lines = toJavaList(graph.checkpointLines());

        for (Graph.CheckpointLine line : lines) {
            if (line.checkpoints().contains(vertex)) return line;
        }

        throw new IllegalStateException("Vertex in no checkpointLine");
    }

    private int indexInLine(Vertex vertex, Graph.CheckpointLine line) {
        int result = 0;
        List<Vertex> checkpoints = toJavaList(line.checkpoints());

        for (Vertex v : checkpoints) {
            if (vertex.equals(v)) return result;
            result++;
        }

        throw new IllegalStateException("Current checkpoint line does not contain the vertex");
    }

    private int lineIndex(Graph.CheckpointLine line) {
        int result = 0;
        List<Graph.CheckpointLine> lines = toJavaList(graph.checkpointLines());

        for (Graph.CheckpointLine currentLine : lines) {
            if (line.equals(currentLine)) return result;
            result++;
        }
        throw new IllegalStateException("Graph does not contain the checkpoint line");
    }

    public synchronized GraphLayoutElementConnector[] getLayoutElementConnectors() {
        prepareLayoutElements();
        List<Edge> edges = toJavaList(graph.edges());
        GraphLayoutElementConnector[] connectors = new GraphLayoutElementConnector[edges.size()];

        GraphLayoutElement begin;
        GraphLayoutElement end;
        String name;
        Edge current;

        for (int i = 0; i < edges.size(); i++) {
            current = edges.get(i);
            begin = vertexRelation.get(current.begin());
            end = vertexRelation.get(current.end());
            name = current.weight().toString();
            connectors[i] = new GraphLayoutElementConnector(begin, end, name);
        }

        return connectors;
    }

}
