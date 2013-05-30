package elements;

import controllers.GraphLayoutElement;
import controllers.GraphLayoutElementConnector;
import controllers.GraphLayoutManager;

import javax.swing.*;
import java.awt.*;



public class GraphPanel extends JPanel {


    public static final int CIRCLES_IN_CELL = 3;    //how many circles can fit into one cell by height or width
    public static final int DEFAULT_PANEL_WIDTH=300;
    private int xCells = 0;
    private int yCells = 0;
    private int circleSize = -1;
    private int heightOfCell = 0;
    private int widthOfCell = 0;


    public GraphPanel() {
        Dimension dimension = getPreferredSize();
        dimension.width = DEFAULT_PANEL_WIDTH;
        setPreferredSize(dimension);
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        updateParameters();
        drawConnectors(graphics);
        drawElements(graphics);

    }

    private void drawElements(Graphics graphics) {
        GraphLayoutElement[] elements = GraphLayoutManager.getInstance().getLayoutElements();
        for (GraphLayoutElement element : elements) {
            drawElement(graphics, element);
        }
    }

    private void drawConnectors(Graphics graphics) {
        GraphLayoutElementConnector[] connectors= GraphLayoutManager.getInstance().getLayoutElementConnectors();
        for(GraphLayoutElementConnector connector:connectors){
            drawConnector(graphics, connector);
        }
    }

    private void updateParameters() {
        updateCellCounts();
        updateCellSize();
        updateCircleSize();
    }

    private void updateCellCounts() {
        GraphLayoutManager layoutManager = GraphLayoutManager.getInstance();
        xCells = layoutManager.getXCellsCount();
        yCells = layoutManager.getYCellsCount();
    }

    private void updateCellSize() {
        if (noElementsToDisplay()) return;


        Dimension dimension = getSize();
        int height = dimension.height;
        int width = dimension.width;

        heightOfCell = height / yCells;
        widthOfCell = width / xCells;
    }

    private boolean noElementsToDisplay() {
        return (xCells <= 0) || (yCells <= 0);
    }

    private void updateCircleSize() {
        if (wrongCellSize()) return;

        circleSize = Math.min(heightOfCell, widthOfCell) / CIRCLES_IN_CELL;
    }

    private boolean wrongCellSize() {
        return (widthOfCell <= 0) || (heightOfCell <= 0);
    }

    private Dimension getCoordinateOfCircle(int xCell, int yCell) {
        if (noElementsToDisplay() || wrongCellSize()) return new Dimension();

        int x = widthOfCell * (xCell - 1) + widthOfCell / CIRCLES_IN_CELL;
        int y = heightOfCell * (yCell - 1) + heightOfCell / CIRCLES_IN_CELL;

        return new Dimension(x, y);
    }

    private void drawElement(Graphics graphics, GraphLayoutElement element) {
        Dimension startCoordinate = getCoordinateOfCircle(element.getX(), element.getY());

        int xCircle = startCoordinate.width;
        int yCircle = startCoordinate.height;

        graphics.setColor(Color.BLACK);
        graphics.drawOval(xCircle, yCircle, circleSize, circleSize);

        int xName = startCoordinate.width + circleSize / 2;
        int yName = startCoordinate.height + circleSize * 3 / 2;

        graphics.setColor(Color.BLACK);
        graphics.drawString(element.getVertex().name(), xName, yName);
    }

    private void drawConnector(Graphics graphics, GraphLayoutElementConnector connector){
        GraphLayoutElement begin=connector.getBegin();
        GraphLayoutElement end=connector.getEnd();
        Dimension beginDimension=getCoordinateOfCircle(begin.getX(),begin.getY());
        Dimension endDimension=getCoordinateOfCircle(end.getX(),end.getY());

        int xBegin=beginDimension.width+circleSize/2;
        int yBegin=beginDimension.height+circleSize/2;
        int xEnd=endDimension.width+circleSize/2;
        int yEnd=endDimension.height+circleSize/2;

        graphics.setColor(Color.DARK_GRAY);
        graphics.drawLine(xBegin,yBegin,xEnd,yEnd);

        String name=connector.getName();
        int xName=(xBegin+xEnd)/2;
        int yName=(yBegin+yEnd)/2;

        graphics.setColor(Color.BLACK);
        graphics.drawString(name,xName,yName);
    }


}
