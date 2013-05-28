package controllers;

import graph.Vertex;

/**
 * Created with IntelliJ IDEA.
 * User: Vinokurov
 * Date: 28.05.13
 * Time: 21:01
 * To change this template use File | Settings | File Templates.
 */
public class GraphLayoutElement {
    int x=-1;
    int y=-1;
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
