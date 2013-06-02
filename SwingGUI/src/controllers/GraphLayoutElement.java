package controllers;

import graph.Vertex;


public class GraphLayoutElement {
    private int x = -1;
    private int y = -1;
    Vertex vertex;

    public GraphLayoutElement(Vertex vertex) {
        this.vertex = vertex;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
