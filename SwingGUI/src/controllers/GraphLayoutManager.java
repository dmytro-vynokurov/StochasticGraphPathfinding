package controllers;

import graph.Graph;
import graph.Vertex;
import scala.collection.immutable.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vinokurov
 * Date: 28.05.13
 * Time: 20:59
 * To change this template use File | Settings | File Templates.
 */
public class GraphLayoutManager {
    private static GraphLayoutManager ourInstance = new GraphLayoutManager();
    private Graph graph;

    private GraphLayoutManager() {
        graph = GraphController.getInstance().getGraph();
    }

    public static synchronized GraphLayoutManager getInstance() {
        return ourInstance;
    }

    public synchronized int totalElements() {
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

    private int maxLength(List<Graph.CheckpointLine> outerLines) {
        int max = -1;
        List<Graph.CheckpointLine> lines = outerLines;
        while (!lines.isEmpty()) {
            if (lines.head().checkpoints().length() > max) max = lines.head().checkpoints().length();
            lines = lines.tail();
        }
        return max;
    }

    public GraphLayoutElement[] getLayoutElements(){
        GraphLayoutElement[] elements = layoutElementsFromVertexes();

        int maxElementsInLine=maxLength(graph.checkpointLines());
        int elementsInLine;
        int indexInLine;
        Graph.CheckpointLine currentLine;
        Vertex vertex;
        for (int i = 0; i < elements.length; i++) {
            vertex=elements[i].vertex;
            currentLine=lineContainsVertex(vertex);
            elementsInLine=currentLine.checkpoints().length();
            indexInLine=indexInLine(vertex,currentLine);
        }

        return elements;
    }

    private GraphLayoutElement[] layoutElementsFromVertexes() {
        GraphLayoutElement[] elements=new GraphLayoutElement[totalElements()];
        List<Vertex> vertexesLeft=graph.vertexes();
        for (int i = 0; i < elements.length; i++) {
            elements[i]=new GraphLayoutElement(vertexesLeft.head());
            vertexesLeft=vertexesLeft.tail();
        }
        return elements;
    }

    private Graph.CheckpointLine lineContainsVertex(Vertex vertex){
        List<Graph.CheckpointLine> lines=graph.checkpointLines();
        while(!lines.isEmpty()){
            if(lines.head().checkpoints().contains(vertex))return lines.head();
            else lines=lines.tail();
        }
        return null;
    }

    private int indexInLine(Vertex vertex,Graph.CheckpointLine line){
        int result=0;
        List<Vertex> checkpointsLeft=line.checkpoints();
        while(!checkpointsLeft.isEmpty()){
            if(vertex.equals(checkpointsLeft.head()))return result;
            result++;
            checkpointsLeft=checkpointsLeft.tail();
        }
        throw new IllegalStateException("Current checkpoint line does not contain the vertex");
    }


}
