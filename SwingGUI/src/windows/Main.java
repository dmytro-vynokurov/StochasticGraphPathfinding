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

    private GraphController controller = GraphController.getInstance();
    private GraphChangedListener updateComboBoxesListener = new GraphChangedListener() {
        @Override
        public void graphChanged(GraphChangedEvent event) {
            fillComboBoxes();
        }
    };
    private GraphChangedListener graphChangedListener = new GraphChangedListener() {
        @Override
        public void graphChanged(GraphChangedEvent event) {
            updateOnGraphChanged();
        }
    };
    private ActionListener graphChangedActionListener =new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateOnGraphChanged();
        }
    };



    public Main() {
        removeEdgeButton.addActionListener(graphChangedActionListener);
        removeEdgeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.removeEdge((Edge) comboBoxRemoveEdge.getSelectedItem());
            }
        });
        removeVertexButton.addActionListener(graphChangedActionListener);
        removeVertexButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.removeVertex((Vertex) comboBoxRemoveVertex.getSelectedItem());
            }
        });
        setStartButton.addActionListener(graphChangedActionListener);
        setStartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.setStart((Vertex) comboBoxSetStart.getSelectedItem());
            }
        });
        setFinishButton.addActionListener(graphChangedActionListener);
        setFinishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.setFinish((Vertex) comboBoxSetFinish.getSelectedItem());
            }
        });
        addVertexButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InputVertex dialog = new InputVertex();
                dialog.pack();
                placeInTheMiddleOfScreen(dialog);
                dialog.addGraphChangedListener(graphChangedListener);
                dialog.addGraphChangedListener(updateComboBoxesListener);
                dialog.setVisible(true);
            }
        });
        addEdgeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InputEdge dialog = new InputEdge();
                dialog.pack();
                placeInTheMiddleOfScreen(dialog);
                dialog.addGraphChangedListener(graphChangedListener);
                dialog.addGraphChangedListener(updateComboBoxesListener);
                dialog.setVisible(true);
            }
        }
        );
    }

    private void createUIComponents() {
        graphPanel = new GraphPanel();
    }

    private void fillComboBoxes() {
        addVertexesToComboBox(comboBoxRemoveVertex);
        addEdgesToComboBox(comboBoxRemoveEdge);
        addVertexesToComboBox(comboBoxSetStart);
        addVertexesToComboBox(comboBoxSetFinish);
        System.out.println(GraphController.getInstance().getVertexes());
        System.out.println(GraphController.getInstance().getEdges());
    }

    public void updateOnGraphChanged(){
        System.out.println("Updating panel size");
        graphPanel.repaint();
        System.out.println("repainted");
        Dimension preferredSize=mainPanel.getPreferredSize();
        mainPanel.setSize(preferredSize);
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

    private static void switchToNimbusLookAndFeel(){
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

    private static void placeInTheMiddleOfScreen(Component component){
        Dimension resolution=java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize=component.getPreferredSize();
        int x=(resolution.width-frameSize.width)/2;
        int y=(resolution.height-frameSize.height)/2;
        component.setBounds(x,y,frameSize.width,frameSize.height);
    }

}
