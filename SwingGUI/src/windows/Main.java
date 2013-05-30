package windows;

import elements.GraphPanel;

import javax.swing.*;


public class Main {
    private JPanel mainPanel;
    private JButton addVertexButton;
    private JComboBox comboBoxRemoveVertex;
    private JButton removeVertexButton;
    private JButton addEdgeButton;
    private JComboBox comboBoxRemoveEdge;
    private JButton removeEdgeButton;
    private JButton setStartButton;
    private JComboBox comboBoxSetStart;
    private JComboBox comboBoxSetFinish;
    private JButton setFinishButton;
    private JPanel graphPanel;
    private JButton findPathButton;
    private JButton setComparisonButton;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Stochastic graph pathfinding");
        frame.setContentPane(new Main().mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        graphPanel = new GraphPanel();
    }
}
