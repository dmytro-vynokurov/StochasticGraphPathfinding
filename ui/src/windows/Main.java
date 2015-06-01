package windows;

import controllers.GraphController;
import elements.GraphPanel;
import eventhandling.GraphChangedEvent;
import eventhandling.GraphChangedListener;
import graph.Edge;
import graph.Vertex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static elements.ComboBoxFiller.addEdgesToComboBox;
import static elements.ComboBoxFiller.addVertexesToComboBox;


public class Main {
    private JPanel mainPanel;
    private JButton addVertexButton;
    private JComboBox comboBoxRemoveVertex;
    private JButton removeVertexButton;
    private JButton addEdgeButton;
    private JComboBox comboBoxRemoveEdge;
    private JButton removeEdgeButton;
    private JComboBox comboBoxSetStart;
    private JButton setStartButton;
    private JComboBox comboBoxSetFinish;
    private JButton setFinishButton;
    private JPanel graphPanel;
    private JButton findPathButton;
    private JButton setComparisonButton;
    private JTextArea resultsField;
    private JButton sampleGraphButton;
    private GraphController controller = GraphController.getInstance();
    private GraphChangedListener updateComboBoxesListener = event -> fillComboBoxes();
    private GraphChangedListener graphChangedListener = event -> repaintAndResize();
    private ActionListener graphChangedActionListener = e -> repaintAndResize();


    public Main() {
        removeEdgeButton.addActionListener(graphChangedActionListener);
        removeEdgeButton.addActionListener(e -> controller.removeEdge((Edge) comboBoxRemoveEdge.getSelectedItem()));
        removeVertexButton.addActionListener(graphChangedActionListener);
        removeVertexButton.addActionListener(e -> controller.removeVertex((Vertex) comboBoxRemoveVertex.getSelectedItem()));
        setStartButton.addActionListener(graphChangedActionListener);
        setStartButton.addActionListener(e -> controller.setStart((Vertex) comboBoxSetStart.getSelectedItem()));
        setFinishButton.addActionListener(graphChangedActionListener);
        setFinishButton.addActionListener(e -> controller.setFinish((Vertex) comboBoxSetFinish.getSelectedItem()));
        addVertexButton.addActionListener(e -> {
            InputVertex dialog = new InputVertex();
            dialog.pack();
            placeInTheMiddleOfScreen(dialog);
            dialog.addGraphChangedListener(graphChangedListener);
            dialog.addGraphChangedListener(updateComboBoxesListener);
            dialog.setVisible(true);
        });
        addEdgeButton.addActionListener(e -> {
            InputEdge dialog = new InputEdge();
            dialog.pack();
            placeInTheMiddleOfScreen(dialog);
            dialog.addGraphChangedListener(graphChangedListener);
            dialog.addGraphChangedListener(updateComboBoxesListener);
            dialog.setVisible(true);
        }
        );
        setComparisonButton.addActionListener(e -> {
            ChooseComparison dialog = new ChooseComparison();
            dialog.pack();
            placeInTheMiddleOfScreen(dialog);
            dialog.setVisible(true);
        });
        findPathButton.addActionListener(e -> {
            resultsField.setText("");
            if (controller.existsConnection(controller.getStart(), controller.getFinish())) {
                for (Vertex vertex : controller.getBestPath()) {
                    resultsField.append(vertex.toString());
                    resultsField.append("\n");
                }

                resultsField.append(
                        "====================\n" +
                        "Total length : " +
                        controller.getBestPathLength()
                );
            } else {
                JOptionPane.showMessageDialog(null, "There is no path between start and finish");
            }
        });
        sampleGraphButton.addActionListener(e -> {
            controller.clearGraph();

            Vertex v1 = new Vertex("1");
            Vertex v2 = new Vertex("2");
            Vertex v3 = new Vertex("3");
            Vertex v4 = new Vertex("4");
            Vertex v5 = new Vertex("5");
            Vertex v6 = new Vertex("6");
            Vertex v7 = new Vertex("7");
            Vertex v8 = new Vertex("8");
            Vertex v9 = new Vertex("9");
            Vertex v10 = new Vertex("10");

            controller.addVertex(v1);
            controller.addVertex(v2);
            controller.addVertex(v3);
            controller.addVertex(v4);
            controller.addVertex(v5);
            controller.addVertex(v6);
            controller.addVertex(v7);
            controller.addVertex(v8);
            controller.addVertex(v9);
            controller.addVertex(v10);

            controller.setStart(v1);
            controller.setFinish(v10);

            controller.addEdge(v1, v2, 5., 20.);
            controller.addEdge(v1, v3, 6., 3.);
            controller.addEdge(v1, v4, 5., 1.);
            controller.addEdge(v2, v5, 6., 25.);
            controller.addEdge(v3, v5, 6., 10.);
            controller.addEdge(v4, v6,11., 1.);
            controller.addEdge(v5, v7, 8., 10.);
            controller.addEdge(v5, v8, 7., 8.);
            controller.addEdge(v6, v8, 10., 12.);
            controller.addEdge(v6, v9,3., 1.);
            controller.addEdge(v7, v10, 3., 6.);
            controller.addEdge(v8, v10, 3., 6.);
            controller.addEdge(v9, v10, 4., 2.);

            fillComboBoxes();
            repaintAndResize();
        });
    }

    public static void main(String[] args) {
        switchToNimbusLookAndFeel();

        JFrame frame = new JFrame("Stochastic graph pathfinding");
        frame.setContentPane(new Main().mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        placeInTheMiddleOfScreen(frame);
        frame.setVisible(true);
    }

    private static void switchToNimbusLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Cannot switch to Nimbus");
        }
    }

    private static void placeInTheMiddleOfScreen(Component component) {
        Dimension resolution = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = component.getPreferredSize();
        int x = (resolution.width - frameSize.width) / 2;
        int y = (resolution.height - frameSize.height) / 2;
        component.setBounds(x, y, frameSize.width, frameSize.height);
    }

    private void createUIComponents() {
        graphPanel = new GraphPanel();
    }

    private void fillComboBoxes() {
        addVertexesToComboBox(comboBoxRemoveVertex);
        addEdgesToComboBox(comboBoxRemoveEdge);
        addVertexesToComboBox(comboBoxSetStart);
        addVertexesToComboBox(comboBoxSetFinish);
        comboBoxSetStart.setSelectedItem(controller.getStart());
        comboBoxSetFinish.setSelectedItem(controller.getFinish());
    }

    private void repaintAndResize() {
        graphPanel.repaint();
        Dimension mainPanelSize = mainPanel.getSize();
        Dimension graphPanelSize = graphPanel.getSize();
        Dimension wideButtonSize = addVertexButton.getSize();
        mainPanel.setSize(new Dimension(graphPanelSize.width + wideButtonSize.width+200, mainPanelSize.height));
    }

}
