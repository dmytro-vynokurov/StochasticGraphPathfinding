package elements;

import controllers.GraphController;
import graph.Edge;
import graph.Vertex;

import javax.swing.*;
import java.util.List;

public class ComboBoxFiller {
    public static void addVertexesToComboBox(JComboBox comboBox) {
        comboBox.removeAllItems();

        GraphController controller = GraphController.getInstance();
        List<Vertex> vertexes = controller.getVertexes();

        for (Vertex vertex : vertexes) {
            comboBox.addItem(vertex);
        }
    }

    public static void addEdgesToComboBox(JComboBox comboBox) {
        comboBox.removeAllItems();

        GraphController controller = GraphController.getInstance();
        List<Edge> edges = controller.getEdges();

        for (Edge edge : edges) {
            comboBox.addItem(edge);
        }
    }

}
