package controllers;

import graph.Edge;
import graph.Graph;
import graph.Vertex;
import scala.collection.immutable.List;

import java.util.HashMap;
import java.util.Map;

public class GraphLayoutManager {
    private static GraphLayoutManager ourInstance = new GraphLayoutManager();
    private Graph graph;
    private Map<Vertex,GraphLayoutElement> vertexRelation;

    private GraphLayoutManager() {
        graph = GraphController.getInstance().getGraph();
        vertexRelation=new HashMap<>(totalElements());
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
        return maxLength(graph.checkpointLines());
    }

    public synchronized GraphLayoutElement[] getLayoutElements() {
        GraphLayoutElement[] elements = layoutElementsFromVertexes();
        setElementsPositions(elements);
        return elements;
    }

    private int maxLength(List<Graph.CheckpointLine> outerLines) {
        int max = -1;
        List<Graph.CheckpointLine> lines = outerLines;
        while (!lines.isEmpty()) {
            if (lines.head().checkpoints().length() > max) max = lines.head().checkpoints().length();
            lines = (List<Graph.CheckpointLine>)lines.tail();
        }
        if (max == -1)
            throw new IllegalStateException("Graph does not contain checkpoint lines or their sizes are wrong");
        return max;
    }

    private GraphLayoutElement[] layoutElementsFromVertexes() {
        mapVertexesToLayoutElements();
        return vertexRelation.values().toArray(new GraphLayoutElement[1]);
    }

    private void mapVertexesToLayoutElements(){
        GraphLayoutElement element;
        Vertex vertex;
        List<Vertex> vertexesLeft = graph.vertexes();

        for (int i = 0; i < totalElements(); i++) {
            vertex=vertexesLeft.head();
            element = new GraphLayoutElement(vertex);
            vertexRelation.put(vertex,element);

            vertexesLeft = (List<Vertex>)vertexesLeft.tail();
        }
    }

    private void setElementsPositions(GraphLayoutElement[] elements) {
        int maxElementsInLine = maxLength(graph.checkpointLines());
        int elementsInLine;
        int indexInLine;
        int lineIndex;
        Graph.CheckpointLine currentLine;
        Vertex vertex;
        for (GraphLayoutElement element : elements) {
            vertex = element.vertex;
            currentLine = lineContainsVertex(vertex);
            elementsInLine = currentLine.checkpoints().length();
            indexInLine = indexInLine(vertex, currentLine);
            lineIndex = lineIndex(currentLine);
            element.setX(lineIndex);
            element.setY(maxElementsInLine * indexInLine / elementsInLine);
        }
    }

    private Graph.CheckpointLine lineContainsVertex(Vertex vertex) {
        List<Graph.CheckpointLine> lines = graph.checkpointLines();
        while (!lines.isEmpty()) {
            if (lines.head().checkpoints().contains(vertex)) return lines.head();
            else lines = (List<Graph.CheckpointLine>)lines.tail();
        }
        return null;
    }

    private int indexInLine(Vertex vertex, Graph.CheckpointLine line) {
        int result = 0;
        List<Vertex> checkpointsLeft = line.checkpoints();
        while (!checkpointsLeft.isEmpty()) {
            if (vertex.equals(checkpointsLeft.head())) return result;
            result++;
            checkpointsLeft = (List<Vertex>)checkpointsLeft.tail();
        }
        throw new IllegalStateException("Current checkpoint line does not contain the vertex");
    }

    private int lineIndex(Graph.CheckpointLine line) {
        int result = 0;
        List<Graph.CheckpointLine> linesLeft = graph.checkpointLines();
        while (!linesLeft.isEmpty()) {
            if (line.equals(linesLeft.head())) return result;
            result++;
            linesLeft = (List<Graph.CheckpointLine>)linesLeft.tail();
        }
        throw new IllegalStateException("Graph does not contain the checkpoint line");
    }

    public synchronized GraphLayoutElementConnector[] getLayoutElementConnectors(){
        mapVertexesToLayoutElements();
        List<Edge> edgesLeft=graph.edges();
        GraphLayoutElementConnector[] connectors=new GraphLayoutElementConnector[edgesLeft.length()];

        GraphLayoutElement begin;
        GraphLayoutElement end;
        String name;
        Edge current;

        int index=0;
        while(!edgesLeft.isEmpty()){
            current=edgesLeft.head();
            begin=vertexRelation.get(current.begin());
            end=vertexRelation.get(current.end());
            name=current.weight().toString();
            connectors[index]=new GraphLayoutElementConnector(begin,end,name);

            index++;
            edgesLeft=(List<Edge>)edgesLeft.tail();
        }

        return connectors;
    }

}
