package windows;

import controllers.GraphController;
import eventhandling.GraphChangedEvent;
import eventhandling.GraphChangedListener;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.event.*;

public class ChooseComparison extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JRadioButton deterministicRadioButton;
    private JRadioButton profitFunctionRadioButton;
    private JRadioButton fixedDelayRadioButton;
    private JTextField possibleDelay;
    private JTextField changePoint;

    public ChooseComparison() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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
        GraphController controller=GraphController.getInstance();

        if (fixedDelayRadioButton.isSelected()) {
            int possibleDelay=retrievePossibleDelay();
            if(possibleDelay>0){
                controller.setComparisonTypeFixedDelay(possibleDelay);
                dispose();
            }else{
                JOptionPane.showMessageDialog(null, "Wrong possible delay percentage!");
            }
        } else if (profitFunctionRadioButton.isSelected()) {
            double changePoint=retrieveChangePoint();
            if(changePoint>0){
                controller.setComparisonTypeProfitFunction(changePoint);
                dispose();
            }else{
                JOptionPane.showMessageDialog(null, "Wrong change point for profit function!");
            }
        }else{
            controller.setComparisonTypeDetermined();
            dispose();
        }
    }

    private int retrievePossibleDelay(){
        try {
            return Integer.parseInt(possibleDelay.getText());
        } catch (Exception e) {
            return -1;
        }
    }

    private double retrieveChangePoint(){
        try {
            return Double.parseDouble(changePoint.getText());
        } catch (Exception e) {
            return -1;
        }
    }

    private void onCancel() {
        dispose();
    }

}
