package windows;

import controllers.GraphController;
import eventhandling.GraphChangedEvent;
import eventhandling.GraphChangedListener;
import graph.Vertex;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.event.*;

public class InputVertex extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nameField;
    private EventListenerList listenerList = new EventListenerList();

    public InputVertex() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        Vertex vertex = new Vertex(nameField.getText());
        GraphController.getInstance().addVertex(vertex);
        fireEvent(new GraphChangedEvent(this));
        dispose();
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
