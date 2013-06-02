package elements;

import controllers.GraphLayoutElement;
import controllers.GraphLayoutElementConnector;
import controllers.GraphLayoutManager;

import javax.swing.*;
import java.awt.*;


public class GraphPanel extends JPanel {


    public static final int CIRCLES_IN_CELL = 3;    //how many circles can fit into one cell by height or width
    public static final int DEFAULT_PANEL_WIDTH = 300;
    private static final int MAX_CIRCLE_SIZE = 30;
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
        GraphLayoutElementConnector[] connectors = GraphLayoutManager.getInstance().getLayoutElementConnectors();
        for (GraphLayoutElementConnector connector : connectors) {
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

        circleSize = Math.min( Math.min(heightOfCell, widthOfCell) / CIRCLES_IN_CELL, MAX_CIRCLE_SIZE);
    }

    private boolean wrongCellSize() {
        return (widthOfCell <= 0) || (heightOfCell <= 0);
    }

    private Point getCoordinateOfCircle(int xCell, int yCell) {
        if (noElementsToDisplay() || wrongCellSize()) return new Point();

        int x = widthOfCell * xCell  + widthOfCell / CIRCLES_IN_CELL;
        int y = heightOfCell * yCell  + heightOfCell / CIRCLES_IN_CELL;

        return new Point(x, y);
    }

    private void drawElement(Graphics graphics, GraphLayoutElement element) {
        Point startCoordinate = getCoordinateOfCircle(element.getX(), element.getY());

        int xCircle = startCoordinate.x;
        int yCircle = startCoordinate.y;

        graphics.setColor(Color.BLACK);
        graphics.fillOval(xCircle, yCircle, circleSize, circleSize);

        int xName = startCoordinate.x + circleSize / 2;
        int yName = startCoordinate.y + circleSize * 3 / 2;

        graphics.setColor(Color.BLACK);
        graphics.drawString(element.getVertex().name(), xName, yName);
    }

    private void drawConnector(Graphics graphics, GraphLayoutElementConnector connector) {
        GraphLayoutElement begin = connector.getBegin();
        GraphLayoutElement end = connector.getEnd();
        Point beginPoint = getCoordinateOfCircle(begin.getX(), begin.getY());
        Point endPoint = getCoordinateOfCircle(end.getX(), end.getY());

        int xBegin = beginPoint.x + circleSize / 2;
        int yBegin = beginPoint.y + circleSize / 2;
        int xEnd = endPoint.x + circleSize / 2;
        int yEnd = endPoint.y + circleSize / 2;

        graphics.setColor(Color.GRAY);
        graphics.drawLine(xBegin, yBegin, xEnd, yEnd);

        String name = connector.getName();
        int xName = (xBegin + xEnd) / 2;
        int yName = (yBegin + yEnd) / 2;

        graphics.setColor(Color.BLACK);
        graphics.drawString(name, xName, yName);
    }
}
