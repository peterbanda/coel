package edu.banda.coel.core.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.banda.core.domain.Point;
import com.banda.core.util.GeometryUtil;

import edu.banda.coel.domain.bn.BooleanNetwork;
import edu.banda.coel.domain.net.FunctionNode;
import edu.banda.coel.domain.net.Node;

/**
 * @author © Peter Banda
 * @since 2011
 */
public class GraphPanel extends JPanel {

	private static final boolean DISPLAY_IDENTIFIERS = true;
	private int xStart = 0;
	private int yStart = 0;
	private int xSize = 0;
	private int ySize = 0;	
	private int zoom = 20;

	private BooleanNetwork graph;

	public GraphPanel() {
		setBorder(BorderFactory.createTitledBorder("Connections"));
		setMinimumSize(new Dimension(200,200));
    	setMaximumSize(new Dimension(200,200));
    	setPreferredSize(new Dimension(200,200));
	}

	public void setGraph(BooleanNetwork graph) {
		this.graph = graph;
		this.xSize = graph.getDimensionSizes().getCoordinate(0);
		this.ySize = graph.getDimensionSizes().getCoordinate(1);
    	if (graph.isTorusFlag()) {
    		xStart = (xSize - 1) / 2;
    		yStart = (ySize - 1) / 2;        		
    	}
    	setMinimumSize(getDimension());
    	setMaximumSize(getDimension());
    	setPreferredSize(getDimension());
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

    public void paintComponent(Graphics g) {
        super.paintComponent(g); 
        if (graph != null) {
        	Graphics2D g2d = (Graphics2D) g;
        	Collection<FunctionNode<Boolean>> nodes = graph.getNodes();
     		g2d.setColor(Color.BLACK); 
        	for (int x = -xStart; x < xSize + xStart; x++) {
        		for (int y = -yStart; y < ySize + yStart; y++) {
                	drawCircle(g2d, GeometryUtil.get2dLocation(x, y));
        		}
        	}
        	Point<Integer> rectStart = getScaledLocation(GeometryUtil.get2dLocation(0, 0));      	
//        	Point<Integer> rectEnd = getScaledLocation(GeometryUtils.get2dLocation(xSize - xStart, ySize - yStart));
        	g2d.drawRect(rectStart.getCoordinate(0), rectStart.getCoordinate(1), xSize * zoom, ySize * zoom);
     		g2d.setColor(Color.RED);
        	for (FunctionNode<Boolean> node : nodes) {
        		Point<Integer> location = node.getLocation();
        		Point<Integer> scaledLocation = getScaledLocation(node.getLocation());
        		if (DISPLAY_IDENTIFIERS && node.getIdentifier() != null) {
             		g2d.setColor(Color.BLACK);
        			g2d.drawString(node.getIdentifier().toString(), scaledLocation.getCoordinate(0) + zoom / 4, scaledLocation.getCoordinate(1) + (3 * zoom) / 4);
        			g2d.setColor(Color.RED);
        		}
        		for (Node<Boolean> neighbor : node.getNeighbors()) {
            		Point<Integer> neighborLocation = neighbor.getLocation();
            		if (graph.isTorusFlag()) {
            			neighborLocation = GeometryUtil.getTorusManhattanPointLocation(
            					location, neighborLocation, GeometryUtil.get2dLocation(xSize, ySize));
            		}
            		neighborLocation = getScaledLocation(neighborLocation);
                	g2d.drawLine(
                			scaledLocation.getCoordinate(0) + zoom / 2,
                			scaledLocation.getCoordinate(1) + zoom / 2,
                			neighborLocation.getCoordinate(0) + zoom / 2,
                			neighborLocation.getCoordinate(1) + zoom / 2);
                }
        	}        	
        }
    }

    Point<Integer> getScaledLocation(Point<Integer> location) {
    	return new Point<Integer>(
    			new Integer[] {7 + (xStart + location.getCoordinate(0)) * zoom,
    					       22 + (yStart + location.getCoordinate(1)) * zoom});
    }

    private void drawCircle(Graphics2D g2d, Point<Integer> originalLocation) {
		Point<Integer> scaledLocation = getScaledLocation(originalLocation);
		g2d.drawOval(scaledLocation.getCoordinate(0), scaledLocation.getCoordinate(1), zoom, zoom);    	
    }

    private Dimension getDimension() {
    	return new Dimension(15 + (2 * xStart + xSize) * zoom, 32 + (2 * yStart + ySize) * zoom);
    }
}