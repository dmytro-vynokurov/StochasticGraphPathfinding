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

        vertexes.forEach(comboBox::addItem);
    }

    public static void addEdgesToComboBox(JComboBox comboBox) {
        comboBox.removeAllItems();

        GraphController controller = GraphController.getInstance();
        List<Edge> edges = controller.getEdges();

        edges.forEach(comboBox::addItem);
    }

}
