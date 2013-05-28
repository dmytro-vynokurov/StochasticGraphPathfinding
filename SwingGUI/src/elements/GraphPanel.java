package elements;

import javax.swing.*;
import java.awt.*;


/**
 * Created with IntelliJ IDEA.
 * User: Vinokurov
 * Date: 26.05.13
 * Time: 16:18
 * To change this template use File | Settings | File Templates.
 */
public class GraphPanel extends JPanel {

    public GraphPanel() {
        Dimension dimension = getPreferredSize();
        dimension.width = 300;
        setPreferredSize(dimension);
    }

    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);

        graphics.setColor(Color.BLACK);
        graphics.fillRect(20,20,100,100);


    }

}
