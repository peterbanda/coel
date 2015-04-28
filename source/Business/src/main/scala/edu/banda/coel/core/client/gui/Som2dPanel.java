package edu.banda.coel.core.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.banda.core.domain.Point;

import edu.banda.coel.domain.som.SelfOrganizedMap;
import edu.banda.coel.domain.som.SomNode;

/**
 * @author © Peter Banda
 * @since 2011
 */
public class Som2dPanel extends JPanel {

	private int zoom = 20;

	private SelfOrganizedMap<Double> som;

	public Som2dPanel() {
		setBorder(BorderFactory.createTitledBorder("SOM Weights Diff"));
		setMinimumSize(new Dimension(200,200));
    	setMaximumSize(new Dimension(200,200));
    	setPreferredSize(new Dimension(200,200));
	}

	public void setSom(SelfOrganizedMap<Double> som) {
		this.som = som;
    	setMinimumSize(getDimension());
    	setMaximumSize(getDimension());
    	setPreferredSize(getDimension());
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

    public void paintComponent(Graphics g) {
        super.paintComponent(g); 
        if (som != null) {
        	Graphics2D g2d = (Graphics2D) g;
        	Collection<SomNode<Double>> nodes = som.getNodes();
        	for (SomNode<Double> node : nodes) {
        		double dif = 0;
        		Point<Integer> location = node.getLocation();
        		if (node.getWeights() == null) {
        			throw new RuntimeException("No weights. Something went wrong!");
        		}
        		int numberOfWeights = node.getWeights().length;
        		for (SomNode<Double> neighbor : node.getTypedNeighbors()) {
        			for (int i = 0; i < numberOfWeights; i++) {
        				dif += Math.abs(neighbor.getWeight(i) - node.getWeight(i));
        			}
        		}
        		float fraction = (float) dif / (node.getNeighbors().size() * numberOfWeights);
         		g2d.setColor(new Color(1 - fraction, 1 - fraction, 1 - fraction)); 
            	g2d.fillRect(10 + location.getCoordinate(0) * zoom, 25 + location.getCoordinate(1) * zoom, zoom, zoom);
        	}
        }
    }

    private Dimension getDimension() {
    	Integer[] dimensionSizes = som.getDimensionSizes().getCoordinates();
    	return new Dimension(20 + dimensionSizes[0] * zoom, 38 + dimensionSizes[1] * zoom);
    }
}