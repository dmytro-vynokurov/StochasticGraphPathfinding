package windows;

import controllers.GraphController;
import eventhandling.GraphChangedEvent;
import eventhandling.GraphChangedListener;
import graph.Vertex;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.event.*;

import static elements.ComboBoxFiller.addVertexesToComboBox;

public class InputEdge extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField expectationField;
    private JTextField sigmaField;
    private JComboBox comboBoxBegin;
    private JComboBox comboBoxEnd;
    private EventListenerList listenerList = new EventListenerList();

    public InputEdge() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        expectationField.setText("10");

        sigmaField.setText("0");

        addVertexesToComboBox(comboBoxBegin);
        if (comboBoxBegin.getItemCount() > 0) comboBoxBegin.setSelectedIndex(0);

        addVertexesToComboBox(comboBoxEnd);
        if (comboBoxEnd.getItemCount() > 1) comboBoxEnd.setSelectedIndex(1);
        else if (comboBoxEnd.getItemCount() == 1) comboBoxEnd.setSelectedIndex(0);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        GraphController controller = GraphController.getInstance();

        double expectation = retrieveExpectation();
        double sigma = retrieveSigma();


        Vertex begin = (Vertex) comboBoxBegin.getSelectedItem();
        Vertex end = (Vertex) comboBoxEnd.getSelectedItem();

        if (isInputOk(expectation, sigma, begin, end)) {
            controller.addEdge(begin, end, expectation, sigma);
            fireEvent(new GraphChangedEvent(this));
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Wrong input!");
        }
    }

    private double retrieveSigma() {
        try {
            return Double.parseDouble(sigmaField.getText());
        } catch (Exception e) {
            return -1;
        }
    }

    private double retrieveExpectation() {
        try {
            return Double.parseDouble(expectationField.getText());
        } catch (Exception e) {
            return -1;
        }
    }

    private boolean isInputOk(double expectation, double sigma, Vertex begin, Vertex end) {
        return weightIsOk(expectation, sigma) && vertexesOk(begin, end);
    }

    private boolean vertexesOk(Vertex begin, Vertex end) {
        return (begin != null) && (end != null) && (!end.equals(begin));
    }

    private boolean weightIsOk(double expectation, double sigma) {
        return (sigma >= 0) && (expectation > 0);
    }

    private void onCancel() {
        dispose();
    }

    private void fireEvent(GraphChangedEvent event) {
        Object[] listeners = listenerList.getListenerList();

        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == GraphChangedListener.class) {
                ((GraphChangedListener) listeners[i + 1]).graphChanged(event);
            }
        }
    }

    public void addGraphChangedListener(GraphChangedListener listener) {
        listenerList.add(GraphChangedListener.class, listener);
    }

    public void removeGraphChangedListener(GraphChangedListener listener) {
        listenerList.remove(GraphChangedListener.class, listener);
    }
}
