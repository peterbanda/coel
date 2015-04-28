package edu.banda.coel.core.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.banda.core.domain.Point;

import edu.banda.coel.domain.bn.BooleanNetwork;
import edu.banda.coel.domain.net.Node;

/**
 * @author © Peter Banda
 * @since 2011
 */
public class BN2dStateChangePanel extends JPanel {

	private int zoom = 20;

	private BooleanNetwork bn;

	public BN2dStateChangePanel() {
		setBorder(BorderFactory.createTitledBorder("BN Changed States"));
		setMinimumSize(new Dimension(200,200));
    	setMaximumSize(new Dimension(200,200));
    	setPreferredSize(new Dimension(200,200));
	}

	public void setBooleanNetwork(BooleanNetwork bn) {
		this.bn = bn;
		setMinimumSize(getDimension());
		setPreferredSize(getDimension());
		setMaximumSize(getDimension());
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

    public void paintComponent(Graphics g) {
        super.paintComponent(g); 
        if (bn != null) {
        	Graphics2D g2d = (Graphics2D) g;
        	for (Node<Boolean> node : bn.getNodes()) {
        		g2d.setColor(node.getChangedNodeStateFlag() ? Color.black : Color.white);

        		Point<Integer> location = node.getLocation();
            	g2d.fillRect(10 + location.getCoordinate(0) * zoom, 25 + location.getCoordinate(1) * zoom, zoom, zoom);
        	}
        }
    }
   
    private Dimension getDimension() {
		Point<Integer> sizes = bn.getDimensionSizes();
    	return new Dimension(20 + zoom * sizes.getCoordinate(0), 38 + zoom * sizes.getCoordinate(1));
    }
}