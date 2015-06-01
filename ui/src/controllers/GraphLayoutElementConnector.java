package controllers;

public class GraphLayoutElementConnector {
    private GraphLayoutElement begin;
    private GraphLayoutElement end;
    private String name;

    public GraphLayoutElementConnector(GraphLayoutElement begin, GraphLayoutElement end, String name) {
        this.begin = begin;
        this.end = end;
        this.name = name;
    }

    public GraphLayoutElement getBegin() {
        return begin;
    }

    public GraphLayoutElement getEnd() {
        return end;
    }

    public String getName() {
        return name;
    }
}
