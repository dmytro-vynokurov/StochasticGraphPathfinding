package windows;

import controllers.GraphController;
import elements.GraphPanel;
import eventhandling.GraphChangedEvent;
import eventhandling.GraphChangedListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


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
    private GraphController controller = GraphController.getInstance();
    private GraphChangedListener repaintGraphListener = new GraphChangedListener() {
        @Override
        public void graphChanged(GraphChangedEvent event) {
            graphPanel.repaint();
        }
    };


    public Main() {
        removeEdgeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                controller.removeEdgeByIndex(comboBoxRemoveEdge.getSelectedIndex());
            }
        });
        removeVertexButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                controller.removeVertexByIndex(comboBoxRemoveVertex.getSelectedIndex());
            }
        });
        setStartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                controller.setStart(controller.getVertexByIndex(comboBoxSetStart.getSelectedIndex()));
            }
        });
        setFinishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                controller.setFinish(controller.getVertexByIndex(comboBoxSetFinish.getSelectedIndex()));
            }
        });
        addVertexButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InputVertex dialog = new InputVertex();
                dialog.pack();
                dialog.addListener(repaintGraphListener);
                dialog.setVisible(true);
            }
        });
        addEdgeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InputEdge dialog = new InputEdge();
                dialog.pack();
                dialog.addListener(repaintGraphListener);
                dialog.setVisible(true);
            }
        }

        );
    }

    private void createUIComponents() {
        graphPanel = new GraphPanel();
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Stochastic graph pathfinding");
        frame.setContentPane(new Main().mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

}
